package com.example.socketnotifs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class SocketnotifsPlugin: FlutterPlugin, MethodCallHandler {
  private lateinit var channel: MethodChannel
  private lateinit var context: Context  // Declare context variable

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    // Initialize the context here from FlutterPluginBinding
    context = flutterPluginBinding.applicationContext

    // Initialize the MethodChannel
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "Notification")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "ConnectToSocket") {
      val url = call.argument<String>("url")

      if (url != null) {
        // Check if notification permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 and above
          if (ContextCompat.checkSelfPermission(
              context,
              Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
          ) {
            startNotificationService(url, result)
          } else {
            Log.e("WebSocketService", "Notification permission not granted")
            result.error(
              "PERMISSION_DENIED",
              "Notification permission not granted",
              null
            )
          }
        } else {
          // For Android versions below 13, start the service directly
          startNotificationService(url, result)
        }
      } else {
        Log.e("WebSocketService", "URL is null")
        result.error("INVALID_ARGUMENT", "WebSocket URL is null", null)
      }
    } else {
      result.notImplemented()
    }
  }

  private fun startNotificationService(url: String, result: Result) {
    val intent = Intent(context, WebSocketService::class.java).apply {
      putExtra("webSocketUrl", url) // Pass the URL to the WebSocketService
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ContextCompat.startForegroundService(context, intent)
    } else {
      context.startService(intent)
    }

    result.success("Notification service started successfully")
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}

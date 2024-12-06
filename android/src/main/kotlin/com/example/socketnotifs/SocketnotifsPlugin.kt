package com.example.socketnotifs

import android.content.Context
import android.content.Intent
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
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "Notif")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "showNotif") {
      val url = call.argument<String>("url")

      if (url != null) {
        val intent = Intent(context, WebSocketService::class.java).apply {
          putExtra("webSocketUrl", url) // Pass the URL to the WebSocketService
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          ContextCompat.startForegroundService(context, intent)
        } else {
          context.startService(intent)
        }

        result.success("Notification service started successfully")
      } else {
        Log.e("WebSocketService", "URL is null")
        result.error("INVALID_ARGUMENT", "WebSocket URL is null", null)
      }
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}

package com.example.socketnotifs

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class SocketnotifsPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel: MethodChannel
  private lateinit var context: Context
  private var activity: Activity? = null

  companion object {
    private const val REQUEST_NOTIFICATION_PERMISSION = 1
  }

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "Notification")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "ConnectToSocket") {
      val url = call.argument<String>("url")

      if (url != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 and above
          if (ContextCompat.checkSelfPermission(
              context,
              Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
          ) {
            startNotificationService(url, result)
          } else {
            // Request permission
            activity?.let {
              ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
              )
            } ?: run {
              result.error(
                "ACTIVITY_NOT_ATTACHED",
                "Activity is null, cannot request permissions",
                null
              )
            }
          }
        } else {
          startNotificationService(url, result)
        }
      } else {
        result.error("INVALID_ARGUMENT", "WebSocket URL is null", null)
      }
    } else {
      result.notImplemented()
    }
  }

  private fun startNotificationService(url: String, result: Result) {
    val intent = Intent(context, WebSocketService::class.java).apply {
      putExtra("webSocketUrl", url)
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

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }
}

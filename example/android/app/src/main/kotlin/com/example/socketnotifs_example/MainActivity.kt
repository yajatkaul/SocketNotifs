package com.example.socketnotifs_example

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity(){

    private val channelName = "Notif";

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName);

        channel.setMethodCallHandler { call, _ ->

            if (call.method == "showNotif"){

                val url = call.argument<String>("url")

                if (url != null) {
                    val intent = Intent(this, WebSocketService::class.java).apply {
                        putExtra("webSocketUrl", url) // Pass the URL to the WebSocketService
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent)
                    } else {
                        startService(intent)
                    }
                } else {
                    Log.e("WebSocketService", "URL is null")
                }
            }
        }
    }
}

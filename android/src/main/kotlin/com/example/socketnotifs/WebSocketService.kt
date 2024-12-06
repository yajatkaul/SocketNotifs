package com.example.socketnotifs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject

class WebSocketService : android.app.Service() {

    private lateinit var webSocket: WebSocket

    companion object {
        const val CHANNEL_ID = "WebSocketServiceChannel"
        var webSocketUrl: String? = null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("WebSocket Service")
            .setContentText("WebSocket is running...")
            .setSmallIcon(applicationContext.applicationInfo.icon)  // You can customize the icon here
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        webSocketUrl = intent.getStringExtra("webSocketUrl")
        if (webSocketUrl != null) {
            Log.d("WebSocketService", "WebSocket URL: $webSocketUrl")
            registerNetworkCallback()
            startWebSocket()  // Pass the URL to startWebSocket method
        } else {
            Log.e("WebSocketService", "WebSocket URL is null")
        }
        return START_STICKY  // Make sure the service stays running
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "WebSocket Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun startWebSocket() {
        Log.d("WebSocketService", "Called")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(webSocketUrl!!) // Replace with your WebSocket server URL
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocketService", "WebSocket connected successfully")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocketService", "Received message: $text")

                try {
                    // Parse the JSON string
                    val jsonObject = JSONObject(text)

                    // Extract fields from the JSON object
                    val title = jsonObject.optString("title", "Default Title")
                    val message = jsonObject.optString("message", "Default Message")

                    // Use extracted values to show notification
                    showNotification(title, message)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("WebSocketService", "Failed to parse JSON: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("WebSocketService", "Connection failed: ${t.message}")
                scheduleReconnect()
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("WebSocketService", "WebSocket closing: $reason")
            }
        })
    }

    private var isReconnecting = false
    private val handler = Handler(Looper.getMainLooper())

    private fun scheduleReconnect() {
        if (!isReconnecting) {
            isReconnecting = true
            handler.postDelayed({
                startWebSocket()
            }, 5000L)
        }
    }

    private fun registerNetworkCallback() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("WebSocketService", "Network available. Attempting to reconnect...")
                if (isReconnecting) {
                    startWebSocket()
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d("WebSocketService", "Network lost. WebSocket may disconnect.")
            }
        })
    }

    private fun showNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(applicationContext.applicationInfo.icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onBind(intent: Intent?) = null

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WebSocketService", "Service destroyed")
        webSocket.close(1000, "Service destroyed")
    }
}

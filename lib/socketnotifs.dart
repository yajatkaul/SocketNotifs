import 'package:flutter/services.dart';
import 'dart:async';

class SocketNotifs {
  static const MethodChannel _channel = MethodChannel('Notif');

  static Future<void> connectToWebSocket(String url) async {
    await _channel.invokeMethod('showNotif', {'url': url});
  }
}

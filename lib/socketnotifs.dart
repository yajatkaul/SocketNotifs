import 'package:flutter/services.dart';

class SocketNotifs {
  static const MethodChannel _channel = MethodChannel('Notification');

  static void connectToWebSocket(String url) {
    _channel.invokeMethod('ConnectToSocket', {'url': url});
  }
}

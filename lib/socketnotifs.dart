import 'package:flutter/services.dart';

/// Plugin class
class SocketNotifs {
  static const MethodChannel _channel = MethodChannel('Notification');

  /// Call to invoke native connections
  static void connectToWebSocket(String url) {
    _channel.invokeMethod('ConnectToSocket', {'url': url});
  }
}

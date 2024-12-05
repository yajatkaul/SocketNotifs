import 'package:flutter/services.dart';
import 'dart:async';

import 'package:socketnotifs/socketnotifs_platform_interface.dart';

class SocketNotifsMethodChannel extends SocketNotifsPlatform {
  static const MethodChannel _channel = MethodChannel('Notif');

  @override
  Future<void> connectToWebSocket(String url) async {
    await _channel.invokeMethod('showNotif', {'url': url});
  }
}

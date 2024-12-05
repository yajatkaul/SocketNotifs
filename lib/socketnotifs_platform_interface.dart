import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:socketnotifs/socketnotifs.dart';

abstract class SocketNotifsPlatform extends PlatformInterface {
  SocketNotifsPlatform() : super(token: _token);

  static final Object _token = Object();

  Future<void> connectToWebSocket(String url);

  static SocketNotifsPlatform get instance => _instance;
  static SocketNotifsPlatform _instance = SocketNotifsMethodChannel();

  static set instance(SocketNotifsPlatform instance) {
    _instance = instance;
  }
}

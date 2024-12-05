import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:socketnotifs/socketnotifs.dart';

abstract class SocketNotifsPlatform extends PlatformInterface {
  // Constructor
  SocketNotifsPlatform() : super(token: _token);

  static final Object _token = Object();

  // This method must be implemented by the native code
  Future<void> connectToWebSocket(String url);

  // A getter to access the current platform-specific implementation
  static SocketNotifsPlatform get instance => _instance;
  static SocketNotifsPlatform _instance = SocketNotifsMethodChannel();

  // Sets the platform implementation (useful for testing or custom platforms)
  static set instance(SocketNotifsPlatform instance) {
    _instance = instance;
  }
}

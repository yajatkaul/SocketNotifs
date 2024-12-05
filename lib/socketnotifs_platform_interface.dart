import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'sadasd_method_channel.dart';

abstract class SadasdPlatform extends PlatformInterface {
  /// Constructs a SadasdPlatform.
  SadasdPlatform() : super(token: _token);

  static final Object _token = Object();

  static SadasdPlatform _instance = MethodChannelSadasd();

  /// The default instance of [SadasdPlatform] to use.
  ///
  /// Defaults to [MethodChannelSadasd].
  static SadasdPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SadasdPlatform] when
  /// they register themselves.
  static set instance(SadasdPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'socketnotifs_platform_interface.dart';

/// An implementation of [SadasdPlatform] that uses method channels.
class MethodChannelSadasd extends SadasdPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('sadasd');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}

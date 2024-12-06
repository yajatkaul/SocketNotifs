# SocketNotifs
SocketNotifs is a Dart plugin for Flutter applications that facilitates easy WebSocket connections and provides notifications. This plugin currently supports the Android platform, while iOS support is still under development.

## Features
* Establishes WebSocket connections seamlessly.
* Android-side implementation is complete with required permissions and services.
* iOS implementation is planned and coming soon.

## Installation
Add the dependency to your pubspec.yaml file:
```yaml
dependencies:
  socketnotifs: <latest_version>
```
Then, run:
```bash
flutter pub get
```

## Usage
Example Code
Below is a minimal example of how to use SocketNotifs:
```dart
import 'package:flutter/material.dart';
import 'package:socketnotifs/socketnotifs_platform_interface.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    // Connect to WebSocket
    SocketNotifsPlatform.instance.connectToWebSocket("ws://10.0.2.2:4000");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('SocketNotifs Example'),
        ),
        body: const Center(
          child: Text('WebSocket is running...'),
        ),
      ),
    );
  }
}
```

## Permissions (Android)
To use this plugin, ensure the following permissions are added to your Android `AndroidManifest.xml` file:
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />
```

## Roadmap
* Completed:
  * Android-side implementation.
* Planned:
  * iOS-side implementation.

## Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request to help improve this plugin.

## License
This project is licensed under the `MIT License`.

Happy coding with SocketNotifs! 🎉

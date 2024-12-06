# SocketNotifs

[![Pub Popularity](https://img.shields.io/pub/popularity/socketnotifs)](https://pub.dev/packages/socketnotifs)
[![Pub Version](https://img.shields.io/pub/v/socketnotifs)](https://pub.dev/packages/socketnotifs)
[![License](https://img.shields.io/github/license/yajatkaul/SocketNotifs)](https://pub.dev/packages/socketnotifs)

SocketNotifs is a Dart plugin for Flutter applications that facilitates easy WebSocket connections and provides notifications. This plugin currently supports the Android platform, while iOS support is still under development.

## Features

[![Pub Points](https://img.shields.io/pub/points/socketnotifs)](https://pub.dev/packages/socketnotifs)

- Establishes WebSocket connections seamlessly.
- Android-side implementation is complete with required permissions and services.
- iOS implementation is planned and coming soon. (I dont have a mac/ios device)

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
import 'package:socketnotifs/socketnotifs.dart';

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
    SocketNotifs.connectToWebSocket("ws://10.0.2.2:4000");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: const Center(
          child: Text('Running on: drugs\n'),
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
    <application>
        <service
            android:name="com.example.socketnotifs.WebSocketService"
            android:exported="true"
            android:foregroundServiceType="dataSync" />
    <application />
```

## Server Side

To send notifications from the websocket follow this example for js:

```js
const WebSocket = require("ws");
const express = require("express");
const app = express();
const port = 4000;

// Initialize WebSocket server
const wss = new WebSocket.Server({ noServer: true });

// WebSocket connection handling
wss.on("connection", (ws) => {
  console.log("Client connected");

  ws.on("close", () => {
    console.log("Client disconnected");
  });
});

// Upgrade HTTP server to WebSocket
app.server = app.listen(port, () => {
  console.log(`Server is listening on port ${port}`);
});

app.server.on("upgrade", (request, socket, head) => {
  wss.handleUpgrade(request, socket, head, (ws) => {
    wss.emit("connection", ws, request);
  });
});

function broadcastMessage(message, title) {
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify({ message, title }));
    }
  });
}

setInterval(() => {
  broadcastMessage("This is a test notification message!", "example");
  console.log("asdas");
}, 5000);
```

## Roadmap

- Completed:
  - Android-side implementation.
- Planned:
  - iOS-side implementation.

## Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request to help improve this plugin.

## License

This project is licensed under the `MIT License`.

Happy coding with SocketNotifs! 🎉

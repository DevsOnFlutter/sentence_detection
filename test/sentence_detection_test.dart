import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sentence_detection/sentence_detection.dart';

void main() {
  const MethodChannel channel = MethodChannel('sentence_detection');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await SentenceDetection.platformVersion, '42');
  });
}

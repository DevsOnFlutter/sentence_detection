import 'dart:async';

import 'package:flutter/services.dart';

class SentenceDetection {
  static const MethodChannel _channel =
      const MethodChannel('sentence_detection');

  static Future<List<String>> getSentences(String text) async {
    List<String> sentences = [];
    final list = await _channel.invokeMethod('getSentences', [text]);
    list.map((e) => sentences.add(e.toString())).toList();    
    return sentences;
  }
}

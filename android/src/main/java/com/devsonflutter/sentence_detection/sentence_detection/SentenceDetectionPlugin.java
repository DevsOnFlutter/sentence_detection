package com.devsonflutter.sentence_detection.sentence_detection;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/** SentenceDetectionPlugin */
public class SentenceDetectionPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sentence_detection");
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getSentences")) {
      List<String> args = call.arguments();

      AssetManager assetManager = context.getAssets();
      InputStream inputStream = null;
      try {
        inputStream = assetManager.open("en-sent.bin");
        SentenceModel model = new SentenceModel(inputStream);

        SentenceDetectorME detector = new SentenceDetectorME(model);

        String[] sentences = detector.sentDetect(args.get(0));
        List<String> sentencesList = Arrays.asList(sentences);
//        Collections.addAll(sentencesList, sentences);

        result.success(sentencesList);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}

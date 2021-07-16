package com.devsonflutter.sentence_detection.sentence_detection;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

//import edu.stanford.nlp.pipeline.CoreDocument;
//import edu.stanford.nlp.pipeline.CoreSentence;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
//import opennlp.tools.sentdetect.SentenceDetectorME;
//import opennlp.tools.sentdetect.SentenceModel;



/** SentenceDetectionPlugin */
public class SentenceDetectionPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
  static final SentenceModel SENTENCE_MODEL  = new MedlineSentenceModel();

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
      String text = args.get(0);

      List<String> tokenList = new ArrayList<String>();
      List<String> whiteList = new ArrayList<String>();
      Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(text.toCharArray(),0,text.length());
      tokenizer.tokenize(tokenList,whiteList);

      String[] tokens = new String[tokenList.size()];
      String[] whites = new String[whiteList.size()];
      tokenList.toArray(tokens);
      whiteList.toArray(whites);
      int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens,whites);

      if (sentenceBoundaries.length < 1) {
        return;
      }
      int sentStartTok = 0;
      int sentEndTok = 0;

      List<String> sentences = new ArrayList<String>();

      for (int i = 0; i < sentenceBoundaries.length; ++i) {
        sentEndTok = sentenceBoundaries[i];
        System.out.println("SENTENCE "+(i+1)+": ");
        StringBuilder sentOut = new StringBuilder();
        for (int j=sentStartTok; j<=sentEndTok; j++) {
          sentOut.append(tokens[j]).append(whites[j + 1]);
        }
        sentences.add(sentOut.toString());
        sentStartTok = sentEndTok+1;
      }
        result.success(sentences);
    }
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}

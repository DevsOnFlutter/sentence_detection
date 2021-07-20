package com.devsonflutter.sentence_detection.sentence_detection;
import androidx.annotation.NonNull;
import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import java.util.ArrayList;
import java.util.List;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** SentenceDetectionPlugin */
public class SentenceDetectionPlugin implements FlutterPlugin, MethodCallHandler {
  private MethodChannel channel;
//  private Context context;

  static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
  static final SentenceModel SENTENCE_MODEL  = new MedlineSentenceModel();

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sentence_detection");
    channel.setMethodCallHandler(this);
//    context = flutterPluginBinding.getApplicationContext();
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

      for (int sentenceBoundary : sentenceBoundaries) {
        sentEndTok = sentenceBoundary;
//        System.out.println("SENTENCE "+(i+1)+": ");
        StringBuilder sentOut = new StringBuilder();
        for (int j = sentStartTok; j <= sentEndTok; j++) {
          sentOut.append(tokens[j]).append(whites[j + 1]);
        }
        sentences.add(sentOut.toString());
        sentStartTok = sentEndTok + 1;
      }
        result.success(sentences);
    }
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}

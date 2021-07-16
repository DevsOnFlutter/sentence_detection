import 'package:flutter/material.dart';
import 'package:sentence_detection/sentence_detection.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {

  final String text =
      "Usually Sentence Detection is done before the text is tokenized and that's the way the pre-trained models on the web site are trained, but it is also possible to perform tokenization first and let the Sentence Detector process the already tokenized text. The OpenNLP Sentence Detector cannot identify sentence boundaries based on the contents of the sentence. A prominent example is the first sentence in an article where the title is mistakenly identified to be the first part of the first sentence. Most components in OpenNLP expect input which is segmented into sentences.";

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Sentence Detection'),
        ),
        body: Container(
            child: FutureBuilder<List<String>>(
          future: SentenceDetection.getSentences(text),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              return list(snapshot.data!);
            } else
              return Container();
          },
        )),
      ),
    );
  }

  ListView list(List<String> sentences) {
    return ListView.builder(
      itemCount: sentences.length,
      itemBuilder: (context, index) => Card(
        child: ListTile(
          title: Text(sentences[index]),
        ),
      ),
    );
  }
}

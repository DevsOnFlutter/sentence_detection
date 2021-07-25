import 'package:flutter/material.dart';
import 'package:sentence_detection/sentence_detection.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  final String text =
      "This is first sentence. I was born in 01.05.2000. This is third sentence";

  // Future<List<String>> sentence(String paragraph) async {
  //   List<String> _sentences = await SentenceDetection.getSentences(paragraph);
  //   return _sentences;
  // }

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

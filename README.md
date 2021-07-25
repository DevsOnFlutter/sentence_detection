# sentence_detection

A flutter plugin that recognises sentences in paragraphs using [lingpipe NLP](http://www.alias-i.com/lingpipe/).

## Installation

### dependencies

````dart
dependencies:
  file_manager: ^1.0.0
````

### import

````dart
import 'package:sentence_detection/sentence_detection.dart';
````"This is 1st sentence. This is ";
````dart
    String paragraph =
        "This is first sentence. I was born in 01.05.2000. This is third sentence";
    List<String> _sentences = await SentenceDetection.getSentences(paragraph);
````

package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;

public class SentenceParser extends AbstractParser {

  private static final String SENTENCE_DELIMITER = "(?<=[.!?])\\s+";

  public SentenceParser(LexemeParser nextParser) {
    this.nextParser = nextParser;
  }

  @Override
  public void parse(String paragraph, TextComposite parentComposite) {
    String[] sentenceArray = paragraph.split(SENTENCE_DELIMITER);

    for (var sentence : sentenceArray) {
      TextComposite sentenceComposite = new TextComposite(TextComponentType.SENTENCE);
      parentComposite.addChild(sentenceComposite);
      nextParser.parse(sentence, sentenceComposite);
    }
  }
}

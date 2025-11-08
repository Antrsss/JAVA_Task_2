package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;

public class WordParser extends AbstractParser {

  private static final String WORD_DELIMITER = "[^a-zA-Z]+";

  public WordParser(SymbolParser nextParser) {
    this.nextParser = nextParser;
  }

  @Override
  public void parse(String lexeme, TextComposite parentComposite) {
    String[] wordsArray = lexeme.split(WORD_DELIMITER);

    for (var word : wordsArray) {
      TextComposite wordComposite = new TextComposite(TextComponentType.WORD);
      parentComposite.addComponent(wordComposite);
      nextParser.parse(word, wordComposite);
    }
  }
}

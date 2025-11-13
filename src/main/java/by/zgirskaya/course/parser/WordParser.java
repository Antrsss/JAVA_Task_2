package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;

public class WordParser extends AbstractParser {

  private static final String WORD_DELIMITER = "[^a-zA-Z]+";

  @Override
  public void parse(String lexeme, TextComposite parentComposite) {
    String[] wordsArray = lexeme.split(WORD_DELIMITER);

    for (var word : wordsArray) {
      TextLeaf wordComponent = new TextLeaf(word, TextComponentType.WORD);
      parentComposite.addComponent(wordComponent);
    }
  }
}

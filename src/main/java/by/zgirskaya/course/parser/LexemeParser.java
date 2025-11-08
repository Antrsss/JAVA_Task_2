package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;

public class LexemeParser extends AbstractParser {

  private static final String LEXEME_DELIMITER = "\\s+";

  //Word or Symbol parser - depending on task
  public LexemeParser(AbstractParser nextParser) {
    this.nextParser = nextParser;
  }

  @Override
  public void parse(String sentence, TextComposite parentComposite) {
    String[] lexemeArray = sentence.split(LEXEME_DELIMITER);

    for (var lexeme : lexemeArray) {
      TextComposite lexemeComposite = new TextComposite(TextComponentType.LEXEME);
      parentComposite.addComponent(lexemeComposite);

      nextParser.parse(lexeme, lexemeComposite);
    }
  }
}

package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;

public class ParagraphParser extends AbstractParser {

  private static final String PARAGRAPH_DELIMITER = "\\p{Blank}{4}";
  private static final String TO_REPLACE_REGEX = "\\p{Blank}{2,}";
  private static final String SPACE = " ";

  public ParagraphParser(SentenceParser nextParser) {
    this.nextParser = nextParser;
  }

  @Override
  public void parse(String text, TextComposite parentComposite) {
    String[] paragraphArray = text.split(PARAGRAPH_DELIMITER);

    for (var paragraph : paragraphArray) {
      paragraph = paragraph.replaceAll(TO_REPLACE_REGEX, SPACE);
      TextComposite paragraphComposite = new TextComposite(TextComponentType.PARAGRAPH);
      parentComposite.addChild(paragraphComposite);
      nextParser.parse(paragraph, paragraphComposite);
    }
  }
}

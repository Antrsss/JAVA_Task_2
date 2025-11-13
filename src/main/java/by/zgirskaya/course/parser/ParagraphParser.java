package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParagraphParser extends AbstractParser {
  private static final Logger logger = LogManager.getLogger();

  private static final String PARAGRAPH_DELIMITER = "\\p{Blank}{4}";
  private static final String TO_REPLACE_REGEX = "\\p{Blank}{2,}";
  private static final String SPACE = " ";

  public ParagraphParser(SentenceParser nextParser) {
    logger.debug("Creating ParagraphParser with next parser: {}",
            nextParser.getClass().getSimpleName());

    this.nextParser = nextParser;
  }

  @Override
  public void parse(String text, TextComposite parentComposite) {
    logger.info("Starting paragraph parsing. Input text length: {}, Parent type: {}",
            text.length(), parentComposite.getComponentType());

    String[] paragraphArray = text.split(PARAGRAPH_DELIMITER);

    for (var paragraph : paragraphArray) {
      paragraph = paragraph.replaceAll(TO_REPLACE_REGEX, SPACE);
      TextComposite paragraphComposite = new TextComposite(TextComponentType.PARAGRAPH);
      parentComposite.addChildComponent(paragraphComposite);
      nextParser.parse(paragraph, paragraphComposite);

      logger.debug("Next parser completed for paragraph: '{}'", paragraph);
    }

    logger.info("Paragraph parsing completed. Total paragraphs processed: {}, Parent children count: {}",
            paragraphArray.length, parentComposite.getChildComponents().size());
  }
}

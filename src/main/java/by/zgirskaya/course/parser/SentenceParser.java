package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SentenceParser extends AbstractParser {
  private static final Logger logger = LogManager.getLogger();

  private static final String SENTENCE_DELIMITER = "(?<=[.!?])\\s+";

  public SentenceParser(LexemeParser nextParser) {
    logger.debug("Creating SentenceParser with next parser: {}",
            nextParser.getClass().getSimpleName());

    this.nextParser = nextParser;
  }

  @Override
  public void parse(String paragraph, TextComposite parentComposite) {
    logger.info("Starting sentence parsing. Paragraph length: {}, Parent type: {}",
            paragraph.length(), parentComposite.getComponentType());

    String[] sentenceArray = paragraph.split(SENTENCE_DELIMITER);

    for (var sentence : sentenceArray) {
      TextComposite sentenceComposite = new TextComposite(TextComponentType.SENTENCE);
      parentComposite.addChildComponent(sentenceComposite);
      nextParser.parse(sentence, sentenceComposite);

      logger.debug("Next parser completed for sentence: '{}'", paragraph);
    }

    logger.info("Sentence parsing completed. Total sentences processed: {}, Parent children count: {}",
            sentenceArray.length, parentComposite.getChildComponents().size());
  }
}

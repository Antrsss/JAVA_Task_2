package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WordParser extends AbstractParser {
  private static final Logger logger = LogManager.getLogger();

  private static final String WORD_DELIMITER = "[^a-zA-Z]+";

  @Override
  public void parse(String lexeme, TextComposite parentComposite) {
    logger.info("Starting word parsing. Lexeme: '{}', Length: {}, Parent type: {}",
            lexeme, lexeme.length(), parentComposite.getComponentType());

    String[] wordsArray = lexeme.split(WORD_DELIMITER);

    for (var word : wordsArray) {
      TextLeaf wordComponent = new TextLeaf(word, TextComponentType.WORD);
      parentComposite.addChildComponent(wordComponent);

      logger.debug("Next parser completed for word: '{}'", word);
    }

    logger.info("Symbol parsing completed. Total words processed: {}, Parent children count: {}",
            wordsArray.length, parentComposite.getChildComponents().size());
  }
}

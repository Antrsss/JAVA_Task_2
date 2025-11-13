package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SymbolParser extends AbstractParser {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public void parse(String lexeme, TextComposite parentComposite) {
    logger.info("Starting symbol parsing. Lexeme: '{}', Length: {}, Parent type: {}",
            lexeme, lexeme.length(), parentComposite.getComponentType());

    for (int i = 0; i < lexeme.length(); i++) {
      char symbol = lexeme.charAt(i);
      TextLeaf letterComponent = new TextLeaf(String.valueOf(symbol), TextComponentType.SYMBOL);
      parentComposite.addChildComponent(letterComponent);

      logger.debug("Next parser completed for symbol: '{}'", symbol);
    }

    logger.info("Symbol parsing completed. Total symbols processed: {}, Parent children count: {}",
            lexeme.length(), parentComposite.getChildComponents().size());
  }
}

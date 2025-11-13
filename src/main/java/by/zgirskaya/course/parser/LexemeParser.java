package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.exception.CustomTextException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LexemeParser extends AbstractParser {
  private static final Logger logger = LogManager.getLogger();

  private static final String LEXEME_DELIMITER = "\\s+";

  // Word or Symbol parser - depending on task
  public LexemeParser(AbstractParser nextParser) throws CustomTextException {
    logger.debug("Creating LexemeParser with next parser: {}",
            nextParser.getClass().getSimpleName());

    if (!(nextParser instanceof WordParser) && !(nextParser instanceof SymbolParser)) {
      throw new CustomTextException("LexemeParser: next parser must be WordParser or SymbolParser");
    }
    this.nextParser = nextParser;
  }

  @Override
  public void parse(String sentence, TextComposite parentComposite) {
    logger.info("Starting lexeme parsing. Parent type: {}, Input sentence: '{}'",
            parentComposite.getComponentType(), sentence);

    String trimmedSentence = sentence.trim();
    String[] lexemeArray = trimmedSentence.split(LEXEME_DELIMITER);

    for (String lexeme : lexemeArray) {
      TextComposite lexemeComposite = new TextComposite(TextComponentType.LEXEME);
      parentComposite.addChildComponent(lexemeComposite);
      nextParser.parse(lexeme, lexemeComposite);

      logger.debug("Next parser completed for lexeme: '{}'", lexeme);
    }

    logger.info("Lexeme parsing completed. Total lexemes processed: {}, Parent children count: {}",
            lexemeArray.length, parentComposite.getChildComponents().size());
  }
}
package by.zgirskaya.course.main;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.exception.CustomTextException;
import by.zgirskaya.course.parser.*;
import by.zgirskaya.course.reader.impl.CustomTextReaderImpl;
import by.zgirskaya.course.service.impl.TextServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
  private static final Logger logger = LogManager.getLogger();
  private static final String FILE_PATH = "recourses\\text.txt";

  public static void main(String[] args) {
    try {
      logger.info("Starting text processing application");

      // 1. Read text from file
      CustomTextReaderImpl reader = new CustomTextReaderImpl();
      String text = reader.readTextFromFile(FILE_PATH);

      logger.info("Successfully read text from file. Text length: {}", text.length());
      logger.info("=== ORIGINAL TEXT ===");
      logger.info(text);
      logger.info("=====================\n");

      // 2. Create TWO different parser chains for different purposes

      // Chain 1: For service operations (WordParser)
      logger.info("=== PARSING FOR SERVICE OPERATIONS (WITH WORDPARSER) ===");
      WordParser wordParser = new WordParser();
      LexemeParser lexemeParserForService = new LexemeParser(wordParser);
      SentenceParser sentenceParserForService = new SentenceParser(lexemeParserForService);
      ParagraphParser paragraphParserForService = new ParagraphParser(sentenceParserForService);

      TextComposite textCompositeForService = new TextComposite(TextComponentType.PARAGRAPH);
      paragraphParserForService.parse(text, textCompositeForService);

      // Display structure info for service parsing
      displayStructureInfo(textCompositeForService, "SERVICE PARSING (WordParser)");

      // Chain 2: For text restoration (SymbolParser)
      logger.info("=== PARSING FOR TEXT RESTORATION (WITH SYMBOLPARSER) ===");
      SymbolParser symbolParser = new SymbolParser();
      LexemeParser lexemeParserForRestoration = new LexemeParser(symbolParser);
      SentenceParser sentenceParserForRestoration = new SentenceParser(lexemeParserForRestoration);
      ParagraphParser paragraphParserForRestoration = new ParagraphParser(sentenceParserForRestoration);

      TextComposite textCompositeForRestoration = new TextComposite(TextComponentType.PARAGRAPH);
      paragraphParserForRestoration.parse(text, textCompositeForRestoration);

      // Display structure info for restoration parsing
      displayStructureInfo(textCompositeForRestoration, "RESTORATION PARSING (SymbolParser)");

      // 3. Restore text from SymbolParser-based composite
      String restoredText = textCompositeForRestoration.toString();

      logger.info("=== RESTORED TEXT ===");
      logger.info(restoredText);
      logger.info("=====================\n");

      // 4. Verify that original and restored texts match
      String normalizedOriginal = text.replaceAll("\\s+", " ").trim();
      String normalizedRestored = restoredText.replaceAll("\\s+", " ").trim();
      boolean textsMatch = normalizedOriginal.equals(normalizedRestored);

      logger.info("Texts match (normalized): {}", textsMatch);
      logger.info("Original length: {}", text.length());
      logger.info("Restored length: {}", restoredText.length());

      if (!textsMatch) {
        logger.warn("Original and restored texts do not match exactly");
        logger.info("Normalized original: '{}'", normalizedOriginal);
        logger.info("Normalized restored: '{}'", normalizedRestored);
      }

      // 5. Test service methods using WordParser-based composite
      TextServiceImpl textService = new TextServiceImpl();

      logger.info("\n=== SERVICE METHODS (USING WORDPARSER COMPOSITE) ===");

      // Find max sentence count with same words
      int maxSameWords = textService.findMaxSentenceCountWithSameWords(textCompositeForService);
      logger.info("Max sentences with same words: {}", maxSameWords);

      // Display sentences by lexeme count
      logger.info("\n--- Sentences sorted by lexeme count ---");
      textService.displaySentencesByLexemeCountAscending(textCompositeForRestoration);

      // Change first and last lexemes
      logger.info("\n=== CHANGING FIRST AND LAST LEXEMES ===");
      var modifiedText = textService.changeFirstAndLastLexemesInSentences(textCompositeForRestoration);
      logger.info("Modified text (first and last lexemes swapped in each sentence):");
      logger.info("=== MODIFIED TEXT ===");
      logger.info(modifiedText.toString());
      logger.info("=====================");

      logger.info("Application completed successfully");

    } catch (CustomTextException e) {
      logger.error("Custom text exception occurred: {}", e.getMessage(), e);
      logger.error("Error: {}", e.getMessage());
    } catch (Exception e) {
      logger.error("Unexpected error occurred: {}", e.getMessage(), e);
      logger.error("Unexpected error: {}", e.getMessage());
    }
  }

  private static void displayStructureInfo(TextComposite textComposite, String title) {
    logger.info("=== {} INFO ===", title);

    int paragraphCount = 0;
    int sentenceCount = 0;
    int lexemeCount = 0;
    int wordCount = 0;
    int symbolCount = 0;

    // Count different components
    for (AbstractTextComponent paragraph : textComposite.getChildComponents()) {
      if (paragraph instanceof TextComposite) {
        paragraphCount++;

        for (AbstractTextComponent sentence : ((TextComposite) paragraph).getChildComponents()) {
          if (sentence instanceof TextComposite) {
            sentenceCount++;

            for (AbstractTextComponent lexeme : ((TextComposite) sentence).getChildComponents()) {
              if (lexeme instanceof TextComposite &&
                      lexeme.getComponentType() == TextComponentType.LEXEME) {
                lexemeCount++;

                for (AbstractTextComponent component : ((TextComposite) lexeme).getChildComponents()) {
                  if (component.getComponentType() == TextComponentType.WORD) {
                    wordCount++;
                  } else if (component.getComponentType() == TextComponentType.SYMBOL) {
                    symbolCount++;
                  } else if (component instanceof TextComposite) {
                    // Если это композит, проверяем его детей
                    for (AbstractTextComponent child : ((TextComposite) component).getChildComponents()) {
                      if (child.getComponentType() == TextComponentType.WORD) {
                        wordCount++;
                      } else if (child.getComponentType() == TextComponentType.SYMBOL) {
                        symbolCount++;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    logger.info("Paragraphs: {}", paragraphCount);
    logger.info("Sentences: {}", sentenceCount);
    logger.info("Lexemes: {}", lexemeCount);
    logger.info("Words: {}", wordCount);
    logger.info("Symbols: {}", symbolCount);
    logger.info("============================\n");
  }
}
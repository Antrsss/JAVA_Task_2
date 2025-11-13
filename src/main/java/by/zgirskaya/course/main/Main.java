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
      System.out.println("=== ORIGINAL TEXT ===");
      System.out.println(text);
      System.out.println("=====================\n");

      // 2. Create TWO different parser chains for different purposes

      // Chain 1: For service operations (WordParser)
      System.out.println("=== PARSING FOR SERVICE OPERATIONS (WITH WORDPARSER) ===");
      WordParser wordParser = new WordParser();
      LexemeParser lexemeParserForService = new LexemeParser(wordParser);
      SentenceParser sentenceParserForService = new SentenceParser(lexemeParserForService);
      ParagraphParser paragraphParserForService = new ParagraphParser(sentenceParserForService);

      TextComposite textCompositeForService = new TextComposite(TextComponentType.PARAGRAPH);
      paragraphParserForService.parse(text, textCompositeForService);

      // Display structure info for service parsing
      displayStructureInfo(textCompositeForService, "SERVICE PARSING (WordParser)");

      // Chain 2: For text restoration (SymbolParser)
      System.out.println("=== PARSING FOR TEXT RESTORATION (WITH SYMBOLPARSER) ===");
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

      System.out.println("=== RESTORED TEXT ===");
      System.out.println(restoredText);
      System.out.println("=====================\n");

      // 4. Verify that original and restored texts match
      String normalizedOriginal = text.replaceAll("\\s+", " ").trim();
      String normalizedRestored = restoredText.replaceAll("\\s+", " ").trim();
      boolean textsMatch = normalizedOriginal.equals(normalizedRestored);

      System.out.println("Texts match (normalized): " + textsMatch);
      System.out.println("Original length: " + text.length());
      System.out.println("Restored length: " + restoredText.length());

      if (!textsMatch) {
        logger.warn("Original and restored texts do not match exactly");
        System.out.println("Normalized original: '" + normalizedOriginal + "'");
        System.out.println("Normalized restored: '" + normalizedRestored + "'");
      }

      // 5. Test service methods using WordParser-based composite
      TextServiceImpl textService = new TextServiceImpl();

      // В методе main замените вывод сервисных методов:
      System.out.println("\n=== SERVICE METHODS (USING WORDPARSER COMPOSITE) ===");

      // Find max sentence count with same words
      int maxSameWords = textService.findMaxSentenceCountWithSameWords(textCompositeForService);
      System.out.println("Max sentences with same words: " + maxSameWords);

      // Display sentences by lexeme count
      System.out.println("\n--- Sentences sorted by lexeme count ---");
      textService.displaySentencesByLexemeCountAscending(textCompositeForRestoration);

      // Change first and last lexemes
      System.out.println("\n=== CHANGING FIRST AND LAST LEXEMES ===");
      var modifiedText = textService.changeFirstAndLastLexemesInSentences(textCompositeForRestoration);
      System.out.println("Modified text (first and last lexemes swapped in each sentence):");
      System.out.println("=== MODIFIED TEXT ===");
      System.out.println(modifiedText.toString());
      System.out.println("=====================");

      logger.info("Application completed successfully");

    } catch (CustomTextException e) {
      logger.error("Custom text exception occurred: {}", e.getMessage(), e);
      System.err.println("Error: " + e.getMessage());
    } catch (Exception e) {
      logger.error("Unexpected error occurred: {}", e.getMessage(), e);
      System.err.println("Unexpected error: " + e.getMessage());
    }
  }

  private static void displayStructureInfo(TextComposite textComposite, String title) {
    System.out.println("=== " + title + " INFO ===");

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

    System.out.println("Paragraphs: " + paragraphCount);
    System.out.println("Sentences: " + sentenceCount);
    System.out.println("Lexemes: " + lexemeCount);
    System.out.println("Words: " + wordCount);
    System.out.println("Symbols: " + symbolCount);
    System.out.println("============================\n");
  }
}
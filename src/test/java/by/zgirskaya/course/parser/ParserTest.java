package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

  @Test
  void testSymbolParser() {
    SymbolParser parser = new SymbolParser();
    TextComposite parent = new TextComposite(TextComponentType.LEXEME);
    String lexeme = "abc";

    parser.parse(lexeme, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(3, children.size());

    assertEquals(TextComponentType.SYMBOL, children.get(0).getComponentType());
    assertEquals("a", children.get(0).toString());

    assertEquals(TextComponentType.SYMBOL, children.get(1).getComponentType());
    assertEquals("b", children.get(1).toString());

    assertEquals(TextComponentType.SYMBOL, children.get(2).getComponentType());
    assertEquals("c", children.get(2).toString());
  }

  @Test
  void testSymbolParserWithEmptyString() {
    SymbolParser parser = new SymbolParser();
    TextComposite parent = new TextComposite(TextComponentType.LEXEME);
    String lexeme = "";

    parser.parse(lexeme, parent);

    assertTrue(parent.getChildren().isEmpty());
  }

  @Test
  void testWordParser() {
    WordParser parser = new WordParser();
    TextComposite parent = new TextComposite(TextComponentType.LEXEME);
    String lexeme = "hello123world!test";

    parser.parse(lexeme, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(3, children.size());

    assertEquals(TextComponentType.WORD, children.get(0).getComponentType());
    assertEquals("hello", children.get(0).toString());

    assertEquals(TextComponentType.WORD, children.get(1).getComponentType());
    assertEquals("world", children.get(1).toString());

    assertEquals(TextComponentType.WORD, children.get(2).getComponentType());
    assertEquals("test", children.get(2).toString());
  }

  @Test
  void testWordParserWithOnlyNonLetters() {
    WordParser parser = new WordParser();
    TextComposite parent = new TextComposite(TextComponentType.LEXEME);
    String lexeme = "123!@#";

    parser.parse(lexeme, parent);

    assertTrue(parent.getChildren().isEmpty());
  }

  @Test
  void testLexemeParser() {
    SymbolParser symbolParser = new SymbolParser();
    LexemeParser lexemeParser = new LexemeParser(symbolParser);
    TextComposite parent = new TextComposite(TextComponentType.SENTENCE);
    String sentence = "hello world";

    lexemeParser.parse(sentence, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(2, children.size());

    assertEquals(TextComponentType.LEXEME, children.get(0).getComponentType());
    assertEquals(TextComponentType.LEXEME, children.get(1).getComponentType());

    TextComposite firstLexeme = (TextComposite) children.get(0);
    assertEquals(5, firstLexeme.getChildren().size()); // "hello" has 5 symbols
  }

  @Test
  void testLexemeParserWithMultipleSpaces() {
    SymbolParser symbolParser = new SymbolParser();
    LexemeParser lexemeParser = new LexemeParser(symbolParser);
    TextComposite parent = new TextComposite(TextComponentType.SENTENCE);
    String sentence = "  hello   world  ";

    lexemeParser.parse(sentence, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(2, children.size()); // Should ignore multiple spaces
  }

  @Test
  void testSentenceParser() {
    SymbolParser symbolParser = new SymbolParser();
    LexemeParser lexemeParser = new LexemeParser(symbolParser);
    SentenceParser sentenceParser = new SentenceParser(lexemeParser);
    TextComposite parent = new TextComposite(TextComponentType.PARAGRAPH);
    String paragraph = "First sentence. Second sentence! Third sentence?";

    sentenceParser.parse(paragraph, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(3, children.size());

    assertEquals(TextComponentType.SENTENCE, children.get(0).getComponentType());
    assertEquals(TextComponentType.SENTENCE, children.get(1).getComponentType());
    assertEquals(TextComponentType.SENTENCE, children.get(2).getComponentType());
  }

  @Test
  void testSentenceParserWithoutDelimiters() {
    SymbolParser symbolParser = new SymbolParser();
    LexemeParser lexemeParser = new LexemeParser(symbolParser);
    SentenceParser sentenceParser = new SentenceParser(lexemeParser);
    TextComposite parent = new TextComposite(TextComponentType.PARAGRAPH);
    String paragraph = "Just one sentence";

    sentenceParser.parse(paragraph, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(1, children.size());
    assertEquals(TextComponentType.SENTENCE, children.getFirst().getComponentType());
  }

  @Test
  void testParagraphParser() {
    SymbolParser symbolParser = new SymbolParser();
    LexemeParser lexemeParser = new LexemeParser(symbolParser);
    SentenceParser sentenceParser = new SentenceParser(lexemeParser);
    ParagraphParser paragraphParser = new ParagraphParser(sentenceParser);
    TextComposite parent = new TextComposite(TextComponentType.PARAGRAPH);

    String text = "First paragraph.    Second paragraph.    Third paragraph.";

    paragraphParser.parse(text, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(3, children.size());

    assertEquals(TextComponentType.PARAGRAPH, children.get(0).getComponentType());
    assertEquals(TextComponentType.PARAGRAPH, children.get(1).getComponentType());
    assertEquals(TextComponentType.PARAGRAPH, children.get(2).getComponentType());
  }

  @Test
  void testParagraphParserWithMultipleSpaces() {
    SymbolParser symbolParser = new SymbolParser();
    LexemeParser lexemeParser = new LexemeParser(symbolParser);
    SentenceParser sentenceParser = new SentenceParser(lexemeParser);
    ParagraphParser paragraphParser = new ParagraphParser(sentenceParser);
    TextComposite parent = new TextComposite(TextComponentType.PARAGRAPH);

    String text = "First.  Second.      Third.";

    paragraphParser.parse(text, parent);

    List<AbstractTextComponent> children = parent.getChildren();
    assertEquals(2, children.size());
  }

  @Test
  void testFullParsingChain() {
    SymbolParser symbolParser = new SymbolParser();
    LexemeParser lexemeParser = new LexemeParser(symbolParser);
    SentenceParser sentenceParser = new SentenceParser(lexemeParser);
    ParagraphParser paragraphParser = new ParagraphParser(sentenceParser);

    TextComposite root = new TextComposite(TextComponentType.PARAGRAPH);
    String text = "Hello world.    How are you?";

    paragraphParser.parse(text, root);

    List<AbstractTextComponent> paragraphs = root.getChildren();
    assertEquals(2, paragraphs.size());

    // First paragraph
    TextComposite firstParagraph = (TextComposite) paragraphs.get(0);
    List<AbstractTextComponent> firstSentences = firstParagraph.getChildren();
    assertEquals(1, firstSentences.size());

    TextComposite firstSentence = (TextComposite) firstSentences.get(0);
    List<AbstractTextComponent> firstLexemes = firstSentence.getChildren();
    assertEquals(2, firstLexemes.size()); // "Hello" and "world"

    // Second paragraph
    TextComposite secondParagraph = (TextComposite) paragraphs.get(1);
    List<AbstractTextComponent> secondSentences = secondParagraph.getChildren();
    assertEquals(1, secondSentences.size());

    TextComposite secondSentence = (TextComposite) secondSentences.get(0);
    List<AbstractTextComponent> secondLexemes = secondSentence.getChildren();
    assertEquals(3, secondLexemes.size()); // "How", "are", "you"
  }

  @Test
  void testWordParserIntegration() {
    WordParser wordParser = new WordParser();
    LexemeParser lexemeParser = new LexemeParser(wordParser);
    TextComposite parent = new TextComposite(TextComponentType.SENTENCE);
    String sentence = "hello123 world456";

    lexemeParser.parse(sentence, parent);

    List<AbstractTextComponent> lexemes = parent.getChildren();
    assertEquals(2, lexemes.size());

    TextComposite firstLexeme = (TextComposite) lexemes.getFirst();
    List<AbstractTextComponent> firstWords = firstLexeme.getChildren();
    assertEquals(1, firstWords.size());
    assertEquals(TextComponentType.WORD, firstWords.getFirst().getComponentType());
    assertEquals("hello", firstWords.getFirst().toString());
  }

  @Test
  void testParserChainWithWordParser() {
    WordParser wordParser = new WordParser();
    LexemeParser lexemeParser = new LexemeParser(wordParser);
    SentenceParser sentenceParser = new SentenceParser(lexemeParser);
    ParagraphParser paragraphParser = new ParagraphParser(sentenceParser);

    TextComposite root = new TextComposite(TextComponentType.PARAGRAPH);
    String text = "Test123 sentence.    Another456 text!";
    paragraphParser.parse(text, root);

    List<AbstractTextComponent> paragraphs = root.getChildren();
    assertEquals(2, paragraphs.size());

    // Verify words are extracted correctly (without numbers)
    TextComposite firstParagraph = (TextComposite) paragraphs.getFirst();
    TextComposite firstSentence = (TextComposite) firstParagraph.getChildren().getFirst();
    TextComposite firstLexeme = (TextComposite) firstSentence.getChildren().getFirst();
    TextLeaf firstWord = (TextLeaf) firstLexeme.getChildren().getFirst();

    assertEquals("Test", firstWord.toString());
    assertEquals(TextComponentType.WORD, firstWord.getComponentType());
  }
}
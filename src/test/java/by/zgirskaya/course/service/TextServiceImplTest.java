package by.zgirskaya.course.service;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;
import by.zgirskaya.course.exception.CustomTextException;
import by.zgirskaya.course.service.impl.TextServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextServiceImplTest {

  private TextServiceImpl textService;
  private TextComposite text;

  @BeforeEach
  void setUp() {
    textService = new TextServiceImpl();
    text = new TextComposite(TextComponentType.PARAGRAPH);
  }

  private TextComposite createSentence(String... words) {
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    for (String word : words) {
      TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
      TextLeaf wordLeaf = new TextLeaf(word, TextComponentType.WORD);
      lexeme.addChildComponent(wordLeaf);
      sentence.addChildComponent(lexeme);
    }
    return sentence;
  }

  private TextComposite createParagraph(TextComposite... sentences) {
    TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
    for (TextComposite sentence : sentences) {
      paragraph.addChildComponent(sentence);
    }
    return paragraph;
  }

  @Test
  void testFindMaxSentenceCountWithSameWords_WithMultipleOccurrences() throws CustomTextException {
    TextComposite sentence1 = createSentence("hello", "world");
    TextComposite sentence2 = createSentence("hello", "java");
    TextComposite sentence3 = createSentence("world", "programming");
    TextComposite sentence4 = createSentence("hello", "world", "test");

    TextComposite paragraph = createParagraph(sentence1, sentence2, sentence3, sentence4);
    text.addChildComponent(paragraph);

    int result = textService.findMaxSentenceCountWithSameWords(text);

    assertEquals(3, result); // "hello" in 3 sentences, "world" in 3 sentences
  }

  @Test
  void testFindMaxSentenceCountWithSameWords_WithUniqueWords() throws CustomTextException {
    TextComposite sentence1 = createSentence("apple", "banana");
    TextComposite sentence2 = createSentence("cherry", "date");
    TextComposite sentence3 = createSentence("elderberry", "fig");

    TextComposite paragraph = createParagraph(sentence1, sentence2, sentence3);
    text.addChildComponent(paragraph);

    int result = textService.findMaxSentenceCountWithSameWords(text);

    assertEquals(1, result); // all words are unique
  }

  @Test
  void testFindMaxSentenceCountWithSameWords_WithSingleSentence() throws CustomTextException {
    TextComposite sentence = createSentence("test", "sentence");

    int result = textService.findMaxSentenceCountWithSameWords(sentence);

    assertEquals(1, result); // only one sentence
  }

  @Test
  void testFindMaxSentenceCountWithSameWords_ThrowsExceptionForWrongComponentType() {
    TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);

    assertThrows(CustomTextException.class,
            () -> textService.findMaxSentenceCountWithSameWords(lexeme));
  }

  @Test
  void testDisplaySentencesByLexemeCountAscending() throws CustomTextException {
    TextComposite sentence1 = createSentence("short"); // 1 lexeme
    TextComposite sentence2 = createSentence("medium", "length"); // 2 lexeme
    TextComposite sentence3 = createSentence("this", "is", "longer"); // 3 lexeme

    TextComposite paragraph = createParagraph(sentence3, sentence1, sentence2);
    text.addChildComponent(paragraph);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    textService.displaySentencesByLexemeCountAscending(text);
    System.setOut(originalOut);

    String output = outputStream.toString();

    assertTrue(output.contains("Sentences in the ascending order by lexemes count:"));

    int shortIndex = output.indexOf("short");
    int mediumIndex = output.indexOf("medium");
    int thisIndex = output.indexOf("this");

    // Correct order: short (1) -> medium (2) -> this (3)
    assertTrue(shortIndex < mediumIndex);
    assertTrue(mediumIndex < thisIndex);
  }

  @Test
  void testChangeFirstAndLastLexemesInSentences_WithMultipleLexemes() throws CustomTextException {
    TextComposite sentence = createSentence("first", "second", "third", "fourth");
    String originalText = sentence.toString(); // "first second third fourth "

    AbstractTextComponent modifiedComponent = textService.changeFirstAndLastLexemesInSentences(sentence);
    String modifiedText = modifiedComponent.toString();

    assertEquals("first second third fourth ", sentence.toString()); // original is not changed
    assertNotEquals(originalText, modifiedText);
    assertEquals("fourth second third first ", modifiedText); // last lexeme -> first, first lexeme -> last
  }

  @Test
  void testChangeFirstAndLastLexemesInSentences_WithSingleLexeme() throws CustomTextException {
    TextComposite sentence = createSentence("single");
    String originalText = sentence.toString();

    AbstractTextComponent modifiedComponent = textService.changeFirstAndLastLexemesInSentences(sentence);

    assertEquals(originalText, sentence.toString());
    assertEquals(originalText, modifiedComponent.toString());
  }

  @Test
  void testChangeFirstAndLastLexemesInSentences_OriginalNotModified() throws CustomTextException {
    TextComposite sentence1 = createSentence("one", "two");
    TextComposite sentence2 = createSentence("three", "four", "five");
    TextComposite paragraph = createParagraph(sentence1, sentence2);
    TextComposite text = new TextComposite(TextComponentType.PARAGRAPH);
    text.addChildComponent(paragraph);

    String originalText = text.toString();

    AbstractTextComponent modifiedComponent = textService.changeFirstAndLastLexemesInSentences(text);

    assertEquals(originalText, text.toString());
    assertNotEquals(originalText, modifiedComponent.toString());
  }

  @Test
  void testChangeFirstAndLastLexemesInSentences_WithNestedStructure() throws CustomTextException {
    TextComposite sentence1 = createSentence("one", "two");
    TextComposite sentence2 = createSentence("three", "four", "five");
    TextComposite sentence3 = createSentence("six", "seven");

    TextComposite paragraph1 = createParagraph(sentence1, sentence2);
    TextComposite paragraph2 = createParagraph(sentence3);

    text.addChildComponent(paragraph1);
    text.addChildComponent(paragraph2);

    String originalSentence1 = sentence1.toString();
    String originalSentence2 = sentence2.toString();
    String originalSentence3 = sentence3.toString();

    AbstractTextComponent modifiedComponent = textService.changeFirstAndLastLexemesInSentences(text);

    assertEquals(originalSentence1, sentence1.toString());
    assertEquals(originalSentence2, sentence2.toString());
    assertEquals(originalSentence3, sentence3.toString());

    TextComposite modifiedText = (TextComposite) modifiedComponent;
    List<AbstractTextComponent> modifiedParagraphs = modifiedText.getChildComponents();

    TextComposite modifiedParagraph1 = (TextComposite) modifiedParagraphs.get(0);
    List<AbstractTextComponent> modifiedSentences1 = modifiedParagraph1.getChildComponents();

    assertEquals("two one ", modifiedSentences1.get(0).toString()); // "one two " -> "two one "
    assertEquals("five four three ", modifiedSentences1.get(1).toString()); // "three four five " -> "five four three "

    TextComposite modifiedParagraph2 = (TextComposite) modifiedParagraphs.get(1);
    List<AbstractTextComponent> modifiedSentences2 = modifiedParagraph2.getChildComponents();
    assertEquals("seven six ", modifiedSentences2.get(0).toString()); // "six seven " -> "seven six "
  }

  @Test
  void testChangeFirstAndLastLexemesInSentences_WithSentenceInput() throws CustomTextException {
    TextComposite sentence = createSentence("first", "last");
    String originalText = sentence.toString();

    AbstractTextComponent modifiedComponent = textService.changeFirstAndLastLexemesInSentences(sentence);
    String modifiedText = modifiedComponent.toString();

    assertEquals("first last ", sentence.toString()); // original is not changed
    assertNotEquals(originalText, modifiedText);
    assertEquals("last first ", modifiedText); // copy is changed
  }

  @Test
  void testComplexScenario_AllMethods() throws CustomTextException {
    TextComposite sentence1 = createSentence("java", "is", "great");
    TextComposite sentence2 = createSentence("python", "is", "also", "great");
    TextComposite sentence3 = createSentence("java", "and", "python");

    TextComposite paragraph = createParagraph(sentence1, sentence2, sentence3);
    text.addChildComponent(paragraph);

    // Test 1: findMaxSentenceCountWithSameWords
    int maxCount = textService.findMaxSentenceCountWithSameWords(text);
    assertEquals(2, maxCount);

    // Test 2: changeFirstAndLastLexemesInSentences
    AbstractTextComponent modifiedComponent = textService.changeFirstAndLastLexemesInSentences(text);

    // original is not changed
    assertEquals("java is great ", sentence1.toString());
    assertEquals("python is also great ", sentence2.toString());
    assertEquals("java and python ", sentence3.toString());

    // copy is changed
    TextComposite modifiedText = (TextComposite) modifiedComponent;
    TextComposite modifiedParagraph = (TextComposite) modifiedText.getChildComponents().get(0);
    List<AbstractTextComponent> modifiedSentences = modifiedParagraph.getChildComponents();

    assertEquals("great is java ", modifiedSentences.get(0).toString()); // "java is great " -> "great is java "
    assertEquals("great is also python ", modifiedSentences.get(1).toString()); // "python is also great " -> "great is also python "
    assertEquals("python and java ", modifiedSentences.get(2).toString()); // "java and python " -> "python and java "

    // Test 3: displaySentencesByLexemeCountAscending (проверяем что не падает)
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    textService.displaySentencesByLexemeCountAscending(text);
    System.setOut(originalOut);

    String output = outputStream.toString();
    assertTrue(output.contains("Sentences in the ascending order by lexemes count:"));
  }

  @Test
  void testEmptyText() throws CustomTextException {
    TextComposite emptyText = new TextComposite(TextComponentType.PARAGRAPH);

    assertEquals(0, textService.findMaxSentenceCountWithSameWords(emptyText));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    textService.displaySentencesByLexemeCountAscending(emptyText);
    System.setOut(originalOut);

    String output = outputStream.toString();
    assertTrue(output.contains("Sentences in the ascending order by lexemes count:"));

    assertDoesNotThrow(() -> textService.changeFirstAndLastLexemesInSentences(emptyText));
  }
}
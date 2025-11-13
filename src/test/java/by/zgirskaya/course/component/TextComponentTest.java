package by.zgirskaya.course.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TextComponentTest {

  @Test
  void testTextLeafCreation() {
    String text = "Hello";
    TextComponentType type = TextComponentType.WORD;

    TextLeaf leaf = new TextLeaf(text, type);

    assertEquals(text, leaf.toString());
    assertEquals(type, leaf.getComponentType());
  }

  @Test
  void testTextLeafToString() {
    TextLeaf leaf = new TextLeaf("Test", TextComponentType.SYMBOL);

    String result = leaf.toString();

    assertEquals("Test", result);
  }

  @Test
  void testTextCompositeCreation() {
    TextComponentType type = TextComponentType.SENTENCE;

    TextComposite composite = new TextComposite(type);

    assertEquals(type, composite.getComponentType());
    assertTrue(composite.getChildren().isEmpty());
  }

  @Test
  void testTextCompositeAddComponent() {
    TextComposite composite = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word = new TextLeaf("word", TextComponentType.WORD);

    composite.addChild(word);

    List<AbstractTextComponent> children = composite.getChildren();
    assertEquals(1, children.size());
    assertEquals(word, children.getFirst());
  }

  @Test
  void testTextCompositeMultipleComponents() {
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word1 = new TextLeaf("Hello", TextComponentType.WORD);
    TextLeaf word2 = new TextLeaf("World", TextComponentType.WORD);

    sentence.addChild(word1);
    sentence.addChild(word2);

    assertEquals(2, sentence.getChildren().size());
  }

  @Test
  void testTextCompositeToStringWithLexeme() {
    TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
    TextLeaf word = new TextLeaf("test", TextComponentType.WORD);
    lexeme.addChild(word);

    String result = lexeme.toString();

    assertEquals("test ", result);
  }

  @Test
  void testTextCompositeToStringWithParagraph() {
    TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word = new TextLeaf("word", TextComponentType.WORD);

    sentence.addChild(word);
    paragraph.addChild(sentence);

    String result = paragraph.toString();

    assertEquals("\tword", result);
  }

  @Test
  void testTextCompositeToStringWithSentence() {
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word1 = new TextLeaf("Hello", TextComponentType.WORD);
    TextLeaf word2 = new TextLeaf("World", TextComponentType.WORD);

    sentence.addChild(word1);
    sentence.addChild(word2);

    String result = sentence.toString();

    assertEquals("HelloWorld", result);
  }

  @Test
  void testTextCompositeNestedStructure() {
    TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
    TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);

    TextLeaf word1 = new TextLeaf("Hello", TextComponentType.WORD);
    TextLeaf word2 = new TextLeaf("World", TextComponentType.WORD);

    lexeme1.addChild(word1);
    lexeme2.addChild(word2);
    sentence.addChild(lexeme1);
    sentence.addChild(lexeme2);
    paragraph.addChild(sentence);

    String result = paragraph.toString();

    assertEquals("\tHello World ", result);
  }

  @Test
  void testTextCompositeGetChildrenReturnsCopy() {
    TextComposite composite = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word = new TextLeaf("test", TextComponentType.WORD);
    composite.addChild(word);

    List<AbstractTextComponent> children1 = composite.getChildren();
    List<AbstractTextComponent> children2 = composite.getChildren();

    assertNotSame(children1, children2);
    assertEquals(children1, children2);
  }

  @Test
  void testTextComponentTypeValues() {
    assertEquals(5, TextComponentType.values().length);
    assertArrayEquals(new TextComponentType[]{
            TextComponentType.PARAGRAPH,
            TextComponentType.SENTENCE,
            TextComponentType.LEXEME,
            TextComponentType.WORD,
            TextComponentType.SYMBOL
    }, TextComponentType.values());
  }

  @Test
  void testAbstractTextComponentTypeManagement() {
    AbstractTextComponent component = new TextComposite(TextComponentType.PARAGRAPH);

    component.setComponentType(TextComponentType.SENTENCE);

    assertEquals(TextComponentType.SENTENCE, component.getComponentType());
  }

  @Test
  void testMixedComponentTypesInComposite() {
    TextComposite composite = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word = new TextLeaf("word", TextComponentType.WORD);
    TextLeaf symbol = new TextLeaf("!", TextComponentType.SYMBOL);

    TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
    lexeme.addChild(word);

    composite.addChild(lexeme);
    composite.addChild(symbol);

    assertEquals(2, composite.getChildren().size());
    assertEquals("word !", composite.toString());
  }
}
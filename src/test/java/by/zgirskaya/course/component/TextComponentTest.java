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
    assertTrue(composite.getChildComponents().isEmpty());
  }

  @Test
  void testTextCompositeAddComponent() {
    TextComposite composite = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word = new TextLeaf("word", TextComponentType.WORD);

    composite.addChildComponent(word);

    List<AbstractTextComponent> children = composite.getChildComponents();
    assertEquals(1, children.size());
    assertEquals(word, children.getFirst());
  }

  @Test
  void testTextCompositeMultipleComponents() {
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word1 = new TextLeaf("Hello", TextComponentType.WORD);
    TextLeaf word2 = new TextLeaf("World", TextComponentType.WORD);

    sentence.addChildComponent(word1);
    sentence.addChildComponent(word2);

    assertEquals(2, sentence.getChildComponents().size());
  }

  @Test
  void testTextCompositeToStringWithLexeme() {
    TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
    TextLeaf word = new TextLeaf("test", TextComponentType.WORD);
    lexeme.addChildComponent(word);

    String result = lexeme.toString();

    assertEquals("test ", result);
  }

  @Test
  void testTextCompositeToStringWithSentence() {
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word1 = new TextLeaf("Hello", TextComponentType.WORD);
    TextLeaf word2 = new TextLeaf("World", TextComponentType.WORD);

    sentence.addChildComponent(word1);
    sentence.addChildComponent(word2);

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

    lexeme1.addChildComponent(word1);
    lexeme2.addChildComponent(word2);
    sentence.addChildComponent(lexeme1);
    sentence.addChildComponent(lexeme2);
    paragraph.addChildComponent(sentence);

    String result = paragraph.toString();

    assertEquals("\tHello World ", result);
  }

  @Test
  void testTextCompositeGetChildrenReturnsCopy() {
    TextComposite composite = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word = new TextLeaf("test", TextComponentType.WORD);
    composite.addChildComponent(word);

    List<AbstractTextComponent> children1 = composite.getChildComponents();
    List<AbstractTextComponent> children2 = composite.getChildComponents();

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
    lexeme.addChildComponent(word);

    composite.addChildComponent(lexeme);
    composite.addChildComponent(symbol);

    assertEquals(2, composite.getChildComponents().size());
    assertEquals("word !", composite.toString());
  }
}
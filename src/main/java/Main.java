import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;

public class Main {
  public static void main(String[] arg) {
    TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
    TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
    TextLeaf word = new TextLeaf("word", TextComponentType.WORD);

    sentence.addChild(word);
    paragraph.addChild(sentence);

    String result = paragraph.toString();

    System.out.println(result);
  }
}

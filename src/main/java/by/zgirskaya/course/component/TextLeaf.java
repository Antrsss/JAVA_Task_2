package by.zgirskaya.course.component;

public class TextLeaf extends AbstractTextComponent {

  private char text;

  public TextLeaf(char text, TextComponentType type) {
    this.text = text;
    this.type = type;
  }

  @Override
  public String toString() { return String.valueOf(this.text); }
}

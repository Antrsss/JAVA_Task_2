package by.zgirskaya.course.component;

public class TextLeaf extends AbstractTextComponent {

  private String text;

  public TextLeaf(String text, TextComponentType type) {
    this.text = text;
    this.setComponentType(type);
  }

  @Override
  public String toString() { return this.text; }
}

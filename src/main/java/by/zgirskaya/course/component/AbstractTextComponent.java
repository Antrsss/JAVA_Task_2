package by.zgirskaya.course.component;

public abstract class AbstractTextComponent {

  protected TextComponentType type;

  public TextComponentType getComponentType() {
    return this.type;
  }
  public void setComponentType(TextComponentType type) {
    this.type = type;
  }

  public abstract String toString();
}

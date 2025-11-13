package by.zgirskaya.course.component;

public abstract class AbstractTextComponent {

  private TextComponentType type;

  public TextComponentType getComponentType() {
    return this.type;
  }
  public void setComponentType(TextComponentType type) {
    this.type = type;
  }

  public abstract String toString();
  public abstract AbstractTextComponent makeCopy();
}

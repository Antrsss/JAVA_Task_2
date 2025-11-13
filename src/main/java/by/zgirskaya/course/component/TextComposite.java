package by.zgirskaya.course.component;

import java.util.ArrayList;
import java.util.List;

public class TextComposite extends AbstractTextComponent {

  private static final String PARAGRAPH = "\t";
  private static final String SPACE = " ";

  private ArrayList<AbstractTextComponent> components = new ArrayList<>();

  public TextComposite(TextComponentType type) {
    this.setComponentType(type);
  }

  public List<AbstractTextComponent> getChildren() { return new ArrayList<>(components); }

  public void setChildren(List<AbstractTextComponent> components) {
    this.components = new ArrayList<>(components);
  }

  public void addChild(AbstractTextComponent component) {
    components.add(component);
  }

  @Override
  public TextComposite makeCopy() {
    TextComposite copy = new TextComposite(this.getComponentType());

    for (AbstractTextComponent child : components) {
      copy.addChild(child.makeCopy());
    }

    return copy;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (this.getComponentType() == TextComponentType.PARAGRAPH) {
      sb.append(PARAGRAPH);
    }

    for (AbstractTextComponent component : components) {
      sb.append(component);
    }

    if (this.getComponentType() == TextComponentType.LEXEME) {
      sb.append(SPACE);
    }

    return sb.toString();
  }
}

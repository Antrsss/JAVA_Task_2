package by.zgirskaya.course.component;

import java.util.ArrayList;

public class TextComposite extends AbstractTextComponent {

  private static final String PARAGRAPH = "\t";
  private static final String SPACE = " ";

  private final ArrayList<AbstractTextComponent> components = new ArrayList<>();

  public TextComposite(TextComponentType type) {
    this.setComponentType(type);
  }

  public void addComponent(AbstractTextComponent component) {
    components.add(component);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (AbstractTextComponent component : components) {

      if (component.getComponentType() == TextComponentType.PARAGRAPH) {
        sb.append(PARAGRAPH);
      }

      sb.append(component);

      if (component.getComponentType() == TextComponentType.LEXEME) {
        sb.append(SPACE);
      }
    }

    return sb.toString();
  }
}

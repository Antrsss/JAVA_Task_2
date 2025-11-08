package by.zgirskaya.course.component;

import java.util.ArrayList;

public class TextComposite extends AbstractTextComponent {

  private static final String PARAGRAPH = "\t";
  private static final String SPACE = " ";

  private final ArrayList<AbstractTextComponent> components = new ArrayList<>();

  public TextComposite(TextComponentType type) {
    this.type = type;
  }

  public void addComponent(AbstractTextComponent component) {
    components.add(component);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (AbstractTextComponent component : components) {

      if (component.type == TextComponentType.PARAGRAPH) {
        sb.append(PARAGRAPH);
      }

      sb.append(component);

      if (component.type == TextComponentType.LEXEME) {
        sb.append(SPACE);
      }
    }

    return sb.toString();
  }
}

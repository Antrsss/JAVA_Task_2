package by.zgirskaya.course.component;

import java.util.ArrayList;

public class TextComposite extends AbstractTextComponent {

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
      sb.append(component.toString());
    }

    return sb.toString();
  }
}

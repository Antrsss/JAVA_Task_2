package by.zgirskaya.course.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextComposite extends AbstractTextComponent {
  private static final Logger logger = LogManager.getLogger();

  private static final String PARAGRAPH = "\t";
  private static final String SPACE = " ";

  private ArrayList<AbstractTextComponent> childComponents = new ArrayList<>();

  public TextComposite(TextComponentType type) {
    logger.debug("Creating TextComposite with type: {}", type);

    this.setComponentType(type);
  }

  public List<AbstractTextComponent> getChildComponents() {
    logger.debug("Getting child components. Current count: {}", childComponents.size());

    return new ArrayList<>(childComponents);
  }

  public void setChildComponents(List<AbstractTextComponent> components) {
    logger.debug("Setting child components. Old count: {}, New count: {}",
            childComponents.size(), components.size());

    childComponents = new ArrayList<>(components);
  }

  public void addChildComponent(AbstractTextComponent component) {
    logger.debug("Adding child component. Type: {}, Current children before: {}",
            component.getComponentType(), childComponents.size());

    childComponents.add(component);
  }

  @Override
  public String toString() {
    logger.debug("Starting toString() for TextComposite. Type: {}, Children count: {}",
            this.getComponentType(), childComponents.size());

    StringBuilder sb = new StringBuilder();

    for (AbstractTextComponent component : childComponents) {
      sb.append(component);

      switch (component.getComponentType()) {
        case TextComponentType.PARAGRAPH -> sb.append(PARAGRAPH);
        case TextComponentType.LEXEME -> sb.append(SPACE);
      }
    }

    return sb.toString();
  }

  @Override
  public TextComposite makeCopy() {
    logger.info("Creating deep copy of TextComposite. Type: {}, Children count: {}",
            this.getComponentType(), childComponents.size());

    TextComposite copy = new TextComposite(this.getComponentType());

    for (AbstractTextComponent child : childComponents) {
      AbstractTextComponent childCopy = child.makeCopy();
      copy.addChildComponent(childCopy);
    }

    logger.info("Deep copy completed successfully. Original children: {}, Copied children: {}",
            childComponents.size(), copy.childComponents.size());

    return copy;
  }
}
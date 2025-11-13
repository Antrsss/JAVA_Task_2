package by.zgirskaya.course.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextLeaf extends AbstractTextComponent {
  private static final Logger logger = LogManager.getLogger();

  private final String text;

  public TextLeaf(String text, TextComponentType type) {
    logger.debug("Creating TextLeaf. Type: {}, Text: '{}', Text length: {}",
            type, text, text.length());

    this.text = text;
    this.setComponentType(type);
  }

  @Override
  public TextLeaf makeCopy() {
    logger.debug("Creating copy of TextLeaf. Original type: {}, Text: '{}'",
            getComponentType(), text);

    return new TextLeaf(this.text, getComponentType());
  }

  @Override
  public String toString() {
    logger.debug("Calling toString() on TextLeaf. Type: {}, Text: '{}'",
            getComponentType(), text);

    return this.text;
  }
}
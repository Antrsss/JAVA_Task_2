package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;

public class SymbolParser extends AbstractParser {

  @Override
  public void parse(String lexeme, TextComposite parentComposite) {
    for (int i = 0; i < lexeme.length(); i++) {
      char character = lexeme.charAt(i);
      TextLeaf letterComponent = new TextLeaf(String.valueOf(character), TextComponentType.SYMBOL);
      parentComposite.addChild(letterComponent);
    }
  }
}

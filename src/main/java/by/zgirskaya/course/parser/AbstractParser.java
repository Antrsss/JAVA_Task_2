package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.TextComposite;

public abstract class AbstractParser {

  protected AbstractParser nextParser;

  public abstract void parse(String text, TextComposite parentComposite);
}

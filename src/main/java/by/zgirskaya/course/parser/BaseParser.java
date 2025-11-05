package by.zgirskaya.course.parser;

import by.zgirskaya.course.component.impl.TextComposite;

public abstract class BaseParser {

    protected BaseParser nextParser;

    public abstract void parseComposite(String text, TextComposite parentComposite);
}

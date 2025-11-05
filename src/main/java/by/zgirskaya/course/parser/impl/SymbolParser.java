package by.zgirskaya.course.parser.impl;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.impl.TextComposite;
import by.zgirskaya.course.component.impl.TextLeaf;
import by.zgirskaya.course.parser.BaseParser;

public class SymbolParser extends BaseParser {

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            String symbol = String.valueOf(character);

            TextLeaf letterComponent = new TextLeaf(symbol, TextComponentType.LETTER);
            parentComposite.addComponent(letterComponent);
        }
    }
}

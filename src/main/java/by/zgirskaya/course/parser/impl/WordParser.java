package by.zgirskaya.course.parser.impl;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.impl.TextComposite;
import by.zgirskaya.course.parser.BaseParser;

public class WordParser extends BaseParser {

    private static final String WORD_DELIMITER = "[^a-zA-Z]+";

    public WordParser(SymbolParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] wordArray = text.split(WORD_DELIMITER);

        for (var string : wordArray) {
            TextComposite word = new TextComposite(TextComponentType.WORD);
            parentComposite.addComponent(word);
            nextParser.parseComposite(string, word);
        }
    }
}

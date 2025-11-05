package by.zgirskaya.course.parser.impl;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.impl.TextComposite;
import by.zgirskaya.course.parser.BaseParser;

public class LexemeParser extends BaseParser {

    private static final String LEXEME_DELIMITER = "\\s+";

    public LexemeParser(WordParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] lexemeArray = text.split(LEXEME_DELIMITER);

        for (var string : lexemeArray) {
            TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
            parentComposite.addComponent(lexeme);
            nextParser.parseComposite(string, lexeme);
        }
    }
}

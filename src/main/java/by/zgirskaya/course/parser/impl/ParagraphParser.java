package by.zgirskaya.course.parser.impl;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.impl.TextComposite;
import by.zgirskaya.course.parser.BaseParser;

public class ParagraphParser extends BaseParser {

    private static final String PARAGRAPH_DELIMITER = "\\p{Blank}{4}";
    private static final String TO_REPLACE_REGEX = "\\p{Blank}{2,}";
    private static final String REPLACE_REGEX_ON = " ";

    public ParagraphParser(SentenceParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] stringArray = text.split(PARAGRAPH_DELIMITER);

        for (var string : stringArray) {
            string = string.replaceAll(TO_REPLACE_REGEX, REPLACE_REGEX_ON);
            TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
            parentComposite.addComponent(paragraph);
            nextParser.parseComposite(string, paragraph);
        }
    }
}

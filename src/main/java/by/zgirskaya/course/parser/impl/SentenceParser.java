package by.zgirskaya.course.parser.impl;

import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.impl.TextComposite;
import by.zgirskaya.course.parser.BaseParser;

public class SentenceParser extends BaseParser {

    private static final String SENTENCE_DELIMITER = "(?<=[.!?])\\s+";

    public SentenceParser(LexemeParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] sentenceArray = text.split(SENTENCE_DELIMITER);

        for (var string : sentenceArray) {
            TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
            parentComposite.addComponent(sentence);
            nextParser.parseComposite(string, sentence);
        }
    }
}

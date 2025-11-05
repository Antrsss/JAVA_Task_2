package by.zgirskaya.course.component.impl;

import by.zgirskaya.course.component.TextComponent;
import by.zgirskaya.course.component.TextComponentType;

public class TextLeaf extends TextComponent {

    private String text;

    public TextLeaf(String text, TextComponentType type) {
        this.text = text;
        this.type = type;
    }

    @Override
    public String getText() {
        return this.text;
    }
}

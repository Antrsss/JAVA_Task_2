package by.zgirskaya.course.component.impl;

import by.zgirskaya.course.component.TextComponent;
import by.zgirskaya.course.component.TextComponentType;

import java.util.ArrayList;

public class TextComposite extends TextComponent {

    private final ArrayList<TextComponent> components = new ArrayList<>();

    public TextComposite(TextComponentType type) {
        this.type = type;
    }

    public void addComponent(TextComponent component) {
        components.add(component);
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();

        for (TextComponent component : components) {
            sb.append(component.getText());
        }

        return sb.toString();
    }
}

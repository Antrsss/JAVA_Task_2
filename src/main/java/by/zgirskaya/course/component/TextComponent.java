package by.zgirskaya.course.component;

public abstract class TextComponent {

    protected TextComponentType type;

    public TextComponentType getComponentType() {
        return this.type;
    }
    public void setComponentType(TextComponentType type) {
        this.type = type;
    }

    public abstract String getText();
}

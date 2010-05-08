package org.lazaro.rt5e.logic.mask;

/**
 * @author Lazaro
 */
public class ChatMessage implements Mask {
    private final int color, effects;
    private final String text;

    public ChatMessage(String text, int color, int effects) {
        this.text = text;
        this.color = color;
        this.effects = effects;
    }

    public int getColor() {
        return color;
    }

    public int getEffects() {
        return effects;
    }

    public String getText() {
        return text;
    }
}

package com.cypher.aes.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Utilidad para crear JLabels que renderizan emojis correctamente en Windows.
 * Usa "Segoe UI Emoji" como primera fuente y hace fallback a "Segoe UI".
 */
public final class EmojiLabel {

    private EmojiLabel() {}

    /**
     * Crea un JLabel con fuente que soporta emojis.
     */
    public static JLabel createEmojiLabel(String text, Font emojiFont, Font fallbackFont, Color foreColor) {
        JLabel label = new JLabel(text);
        label.setFont(createFont(emojiFont, fallbackFont));
        label.setForeground(foreColor);
        return label;
    }

    /**
     * Crea un JLabel con fuente que soporta emojis (color por defecto).
     */
    public static JLabel createEmojiLabel(String text, Font emojiFont, Font fallbackFont) {
        return createEmojiLabel(text, emojiFont, fallbackFont, Color.WHITE);
    }

    /**
     * Crea un JLabel con fuente que soporta emojis (color por defecto, negrita).
     */
    public static JLabel createEmojiLabel(String text, Font fallbackFont, Color foreColor) {
        JLabel label = new JLabel(text);
        label.setFont(createFont(new Font("Segoe UI Emoji", Font.PLAIN, 16), fallbackFont));
        label.setForeground(foreColor);
        return label;
    }

    /**
     * Crea un JLabel con fuente que soporta emojis (tamaño por defecto 16).
     */
    public static JLabel createEmojiLabel(String text, Font fallbackFont) {
        JLabel label = new JLabel(text);
        label.setFont(createFont(new Font("Segoe UI Emoji", Font.PLAIN, 16), fallbackFont));
        return label;
    }

    /**
     * Crea un Font que soporta emojis, con fallback a la fuente normal.
     * El truco: crear una fuente con nombre "Segoe UI Emoji" pero con la
     * familia de "Segoe UI" para que la tipografía general sea consistente.
     */
    public static Font createFont(Font emojiFont, Font fallbackFont) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();

        boolean hasEmoji = false;
        for (String f : fonts) {
            if (f.equalsIgnoreCase("Segoe UI Emoji")) {
                hasEmoji = true;
                break;
            }
        }

        if (hasEmoji) {
            // Crear fuente con familia "Segoe UI Emoji" pero estilo/tamaño de fallback
            return new Font("Segoe UI Emoji", fallbackFont.getStyle(), fallbackFont.getSize());
        }

        // Fallback: usar la fuente normal
        return fallbackFont;
    }
}

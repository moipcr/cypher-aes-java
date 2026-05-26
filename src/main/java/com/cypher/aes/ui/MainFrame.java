package com.cypher.aes.ui;

import com.cypher.aes.crypto.AesCipher;
import com.cypher.aes.ui.components.CipherPanel;
import com.cypher.aes.ui.components.DecipherPanel;
import com.cypher.aes.ui.components.EmojiLabel;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación AES Cipher.
 */
public class MainFrame extends JFrame {

    private static final String APP_TITLE = "AES Cipher - Cifrador Simétrico";
    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 800;

    private static final Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 28);

    private final AesCipher aesCipher;
    private CipherPanel cipherPanel;
    private DecipherPanel decipherPanel;
    private JLabel statusTextLabel;

    public MainFrame() {
        this.aesCipher = new AesCipher();
        initializeWindow();
        buildUI();
    }

    public MainFrame(AesCipher aesCipher) {
        this.aesCipher = aesCipher;
        initializeWindow();
        buildUI();
    }

    private void initializeWindow() {
        setTitle(APP_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 650));

        // Usar FlatLaf Modern Dark
        UIManager.put("DefaultTextPane.background", new Color(30, 33, 40));
        UIManager.put("TextField.background", new Color(30, 33, 40));
        UIManager.put("TextArea.background", new Color(30, 33, 40));
        UIManager.put("Label.background", new Color(30, 33, 40));
        UIManager.put("Panel.background", new Color(30, 33, 40));
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 0));

        // Header superior
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Contenido principal dividido en dos paneles
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setDividerLocation(0.5);
        mainSplit.setResizeWeight(0.5);
        mainSplit.setDividerSize(8);
        mainSplit.setDividerLocation(400);
        mainSplit.setContinuousLayout(true);

        // Paneles de cifrado y descifrado
        cipherPanel = new CipherPanel(aesCipher, this);
        decipherPanel = new DecipherPanel(aesCipher, cipherPanel, this);

        mainSplit.setTopComponent(cipherPanel);
        mainSplit.setBottomComponent(decipherPanel);

        add(mainSplit, BorderLayout.CENTER);

        // Barra de estado inferior
        JPanel statusPanel = createStatusBar();
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(22, 27, 34));
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        // Logo / Título principal con soporte de emojis
        JLabel titleLabel = new JLabel("🔐 AES Cipher");
        titleLabel.setFont(EmojiLabel.createFont(EMOJI_FONT, new Font("Segoe UI", Font.BOLD, 28)));
        titleLabel.setForeground(new Color(136, 192, 208));
        header.add(titleLabel, BorderLayout.WEST);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Cifrado simétrico AES-256-GCM");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(139, 148, 158));
        header.add(subtitleLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(22, 27, 34));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(6, 24, 6, 24));

        statusTextLabel = new JLabel("Listo — Escribe un mensaje y genera una clave para cifrar");
        statusTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusTextLabel.setForeground(new Color(139, 148, 158));
        statusPanel.add(statusTextLabel, BorderLayout.WEST);

        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(82, 87, 97));
        statusPanel.add(versionLabel, BorderLayout.EAST);

        return statusPanel;
    }

    /**
     * Actualiza el texto de la barra de estado.
     */
    public void updateStatus(String message) {
        if (statusTextLabel != null) {
            statusTextLabel.setText(message);
        }
    }

    /**
     * Muestra un mensaje de error.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de información.
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Getter para el panel de cifrado (usado por el panel de descifrado).
     */
    public CipherPanel getCipherPanel() {
        return cipherPanel;
    }

    /**
     * Getter para el generador de claves.
     */
    public AesCipher getAesCipher() {
        return aesCipher;
    }

    /**
     * Actualiza el contador de caracteres del panel de cifrado.
     */
    public void updateCharCount(int count, int max) {
        cipherPanel.updateCharCount(count, max);
    }

    /**
     * Establece el texto del campo de clave en el panel de cifrado.
     */
    public void setCipherKey(String key) {
        cipherPanel.setKeyFieldText(key);
    }

    /**
     * Establece el texto del campo de IV en el panel de cifrado.
     */
    public void setCipherIV(String iv) {
        cipherPanel.setIVFieldText(iv);
    }

    /**
     * Establece el texto del campo de resultado en el panel de cifrado.
     */
    public void setCipherResult(String result) {
        cipherPanel.setResultFieldText(result);
    }

    /**
     * Establece el texto del campo de texto original en el panel de cifrado.
     */
    public void setCipherInputText(String text) {
        cipherPanel.setInputFieldText(text);
    }

    /**
     * Establece el texto del campo de IV en el panel de descifrado.
     */
    public void setDecipherIV(String iv) {
        decipherPanel.setIVFieldText(iv);
    }

    /**
     * Establece el texto del campo de resultado en el panel de descifrado.
     */
    public void setDecipherResult(String result) {
        decipherPanel.setResultFieldText(result);
    }

    /**
     * Establece el texto del campo de texto cifrado en el panel de descifrado.
     */
    public void setDecipherInputText(String text) {
        decipherPanel.setCipheredInputFieldText(text);
    }
}

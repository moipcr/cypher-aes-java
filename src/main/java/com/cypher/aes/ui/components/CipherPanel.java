package com.cypher.aes.ui.components;

import com.cypher.aes.crypto.AesCipher;
import com.cypher.aes.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * Panel superior para cifrar mensajes.
 */
public class CipherPanel extends JPanel {

    private static final int MAX_CHARS = 500;
    private static final int TEXTAREA_ROWS = 8;
    private static final int TEXTAREA_COLUMNS = 60;

    // Fuente que soporta emojis en Windows
    private static final Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 16);

    private final AesCipher aesCipher;
    private final MainFrame mainFrame;

    private JTextArea inputTextArea;
    private JTextField keyField;
    private JTextField ivField;
    private JTextArea resultTextArea;
    private JLabel charCountLabel;

    private String currentKey = "";
    private String currentIV = "";

    public CipherPanel(AesCipher aesCipher, MainFrame mainFrame) {
        this.aesCipher = aesCipher;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setBackground(new Color(30, 33, 40));
        buildUI();
    }

    private void buildUI() {
        // Título del panel con fuente de emojis
        JLabel titleLabel = new JLabel("🔒 CIFRAR MENSAJE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(136, 192, 208));
        titleLabel.setFont(EmojiLabel.createFont(EMOJI_FONT, new Font("Segoe UI", Font.BOLD, 16)));
        add(titleLabel, BorderLayout.NORTH);

        // Contenido principal
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(30, 33, 40));
        // Fijar tamaño mínimo para que GridBagLayout no colapse
        contentPanel.setMinimumSize(new Dimension(800, 500));
        contentPanel.setPreferredSize(new Dimension(800, 500));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // 2 columnas, ambas con weight
        gbc.gridwidth = 1;

        // Sección de texto a cifrar
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.0;
        contentPanel.add(createLabel("Texto a cifrar:"), gbc);

        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        inputTextArea = createStyledTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
        inputTextArea.setPreferredSize(new Dimension(500, 180));
        inputTextArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
        });
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputScrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        inputScrollPane.setBackground(new Color(30, 33, 40));
        inputScrollPane.getViewport().setBackground(new Color(30, 33, 40));
        inputScrollPane.setMinimumSize(new Dimension(400, 180));
        contentPanel.add(inputScrollPane, gbc);

        // Contador de caracteres
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        charCountLabel = new JLabel("0 / " + MAX_CHARS + " caracteres");
        charCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        charCountLabel.setForeground(new Color(139, 148, 158));
        contentPanel.add(charCountLabel, gbc);

        // Sección de clave
        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(createLabel("Clave AES-256 (Base64):"), gbc);

        gbc.gridy = 4;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        keyField = createStyledTextField(3);
        keyField.setEditable(false);
        keyField.setBackground(new Color(40, 45, 54));
        keyField.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        contentPanel.add(keyField, gbc);

        // IV
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        contentPanel.add(createLabel("IV (Base64):"), gbc);

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        ivField = createStyledTextField(3);
        ivField.setEditable(false);
        ivField.setBackground(new Color(40, 45, 54));
        ivField.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        contentPanel.add(ivField, gbc);

        // Botones de acción
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        buttonRow.setBackground(new Color(30, 33, 40));

        JButton generateKeyBtn = createAccentButton("⚡ Generar Clave", new Color(78, 205, 196));
        generateKeyBtn.addActionListener(e -> generateNewKey());

        JButton copyKeyBtn = createSecondaryButton("📋 Copiar Clave");
        copyKeyBtn.addActionListener(e -> copyToClipboard(keyField.getText()));

        JButton copyIVBtn = createSecondaryButton("📋 Copiar IV");
        copyIVBtn.addActionListener(e -> copyToClipboard(ivField.getText()));

        buttonRow.add(generateKeyBtn);
        buttonRow.add(copyKeyBtn);
        buttonRow.add(copyIVBtn);
        contentPanel.add(buttonRow, gbc);

        // Botón cifrar principal
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton cipherBtn = createPrimaryButton("🔐 CIFRAR MENSAJE");
        cipherBtn.setPreferredSize(new Dimension(200, 40));
        cipherBtn.addActionListener(e -> cipherMessage());
        contentPanel.add(cipherBtn, gbc);

        // Sección de resultado
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        contentPanel.add(createLabel("Resultado (Base64):"), gbc);

        gbc.gridy = 10;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        resultTextArea = createStyledTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
        resultTextArea.setEditable(false);
        resultTextArea.setBackground(new Color(40, 45, 54));
        resultTextArea.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBackground(new Color(30, 33, 40));
        resultScrollPane.getViewport().setBackground(new Color(30, 33, 40));
        resultScrollPane.setMinimumSize(new Dimension(400, 180));
        contentPanel.add(resultScrollPane, gbc);

        // Botón copiar resultado
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton copyResultBtn = createSecondaryButton("📋 Copiar Resultado");
        copyResultBtn.addActionListener(e -> copyToClipboard(resultTextArea.getText()));
        contentPanel.add(copyResultBtn, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 500);
    }

    private void generateNewKey() {
        String[] keyAndIV = aesCipher.generateKeyAndIV();
        currentKey = keyAndIV[0];
        currentIV = keyAndIV[1];
        keyField.setText(currentKey);
        ivField.setText(currentIV);
        mainFrame.updateStatus("✅ Nueva clave generada — copia y guarda esta clave para descifrar después");
    }

    private void cipherMessage() {
        String input = inputTextArea.getText().trim();
        if (input.isEmpty()) {
            mainFrame.showError("El campo de texto no puede estar vacío.");
            return;
        }
        if (input.length() > MAX_CHARS) {
            mainFrame.showError("El texto excede el límite de " + MAX_CHARS + " caracteres.");
            return;
        }
        if (currentKey.isEmpty() || currentIV.isEmpty()) {
            mainFrame.showError("Primero genera una clave con el botón 'Generar Clave'.");
            return;
        }

        try {
            String encrypted = aesCipher.encrypt(input, aesCipher.stringToSecretKey(currentKey), aesCipher.stringToIV(currentIV));
            resultTextArea.setText(encrypted);
            mainFrame.updateStatus("✅ Mensaje cifrado correctamente — " + encrypted.length() + " caracteres en Base64");
        } catch (Exception e) {
            mainFrame.showError("Error al cifrar: " + e.getMessage());
        }
    }

    private void updateCharCount() {
        int count = inputTextArea.getText().length();
        charCountLabel.setText(count + " / " + MAX_CHARS + " caracteres");
        if (count > MAX_CHARS) {
            charCountLabel.setForeground(Color.RED);
        } else if (count > MAX_CHARS * 0.9) {
            charCountLabel.setForeground(new Color(210, 153, 34));
        } else {
            charCountLabel.setForeground(new Color(139, 148, 158));
        }
    }

    private JLabel createLabel(String text) {
        return EmojiLabel.createEmojiLabel(text, new Font("Segoe UI", Font.BOLD, 13), new Color(199, 208, 216));
    }

    private JTextArea createStyledTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(8, 10, 8, 10));
        textArea.setBackground(new Color(30, 33, 40));
        textArea.setForeground(new Color(210, 215, 222));
        textArea.setCaretColor(new Color(136, 192, 208));
        return textArea;
    }

    private JTextField createStyledTextField(int rows) {
        JTextField field = new JTextField(rows);
        field.setFont(new Font("Consolas", Font.PLAIN, 13));
        field.setForeground(new Color(210, 215, 222));
        field.setBackground(new Color(40, 45, 54));
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(EmojiLabel.createFont(EMOJI_FONT, new Font("Segoe UI", Font.BOLD, 14)));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(49, 130, 206));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(49, 130, 206), 2),
                BorderFactory.createEmptyBorder(10, 24, 10, 24)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createAccentButton(String text, Color accentColor) {
        JButton button = new JButton(text);
        button.setFont(EmojiLabel.createFont(EMOJI_FONT, new Font("Segoe UI", Font.BOLD, 12)));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(accentColor);
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(EmojiLabel.createFont(EMOJI_FONT, new Font("Segoe UI", Font.PLAIN, 12)));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(new Color(139, 148, 158));
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void copyToClipboard(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        mainFrame.updateStatus("📋 Copiado al portapapeles");
    }

    // Getters para setters del MainFrame
    public void updateCharCount(int count, int max) {
        charCountLabel.setText(count + " / " + max + " caracteres");
    }

    public void setKeyFieldText(String text) {
        keyField.setText(text);
    }

    public void setIVFieldText(String text) {
        ivField.setText(text);
    }

    public void setResultFieldText(String text) {
        resultTextArea.setText(text);
    }

    public void setInputFieldText(String text) {
        inputTextArea.setText(text);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}

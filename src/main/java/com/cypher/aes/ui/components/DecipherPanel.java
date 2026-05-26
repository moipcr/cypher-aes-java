package com.cypher.aes.ui.components;

import com.cypher.aes.crypto.AesCipher;
import com.cypher.aes.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * Panel inferior para descifrar mensajes.
 */
public class DecipherPanel extends JPanel {

    private static final int TEXTAREA_ROWS = 8;
    private static final int TEXTAREA_COLUMNS = 60;

    // Fuente que soporta emojis en Windows
    private static final Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 16);

    private final AesCipher aesCipher;
    private final MainFrame mainFrame;
    private final CipherPanel cipherPanel;

    private JTextField decipherKeyField;
    private JTextField decipherIVField;
    private JTextArea cipheredInputTextArea;
    private JTextArea decipherResultTextArea;

    public DecipherPanel(AesCipher aesCipher, CipherPanel cipherPanel, MainFrame mainFrame) {
        this.aesCipher = aesCipher;
        this.cipherPanel = cipherPanel;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setBackground(new Color(30, 33, 40));
        buildUI();
    }

    private void buildUI() {
        // Título del panel con fuente de emojis
        JLabel titleLabel = new JLabel("🔓 DESCIFRAR MENSAJE");
        titleLabel.setFont(EmojiLabel.createFont(EMOJI_FONT, new Font("Segoe UI", Font.BOLD, 16)));
        titleLabel.setForeground(new Color(136, 192, 208));
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

        // Sección de clave
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.0;
        contentPanel.add(createLabel("Clave AES-256 (Base64):"), gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.0;
        gbc.weightx = 0.5;
        decipherKeyField = createStyledTextField(3);
        decipherKeyField.setBackground(new Color(40, 45, 54));
        decipherKeyField.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        contentPanel.add(decipherKeyField, gbc);

        // IV
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        contentPanel.add(createLabel("IV (Base64):"), gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        decipherIVField = createStyledTextField(3);
        decipherIVField.setBackground(new Color(40, 45, 54));
        decipherIVField.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        contentPanel.add(decipherIVField, gbc);

        // Botones de autocompletado desde panel de cifrado
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JPanel autoFillRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        autoFillRow.setBackground(new Color(30, 33, 40));

        JButton autoFillBtn = createAccentButton("🔄 Autocompletar desde Cifrado", new Color(78, 205, 196));
        autoFillBtn.addActionListener(e -> autoFillFromCipher());

        JButton clearDecipherBtn = createSecondaryButton("🗑️ Limpiar Todo");
        clearDecipherBtn.addActionListener(e -> clearAll());

        autoFillRow.add(autoFillBtn);
        autoFillRow.add(clearDecipherBtn);
        contentPanel.add(autoFillRow, gbc);

        // Sección de texto cifrado
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        contentPanel.add(createLabel("Mensaje cifrado (Base64):"), gbc);

        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        cipheredInputTextArea = createStyledTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
        cipheredInputTextArea.setPreferredSize(new Dimension(500, 180));
        cipheredInputTextArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updatePasteHint(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updatePasteHint(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updatePasteHint(); }
        });
        JScrollPane cipheredScrollPane = new JScrollPane(cipheredInputTextArea);
        cipheredScrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        cipheredScrollPane.setBackground(new Color(30, 33, 40));
        cipheredScrollPane.getViewport().setBackground(new Color(30, 33, 40));
        cipheredScrollPane.setMinimumSize(new Dimension(400, 180));
        contentPanel.add(cipheredScrollPane, gbc);

        // Botón descifrar principal
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton decipherBtn = createPrimaryButton("🔓 DESCIFRAR MENSAJE");
        decipherBtn.setPreferredSize(new Dimension(200, 40));
        decipherBtn.addActionListener(e -> decipherMessage());
        contentPanel.add(decipherBtn, gbc);

        // Sección de resultado
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        contentPanel.add(createLabel("Mensaje descifrado:"), gbc);

        gbc.gridy = 9;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        decipherResultTextArea = createStyledTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
        decipherResultTextArea.setEditable(false);
        decipherResultTextArea.setBackground(new Color(40, 45, 54));
        decipherResultTextArea.setBorder(BorderFactory.createLineBorder(new Color(55, 60, 69), 1));
        JScrollPane resultScrollPane = new JScrollPane(decipherResultTextArea);
        resultScrollPane.setBackground(new Color(30, 33, 40));
        resultScrollPane.getViewport().setBackground(new Color(30, 33, 40));
        resultScrollPane.setMinimumSize(new Dimension(400, 180));
        contentPanel.add(resultScrollPane, gbc);

        // Botón copiar resultado
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton copyResultBtn = createSecondaryButton("📋 Copiar Resultado");
        copyResultBtn.addActionListener(e -> copyToClipboard(decipherResultTextArea.getText()));
        contentPanel.add(copyResultBtn, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 500);
    }

    private void autoFillFromCipher() {
        String key = cipherPanel != null ? getKeyFromCipherPanel() : "";
        String iv = cipherPanel != null ? getIVFromCipherPanel() : "";

        if (key.isEmpty()) {
            mainFrame.showError("No hay clave generada en el panel de cifrado. Genera una clave primero.");
            return;
        }

        decipherKeyField.setText(key);
        decipherIVField.setText(iv);

        // Copiar también el resultado del cifrado al campo de texto cifrado
        String cipherResult = getResultFromCipherPanel();
        if (!cipherResult.isEmpty()) {
            cipheredInputTextArea.setText(cipherResult);
        }

        mainFrame.updateStatus("🔄 Clave e IV autocompletados desde el panel de cifrado");
    }

    private String getKeyFromCipherPanel() {
        JTextField keyField = getKeyField();
        return keyField != null ? keyField.getText() : "";
    }

    private String getIVFromCipherPanel() {
        JTextField ivField = getIVField();
        return ivField != null ? ivField.getText() : "";
    }

    private String getResultFromCipherPanel() {
        JTextArea resultArea = getResultArea();
        return resultArea != null ? resultArea.getText() : "";
    }

    private void decipherMessage() {
        String key = decipherKeyField.getText().trim();
        String iv = decipherIVField.getText().trim();
        String ciphertext = cipheredInputTextArea.getText().trim();

        if (key.isEmpty() || iv.isEmpty()) {
            mainFrame.showError("Debes proporcionar la clave y el IV generados durante el cifrado.");
            return;
        }
        if (ciphertext.isEmpty()) {
            mainFrame.showError("El campo de mensaje cifrado no puede estar vacío.");
            return;
        }

        try {
            String decrypted = aesCipher.decrypt(ciphertext, aesCipher.stringToSecretKey(key), aesCipher.stringToIV(iv));
            decipherResultTextArea.setText(decrypted);
            mainFrame.updateStatus("✅ Mensaje descifrado correctamente");
        } catch (Exception e) {
            mainFrame.showError("Error al descifrar: " + e.getMessage());
            decipherResultTextArea.setText("");
        }
    }

    private void clearAll() {
        decipherKeyField.setText("");
        decipherIVField.setText("");
        cipheredInputTextArea.setText("");
        decipherResultTextArea.setText("");
        mainFrame.updateStatus("🗑️ Panel de descifrado limpiado");
    }

    private void updatePasteHint() {
        if (cipheredInputTextArea.getText().isEmpty()) {
            mainFrame.updateStatus("Pega tu texto cifrado aquí y proporciona la clave para descifrar");
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
        button.setBackground(new Color(78, 140, 206));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(78, 140, 206), 2),
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

    // Getters para los campos (usados por autoFill)
    public JTextField getKeyField() {
        return decipherKeyField;
    }

    public JTextField getIVField() {
        return decipherIVField;
    }

    public JTextArea getResultArea() {
        return decipherResultTextArea;
    }

    // Setters para el MainFrame
    public void setIVFieldText(String text) {
        decipherIVField.setText(text);
    }

    public void setResultFieldText(String text) {
        decipherResultTextArea.setText(text);
    }

    public void setCipheredInputFieldText(String text) {
        cipheredInputTextArea.setText(text);
    }
}

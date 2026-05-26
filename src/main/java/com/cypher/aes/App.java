package com.cypher.aes;

import com.cypher.aes.crypto.AesCipher;
import com.cypher.aes.ui.MainFrame;

import javax.swing.*;

/**
 * Punto de entrada de la aplicaciÃ³n AES Cipher.
 * Inicializa el tema FlatLaf y lanza la interfaz grÃ¡fica.
 */
public class App {

    public static void main(String[] args) {
        // Configurar FlatLaf Modern Dark
        try {
            // Configurar FlatLaf antes de crear cualquier componente Swing
            com.formdev.flatlaf.FlatLightLaf.setup();
            com.formdev.flatlaf.FlatDarkLaf.setup();
            com.formdev.flatlaf.FlatLaf.setGlobalExtraDefaults(new java.util.HashMap<>());
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo cargar FlatLaf. Usando L&F por defecto.");
            e.printStackTrace();
        }

        // Iniciar aplicaciÃ³n en el EDT
        SwingUtilities.invokeLater(() -> {
            try {
                AesCipher aesCipher = new AesCipher();
                MainFrame mainFrame = new MainFrame(aesCipher);
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

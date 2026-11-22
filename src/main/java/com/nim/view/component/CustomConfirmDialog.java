package com.nim.view.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomConfirmDialog extends JDialog {
    private boolean confirmed = false;

    public CustomConfirmDialog(Window parent, String title, String message) {
        super(parent, title, ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 255, 255)); //

        // --- Phần chữ thông báo ---
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        messageLabel.setBorder(new EmptyBorder(25, 20, 15, 20));
        add(messageLabel, BorderLayout.CENTER);

        // --- Phần nút bấm ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        CustomButton btnYes = new CustomButton("Xóa", new Color(220, 58, 58), 90, 35);
        btnYes.setFont(new Font("Arial", Font.BOLD, 14));
        btnYes.setForeground(Color.WHITE);
        btnYes.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        CustomButton btnNo = new CustomButton("Hủy", new Color(83, 172, 255), 90, 35); // Nút xám
        btnNo.setFont(new Font("Arial", Font.BOLD, 14));
        btnNo.setForeground(Color.WHITE);
        btnNo.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(btnYes);
        buttonPanel.add(btnNo);

        add(buttonPanel, BorderLayout.SOUTH);

        // --- Cài đặt hiển thị ---
        setSize(350, 180);
        setLocationRelativeTo(parent); // Hiển thị ở giữa màn hình cha
        setResizable(false);
    }

    // Hàm static để gọi nhanh giống như JOptionPane
    public static boolean showDialog(Component parentComponent, String title, String message) {
        Window window = SwingUtilities.getWindowAncestor(parentComponent);
        CustomConfirmDialog dialog = new CustomConfirmDialog(window, title, message);
        dialog.setVisible(true); // Dừng luồng tại đây chờ người dùng bấm nút
        return dialog.confirmed;
    }
}
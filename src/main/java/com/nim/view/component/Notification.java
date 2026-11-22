package com.nim.view.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Notification extends JDialog {

    public enum Type {
        SUCCESS, ERROR, WARNING, INFO
    }

    private float opacity = 0f;


    public Notification(JFrame parent, Type type, String messageText) {
        super(parent, false);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(420, 150);
        setLocationRelativeTo(parent);

        String iconChar = "ℹ";
        String mainColorHex = "#3b82f6";

        switch (type) {
            case SUCCESS: iconChar = "✓"; mainColorHex = "#10b981"; break;
            case ERROR: iconChar = "✕"; mainColorHex = "#ef4444"; break;
            case WARNING: iconChar = "⚠"; mainColorHex = "#f59e0b"; break;
            case INFO: iconChar = "ℹ"; mainColorHex = "#3b82f6"; break;
        }

        final Color themeColor = Color.decode(mainColorHex);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int shadowSize = 8;
                for (int i = 0; i < shadowSize; i++) {
                    g2d.setColor(new Color(0, 0, 0, 8 - i));
                    g2d.drawRoundRect(i, i, getWidth() - 1 - (i * 2), getHeight() - 1 - (i * 2), 20, 20);
                }

                g2d.setColor(Color.decode("#1e293b"));
                g2d.fillRoundRect(shadowSize, shadowSize, getWidth() - (shadowSize * 2), getHeight() - (shadowSize * 2), 20, 20);
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(15, 0));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(22, 25, 20, 25)); // Đẩy nhẹ căn lề trên xuống

        // Thiết kế phần Icon bên trái
        final String finalIconChar = iconChar;
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 40));
                g2d.fillOval(0, 0, 50, 50);

                g2d.setColor(themeColor);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 26));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (50 - fm.stringWidth(finalIconChar)) / 2;
                int y = ((50 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(finalIconChar, x, y);

                g2d.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(50, 80));
        iconPanel.setOpaque(false);
        mainPanel.add(iconPanel, BorderLayout.WEST);

        JTextArea messageArea = new JTextArea(messageText);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        messageArea.setForeground(Color.decode("#cbd5e1"));
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setOpaque(false);

        //thêm khoảng cách đệm phía trên chữ để nó thẳng hàng tâm với Icon
        messageArea.setBorder(new EmptyBorder(10, 0, 0, 0));
        mainPanel.add(messageArea, BorderLayout.CENTER);

        //thiết kế Nút bấm
        JButton btnClose = new JButton("Đóng") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(themeColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setForeground(Color.WHITE);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new Dimension(85, 30));
        btnClose.addActionListener(e -> fadeOutAndClose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnClose);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public void showNotification(boolean check) {
        opacity = 0f;
        setOpacity(0f);
        setVisible(true);

        Timer fadeInTimer = new Timer(10, null);

        fadeInTimer.addActionListener(e -> {
            opacity += 0.08f;

            if (opacity >= 1.0f) {
                opacity = 1.0f;
                setOpacity(1.0f);
                fadeInTimer.stop();
            } else {
                setOpacity(opacity);
            }
        });

        fadeInTimer.start();
        // Chỉ tự đóng nếu check=false
        if (!check) {
            Timer closeTimer = new Timer(1200, e -> {
                ((Timer) e.getSource()).stop();
                fadeOutAndClose();
            });

            closeTimer.setRepeats(false);
            closeTimer.start();
        }

    }
    public void showNotificationWithoutClose(boolean check) {
        opacity = 0f;
        setOpacity(0f);
        setVisible(true);

        Timer fadeInTimer = new Timer(10, null);

        fadeInTimer.addActionListener(e -> {
            opacity += 0.08f;

            if (opacity >= 1.0f) {
                opacity = 1.0f;
                setOpacity(1.0f);
                fadeInTimer.stop();
            } else {
                setOpacity(opacity);
            }
        });

        fadeInTimer.start();
    }


    private void fadeOutAndClose() {
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            opacity -= 0.08f;
            if (opacity <= 0.0f) {
                setOpacity(0.0f);
                ((Timer)e.getSource()).stop();
                dispose();
            } else {
                setOpacity(opacity);
            }
        });
        timer.start();
    }
}

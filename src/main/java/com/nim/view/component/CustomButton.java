package com.nim.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    private final Color backgroundColor;

    private final Dimension normalSize;
    private final Dimension hoverSize;

    public  CustomButton(String name,Color backgroundColor,int width,int height) {
        super(name);
        this.backgroundColor = backgroundColor;

        normalSize = new Dimension(width, height);
        hoverSize = new Dimension(normalSize.width+15, normalSize.height+5);

        setFont(new Font("Arial", Font.BOLD, 22));
        setForeground(Color.WHITE);

        setContentAreaFilled(false); // tắt màu nền
        setBorderPainted(false); // Tắt viền
        setFocusPainted(false); //Tắt khung viền khi Button được chọn
        setOpaque(false);

        changeSize(normalSize);

        setCursor(new Cursor(Cursor.HAND_CURSOR)); // thay đổi con trỏ chuột khi người dùng rê chuột vào

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changeSize(hoverSize);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changeSize(normalSize);
            }
        });
    }

    private void changeSize(Dimension size) {
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        revalidate();
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        Color hoverColor = backgroundColor.brighter();
        // xóa răng cưa
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color color = getModel().isRollover() ? hoverColor : backgroundColor;

        g2.setColor(color);
        int cornerRadius = 25;
        g2.fillRoundRect(0,0,getWidth(),getHeight(), cornerRadius, cornerRadius);

        g2.dispose();

        super.paintComponent(g);
    }
}

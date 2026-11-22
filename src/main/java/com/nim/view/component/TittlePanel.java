package com.nim.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class TittlePanel extends JPanel {

    public TittlePanel() {
        Dimension d = new Dimension(600,180);
        setOpaque(false);
        setPreferredSize(d);

        setMaximumSize(d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Xóa răng cưa đường viền và chữ
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        String title = "NIM GAME";

        // Font chữ đậm, kích thước lớn
        Font font = new Font(
                "Arial Rounded MT Bold",
                Font.BOLD,
                38
        );

        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout textLayout = new TextLayout(title, font, frc);

        // Lấy kích thước chữ để căn giữa
        float textWidth = (float) textLayout.getBounds().getWidth();
        float textHeight = (float) textLayout.getBounds().getHeight();

        float x = (getWidth() - textWidth) / 2f
                - (float) textLayout.getBounds().getX();

        float y = (getHeight() - textHeight) / 2f
                - (float) textLayout.getBounds().getY();

        // Tạo hình dạng vector của dòng chữ
        Shape textShape = textLayout.getOutline(
                java.awt.geom.AffineTransform.getTranslateInstance(x, y)
        );

        // 1. Bóng đổ màu nhạt
        g2.translate(0, 5);
        g2.setColor(new Color(77, 118, 211, 90));
        g2.fill(textShape);
        g2.translate(0, -5);

        // 2. Viền trắng kem dày
        g2.setStroke(new BasicStroke(
                12f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        g2.setColor(new Color(255, 250, 225));
        g2.draw(textShape);

        // 3. Phần ruột chữ màu đậm
        g2.fill(textShape);

        g2.setColor(new Color(42, 130, 221));
        g2.fill(textShape);

        g2.dispose();
    }
}


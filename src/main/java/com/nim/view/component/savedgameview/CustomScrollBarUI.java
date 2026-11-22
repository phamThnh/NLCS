package com.nim.view.component.savedgameview;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollBarUI extends BasicScrollBarUI {

    private final Color thumbColor = new Color(145, 149, 154); // Màu của thanh cuộn (đồng bộ với viền hàng)
    private final Color trackColor = new Color(250, 250, 250);   // Màu nền rãnh cuộn (đồng bộ với nền list)

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(trackColor);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor);

        // Vẽ thanh cuộn bo góc, thụt vào một chút
        int padding = 2;
        g2.fillRoundRect(thumbBounds.x + padding, thumbBounds.y + padding,
                thumbBounds.width - padding * 2, thumbBounds.height - padding * 2,
                10, 10);
        g2.dispose();
    }

    // Ẩn nút mũi tên cuộn lên
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    // Ẩn nút mũi tên cuộn xuống
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    // Tạo một nút trống có kích thước = 0 để thay thế mặc định
    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}

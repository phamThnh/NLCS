
package com.nim.view.component.gameview;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GifBackgroundPanel extends JPanel {
    private final Image flower;

    public GifBackgroundPanel() {
        URL url = getClass().getResource("/view/gameview/m5mi0S.gif");
        if(url == null) throw new IllegalStateException("không tìm thấy /view/m5mi0S.gif");

        flower = new ImageIcon(url).getImage();

        setOpaque(true);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();


        g2.drawImage(flower,0,0,w,h,this);
        g2.dispose();
    }
}

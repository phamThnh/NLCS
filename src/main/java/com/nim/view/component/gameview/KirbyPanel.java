package com.nim.view.component.gameview;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class KirbyPanel extends JPanel {
    private final Image kirby;
    public KirbyPanel() {
        URL url = getClass().getResource("/view/gameview/kirby.png");
        if(url == null) throw new IllegalStateException("Không tìm thấy /view/Kirby.png");

        kirby = new ImageIcon(url).getImage();

        setOpaque(false);
        Dimension d = new Dimension(250,250);
        setMinimumSize(d);
        setPreferredSize(d);


    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int imgHeight = 250;
        int imgWidth = 250;

        int x = (getWidth() - imgWidth)/2;
        int y = 0;


        g2.drawImage(
                kirby,
                x,
                y,
                imgWidth,
                imgHeight,
                this
        );
        g2.dispose();
    }
}

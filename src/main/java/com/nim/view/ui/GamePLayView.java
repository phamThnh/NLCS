
package com.nim.view.ui;

import com.nim.Entity.DongSoi;
import com.nim.Entity.Game;
import com.nim.view.component.CustomButton;
import com.nim.view.component.gameplayview.DongSoiPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePLayView extends JPanel {

    // Các nút chức năng
    private final CustomButton backButton;
    private final CustomButton saveButton;
    private final CustomButton playButton;
    private final CustomButton previousButton;
    private final CustomButton hintButton;

    //số lượng sỏi muốn lấy
    private final JLabel numOfDS;

    private final JLabel currentDS;
    private final JLabel currentTurnLabel;

    //danh sách các đống sỏi
    private final List<DongSoiPanel> dongSoiPanelList =
            new ArrayList<>();

    // Đống sỏi hiện đang được chọn
    private DongSoiPanel selectedPanel;

    public CustomButton getBackButton() {
        return backButton;
    }

    public CustomButton getSaveButton() {
        return saveButton;
    }

    public CustomButton getPlayButton() {
        return playButton;
    }

    public CustomButton getPreviousButton() {
        return previousButton;
    }

    public CustomButton getHintButton() {return hintButton;}

    public JLabel getNumOfDS() {
        return numOfDS;
    }

    public List<DongSoiPanel> getDongSoiPanelList() {
        return dongSoiPanelList;
    }

    public DongSoiPanel getSelectedPanel() {
        return selectedPanel;
    }

    public GamePLayView(Game game) {
        setPreferredSize(new Dimension(1100, 700));
        setLayout(new BorderLayout(18, 0));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setOpaque(true);
        setBackground(new Color(43, 80, 140));

        Font normalFont = new Font("Arial", Font.PLAIN, 16);
        Font slSoiFont = new Font("Arial", Font.PLAIN, 20);
        //trái: khu vực đống sỏi
        DongSoi[] dongSois = game.getDongSois();

        int columns = Math.max(1, dongSois.length);

        JPanel gridJPanel = new JPanel(
                new GridLayout(1, columns, 2, 0)
        );

        gridJPanel.setOpaque(false);

        for (DongSoi dongSoi : dongSois) {
            DongSoiPanel panel = new DongSoiPanel(
                    dongSoi,
                    this::selectPile
            );

            dongSoiPanelList.add(panel);
            gridJPanel.add(panel);
        }

        JPanel boardPanel = new RoundedPanel(
                new Color(245, 248, 250), 24
        );

        boardPanel.setLayout(new BorderLayout());
        boardPanel.setBorder(
                new EmptyBorder(12, 8, 12, 8)
        );

        boardPanel.add(gridJPanel, BorderLayout.CENTER);

        add(boardPanel, BorderLayout.CENTER);

        //bên phải
        JPanel rightPanel = new RoundedPanel(new Color(239, 246, 250), 24);

        rightPanel.setPreferredSize(new Dimension(310, 600));
        rightPanel.setLayout(new BoxLayout(
                rightPanel, BoxLayout.Y_AXIS
        ));
        rightPanel.setBorder(new EmptyBorder(22, 18, 22, 18));

        JLabel titleLabel = new JLabel("THAO TÁC");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(39, 75, 105));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(titleLabel);
        rightPanel.add(Box.createVerticalStrut(28));

        // Chọn số lượng sỏi
        JLabel quantityLabel = new JLabel("Số lượng sỏi muốn lấy");
        quantityLabel.setFont(normalFont);
        quantityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        numOfDS = new JLabel();
        numOfDS.setFont(slSoiFont);
        numOfDS.setPreferredSize(new Dimension(200,40));
        numOfDS.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        numOfDS.setHorizontalAlignment(SwingConstants.CENTER);

        rightPanel.add(quantityLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(numOfDS);
        rightPanel.add(Box.createVerticalStrut(10));

        currentDS = new JLabel();
        currentDS.setFont(normalFont);
        currentDS.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(currentDS);

        rightPanel.add(Box.createVerticalStrut(18));


        JPanel functionPanel = new JPanel();
        functionPanel.setMinimumSize(new Dimension(310,190));
        functionPanel.setLayout(new BoxLayout(functionPanel,BoxLayout.Y_AXIS));
        // Nút thực hiện
        playButton = new CustomButton("Thực hiện", new Color(43, 112, 220), 260, 44);
        playButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        functionPanel.add(playButton);
        functionPanel.add(Box.createVerticalStrut(12));

        // Nút quay lại
        backButton = new CustomButton("Quay lại", new Color(119, 160, 205), 260, 44);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        functionPanel.add(backButton);
        functionPanel.add(Box.createVerticalStrut(12));

        // Nút lượt trước
        previousButton = new CustomButton("Lượt trước", new Color(103, 153, 184), 260, 44);
        previousButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        functionPanel.add(previousButton);
        functionPanel.add(Box.createVerticalStrut(12));

        // Nút lưu game
        saveButton = new CustomButton("Lưu game", new Color(214, 61, 61), 260, 44);
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        functionPanel.add(saveButton);
        functionPanel.add(Box.createVerticalStrut(12));

        //nút gợi ý
        hintButton = new CustomButton("Gợi ý",new Color(216, 216, 18),260,44);
        hintButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        functionPanel.add(hintButton);

        rightPanel.add(functionPanel);
        rightPanel.add(Box.createVerticalGlue());

        // Hiển thị lượt hiện tại
        currentTurnLabel = new JLabel();
        currentTurnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentTurnLabel.setForeground(new Color(39, 75, 105));
        currentTurnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(currentTurnLabel);
        add(rightPanel, BorderLayout.EAST);
    }
    //cập nhật lượt hiện tại
    public void updateCurrentTurn(String message) {
        currentTurnLabel.setText(message);
    }
    //Xử lý khi người chơi chọn một đống sỏi.
    private void selectPile(DongSoiPanel panel) {
        // Bỏ chọn đống sỏi cũ
//        for (DongSoiPanel pile : dongSoiPanelList) {
//            if (pile != panel) {
//                pile.clearSelection();
//            }
//        }
        if(selectedPanel != null && selectedPanel != panel) selectedPanel.clearSelection();
        selectedPanel = panel;

        // Cập nhật số lượng sỏi có thể lấy
        int slSoi = selectedPanel.getSelectedCount();
        numOfDS.setText(String.valueOf(slSoi));

        //cập nhật stt đống sỏi
        int sttDS = selectedPanel.getDongSoi().getDs_id();
        currentDS.setText("Đống sỏi hiện tại: "+sttDS);
    }

    //làm mới giao diện sau khi hoàn thành lượt chơi.
    public void refreshPiles() {

        selectedPanel.refreshStones();

        numOfDS.setText("");
        //selectedPanel = null;
        currentDS.setText("");
    }
    public void selectPanelByDongSoiId(int idDongSoi) {
        for (DongSoiPanel panel : dongSoiPanelList) {
            if (panel.getDongSoi().getDs_id() == idDongSoi) {
                selectPile(panel);
                return;
            }
        }
    }
    private static class RoundedPanel extends JPanel {

        private final Color panelColor;
        private final int radius;

        public RoundedPanel(Color color, int radius) {
            this.panelColor = color;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(30, 60, 80, 25));
            g2.fillRoundRect(
                    3, 4,
                    getWidth() - 6,
                    getHeight() - 6,
                    radius, radius
            );

            g2.setColor(panelColor);
            g2.fillRoundRect(
                    0, 0,
                    getWidth() - 6,
                    getHeight() - 6,
                    radius, radius
            );

            g2.dispose();
            super.paintComponent(g);
        }
    }
}

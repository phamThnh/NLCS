
package com.nim.view.component.gameplayview;

import com.nim.Entity.DongSoi;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DongSoiPanel extends JPanel {

    private final DongSoi dongSoi;
    private final Consumer<DongSoiPanel> onSelected;
    private final JPanel stoneContainer = new JPanel();

    // Danh sách các nút sỏi, sắp xếp từ dưới lên trên
    private final List<JButton> stones = new ArrayList<>();

    // Số lượng sỏi đang được chọn
    private int selectedCount = 0;

    private static final Color STONE_COLOR =
            new Color(35, 135, 220);

    private static final Color SELECTED_COLOR =
            new Color(255, 165, 0);

    private static final Color BORDER_COLOR =
            new Color(20, 90, 160);

    private static final int STONE_WIDTH = 65;
    private static final int STONE_HEIGHT = 16;
    private static final int STONE_GAP = 3;

    public DongSoiPanel(
            DongSoi dongSoi,
            Consumer<DongSoiPanel> onSelected
    ) {
        this.dongSoi = dongSoi;
        this.onSelected = onSelected;

        setLayout(new BorderLayout());
        setOpaque(false);

        setPreferredSize(new Dimension(100, 350));
        setMinimumSize(new Dimension(1, 100));

        // Khu vực chứa sỏi
        stoneContainer.setLayout(
                new BoxLayout(stoneContainer, BoxLayout.Y_AXIS)
        );
        stoneContainer.setOpaque(false);

        add(stoneContainer, BorderLayout.SOUTH);

        refreshStones();
    }


    //Tạo lại các viên sỏi theo số lượng hiện tại.
    public void refreshStones() {
        stoneContainer.removeAll();
        stones.clear();
        selectedCount = 0;

        int quantity = dongSoi.getSlhientai();

        for (int i = quantity - 1; i >= 0; i--) {
            JButton stone = new JButton();

            Dimension stoneSize = new Dimension(
                    STONE_WIDTH, STONE_HEIGHT
            );

            stone.setPreferredSize(stoneSize);
            stone.setMinimumSize(stoneSize);
            stone.setMaximumSize(stoneSize);

            stone.setBackground(STONE_COLOR);
            stone.setOpaque(true);
            stone.setContentAreaFilled(true);
            stone.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

            stone.setFocusPainted(false);
            stone.setFocusable(false);
            stone.setAlignmentX(Component.CENTER_ALIGNMENT);

            stone.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            int stoneIndex = i;
            stone.addActionListener(e -> selectStonesFrom(stoneIndex));

            // Lưu danh sách theo thứ tự logic từ dưới lên
            stones.add(0, stone);

            stoneContainer.add(stone);

            if (i > 0) {
                stoneContainer.add(Box.createVerticalStrut(STONE_GAP));
            }
        }

        stoneContainer.revalidate();
        stoneContainer.repaint();

        revalidate();
        repaint();
    }

    //Chọn viên được nhấn và toàn bộ viên phía trên nó. stoneIndex = 0 là viên dưới cùng.
    private void selectStonesFrom(int stoneIndex) {
        selectedCount = stones.size() - stoneIndex;

        for (int i = 0; i < stones.size(); i++) {
            JButton stone = stones.get(i);

            Color color = (i >= stoneIndex)
                    ? SELECTED_COLOR
                    : STONE_COLOR;

            if (!stone.getBackground().equals(color)) {
                stone.setBackground(color);
            }
        }

        if (onSelected != null) {
            onSelected.accept(this);
        }
    }

    //Bỏ chọn tất cả viên sỏi trong đống này.
    public void clearSelection() {
        selectedCount = 0;
        for (JButton stone : stones) {
            if (!stone.getBackground().equals(STONE_COLOR)) {
                stone.setBackground(STONE_COLOR);
            }
        }
    }
    public void setSelectedCount(int count) {
        if (count < 0 || count > stones.size()) {
            return;
        }

        selectedCount = count;

        int startIndex = stones.size() - count;

        for (int i = 0; i < stones.size(); i++) {
            JButton stone = stones.get(i);

            Color color = (i >= startIndex)
                    ? SELECTED_COLOR
                    : STONE_COLOR;

            stone.setBackground(color);
        }

        repaint();
    }
    public DongSoi getDongSoi() {
        return dongSoi;
    }

    public int getSelectedCount() {
        return selectedCount;
    }
}

package com.nim.view.ui;

import com.nim.Entity.DongSoi;
import com.nim.Entity.Game;
import com.nim.view.component.CustomButton;
import com.nim.view.component.CustomConfirmDialog;
import com.nim.view.component.savedgameview.CustomScrollBarUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class SavedGameView extends JPanel {

    private final Consumer<Game> onGameSelected;
    private final Consumer<Game> onDeleteSelected;
    private final CustomButton backButton;
    private final Color rowColor = new Color(69, 137, 228);
    private final Color textColor = new Color(180, 205, 240);
    private final Font textFont = new Font("Arial", Font.BOLD, 14);

    public CustomButton getBackButton() {
        return backButton;
    }

    public SavedGameView(List<Game> games, Consumer<Game> onGameSelected, Consumer<Game> onDeleteSelected) {

        this.onGameSelected = onGameSelected;
        this.onDeleteSelected = onDeleteSelected;

        setLayout(new BorderLayout(10, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("VÁN CHƠI ĐÃ LƯU");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(new Color(225, 44, 71));
        title.setBorder(new EmptyBorder(0, 5, 10, 0));

        add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(38, 87, 151));

        if (games == null || games.isEmpty()) {
            JLabel emptyLabel = new JLabel("Chưa có ván chơi nào được lưu");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.LIGHT_GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(30));
            listPanel.add(emptyLabel);
        } else {
            for (Game game : games) {
                listPanel.add(createGameRow(game));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null); // Xóa viền bao quanh mặc định
        scrollPane.getViewport().setBackground(new Color(250, 250, 250));

        // Tăng tốc độ lăn chuột
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // --- Áp dụng thanh cuộn Custom ---
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        // Chỉnh độ rộng của thanh cuộn nhỏ lại
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        // Tắt hẳn thanh cuộn ngang
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        add(scrollPane, BorderLayout.CENTER);

        backButton = new CustomButton("← Quay lại", new Color(220, 58, 58), 120, 44);
        backButton.setFont(textFont);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createGameRow(Game game) {
        boolean canContinue = game.getNgthang() == 0;

        JPanel row = new JPanel(new BorderLayout(10, 8));
        row.setBackground(rowColor);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 100, 145), 1),
                new EmptyBorder(12, 14, 12, 14)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 175));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setCursor(new Cursor(canContinue ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));

        // ----- Thông tin bên trái -----
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel idLabel = new JLabel("Ván chơi #" + game.getGame_id());
        idLabel.setFont(new Font("Arial", Font.BOLD, 17));
        idLabel.setForeground(Color.WHITE);

        JLabel modeLabel = new JLabel("Chế độ: " + (game.isGamemode() ? "PVP" : "PVE"));
        modeLabel.setFont(textFont);
        modeLabel.setForeground(textColor);

        String winner = "";
        if (game.getNgthang() != 0) {
            winner = game.isGamemode() ? "Người chơi " + game.getNgthang() : (game.getNgthang() == 1 ? "Bạn" : "AI");
        }
        JLabel winnerLabel = new JLabel("Người chiến thắng: " + (winner.isEmpty() ? "Đang chơi" : winner));
        winnerLabel.setFont(textFont);
        winnerLabel.setForeground(textColor);

        JLabel startLabel = new JLabel("Bắt đầu: " + formatTime(game.getTimeStart()));
        startLabel.setFont(textFont);
        startLabel.setForeground(textColor);

        JLabel endLabel = new JLabel("Kết thúc: " + (canContinue ? "--" : formatTime(game.getTimeEnd())));
        endLabel.setFont(textFont);
        endLabel.setForeground(textColor);

        String currentTurn = "Người chơi 2";
        if(!game.isGamemode()) {
            if (game.getLuothientai() == 1) currentTurn = "Người chơi 1";
            else currentTurn = "AI";
        }
        JLabel turnLabel = new JLabel("Lượt hiện tại: " + currentTurn);
        turnLabel.setFont(textFont);
        turnLabel.setForeground(textColor);

        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(modeLabel);
        infoPanel.add(winnerLabel);
        infoPanel.add(startLabel);
        infoPanel.add(endLabel);
        infoPanel.add(turnLabel);

        row.add(infoPanel, BorderLayout.CENTER);

        // ----- Trạng thái và nút bên phải -----
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(100,175));

        JLabel statusLabel = new JLabel(canContinue ? "● Có thể tiếp tục" : "● Đã kết thúc");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(canContinue ? new Color(52, 211, 153) : new Color(248, 113, 113));
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Sử dụng CustomButton thay cho JButton cho nút Xóa
        CustomButton deleteBtn = new CustomButton("Xóa", new Color(220, 58, 58), 50, 35);
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> {
            boolean confirm = CustomConfirmDialog.showDialog(this, "Xác nhận xóa", "Bạn có chắc chắn muốn xóa ván chơi #" + game.getGame_id() + " không?");

            if (confirm) {
                onDeleteSelected.accept(game);
            }
        });

        rightPanel.add(statusLabel, BorderLayout.NORTH);
        rightPanel.add(deleteBtn, BorderLayout.SOUTH);

        row.add(rightPanel, BorderLayout.EAST);

        // ----- Sự kiện click để tiếp tục chơi -----
        if (canContinue) {
            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onGameSelected.accept(game);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    row.setBackground(new Color(69, 142, 243));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    row.setBackground(rowColor);
                }
            });
        }

        return row;
    }

    private String formatTime(Object time) {
        if (time == null) return "";
        if (time instanceof LocalDateTime ldt) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
            return ldt.format(formatter);
        }
        return time.toString();
    }
}
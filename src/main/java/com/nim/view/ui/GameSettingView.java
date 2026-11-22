
package com.nim.view.ui;

import com.nim.view.component.CustomButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameSettingView extends JPanel {

    private JRadioButton pvpButton;
    private JRadioButton pveButton;

    private JRadioButton randomButton;
    private JRadioButton manualButton;
    private JTextField pileCountField;

    private JRadioButton player1Button;
    private JRadioButton player2Button;

    private CustomButton backButton;
    private CustomButton startButton;

    // Màu sắc đồng bộ với SavedGameView
    private final Color white = Color.WHITE;
    private final Color panelColor = new Color(43, 80, 140);
    private final Color titleColor = new Color(77, 188, 228);
    private final Color textColor = Color.WHITE;
    private final Color secondaryTextColor = new Color(180, 205, 240);
    private final Color backButtonColor = new Color(220, 58, 58);
    private final Color startButtonColor = new Color(77, 188, 228);

    public GameSettingView() {
        setBackground(white);
        setOpaque(true);
        initComponents();
        initEvents();
    }

    private void initComponents() {
        Font boldFont = new Font("Arial", Font.BOLD, 17);
        Font font = new Font("Arial", Font.PLAIN,15);
        setLayout(new BorderLayout(10, 15));
        setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("CÀI ĐẶT VÁN CHƠI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(titleColor);

        JLabel subtitleLabel = new JLabel("Tùy chỉnh thông số trước khi bắt đầu", SwingConstants.CENTER);
        subtitleLabel.setFont(font);
        subtitleLabel.setForeground(new Color(225, 41, 74));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);


        JPanel settingPanel = new JPanel(new GridBagLayout());
        settingPanel.setBackground(panelColor);
        settingPanel.setOpaque(true);
        settingPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel modeLabel = new JLabel("1. CHẾ ĐỘ CHƠI");
        modeLabel.setFont(boldFont);
        modeLabel.setForeground(titleColor);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        settingPanel.add(modeLabel, gbc);

        pvpButton = new JRadioButton("PVP - Hai người chơi");
        pveButton = new JRadioButton("PVE - Người chơi đấu với máy");

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(pvpButton);
        modeGroup.add(pveButton);
        pvpButton.setSelected(true);

        pvpButton.setOpaque(false);
        pveButton.setOpaque(false);

        pvpButton.setForeground(textColor);
        pveButton.setForeground(textColor);

        pvpButton.setFont(font);
        pveButton.setFont(font);

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        modePanel.setOpaque(false);
        modePanel.add(pvpButton);
        modePanel.add(Box.createHorizontalStrut(20));
        modePanel.add(pveButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        settingPanel.add(modePanel, gbc);

        JLabel pileLabel = new JLabel("2. SỐ LƯỢNG ĐỐNG SỎI");
        pileLabel.setFont(boldFont);
        pileLabel.setForeground(titleColor);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        settingPanel.add(pileLabel, gbc);

        randomButton = new JRadioButton("Ngẫu nhiên (1 - 50 đống)");
        manualButton = new JRadioButton("Nhập số lượng từ bàn phím");

        ButtonGroup pileModeGroup = new ButtonGroup();
        pileModeGroup.add(randomButton);
        pileModeGroup.add(manualButton);
        randomButton.setSelected(true);

        randomButton.setOpaque(false);
        manualButton.setOpaque(false);

        randomButton.setForeground(textColor);
        manualButton.setForeground(textColor);

        randomButton.setFont(font);
        manualButton.setFont(font);

        randomButton.setFocusPainted(false);
        manualButton.setFocusPainted(false);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 5, 0);
        settingPanel.add(randomButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 8, 0);
        settingPanel.add(manualButton, gbc);

        // Ô nhập số lượng sỏi
        JLabel inputLabel = new JLabel("Số đống sỏi:");
        inputLabel.setFont(font);
        inputLabel.setForeground(textColor);

        pileCountField = new JTextField("5", 8);
        pileCountField.setFont(font);
        pileCountField.setBackground(Color.WHITE);
        pileCountField.setForeground(panelColor);
        pileCountField.setCaretColor(panelColor);
        pileCountField.setEnabled(false);

        pileCountField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(titleColor, 1),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                )
        );

        JLabel rangeLabel = new JLabel("(1 - 50)");
        rangeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        rangeLabel.setForeground(secondaryTextColor);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(inputLabel);
        inputPanel.add(Box.createHorizontalStrut(8));
        inputPanel.add(pileCountField);
        inputPanel.add(Box.createHorizontalStrut(6));
        inputPanel.add(rangeLabel);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 25, 25, 0);
        settingPanel.add(inputPanel, gbc);


        JLabel firstPlayerLabel = new JLabel("3. NGƯỜI CHƠI ĐI TRƯỚC");
        firstPlayerLabel.setFont(boldFont);
        firstPlayerLabel.setForeground(titleColor);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 10, 0);
        settingPanel.add(firstPlayerLabel, gbc);

        player1Button = new JRadioButton("Người chơi 1");
        player2Button = new JRadioButton("Người chơi 2");

        ButtonGroup firstPlayerGroup = new ButtonGroup();
        firstPlayerGroup.add(player1Button);
        firstPlayerGroup.add(player2Button);
        player1Button.setSelected(true);

        player1Button.setOpaque(false);
        player2Button.setOpaque(false);

        player1Button.setForeground(textColor);
        player2Button.setForeground(textColor);

        player1Button.setFont(font);
        player2Button.setFont(font);

        player1Button.setFocusPainted(false);
        player2Button.setFocusPainted(false);

        JPanel firstPlayerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        firstPlayerPanel.setOpaque(false);
        firstPlayerPanel.add(player1Button);
        firstPlayerPanel.add(Box.createHorizontalStrut(20));
        firstPlayerPanel.add(player2Button);

        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        settingPanel.add(firstPlayerPanel, gbc);

        add(settingPanel, BorderLayout.CENTER);


        backButton = new CustomButton("Quay lại",backButtonColor,120,44);
        backButton.setFont(boldFont);
        backButton.setForeground(white);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton = new CustomButton("Bắt đầu",startButtonColor,120,44);
        startButton.setFont(boldFont);
        startButton.setForeground(white);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE,60));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(startButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initEvents() {
        randomButton.addActionListener(e ->
                pileCountField.setEnabled(false)
        );

        manualButton.addActionListener(e -> {
            pileCountField.setEnabled(true);
            pileCountField.requestFocusInWindow();
            pileCountField.selectAll();
        });

        pveButton.addActionListener(e ->
                player2Button.setText("Máy")
        );

        pvpButton.addActionListener(e ->
                player2Button.setText("Người chơi 2")
        );
    }

    // true = PVP, false = PVE
    public boolean isGameMode() {
        return pvpButton.isSelected();
    }

    public boolean isRandomPileCount() {
        return randomButton.isSelected();
    }

    public int getPileCount() throws NumberFormatException{
        if (randomButton.isSelected()) {
            return ThreadLocalRandom.current().nextInt(1, 51);
        }

        return Integer.parseInt(
                pileCountField.getText().trim()
        );
    }

    public boolean isValidInput(){
        if (randomButton.isSelected()) {
            return true;
        }

        int count = Integer.parseInt(pileCountField.getText().trim());

        return count >= 1 && count <= 50;
    }

    public int getFirstPlayer() {
        return player1Button.isSelected() ? 1 : 2;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getStartButton() {
        return startButton;
    }
}

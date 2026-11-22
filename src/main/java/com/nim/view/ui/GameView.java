package com.nim.view.ui;

import com.nim.controller.GameController;
import com.nim.view.component.CustomButton;
import com.nim.view.component.Notification;
import com.nim.view.component.gameview.GifBackgroundPanel;
import com.nim.view.component.gameview.KirbyPanel;
import com.nim.view.component.TittlePanel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameView extends JFrame {
    private final JPanel cardPanel;
    private final CardLayout cardLayout;

    private final CustomButton newGameButton;
    private final CustomButton aiGameButton;
    private final CustomButton savedGamesButton;
    private final CustomButton settingButon;
    private final CustomButton continueButton;

    private String nameScreen;

    public String getNameScreen() {
        return nameScreen;
    }

    public void setNameScreen(String nameScreen) {
        this.nameScreen = nameScreen;
    }

    public CustomButton getContinueButton() { return continueButton; }

    public CustomButton getNewGameButton() {
        return newGameButton;
    }

    public CustomButton getAiGameButton() {
        return aiGameButton;
    }

    public CustomButton getSavedGamesButton() {
        return savedGamesButton;
    }

    public CustomButton getSettingButon() {
        return settingButon;
    }

    public GameView() {
        setTitle("NIM Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        //icon
        URL iconURL = getClass().getResource("/view/gameview/icon.png");
        if(iconURL == null) throw new IllegalStateException("không tìm thấy /view/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);

        setIconImage(icon.getImage());


        // Background chứa toàn bộ màn hình
        GifBackgroundPanel backgroundPanel = new GifBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        // CardLayout quản lý các màn hình
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // Menu chính
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setOpaque(false);

        // Đăng ký màn hình menu
        cardPanel.add(menuPanel, "MAINPAGE");

        // Đặt các màn hình lên background
        backgroundPanel.add(cardPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);


        GridBagConstraints c = new GridBagConstraints();
        TittlePanel titlePanel = new TittlePanel();
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(600, 100));
        titlePanel.setMinimumSize(new Dimension(600, 100));

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.25;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 0, 0, 0);

        menuPanel.add(titlePanel, c);

        // Nhân vật Kirby
        KirbyPanel kirbyPanel = new KirbyPanel();
        kirbyPanel.setOpaque(false);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.25;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 0, 0);

        menuPanel.add(kirbyPanel, c);

        // Khu vực các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(
                new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        );

        Dimension buttonPanelSize = new Dimension(280, 300);
        buttonPanel.setPreferredSize(buttonPanelSize);
        buttonPanel.setMinimumSize(buttonPanelSize);
        buttonPanel.setMaximumSize(buttonPanelSize);

        //thêm các button vào buttonPanel
        continueButton = new CustomButton("Tiếp tục", Color.YELLOW,280,40);
        newGameButton = new CustomButton("Chơi với người", new Color(50, 110, 71),280,40);
        aiGameButton = new CustomButton("Chơi với máy", new Color(42, 126, 197),280,40);
        savedGamesButton = new CustomButton("Ván chơi đã lưu", new Color(232, 65, 48),280,40);
        settingButon = new CustomButton("Thiết lập ván chơi",new Color(75, 55, 45),280,40);

        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        savedGamesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingButon.setAlignmentX(Component.CENTER_ALIGNMENT);

        //set icon cho các button
        URL newgame = getClass().getResource("/view/gameview/icon_newgame_.png");
        URL savedgame = getClass().getResource("/view/gameview/icon_listgame.png");
        URL settinggame = getClass().getResource("/view/gameview/icon_setting.png");

        ImageIcon newgame_icon = new ImageIcon(newgame);
        Image newgame_image = newgame_icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon savedgame_icon = new ImageIcon(savedgame);
        Image savedgame_image = savedgame_icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon setting_icon = new ImageIcon(settinggame);
        Image settinggame_image = setting_icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);

        newGameButton.setIcon(new ImageIcon(newgame_image));
        newGameButton.setIconTextGap(12);


        aiGameButton.setIcon(new ImageIcon(newgame_image));
        aiGameButton.setIconTextGap(12);


        savedGamesButton.setIcon(new ImageIcon(savedgame_image));
        savedGamesButton.setIconTextGap(12);


        settingButon.setIcon(new ImageIcon(settinggame_image));
        settingButon.setIconTextGap(12);

        buttonPanel.add(continueButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(aiGameButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(savedGamesButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(settingButon);

        // Cấu hình GridBagConstraints cho buttonPanel
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 0.55;
        c.fill = GridBagConstraints.NONE;   //giữ nguyên kích thước của nút
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 40, 0); //Đẩy nút lêm 40px

        menuPanel.add(buttonPanel, c);

        GameController gameController = new GameController(this);

    }

    public void showScreen(String screenName){
        setNameScreen(screenName);
        cardLayout.show(cardPanel,screenName);

    }

    public void replaceScreen(JPanel screen, String name) {
        // Xóa màn hình cũ có cùng tên
        for (Component component : cardPanel.getComponents()) {
            if (name.equals(component.getName())) {
                cardPanel.remove(component);
                break;
            }
        }

        screen.setName(name);
        cardPanel.add(screen, name);

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void addScreen(JPanel screen, String name) {
        cardPanel.add(screen,name);
    }

}
package com.nim.controller;

import com.nim.file.FileManager;
import com.nim.service.GameService;
import com.nim.Entity.DongSoi;
import com.nim.Entity.Game;
import com.nim.Entity.LuotChoi;
import com.nim.view.component.Notification;
import com.nim.view.component.gameplayview.DongSoiPanel;
import com.nim.view.ui.GamePLayView;
import com.nim.view.ui.GameSettingView;
import com.nim.view.ui.GameView;
import com.nim.view.ui.SavedGameView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GameController {
    private final GameView gameView;
    private final GameService gameService;
    private Game game;
    private final FileManager fileManager;
    private GamePLayView gamePLayView;

    private boolean isBusy = false;

    public GameController(GameView gameView) {
        this.gameView = gameView;
        this.gameService = new GameService();
        this.fileManager = new FileManager();
        initController();
    }

    public void initController() {
        //tiếp tục
        gameView.getContinueButton().addActionListener(e -> {
            try {
                //tìm Game sớm nhất đã lưu
                if(recentlyGame()) {
                    //nếu game chưa xong thì tiếp tục
                    if (game.getNgthang() == 0) {
                        gameView.showScreen("PLAY");
                        play();
                    }
                    //nếu Game đã có người thắng thì hiển thị thông báo
                    else showNotification(Notification.Type.WARNING,"Trò chơi đã kết thúc",false);
                }
            } catch (IOException ex) {
                showNotification(Notification.Type.ERROR,"Không tìm thấy Game",false);
            }
        });
        //chơi với người
        gameView.getNewGameButton().addActionListener(e -> {
            startNewGame(true);
            gameView.showScreen("PLAY");
            play();
        });

        //chơi với máy
        gameView.getAiGameButton().addActionListener(e -> {
            startNewGame(false);
            gameView.showScreen("PLAY");
            play();
        });
        //ván chơi đã lưu
        gameView.getSavedGamesButton().addActionListener(e -> showALlGames());

        //thiết lập ván chơi
        gameView.getSettingButon().addActionListener(e -> settingGame());
    }
    private void startNewGame(boolean gamemode) {
        try {
            game = gameService.createGame(gamemode);
            //showNotification(Notification.Type.SUCCESS,"Tạo ván chơi thành công!",false,1000);

            gamePLayView = new GamePLayView(game);
            gameView.addScreen(gamePLayView, "PLAY");
            updateCurrentTurn();
        } catch (IOException e) {
            showNotification(Notification.Type.ERROR,"Không thể tải ván chơi",false);
        }
    }
    //hàm xử lí sự kiện bên GamePLayView
    //thực hiện lượt chơi
    public void play() {
        // Xóa sự kiện cũ để tránh đăng ký lặp khi mở lại màn hình
        for (java.awt.event.ActionListener al : gamePLayView.getPlayButton().getActionListeners()) {
            gamePLayView.getPlayButton().removeActionListener(al);
        }

        // Sự kiện thực hiện lượt chơi của người chơi
        gamePLayView.getPlayButton().addActionListener(e -> {
            // Không cho người chơi thao tác khi đang đến lượt AI
            if (!game.isGamemode() && game.getLuothientai() == 2) {
                return;
            }

            boolean moved = moveOfPlayer();

            // Nước đi không hợp lệ thì không xử lý tiếp
            if (!moved) return;

            // Cập nhật giao diện sau lượt người chơi
            gamePLayView.refreshPiles();
            updateCurrentTurn();


            // Kiểm tra kết thúc
            if (!gameService.isContinue(game)) {
                saveFinishedGame();
                printTheWinner(game);
                return;
            }

            // AI đi sau người chơi
            if (!game.isGamemode() && game.getLuothientai() == 2) {
                setEnableButton(false);

                Timer timer = new Timer(1000, ev -> {
                    ((Timer) ev.getSource()).stop();

                    if(!isBusy)  moveOfAI();

                    // Nếu AI chưa kết thúc game thì cập nhật giao diện
                    if (game.getNgthang() == 0) {
                        gamePLayView.refreshPiles();
                    }
                });

                timer.setRepeats(false);
                timer.start();

                Timer unlockButton = new Timer(1300, ev -> {
                    ((Timer) ev.getSource()).stop();

                    setEnableButton(true);
                });
                unlockButton.setRepeats(false);
                unlockButton.start();

            }
        });

        // Sự kiện quay lại
        gamePLayView.getBackButton().addActionListener(e -> {
            if (game.getNgthang() == 0) {
                try {
                    fileManager.saveGame(game);
                } catch (IOException ex) {
                    showNotification(Notification.Type.ERROR,"Không thể lưu ván chơi",false);
                }
            }

            gameView.showScreen("MAINPAGE");
        });

        //sự kiện lượt trước đó
            gamePLayView.getPreviousButton().addActionListener(e -> {
                try {
                    if(game.isGamemode()) {
                        //lấy lượt chơi trước đó
                        LuotChoi previousLC = fileManager.loadPreviousLuotChoi(game.getGame_id());

                        //cập nhật lại số lượng sỏi của đống
                        gamePLayView.selectPanelByDongSoiId(previousLC.getIddongsoi());
                        DongSoiPanel dsPanel = gamePLayView.getSelectedPanel();
                        dsPanel.getDongSoi().setSlhientai(previousLC.getBefore());

                        //cập nhật lại màn hình
                        gamePLayView.refreshPiles();

                        //cập nhật người thực hiện
                        gameService.switchTurn(game);
                    }
                    else showNotification(Notification.Type.INFO,"Quay lại lượt trước cũng không thay đổi được gì đâu!!",true);
                } catch (IOException ex) {
                    showNotification(Notification.Type.WARNING, "Không còn lượt để quay lại!", false);
                } catch (IndexOutOfBoundsException ex) {
                    showNotification(Notification.Type.WARNING, "Không còn lượt để quay lại!", false);
                }
            });



        // Sự kiện lưu game
        gamePLayView.getSaveButton().addActionListener(e -> {
            try {
                fileManager.saveGame(game);

            } catch (IOException ex) {
                showNotification(Notification.Type.ERROR,"Không thể lưu ván chơi",false);
                return;
            }
            showNotification(Notification.Type.SUCCESS,"Lưu ván chơi thành công",false);
        });

        //sự kiện gợi ý
        gamePLayView.getHintButton().addActionListener(e -> {
            String hint = gameService.hint(game.getDongSois());
            setEnableButton(false);
            showNotification(Notification.Type.INFO,hint,true);
            setEnableButton(true);
        });

        //AI đi trước
        if (!game.isGamemode() && game.getLuothientai() == 2) {
            setEnableButton(false);
            Timer timer = new Timer(800, ev -> {
                ((Timer) ev.getSource()).stop();
                moveOfAI();
            });

            timer.setRepeats(false);
            timer.start();
            Timer unlockButton = new Timer(1100, ev -> {
                ((Timer) ev.getSource()).stop();

                setEnableButton(true);
            });
            unlockButton.setRepeats(false);
            unlockButton.start();
        }

    }

    //hm thực hiện lượt chơi của player
    private boolean moveOfPlayer() {
        DongSoiPanel selectedPanel = gamePLayView.getSelectedPanel();

        if (selectedPanel == null) {
            showNotification(Notification.Type.WARNING,"Vui lòng chọn một đống sỏi bất kì",false);
            return false;
        }
        Integer quantity;
        try {
            quantity = Integer.parseInt(gamePLayView.getNumOfDS().getText());
        } catch (NumberFormatException e) {
            showNotification(Notification.Type.WARNING,"Vui lòng chọn một đống sỏi bất kì",false);
            return false;
        }

        DongSoi dongSoi = selectedPanel.getDongSoi();

        LuotChoi luotChoi = gameService.moveOfPLayer(game, dongSoi, quantity, game.getLuothientai());

        if (luotChoi == null) {
            showNotification(Notification.Type.ERROR,"Nước đi không hợp lệ",false);
            return false;
        }
        saveLuotChoi(luotChoi, gameService.getNextLuotChoiId());

        return true;
    }
    //hàm thực hiện lượt chơi của AI
    private void moveOfAI() {
        try {
            //bắt đầu đo thời gian tính toán của hệ thống
            long startTime = System.nanoTime();

            LuotChoi luotChoiAI = gameService.moveOfAI(game);
            //Ghi nhận thời gian kết thúc
            long endTime = System.nanoTime();
            //Tính toán
            long thoiGianNano = endTime - startTime;
            // Chuyển đổi sang mili-giây (chia cho 1 triệu) để dễ đọc hơn
            double thoiGianMili = thoiGianNano / 1_000_000.0;
            System.out.println("Thời gian AI tính toán: " + thoiGianNano + " ns (" + thoiGianMili + " ms)");
            if (luotChoiAI == null) return;

            gamePLayView.selectPanelByDongSoiId(luotChoiAI.getIddongsoi());
            // Cập nhật các đống sỏi theo trạng thái mới
            gamePLayView.refreshPiles();
            saveLuotChoi(luotChoiAI,gameService.getNextLuotChoiId());
            updateCurrentTurn();

            int quantity = luotChoiAI.getBefore() - luotChoiAI.getAfter();

            showNotification(Notification.Type.INFO,"AI đã bốc " + quantity + " viên sỏi từ đống sỏi thứ " + luotChoiAI.getIddongsoi(),false);

            if (!gameService.isContinue(game)) {
                fileManager.saveGame(game);
                printTheWinner(game);
            }
        }
        catch (IOException ex) {
        }
    }
    //hàm in thông báo thắng thua
    private void printTheWinner(Game game) {
        if (game.isGamemode()) {
            int winner = 2;
            if (game.getLuothientai() == 2) winner = 1;
            showNotification(Notification.Type.INFO,"Trò chơi kết thúc\nXin chúc mừng người chơi " + winner + " đã giành chiến thắng",true);
        }
        else {
            if (game.getLuothientai() == 1) { //AI thắng
                showNotification(Notification.Type.INFO,"Bạn đã thua\nHãy cố gắng hơn ở lần sau nhé",true);
            } else showNotification(Notification.Type.INFO,"Thật tuyệt vời!!\nBạn đã chiến thắng AI\nXin chúc mừng",true);
        }
        Timer timer = new Timer(1500, e -> {
            ((Timer) e.getSource()).stop();
            gameView.showScreen("MAINPAGE");
        });

        timer.setRepeats(false);
        timer.start();
    }

    //hàm tìm game sớm nhất đã lưu
    public boolean recentlyGame() throws IOException {
        int id = fileManager.recentlyFile();
        if(id < 0) {
            showNotification(Notification.Type.ERROR, "Game không tồn tại",false);
            return false;
        }
        else {
            this.game = fileManager.loadGame(id);

            gamePLayView = new GamePLayView(game);
            gameView.addScreen(gamePLayView, "PLAY");
            updateCurrentTurn();
            return true;
        }
    }
    private void saveFinishedGame() {
        try {
            fileManager.saveGame(game);
        } catch (IOException ex) {
        }
    }
    private void showNotification(Notification.Type type, String message,boolean check) {
        Notification notification = new Notification(gameView, type, message);
        notification.showNotification(check);
    }
//    private void showNotificationWithoutClose(Notification.Type type, String message,boolean check) {
//        Notification notification = new Notification(gameView, type, message);
//        notification.showNotificationWithoutClose(check);
//    }
    //hàm lưu lượt chơi trên luồng khác
    public void saveLuotChoi(LuotChoi luotChoi, int idLuotChoi) {
        gamePLayView.getPlayButton().setEnabled(false); // khóa nút play
        Thread thread = new Thread(() -> {
            try {
                // Ghi file trên luồng nền
                isBusy = true;
                fileManager.saveLuotChoi(luotChoi, idLuotChoi);

                SwingUtilities.invokeLater(() -> {
                    //chỉ cập nhật sau khi lưu thành công
                    gameService.generateNextLuotChoiId();
                    isBusy = false;
                    game.addLuotChoi(luotChoi);

                    //mở khóa nút chơi
                    gamePLayView.getPlayButton().setEnabled(true);
                });
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> {
                    gamePLayView.getPlayButton().setEnabled(true);
                    showNotification(Notification.Type.ERROR, "Có lỗi khi lưu lượt chơi", false);
                });
            }
        });
        thread.start();
    }
    private void updateCurrentTurn() {
        int currentPlayer = game.getLuothientai();

        if (game.isGamemode()) {
            // Chế độ 2 người chơi
            gamePLayView.updateCurrentTurn(
                    "Lượt hiện tại: Người chơi " + currentPlayer
            );
        } else {
            // Chế độ người chơi đấu với AI
            if (currentPlayer == 1) {
                gamePLayView.updateCurrentTurn("Lượt hiện tại: Bạn");
            } else {
                gamePLayView.updateCurrentTurn("Lượt hiện tại: AI");
            }
        }
    }

    //hàm khóa/mở khóa việc bấm nút
    public void setEnableButton(boolean isEnable) {
        gamePLayView.getPlayButton().setEnabled(isEnable);
        gamePLayView.getBackButton().setEnabled(isEnable);
        gamePLayView.getPreviousButton().setEnabled(isEnable);
        gamePLayView.getSaveButton().setEnabled(isEnable);
        gamePLayView.getHintButton().setEnabled(isEnable);
    }

    //hàm hiển thị danh sách ván chơi đã lưu
    public void showALlGames() {
        List<Game> games = fileManager.loadAllGames();

        SavedGameView savedGameView = new SavedGameView(games,
                selectedGame -> {
                    game = selectedGame;
                    gamePLayView = new GamePLayView(game);
                    gameView.addScreen(gamePLayView, "PLAY");
                    updateCurrentTurn();
                    gameView.showScreen("PLAY");
                    play();
                },
                deleteGame -> {
                    fileManager.deleteGame(deleteGame);
                    showNotification(Notification.Type.SUCCESS, "Đã xóa game" + deleteGame.getGame_id(), false);
                    showALlGames();
                }
        );


        gameView.addScreen(savedGameView, "SAVED");
        gameView.showScreen("SAVED");
        savedGameView.getBackButton().addActionListener(e -> {
            gameView.showScreen("MAINPAGE");
        });
    }

    public void settingGame() {
        GameSettingView gameSettingView = new GameSettingView();

        gameView.addScreen(gameSettingView, "SETTING");
        gameView.showScreen("SETTING");

        gameSettingView.getStartButton().addActionListener(e -> {
            boolean gamemode = gameSettingView.isGameMode();
            int firstPlayer = gameSettingView.getFirstPlayer();

            if (!gameSettingView.isRandomPileCount()) {
                // Kiểm tra số lượng đống sỏi
                if (!gameSettingView.isValidInput()) {
                    showNotification(Notification.Type.ERROR, "Số lượng sỏi phải nằm trong khoảng 1 - 50", true);
                    return;
                }
                int numOfDS = gameSettingView.getPileCount();

                try {
                    game = gameService.createGame(gamemode, numOfDS, firstPlayer);
                    if (game == null) {
                        showNotification(Notification.Type.ERROR, "Không thể khởi tạo ván chơi", false);
                        return;
                    }

                    gamePLayView = new GamePLayView(game);
                    gameView.addScreen(gamePLayView, "PLAY");
                    gameView.showScreen("PLAY");
                    play();
                    updateCurrentTurn();
                } catch (IOException ex) {
                    showNotification(Notification.Type.ERROR, "Không thể tạo ván chơi", false);
                }

            }
            else {
                try {
                    game = gameService.createGame(gamemode,firstPlayer);
                    if (game == null) {
                        showNotification(Notification.Type.ERROR, "Không thể khởi tạo ván chơi", false);
                        return;
                    }

                    gamePLayView = new GamePLayView(game);
                    gameView.addScreen(gamePLayView, "PLAY");
                    gameView.showScreen("PLAY");
                    play();
                    updateCurrentTurn();
                } catch (IOException ex) {
                }
            }
        });
        gameSettingView.getBackButton().addActionListener(e -> {
            gameView.showScreen("MAINPAGE");
        });
    }
}
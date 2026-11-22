package com.nim.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int game_id; //ID GAME
    private boolean gamemode; // CHẾ ĐỘ CHƠI: PVP HOẶC PVE
    private int player1; // NGƯỜI CHƠI 1
    private int player2; //NGƯỜI CHƠI 2
    private int luothientai; // LƯỢT HIỆN TẠI THUỘC VỀ AI
    private int ngthang; // NGUỜI CHIẾN THẮNG TRÒ CHƠI

    private LocalDateTime timeStart; //thgian bắt đầu tạo game
    private LocalDateTime timeEnd; // thgian game keest thúc

    private DongSoi[] dongSois;
    private List<LuotChoi> luotChois = new ArrayList<>();

    public Game() {
        this.player1 = 1;
        this.player2 = 2;
        this.ngthang = 0;
        this.timeStart = LocalDateTime.now();
        this.timeEnd = null;
    }

    public Game(int game_id) {
        this.game_id=game_id;
    }


    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public boolean isGamemode() {
        return gamemode;
    }

    public void setGamemode(boolean gamemode) {
        this.gamemode = gamemode;
    }

    public int getPlayer1() {
        return player1;
    }

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public int getPlayer2() {
        return player2;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    public int getLuothientai() {
        return luothientai;
    }

    public void setLuothientai(int luothientai) {
        this.luothientai = luothientai;
    }

    public int getNgthang() {
        return ngthang;
    }

    public void setNgthang(int ngthang) {
        this.ngthang = ngthang;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public DongSoi[] getDongSois() {
        return dongSois;
    }

    public void setDongSois(DongSoi[] dongSois) {
        this.dongSois = dongSois;
    }

    //thêm lượt chơi vào game
    public void addLuotChoi(LuotChoi luotChoi) {
        this.luotChois.add(luotChoi);
    }
}

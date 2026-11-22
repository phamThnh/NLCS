package com.nim.Entity;

public class DongSoi {
    private int ds_id; //id đống sỏi
    private int game_id; // id game chứa đống sỏi
    private int slbandau; //sl sỏi ban đầu của đống
    private int slhientai; //sl sỏi hiện tại của đống

    public DongSoi() {}

    public DongSoi(int game_id, int stt, int slbandau, int slhientai) {
        this.game_id = game_id;
        this.slbandau = slbandau;
        this.slhientai = slhientai;
    }


    public int getDs_id() {
        return ds_id;
    }

    public void setDs_id(int ds_id) {
        this.ds_id = ds_id;
    }

    public long getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getSlbandau() {
        return slbandau;
    }

    public void setSlbandau(int slbandau) {
        this.slbandau = slbandau;
    }

    public int getSlhientai() {
        return slhientai;
    }

    public void setSlhientai(int slhientai) {
        this.slhientai = slhientai;
    }
}

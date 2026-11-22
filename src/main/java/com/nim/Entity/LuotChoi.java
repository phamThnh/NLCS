package com.nim.Entity;

public class LuotChoi {
    private int luotchoi_id;
    private int game_id;
    private int ngthuchien; // người thực hiện lượt chơi hiện tại
    private int iddongsoi; //stt đống sỏi
    private int before; // số sỏi trước khi lấy
    private int after; // số sỏi sau khi lấy

    public LuotChoi() {}

    public LuotChoi(int game_id, int ngthuchien,
                    int iddongsoi, int before, int after) {
        this.game_id = game_id;
        this.ngthuchien = ngthuchien;
        this.iddongsoi = iddongsoi;
        this.before = before;
        this.after = after;
    }

    public int getLuotchoi_id() {
        return luotchoi_id;
    }

    public void setLuotchoi_id(int luotchoi_id) {
        this.luotchoi_id = luotchoi_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getNgthuchien() {
        return ngthuchien;
    }

    public void setNgthuchien(int ngthuchien) {
        this.ngthuchien = ngthuchien;
    }

    public int getIddongsoi() {
        return iddongsoi;
    }

    public void setIddongsoi(int iddongsoi) {
        this.iddongsoi = iddongsoi;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }

    //hàm thực hiện lượt chơi
}

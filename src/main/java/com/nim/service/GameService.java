package com.nim.service;

import com.nim.file.FileManager;

import com.nim.Entity.DongSoi;
import com.nim.Entity.Game;
import com.nim.Entity.LuotChoi;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class GameService {


    private int nextLuotChoiId = 1; // lưu id lượt chơi

    public int getNextLuotChoiId() {
        return nextLuotChoiId;
    }

    public void generateNextLuotChoiId() {
        this.nextLuotChoiId++;
    }

    //thuật toán nim sum cho AI
    public int nimSum(DongSoi[] dongSois) {
        int nimsum = 0;
        for(DongSoi d: dongSois) {
            nimsum^=d.getSlhientai();
        }

        return  nimsum;
    }
    //hàm tìm đống sỏi phù hợp vơi nimsum
    public String hint(DongSoi[] dongSois) {
        int nimSum = nimSum(dongSois);
        StringBuilder sb = new StringBuilder();
        if(nimSum != 0) {
            for (int i = 0; i < dongSois.length; i++) {
                int num = dongSois[i].getSlhientai(); // lấy số lượng của đống sỏi đang xét
                if (num == 0) continue; //bỏ qua đống sỏi đã hết
                int result = num ^ nimSum;

                if (result < num) {//đống sỏi cần tìm
                    sb.append("Lấy ").append(num - result).append(" viên sỏi từ đống sỏi thứ ").append(dongSois[i].getDs_id());
                    return sb.toString();
                }
            }
        }
        return "Bạn đang ở thế thua\n Hãy tìm cách câu giờ để đối thủ mắc sai lầm!!";
    }

    //hàm xác định bước đi của AI
    public LuotChoi moveOfAI(Game game) {
        //kiểm tra trò chơi còn tiếp tục được không
        if(!isContinue(game)) return null;

        LuotChoi luotChoiAI = null;
        DongSoi[] dongSois = game.getDongSois();

        int nimSum = nimSum(dongSois);
        if(nimSum != 0) { //AI đang ở thế thắng
            //tìm đống sỏi đầu tiên mà giá trị sau xor với nimSum nhỏ hơn đống đó
            for (int i=0; i< dongSois.length;i++) {
                int num = dongSois[i].getSlhientai(); // lấy số lượng của đống sỏi đang xét
                if(num == 0) continue; //bỏ qua đống sỏi đã hết
                int result = num ^ nimSum;

                if(result < num) { //đống sỏi cần tìm
                    luotChoiAI = new LuotChoi(game.getGame_id(),2,dongSois[i].getDs_id(),num,result);
                    //cập nhật lại số lượng đống sỏi
                    dongSois[i].setSlhientai(result);
                    //chuyển lượt
                    switchTurn(game);
                    return luotChoiAI;
                }
            }
        }
        //ngược lại AI đang ở thế thua -> chọn đống sỏi nhiều nhất và bốc
        int max = 0;
        DongSoi maxi = null;
        for(DongSoi d : dongSois) {
            if(max < d.getSlhientai()) {
                max = d.getSlhientai();
                maxi = d;
            }
        }
        if(maxi == null) return null;
        int slhientai = maxi.getSlhientai();

        int randomTake = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, slhientai + 1);
        luotChoiAI = new LuotChoi(game.getGame_id(),2,maxi.getDs_id(),slhientai, slhientai-randomTake);
        maxi.setSlhientai(slhientai-randomTake);
        //chuyển lượt
        switchTurn(game);

        return luotChoiAI;

    }

    //hàm sinh số lượng đống sỏi ngẫu nhiên
    public static int generateDongSoi() {
        return ThreadLocalRandom.current().nextInt(1,51); //sinh số lượng đống sỏi ngẫu nhiên từ 1 - 50
    }

    //hàm sinh số lượng sỏi ngẫu nhiên của mỗi đống
    public static int generateNumOfSoi() {
        return ThreadLocalRandom.current().nextInt(1,35); //sinh sl sỏi ngẫu nhiên từ 1 đến 34
    }

    //hàm kiểm tra xem còn sỏi trong game không
    public boolean isContinue(Game game) {
        DongSoi[] ds = game.getDongSois();
        for(int i=0; i<ds.length; i++) {
            if(ds[i].getSlhientai() > 0) return true;
        }
        //nếu không còn sỏi thì xác định người thắng
        if(game.getLuothientai() == 1) game.setNgthang(2);
        else game.setNgthang(1);
        game.setTimeEnd(LocalDateTime.now());
        return false;
    }

    //hàm lựa chọn người chơi đầu tiên
    public int choosseFirstPlayer() {
        int rand = ThreadLocalRandom.current().nextInt(1,3); //sinh ngẫu nhiên 1 hoặc 2
        if(rand % 2 != 0) return 1;
        else return 2;
    }

    //hàm chuyển lượt
    public void switchTurn(Game game) {
        if(game.getLuothientai() == 1) game.setLuothientai(2);
        else game.setLuothientai(1);
    }
    //hàm thực hiện lượt chơi của player
    public LuotChoi moveOfPLayer(Game game,DongSoi dongSoi, int quantity,int player) {

        int before = dongSoi.getSlhientai();
        if(before - quantity < 0 ) return null;
        dongSoi.setSlhientai(before-quantity);
        LuotChoi current = new LuotChoi(game.getGame_id(),player,dongSoi.getDs_id(),before,before-quantity);

        //chuyển lượt
        switchTurn(game);
        return current;
    }

    //hàm khởi tạo ván chơi
    public Game createGame(boolean gamemode) throws IOException {
        Game game = new Game();

        //sinh ID
        FileManager fileManager = new FileManager();
        int game_id = fileManager.generateID();
        game.setGame_id(game_id);

        //chọn chế độ chơi TRUE là PVP, FALSE là PVE
        game.setGamemode(gamemode);

        //lựa chọn người thực hiện nước đi đầu tiên
        int first = choosseFirstPlayer();
        game.setLuothientai(first);

        //sinh đống sỏi
        int numDongSoi = generateDongSoi();
        DongSoi[] d = new DongSoi[numDongSoi];
        game.setDongSois(d);

        //sinh số lượng sỏi cho mỗi đống
        for(int i=0; i< d.length; i++) {
            d[i] = new DongSoi();
            int numSoi = generateNumOfSoi();
            d[i].setSlbandau(numSoi);
            d[i].setSlhientai(numSoi);
            d[i].setDs_id(i+1);
            d[i].setGame_id(game.getGame_id());
        }
        fileManager.saveGame(game);
        return game;
    }
    public Game createGame(boolean gamemode, int numofDS, int firstPLayer) throws IOException{
        Game game = new Game();

        //sinh ID
        FileManager fileManager = new FileManager();
        int game_id = fileManager.generateID();
        game.setGame_id(game_id);

        game.setGamemode(gamemode);
        game.setLuothientai(firstPLayer);

        DongSoi[] d = new DongSoi[numofDS];
        game.setDongSois(d);
        for(int i=0; i<numofDS; i++) {
            d[i] = new DongSoi();
            int numSoi = generateNumOfSoi();
            d[i].setSlbandau(numSoi);
            d[i].setSlhientai(numSoi);
            d[i].setDs_id(i+1);
            d[i].setGame_id(game.getGame_id());
        }
        fileManager.saveGame(game);
        return game;
    }
    public Game createGame(boolean gamemode, int firstPLayer) throws IOException{
        Game game = new Game();

        //sinh ID
        FileManager fileManager = new FileManager();
        int game_id = fileManager.generateID();
        game.setGame_id(game_id);

        game.setGamemode(gamemode);
        game.setLuothientai(firstPLayer);

        int numofDS = generateDongSoi();
        DongSoi[] d = new DongSoi[numofDS];
        game.setDongSois(d);
        for(int i=0; i<numofDS; i++) {
            d[i] = new DongSoi();
            int numSoi = generateNumOfSoi();
            d[i].setSlbandau(numSoi);
            d[i].setSlhientai(numSoi);
            d[i].setDs_id(i+1);
            d[i].setGame_id(game.getGame_id());
        }
        fileManager.saveGame(game);
        return game;
    }
}

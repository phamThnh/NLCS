package com.nim.file;

import com.nim.Entity.DongSoi;
import com.nim.Entity.Game;
import com.nim.Entity.LuotChoi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class FileManager {

    // Khai báo biến đường dẫn động
    private final Path gamePath;
    private final Path lcPath;

    public FileManager() {
        // Lấy đường dẫn AppData trên Windows (hoặc user.home trên Mac/Linux)
        String appData = System.getenv("APPDATA");
        Path baseDir;
        if (appData != null) {
            baseDir = Path.of(appData, "NIM_Game_Data");
        } else {
            baseDir = Path.of(System.getProperty("user.home"), "NIM_Game_Data");
        }

        // Tạo đường dẫn cho thư mục game và luotchoi
        gamePath = baseDir.resolve("game");
        lcPath = baseDir.resolve("luotchoi");

        // Đảm bảo thư mục luôn tồn tại khi khởi tạo FileManager
        try {
            Files.createDirectories(gamePath);
            Files.createDirectories(lcPath);
        } catch (IOException e) {
            System.err.println("Không thể tạo thư mục lưu game: " + e.getMessage());
        }
    }

    // hàm lấy đường dẫn file game
    public Path getFilePath(int id) {
        return gamePath.resolve("game" + id + ".txt");
    }

    // tự sinh mã id bắt đầu từ 1
    public int generateID() throws IOException {
        int id = 1;
        while (Files.exists(getFilePath(id))) id++;
        return id;
    }

    // hàm lưu game, tạo file nếu file chưa tồn tại
    public void saveGame(Game game) throws IOException {
        int game_id = game.getGame_id();
        Path filePath = getFilePath(game_id);

        // lưu đống sỏi
        StringBuilder sb = new StringBuilder();
        DongSoi[] dongSoi = game.getDongSois();
        for (int i = 0; i < dongSoi.length; i++) {
            sb.append("ds_id=").append(i + 1)
                    .append(";slbandau=").append(dongSoi[i].getSlbandau())
                    .append(";slhientai=").append(dongSoi[i].getSlhientai());
            // Đã sửa lại lỗi cú pháp ở dòng này
            if (i < dongSoi.length - 1) sb.append("\n");
        }

        // lưu game
        List<String> lines = List.of("game_id=" + game_id,
                "gamemode=" + game.isGamemode(),
                "Player1=" + game.getPlayer1(),
                "Player2=" + game.getPlayer2(),
                "luothientai=" + game.getLuothientai(),
                "ngthang=" + game.getNgthang(),
                "timeStart=" + game.getTimeStart(),
                "timeEnd=" + game.getTimeEnd(),
                sb.toString());

        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // hàm lưu lượt chơi của game
    public void saveLuotChoi(LuotChoi luotChoi, int id) throws IOException {
        Path filelc = lcPath.resolve("luotchoi_game" + luotChoi.getGame_id() + ".txt");

        // lưu lượt chơi
        StringBuilder sb = new StringBuilder();
        sb.append("luotchoi_id=").append(id)
                .append(";ngthuchien=").append(luotChoi.getNgthuchien())
                .append(";iddongsoi=").append(luotChoi.getIddongsoi())
                .append(";before=").append(luotChoi.getBefore())
                .append(";after=").append(luotChoi.getAfter());

        List<String> line = List.of(sb.toString());

        if (!Files.exists(filelc)) Files.write(filelc, line);
        else Files.write(filelc, line, StandardOpenOption.APPEND);
    }

    // hàm đọc dữ liệu game đã lưu
    public Game loadGame(int gameId) throws IOException {
        Path filePath = getFilePath(gameId);
        if (!Files.exists(filePath)) throw new IOException("không tìm thấy Game nào");
        List<String> lines = Files.readAllLines(filePath);

        Game game = new Game(gameId);
        List<DongSoi> dongSois = new ArrayList<>();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (!line.startsWith("ds_id")) {
                String[] parts = line.split("=", 2);
                if (parts.length != 2) throw new IOException("dữ liệu game không hợp lệ");
                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "game_id" -> game.setGame_id(Integer.parseInt(value));
                    case "gamemode" -> game.setGamemode(Boolean.parseBoolean(value));
                    case "Player1" -> game.setPlayer1(Integer.parseInt(value));
                    case "Player2" -> game.setPlayer2(Integer.parseInt(value));
                    case "luothientai" -> game.setLuothientai(Integer.parseInt(value));
                    case "ngthang" -> game.setNgthang(Integer.parseInt(value));
                    case "timeStart" -> game.setTimeStart(LocalDateTime.parse(value));
                    case "timeEnd" -> {
                        if (value.equals("null")) game.setTimeEnd(null);
                        else game.setTimeEnd(LocalDateTime.parse(value));
                    }
                }
            } else {
                String[] fields = line.split(";");
                DongSoi dongSoi = new DongSoi();
                for (String field : fields) {
                    String[] pair = field.split("=", 2);
                    if (pair.length != 2) throw new IOException("Dữ liệu đống sỏi không hợp lệ: " + line);
                    else {
                        String key = pair[0];
                        String value = pair[1];

                        switch (key) {
                            case "ds_id" -> dongSoi.setDs_id(Integer.parseInt(value));
                            case "slbandau" -> dongSoi.setSlbandau(Integer.parseInt(value));
                            case "slhientai" -> dongSoi.setSlhientai(Integer.parseInt(value));
                        }
                    }
                }
                dongSois.add(dongSoi);
            }
        }
        game.setDongSois(dongSois.toArray(new DongSoi[0]));
        return game;
    }

    // hàm đọc dữ liệu lượt chơi trước đó
    public LuotChoi loadPreviousLuotChoi(int gameId) throws IOException, IndexOutOfBoundsException {
        Path filelc = lcPath.resolve("luotchoi_game" + gameId + ".txt");
        List<String> lines = Files.readAllLines(filelc);

        LuotChoi luotChoi = new LuotChoi();

        String current = lines.get(lines.size() - 1);
        String[] fields = current.split(";");
        for (String field : fields) {
            String[] part = field.split("=", 2);
            String key = part[0];

            switch (key) {
                case "luotchoi_id" -> luotChoi.setLuotchoi_id(Integer.parseInt(part[1]));
                case "ngthuchien" -> luotChoi.setNgthuchien(Integer.parseInt(part[1]));
                case "iddongsoi" -> luotChoi.setIddongsoi(Integer.parseInt(part[1]));
                case "before" -> luotChoi.setBefore(Integer.parseInt(part[1]));
                case "after" -> luotChoi.setAfter(Integer.parseInt(part[1]));
            }
        }
        // xóa dòng cuối trong File
        lines.remove(lines.size() - 1);
        Files.write(filelc, lines);
        return luotChoi;
    }

    // hàm đọc file sớm nhất
    public int recentlyFile() {
        try (Stream<Path> list_file = Files.list(gamePath)) {
            Path recentFile = list_file.filter(Files::isRegularFile).max(Comparator.comparingLong(p -> p.toFile().lastModified())).orElse(null);

            if(recentFile != null) {
                String fileName = recentFile.getFileName().toString().trim();
                String id = fileName.replaceAll("\\D+","");
                if(!id.isEmpty()) return Integer.parseInt(id);
            }
        } catch (IOException e) {
            System.out.println("Thư mục không tồn tại----FileManager.java");
        }
        return -1;
    }

    // hàm tải tất cả ván chơi hiện có của thư mục
    public List<Game> loadAllGames() {
        List<Game> games = new ArrayList<>();
        try (Stream<Path> list_game = Files.list(gamePath)) {
            List<Path> only_File = list_game.filter(Files::isRegularFile).toList();

            for (Path p : only_File) {
                String name = p.getFileName().toString().trim();
                String number = name.replaceAll("\\D+", "");

                int idgame = Integer.parseInt(number);
                Game gameNeedLoad = loadGame(idgame);

                games.add(gameNeedLoad);
            }
        } catch (IOException e) {
            System.out.println("Thư mục không tồn tại----FileManager.java");
        }
        return games;
    }

    public void deleteGame(Game game) {
        Path fileGamePath = getFilePath(game.getGame_id());
        Path fileLCPath = lcPath.resolve("luotchoi_game" + game.getGame_id() + ".txt");
        try {
            Files.deleteIfExists(fileGamePath);
            Files.deleteIfExists(fileLCPath);
        }
        catch (IOException e) {
        }
    }
}
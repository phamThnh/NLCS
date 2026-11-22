package com.nim.view;
import com.nim.view.ui.GameView;
import javax.swing.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Main {
    private static FileLock fileLock;
    private static FileChannel fileChannel;

    private static boolean checkInstance() {
        try {
            // Lấy đúng đường dẫn thư mục AppData bạn đã cài đặt trong FileManager
            String appData = System.getenv("APPDATA");
            File lockFile = new File(appData != null ? appData : System.getProperty("user.home"), "NIM_Game_Data/app.lock");

            // Đảm bảo thư mục đã tồn tại trước khi tạo file lock
            lockFile.getParentFile().mkdirs();

            fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            // Cố gắng khóa file
            fileLock = fileChannel.tryLock();

            // Nếu fileLock == null nghĩa là đã có một tiến trình game khác đang giữ khóa
            if (fileLock == null) {
                return false;
            }

            // Giải phóng khóa và xóa file khi tắt game
            lockFile.deleteOnExit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static void main(String[] args) {
        if (!checkInstance()) {
            System.exit(0); // Dừng khởi động ứng dụng thứ 2 ngay lập tức
        }
        //ghi nhận thời gian mở ứng dụng
        long startTime = System.currentTimeMillis();
        SwingUtilities.invokeLater(() -> {
            GameView view = new GameView();

            view.setVisible(true);
            //kết thúc thời gian
            long endTime = System.currentTimeMillis();

            //Tính toán và in ra thời gian khởi động
            long thoiGianMoApp = endTime - startTime;
            System.out.println("Thời gian mở ứng dụng: " + thoiGianMoApp + " ms");
        });
    }
}


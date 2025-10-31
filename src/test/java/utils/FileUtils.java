package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static String getProjectTestDataPath() {
        return System.getProperty("user.dir") + File.separator + "test-data" + File.separator;
    }

    public static String getDownloadPath() {
        return System.getProperty("user.dir") + File.separator + "downloads" + File.separator;
    }

    public static void createTestFile(String fileName, String content) throws IOException {
        Path filePath = Paths.get(getProjectTestDataPath() + fileName);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, content);
    }

    public static boolean isFileDownloaded(String fileName) {
        File file = new File(getDownloadPath() + fileName);
        return file.exists();
    }

    public static void cleanupDownload(String fileName) {
        File file = new File(getDownloadPath() + fileName);
        cleanupDownload(file, false);
    }

    public static void cleanupDownloads() {
        File file = new File(getDownloadPath());
        if (file.exists()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (var childFile: children) {
                    cleanupDownload(childFile, true);
                }
            }

            try {
                if(!file.delete()) {
                    System.out.println("Could not delete '" + file.getAbsolutePath() + "'");
                }
            } catch (SecurityException e) {
                System.out.println("Could not delete '" + file.getAbsolutePath() + "': " + e.getMessage());
            }
        }
    }

    private static boolean cleanupDownload(File file, boolean recursive) {
        if (file.exists()) {
            if (recursive) {
                File[] children = file.listFiles();
                if (children != null) {
                    for (var childFile: children) {
                        cleanupDownload(childFile, true);
                    }
                }
            }
            try {
                if(!file.delete()) {
                    System.out.println("Could not delete '" + file.getAbsolutePath() + "'");
                    return false;
                }
            } catch (SecurityException e) {
                System.out.println("Could not delete '" + file.getAbsolutePath() + "': " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}

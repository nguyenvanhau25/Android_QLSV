package com.example.qlsv_kthp.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileUtils {

    public static void exportToCSV(Context context, String fileName, List<String[]> data) {
        File folder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, fileName + ".csv");
        try {
            FileWriter writer = new FileWriter(file);
            for (String[] row : data) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    line.append(row[i]);
                    if (i < row.length - 1) line.append(",");
                }
                writer.append(line.toString()).append("\n");
            }
            writer.flush();
            writer.close();
            Toast.makeText(context, "Đã xuất file tại: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi xuất file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

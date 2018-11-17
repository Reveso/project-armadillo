package com.lukasrosz.armadillo;

import com.sun.istack.internal.Nullable;
import com.sun.jndi.toolkit.url.Uri;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    @Nullable
    private static List<File> files;
    private static File dir;
    FileReader fileReader;
    Uri uri;

    public static void loadFiles() {
//        dir = Paths.get()
//                fileReader.

        files = new ArrayList<>();
//        uri = new Uri()
        dir = new File(String.valueOf(Paths.get("programs")));
        files = Arrays.asList((dir.listFiles()));
        for (File file1: files) {
            if (!file1.canExecute()) {
                files.remove(file1);
            }
        }
    }

    public static void main(String[] args) {
        loadFiles();
        System.out.println(dir.getAbsolutePath());
        System.out.println(files);
    }

}

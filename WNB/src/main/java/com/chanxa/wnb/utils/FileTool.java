package com.chanxa.wnb.utils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by CHANXA on 2014/12/26.
 */
public class FileTool {

    public static void deleteFolder(String folderPath) throws FileNotFoundException {
        if (folderPath == null || folderPath.equals("")) {
            //return;
            throw new FileNotFoundException("未找到此文件夹");
        }
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw  new FileNotFoundException("不是一个文件夹");
        }
        File[] childFile = folder.listFiles();
        for (File file : childFile) {
            if (file.isDirectory()) {
                deleteFolder(file.getPath());
            }
            file.delete();
        }
        folder.delete();
    }
}

package pers.lomesome.compliation.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void findFileAndFolderList(File dir, List<String> fileNames, List<String> folderNames) {
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            return;
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果是文件
                if (!file.getName().equals(".DS_Store"))
                    fileNames.add(dir + "/" + file.getName());// 添加文件全路径名
            } else {// 如果是目录
                folderNames.add(dir + "/" + file.getName());
                findFileAndFolderList(file, fileNames, folderNames);// 回调自身继续查询
            }
        }
    }

    public static List<String> findAll() {
        List<String> fileNames = new ArrayList<>();
        List<String> folderNames = new ArrayList<>();
        FileUtil.findFileAndFolderList(new File(OpenProject.getMyProject().getPath() + "/" + OpenProject.getMyProject().getName()), fileNames, folderNames);
        return fileNames;
    }

    public static void deleteDir(File file) {

        if (!file.exists()) {
            return;
        }

        if (!file.isFile()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        file.delete();
    }
}

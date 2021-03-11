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
        assert files != null;
        for (String s : files) {// 循环，添加文件名或回调自身
            File file = new File(dir, s);
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
        if (!file.exists()) { //如果该文件夹不存在则直接返回
            return;
        }
        if (!file.isFile()) { //如果是文件夹递归删除该文件夹内的子文件
            File[] files = file.listFiles();
            assert files != null;
            for (File value : files) {
                deleteDir(value);
            }
        }
        boolean delete = file.delete(); //如果是文件就删除
    }
}

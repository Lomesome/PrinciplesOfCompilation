package pers.lomesome.compliation.controller;

import pers.lomesome.compliation.model.MyProject;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReadAndWriteFile {
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            int data;
            while ((data = reader.read()) != -1) {
                sbf.append((char) data);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) { }
            }
        }
        return sbf.toString();
    }

    public static void write(String path, String content) throws IOException {
        //将写入转化为流的形式
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(content);
        //关闭流
        bw.close();
    }

    public static void save(Map<String, MyProject> projects) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(".projects.dat")));
            oos.writeObject(projects);
            oos.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void readObj() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(".projects.dat")))) {
            ManageProjects.setProjectsMap((LinkedHashMap) ois.readObject());
        } catch (Exception ignored) { }
    }
}

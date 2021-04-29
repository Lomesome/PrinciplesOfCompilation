package pers.lomesome.compliation.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DrawTree {

    public static void draw(String json){
        Process proc;
        try {
            String[] args1 = new String[] { "python", "./src/pers/lomesome/compliation/python/GrammerTree.py", json};
            proc = Runtime.getRuntime().exec(args1);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream())); //用输入输出流来截取结果
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

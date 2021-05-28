package pers.lomesome.compliation.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunC {
    public static void runAsm(){
        Process proc;
        try {
            String[] args1 = new String[] { "gcc", "/Users/leiyunhong/Desktop/t1/test2.c", "-o", "./src/pers/lomesome/compliation/c/asm.out"};
            Runtime.getRuntime().exec(args1);// 执行
            String[] args2 = new String[] { "./src/pers/lomesome/compliation/c/asm.out", "hello mac!"};
            proc = Runtime.getRuntime().exec(args2);// 执行
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

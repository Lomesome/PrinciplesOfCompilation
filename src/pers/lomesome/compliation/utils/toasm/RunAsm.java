package pers.lomesome.compliation.utils.toasm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RunAsm {
    public List<String> results = new ArrayList<>();
    public void bianyiAsm(){
        Process proc;
        try {
            String[] args1 = new String[] { "nasm", "-f", "macho64", "/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/pers/lomesome/compliation/utils/toasm/test.asm"};
            Runtime.getRuntime().exec(args1);// 执行
            String[] args2 = new String[] { "ld", "-macosx_version_min", "10.14", "-o", "/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/pers/lomesome/compliation/utils/toasm/test", "-e", "_start", "/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/pers/lomesome/compliation/utils/toasm/test.o", "-lSystem", "-L/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/lib", "-no_pie"};
            proc = Runtime.getRuntime().exec(args2);// 执行
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runAsm() {
        Process proc;
        try {
            String[] args = new String[]{"/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/pers/lomesome/compliation/utils/toasm/test"};
            proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream())); //用输入输出流来截取结果
            String line = null;
            while ((line = in.readLine()) != null) {
//                System.out.println(line);
                results.add(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> getResults() {
        return results;
    }

    public static void main(String[] args) {
        RunAsm runAsm = new RunAsm();
        runAsm.bianyiAsm();
        runAsm.runAsm();
        for (String s : runAsm.getResults()){
            System.out.print(s);

        }
    }
}

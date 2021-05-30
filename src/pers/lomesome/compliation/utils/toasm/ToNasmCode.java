package pers.lomesome.compliation.utils.toasm;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.utils.semantic.SymbolTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ToNasmCode {
    private ArrayList<String> asmCode = new ArrayList<>();//存储汇编代码
    private ArrayList<String> preAsmCode = new ArrayList<>();//存储汇编代码
    private int asmCount = 0;//跳转计数器
    private int[] asmJump;//记录每个四元式对应的第一条指令位置
    private int msgCount = 0;

    public void cToAsm(SymbolTable table, LiveStatu liveStatu) {
        asmJump = new int[liveStatu.getQt().size()];
        preAsmCode.add("global _start");  //定义入口函数
        preAsmCode.add("extern _printf");  //导入printf输出
        preAsmCode.add("section .data");  //数据段
        for (int i = 0; i < table.Const.size(); i++) {
            if (table.Const.get(i).type.equals("int") || table.Const.get(i).type.equals("char") || table.Const.get(i).type.equals("float")) {
                preAsmCode.add("    "+table.Const.get(i).name + "   equ   " + table.Const.get(i).value);
            }
        }

        preAsmCode.add("    fmt: db '%ld',0xa,0");

        preAsmCode.add("section .bss");  //数据段
        for (int i = 0; i < table.Synbl.size(); i++) {
            if (table.Synbl.get(i).type.equals("int") || table.Synbl.get(i).type.equals("char") || table.Synbl.get(i).type.equals("float")) {
                preAsmCode.add(table.Synbl.get(i).name + ": resb 4");
            }
        }


        preAsmCode.add("section .text");;
        preAsmCode.add("_start:");
        preAsmCode.add("    push rbp");

        for (int i = 0; i < liveStatu.getQt().size(); i++) {
            if (i == 6 || i ==7 ){
                System.out.println(111);
            }
            Quaternary temp = liveStatu.getQt().get(i);
            if (temp.getOperator().getWord().equals("+")) {//如果四元式的首符号是+
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    add rax, rbx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("-")) {//如果四元式的首符号是-
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    sub rax, rbx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("*")) {//如果四元式的首符号是*
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    mul rbx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("/")) {//如果四元式的首符号是/
                asmCode.add("    mov rdx, 0");
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    div rbx");
                asmCode.add("    mov rdx, rcx");
                asmCode.add("    mov [rel " + temp.getResult() + "], rax");
                asmJump[i] = asmCount;
                asmCount += 6;
            } else if (liveStatu.getQt().get(i).getOperator().getWord().equals("=")) {//赋值语句
                if (liveStatu.getQt().get(i).getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + liveStatu.getQt().get(i).getArg1());
                    asmCode.add("    mov [rel " + liveStatu.getQt().get(i).getResult() + "], rax");
                }else {
                    asmCode.add("    mov rax, [rel " + liveStatu.getQt().get(i).getArg1() + "]");
                    asmCode.add("    mov [rel " + liveStatu.getQt().get(i).getResult() + "], rax");
                }
                asmJump[i] = asmCount;
                asmCount += 2;
            } else if (temp.getOperator().getWord().equals("==")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("je " + liveStatu.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals(">")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("jg " + liveStatu.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("<")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("jl " + liveStatu.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals(">=")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("jge " + liveStatu.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("<=")) {
                if (temp.getArg1().getName().contains("const")){
                    asmCode.add("    mov rax, " + temp.getArg1());
                }else {
                    asmCode.add("    mov rax, [rel " + temp.getArg1()+"]");
                }
                if (temp.getArg2().getName().contains("const")){
                    asmCode.add("    mov rbx, " + temp.getArg2());
                }else {
                    asmCode.add("    mov rbx, [rel " + temp.getArg2()+"]");
                }
                asmCode.add("    cmp rax, rbx");
                asmCode.add("jle " + liveStatu.getQt().get(i).getResult());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getOperator().getWord().equals("j")) {
                asmCode.add("jmp " + temp.getResult());
                asmJump[i] = asmCount;
                asmCount += 1;
            } else if (temp.getOperator().getWord().equals("print")) {
                if (temp.getResult().getName().contains("const")){
                    asmCode.add("   mov rax, 0x2000004");
                    asmCode.add("   mov rdi, 1");
                    asmCode.add("   mov rsi, msg" + msgCount);
                    asmCode.add("   mov rdx, msg_len" + msgCount);
                    asmCode.add("   syscall");
                    addMsg("msg" + msgCount + ": db " + temp.getResult() + ",10\nmsg_len" + msgCount + ": equ $ - msg" + msgCount);
                }else {
                    asmCode.add("    mov rdi, fmt");
                    asmCode.add("    mov rax, [rel " + temp.getResult() + "]");
                    asmCode.add("    mov rsi, rax");
                    asmCode.add("    mov rax, 0");
                    asmCode.add("    call _printf");
                }
                asmJump[i] = asmCount;
                asmCount += 5;
            }
        }
        asmJump[liveStatu.getQt().size() - 1] = asmCode.size();

        asmCode.add("    pop rbp");
        asmCode.add("    mov rax, 60");
        asmCode.add("    syscall");
        asmCode.add("    ret");

        Map<String, String> labelMap = new LinkedHashMap<>();
        AtomicInteger labelnum = new AtomicInteger();
        asmCode.forEach(s -> {
            if (s.startsWith("j")){
                String[] getJumpNum = s.split(" ");
                String toWhere = getJumpNum[1];
                if (labelMap.get(toWhere) == null){
                    labelMap.put(toWhere, "TURN" + labelnum.getAndIncrement());
                }
            }
        });

        for (int j = 0; j < asmCode.size(); j++) {
            if (asmCode.get(j).startsWith("j")){
                String[] getJumpNum = asmCode.get(j).split(" ");
                String toWhere = getJumpNum[1];
                if (labelMap.get(toWhere) == null){
                    labelMap.put(toWhere, "TURN" + labelnum.getAndIncrement());
                }
            }
        }

        for (int j = 0; j < asmCode.size(); j++) {
            if (asmCode.get(j).startsWith("j")){
                String[] getJumpNum = asmCode.get(j).split(" ");
                String toWhere = getJumpNum[1];
                asmCode.set(j, getJumpNum[0] + " " + labelMap.get(toWhere));
            }
        }


        labelMap.forEach((k, v)->{
            String temp = asmCode.get(asmJump[Integer.parseInt(k)]);
            asmCode.set(asmJump[Integer.parseInt(k)], v + ":\n" + temp);
        });

        asmCode.forEach(System.out::println);

    }

    public void addMsg(String msg){
        for (int i = 0; i < preAsmCode.size(); i++){
            if (preAsmCode.get(i).equals("section .data")){
                preAsmCode.add(i + 1, msg);
                msgCount++;
                return;
            }
        }
    }

    public ArrayList<String> getAsmCode() {
        return asmCode;
    }

    public ArrayList<String> getPreAsmCode() {
        return preAsmCode;
    }

    public List<String> getResults() throws IOException {
        List<String> results = new ArrayList<>(preAsmCode);
        results.addAll(asmCode);
        StringBuilder content = new StringBuilder();
        results.forEach(s -> {
            content.append(s).append("\n");
        });
        ReadAndWriteFile.write("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/pers/lomesome/compliation/utils/toasm/test.asm", content.toString());
        return results;
    }
}

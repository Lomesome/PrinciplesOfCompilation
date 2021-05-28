package pers.lomesome.compliation.utils.toasm;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.utils.semantic.SymbolTable;
import java.util.ArrayList;

public class ToAsm {
    public ArrayList<String> asmCode = new ArrayList<>();//存储汇编代码
    public ArrayList<String> preAsmCode = new ArrayList<>();//存储汇编代码
    public int asmCount = 0;//跳转计数器
    public int[] asmJump;//记录每个四元式对应的第一条指令位置

    public void cToAsm(SymbolTable table, LiveStatu liveStatu) {
        asmJump = new int[liveStatu.getQt().size()];
        preAsmCode.add("DATAS SEGMENT");
        for (int i = 0; i < table.Const.size(); i++) {
            if (table.Const.get(i).type.equals("int") || table.Const.get(i).type.equals("char") || table.Const.get(i).type.equals("float")) {
                preAsmCode.add(table.Const.get(i).name + "   DB   " + table.Const.get(i).value);
            }
        }
        for (int i = 0; i < table.Synbl.size(); i++) {
            if (table.Synbl.get(i).type.equals("int") || table.Synbl.get(i).type.equals("char") || table.Synbl.get(i).type.equals("float")) {
                preAsmCode.add(table.Synbl.get(i).name + "   DB   ?");
            } else if (table.Synbl.get(i).type.contains("[")) {
                preAsmCode.add(table.Synbl.get(i).name + "   DB   " + table.Synbl.get(i).tp + " DUP(0)");
            }
        }
        preAsmCode.add("DATAS ENDS");
        preAsmCode.add("STACKS SEGMENT");
        preAsmCode.add("    DB 128 DUP (?)");
        preAsmCode.add("    DW 64 DUP (?)");
        preAsmCode.add("STACKS ENDS");
        preAsmCode.add("CODES SEGMENT");
        preAsmCode.add("    ASSUME CS:CODES,DS:DATAS,SS:STACKS");

        preAsmCode.add("PRINT_NUM_OF_DL PROC NEAR");
        preAsmCode.add("    PUSH DX");
        preAsmCode.add("    XOR AH, AH");
        preAsmCode.add("    MOV AL, DL");
        preAsmCode.add("    MOV DH, 10");
        preAsmCode.add("    DIV DH ");
        preAsmCode.add("    TEST AL, 0FFH");
        preAsmCode.add("    JZ SINGLE");
        preAsmCode.add("    PUSH AX");
        preAsmCode.add("    XOR AH, AH");
        preAsmCode.add("    DIV DH ");
        preAsmCode.add("    TEST AL, 0FFH");
        preAsmCode.add("    JZ TWO");
        preAsmCode.add("    PUSH AX ");
        preAsmCode.add("    MOV DL, AL");
        preAsmCode.add("    ADD DL, '0'");
        preAsmCode.add("    MOV AH, 02H");
        preAsmCode.add("    int 21h");
        preAsmCode.add("    POP AX");
        preAsmCode.add("TWO:");
        preAsmCode.add("    MOV DL, AH");
        preAsmCode.add("    ADD DL, '0'");
        preAsmCode.add("    MOV AH, 02H");
        preAsmCode.add("    int 21H");
        preAsmCode.add("    POP AX ");
        preAsmCode.add("SINGLE:");
        preAsmCode.add("    MOV DL, AH");
        preAsmCode.add("    ADD DL, '0'");
        preAsmCode.add("    MOV AH, 02h");
        preAsmCode.add("    int 21h");
        preAsmCode.add("    POP DX");
        preAsmCode.add("    RET");
        preAsmCode.add("PRINT_NUM_OF_DL ENDP");

        preAsmCode.add("START:");
        preAsmCode.add("    MOV AX,DATAS");
        preAsmCode.add("    MOV DS,AX");

        for (int i = 0; i < liveStatu.getQt().size(); i++) {
            Quaternary temp = liveStatu.getQt().get(i);
            if (temp.getFirst().getWord().equals("+")) {//如果四元式的首符号是+
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    ADD AX," + temp.getThird());
                asmCode.add("    MOV " + temp.getFourth() + ",AX");
                asmJump[i] = asmCount;
                asmCount += 3;
            } else if (temp.getFirst().getWord().equals("-")) {//如果四元式的首符号是-
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    SUB AX," + temp.getThird());
                asmCode.add("    MOV " + temp.getFourth() + ",AX");
                asmJump[i] = asmCount;
                asmCount += 3;
            } else if (temp.getFirst().getWord().equals("*")) {//如果四元式的首符号是*
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    SUB BX," + temp.getThird());
                asmCode.add("    MUL BX");
                asmCode.add("    MOV " + temp.getFourth() + ",AX");
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getFirst().getWord().equals("/")) {//如果四元式的首符号是/
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    MOV DX,0");
                asmCode.add("    MOV BX," + temp.getThird());
                asmCode.add("    DIV BX");
                asmCode.add("    MOV " + temp.getFourth() + ",DX");
                asmJump[i] = asmCount;
                asmCount += 5;
            } else if (liveStatu.getQt().get(i).getFirst().getWord().equals("=")) {//赋值语句
                asmCode.add("    MOV AX," + liveStatu.getQt().get(i).getSecond());
                asmCode.add("    MOV " + liveStatu.getQt().get(i).getFourth() + ",AX");
                asmJump[i] = asmCount;
                asmCount += 2;
            } else if (temp.getFirst().getWord().equals("==")) {
                asmCode.add("    MOV DX,1");
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    CMP AX," + temp.getThird());
                asmCode.add("JE " + liveStatu.getQt().get(i).getFourth());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getFirst().getWord().equals(">")) {
                asmCode.add("    MOV DX,1");
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    CMP AX," + temp.getThird());
                asmCode.add("JG " + liveStatu.getQt().get(i).getFourth());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getFirst().getWord().equals("<")) {
                asmCode.add("    MOV DX,1");
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    CMP AX," + temp.getThird());
                asmCode.add("JL " + liveStatu.getQt().get(i).getFourth());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getFirst().getWord().equals(">=")) {
                asmCode.add("    MOV DX,1");
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    CMP AX," + temp.getThird());
                asmCode.add("JGE " + liveStatu.getQt().get(i).getFourth());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getFirst().getWord().equals("<=")) {
                asmCode.add("    MOV DX,1");
                asmCode.add("    MOV AX," + temp.getSecond());
                asmCode.add("    CMP AX," + temp.getThird());
                asmCode.add("JLE " + liveStatu.getQt().get(i).getFourth());
                asmJump[i] = asmCount;
                asmCount += 4;
            } else if (temp.getFirst().getWord().equals("j")) {
                asmCode.add("JMP " + temp.getFourth());
                asmJump[i] = asmCount;
                asmCount += 1;
            } else if (temp.getFirst().getWord().equals("print")) {
                asmCode.add("    MOV DL," + temp.getFourth());
                asmCode.add("    CALL PRINT_NUM_OF_DL");
                asmCode.add("    MOV AH,2");
                asmCode.add("    MOV DL,32");
                asmCode.add("    int 21H");
                asmJump[i] = asmCount;
                asmCount += 5;
            }
        }
        asmJump[liveStatu.getQt().size() - 1] = asmCode.size();
        asmCode.add("    MOV AH,4CH");
        asmCode.add("    INT 21H");
        asmCode.add("CODES ENDS");
        asmCode.add("    END START");

        int jumpCount = 0;//记录跳转位置
        for (int j = 0; j < asmCode.size(); j++) {
            if (asmCode.get(j).charAt(0) == 'J') {
                //如果是跳转指令
                String[] getJumpNum = asmCode.get(j).split(" ");
                String toWhere = getJumpNum[1];
                System.out.println(getJumpNum[0]  + " "+toWhere);
                if (asmCode.get(asmJump[Integer.parseInt(toWhere)]).charAt(0) == 'T') {//如果该处已经填有标号
                    String[] getWhere = asmCode.get(asmJump[Integer.parseInt(toWhere)]).split(":");
                    asmCode.set(j, getJumpNum[0] + " " + getWhere[0]);
                } else {//如果该处没有标号
                    String temp = asmCode.get(asmJump[Integer.parseInt(toWhere)]);
                    asmCode.set(asmJump[Integer.parseInt(toWhere)], "TURN" + jumpCount + ":\n" + temp);
                    asmCode.set(j, getJumpNum[0] + " TURN" + jumpCount);
                    jumpCount++;
                }
            }
        }
    }
}

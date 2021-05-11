package pers.lomesome.compliation.utils.toasm;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.utils.semantic.SymbolTable;
import java.util.ArrayList;

public class ToAsmCode {
    public ArrayList<String> asmCode = new ArrayList<>();//存储汇编代码
    public ArrayList<String> preAsmCode = new ArrayList<>();//存储汇编代码
    public String RDL = "";
    public int asmCount = 0;//跳转计数器
    public int[] asmJump;//记录每个四元式对应的第一条指令位置

    public void cToAsm(SymbolTable table, LiveStatu liveStatu) {
        System.out.println("汇编指令：");
        asmJump = new int[liveStatu.getQt().size() + 1];
        ObjectCode.Blocked(liveStatu);
        preAsmCode.add("ASSUME CS:CODE,DS:DATAS");
        int whereEnds = -1;
        for (int i = 0; i < table.Synbl.size(); i++) {
            if (table.Synbl.get(i).equals("struct")) {//如果是struct的情况
                preAsmCode.add(table.Synbl.get(i).name + " STRUC");
                whereEnds = i;
                for (int j = 0; j < table.Rinfl.size(); j++) {
                    if (table.Rinfl.get(j).type.equals("int") || table.Rinfl.get(j).type.equals("char")) {
                        //如果是int或者char型 则分配一个字节
                        preAsmCode.add(table.Rinfl.get(j).name + "   DB   ?");
                    } else if (table.Rinfl.get(j).type.equals("int[]") || table.Rinfl.get(j).type.equals("char[]")) {
                        //如果是数组类型的变量
                        preAsmCode.add(table.Rinfl.get(j).name + "   DB   " + table.Rinfl.get(j).tp + " DUP(0)");
                    }
                }
            }
        }
        if (whereEnds != -1) {
            preAsmCode.add(table.Synbl.get(whereEnds).name + " ENDS");
        }

        preAsmCode.add("DATAS SEGMENT");
        for (int i = 0; i < table.Synbl.size(); i++) {
            if (table.Synbl.get(i).type.contains("[")) {
                preAsmCode.add(table.Synbl.get(i).name + "   DB   " + table.Synbl.get(i).tp + " DUP(0)");
            }
        }
        for (int i = 0; i < table.Synbl.size(); i++) {
            if (table.Synbl.get(i).type.equals("int") || table.Synbl.get(i).type.equals("char")) {
                preAsmCode.add(table.Synbl.get(i).name + "   DB   ?");
            }
        }
        preAsmCode.add("DATAS ENDS");
        preAsmCode.add("CODE SEGMENT");
        preAsmCode.add("START:MOV AX,DATAS");
        preAsmCode.add("MOV DS,AX");

        for (int i = 0; i < liveStatu.getQt().size(); i++) {
            Quaternary temp = liveStatu.getQt().get(i);
            if (temp.getFirst().equals("+")) {//如果四元式的首符号是+
                if (RDL.equals("")) {
                    asmCode.add("      MOV AL," + temp.getSecond());
                    asmCode.add("      ADD AL," + temp.getThird());
                    asmJump[i] = asmCount;
                    asmCount += 2;
                } else if (RDL.equals(temp.getSecond())) {
                    if (liveStatu.getActiveLable().get(i).getSecond().equals("Active")) {
                        asmCode.add("      MOV " + temp.getSecond() + ",AL");
                        asmCode.add("      ADD AL," + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 2;
                    } else {
                        asmCode.add("      ADD AL," + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 1;

                    }

                } else if (RDL.equals(temp.getThird())) {
                    if (liveStatu.getActiveLable().get(i).getThird().equals("Active")) {
                        //THIRD活跃 要存入内存中 后面还会使用
                        asmCode.add("      MOV " + temp.getThird() + ",AL");
                        asmCode.add("      ADD AL," + temp.getSecond());
                        asmJump[i] = asmCount;
                        asmCount += 2;
                    } else {
                        asmCode.add("      ADD AL," + temp.getSecond());
                        asmJump[i] = asmCount;
                        asmCount += 1;
                    }
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            asmCode.add("     MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("     ADD AL," + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 5;
                        } else {
                            asmCode.add("      MOV " + RDL + ", AL");
                            asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("      ADD AL," + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 3;
                        }

                    } else if (s.equals("NonActive")) {//???nosense
                        asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                        asmCode.add("      ADD AL," + liveStatu.getQt().get(i).getThird());
                        asmJump[i] = asmCount;
                        asmCount += 2;
                    }
                    //可能活跃 要存入内存中 后面还会使用
                }
                RDL = temp.getFourth();
            } else if (temp.getFirst().equals("-")) {//如果四元式的首符号是-
                if (RDL.equals("")) {
                    asmCode.add("      MOV AL," + temp.getSecond());
                    asmCode.add("      SUB AL," + temp.getThird());
                    asmJump[i] = asmCount;
                    asmCount += 2;
                } else if (RDL.equals(temp.getSecond())) {
                    if (liveStatu.getActiveLable().get(i).getSecond().equals("Active")) {
                        asmCode.add("      MOV " + temp.getSecond() + ",AL");
                        asmCode.add("      SUB AL," + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 2;
                    } else {
                        asmCode.add("      SUB AL," + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 1;
                    }
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            asmCode.add("     MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("     SUB AL," + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 5;
                        } else {
                            asmCode.add("      MOV " + RDL + ", AL");
                            asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("      SUB AL," + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 3;
                        }
                    } else if (s.equals("NonActive")) {//???nosense
                        asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                        asmCode.add("      SUB AL," + liveStatu.getQt().get(i).getThird());
                        asmJump[i] = asmCount;
                        asmCount += 2;
                    }
                    //可能活跃 要存入内存中 后面还会使用
                }
                RDL = temp.getFourth();
            } else if (temp.getFirst().equals("*")) {//如果四元式的首符号是*
                if (RDL.equals("")) {
                    asmCode.add("      MOV AX,0000H");
                    asmCode.add("      MOV AL," + temp.getSecond());
                    asmCode.add("      MUL " + temp.getThird());
                    asmJump[i] = asmCount;
                    asmCount += 3;
                } else if (RDL.equals(temp.getSecond())) {
                    if (liveStatu.getActiveLable().get(i).getSecond().equals("Active")) {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MOV " + temp.getSecond() + ",AL");
                        asmCode.add("      MUL " + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 3;
                    } else {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MUL " + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 2;

                    }

                } else if (RDL.equals(temp.getThird())) {
                    if (liveStatu.getActiveLable().get(i).getThird().equals("Active")) {
                        //THIRD活跃 要存入内存中 后面还会使用
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MOV " + temp.getThird() + ",AL");
                        asmCode.add("      MUL " + temp.getSecond());
                        asmJump[i] = asmCount;
                        asmCount += 3;
                    } else {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MUL " + temp.getSecond());
                        asmJump[i] = asmCount;
                        asmCount += 2;
                    }
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            asmCode.add("     MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("     MUL " + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 5;
                        } else {
                            asmCode.add("      MOV " + RDL + ", AL");
                            asmCode.add("      XOR AH,AH");
                            asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("      MUL " + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 4;
                        }
                    } else if (s.equals("NonActive")) {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                        asmCode.add("      MUL " + liveStatu.getQt().get(i).getThird());
                        asmJump[i] = asmCount;
                        asmCount += 3;
                    }
                    //可能活跃 要存入内存中 后面还会使用
                }
                RDL = temp.getFourth();
            } else if (temp.getFirst().equals("/")) {//如果四元式的首符号是/
                if (RDL.equals("")) {
                    asmCode.add("      MOV AX,0000H");
                    asmCode.add("      MOV AL," + temp.getSecond());
                    asmCode.add("      DIV " + temp.getThird());
                    asmJump[i] = asmCount;
                    asmCount += 3;
                } else if (RDL.equals(temp.getSecond())) {
                    if (liveStatu.getActiveLable().get(i).getSecond().equals("Active")) {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MOV " + temp.getSecond() + ",AL");
                        asmCode.add("      DIV " + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 3;
                    } else {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MUL " + temp.getThird());
                        asmJump[i] = asmCount;
                        asmCount += 2;

                    }

                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            asmCode.add("     MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("     DIV " + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 5;
                        } else {
                            asmCode.add("      MOV " + RDL + ", AL");
                            asmCode.add("      XOR AH,AH");
                            asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmCode.add("      DIV " + liveStatu.getQt().get(i).getThird());
                            asmJump[i] = asmCount;
                            asmCount += 4;
                        }

                    } else if (s.equals("NonActive")) {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                        asmCode.add("      DIV " + liveStatu.getQt().get(i).getThird());
                        asmJump[i] = asmCount;
                        asmCount += 3;
                    }
                    //可能活跃 要存入内存中 后面还会使用
                }
                RDL = temp.getFourth();
            } else if (liveStatu.getQt().get(i).getFirst().equals("=")) {//赋值语句
                if (RDL.equals("")) {
                    asmCode.add("      XOR AX,AX");
                    asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                    asmJump[i] = asmCount;
                    asmCount += 2;

                } else if (RDL.equals(liveStatu.getQt().get(i).getSecond())) {
                    if (liveStatu.getActiveLable().get(i).getSecond().equals("Active")) {
                        asmCode.add("      MOV " + liveStatu.getQt().get(i).getSecond() + ",AL");
                        asmJump[i] = asmCount;
                        asmCount += 1;
                    }

                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            asmCode.add("     MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmJump[i] = asmCount;
                            asmCount += 4;
                        } else {
                            asmCode.add("      MOV " + RDL + ", AL");
                            asmCode.add("      XOR AH,AH");
                            asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                            asmJump[i] = asmCount;
                            asmCount += 3;
                        }
                    } else if (s.equals("NonActive")) {
                        asmCode.add("      XOR AH,AH");
                        asmCode.add("      MOV AL," + liveStatu.getQt().get(i).getSecond());
                        asmJump[i] = asmCount;
                        asmCount += 2;
                    }
                    //可能活跃 要存入内存中 后面还会使用
                }
                RDL = temp.getFourth();
            } else if (temp.getFirst().equals("==")) {
                if (RDL.equals("")) {
                    asmCode.add("      MOV BL," + temp.getSecond());
                    asmCode.add("      CMP BL," + temp.getThird());
                    asmCode.add("JNE " + liveStatu.getQt().get(i + 1).getFourth());
                    asmJump[i] = asmCount;
                    asmCount += 3;
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            //System.out.println(arrayLength);
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JNE " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 3;
                            asmCount += 6;
                        } else {
                            asmCode.add("     MOV " + RDL + ",AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JNE " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 1;
                            asmCount += 4;
                        }
                    }
                    RDL = "";
                }
            } else if (temp.getFirst().equals(">")) {
                if (RDL.equals("")) {
                    asmCode.add("      MOV BL," + temp.getSecond());
                    asmCode.add("      CMP BL," + temp.getThird());
                    asmCode.add("JBE " + liveStatu.getQt().get(i + 1).getFourth());
                    asmJump[i] = asmCount;
                    asmCount += 3;
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JBE " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 3;
                            asmCount += 6;
                        } else {
                            asmCode.add("     MOV " + RDL + ",AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JBE " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 1;
                            asmCount += 4;
                        }
                    }
                    RDL = "";
                }

            } else if (temp.getFirst().equals("<")) {
                if (RDL.equals("")) {
                    asmCode.add("      MOV BL," + temp.getSecond());
                    asmCode.add("      CMP BL," + temp.getThird());
                    asmCode.add("JAE " + liveStatu.getQt().get(i + 1).getFourth());
                    asmJump[i] = asmCount;
                    asmCount += 3;
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JAE " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 3;
                            asmCount += 6;
                        } else {
                            asmCode.add("     MOV " + RDL + ",AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JAE " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 1;
                            asmCount += 4;
                        }
                    }
                    RDL = "";
                }

            } else if (temp.getFirst().equals(">=")) {
                if (RDL.equals("")) {
                    asmCode.add("      MOV BL," + temp.getSecond());
                    asmCode.add("      CMP BL," + temp.getThird());
                    asmCode.add("JB " + liveStatu.getQt().get(i + 1).getFourth());
                    asmJump[i] = asmCount;
                    asmCount += 3;
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JB " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 3;
                            asmCount += 6;
                        } else {
                            asmCode.add("     MOV " + RDL + ",AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JB " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 1;
                            asmCount += 4;
                        }
                    }
                    RDL = "";
                }

            } else if (temp.getFirst().equals("<=")) {
                if (RDL.equals("")) {
                    asmCode.add("      MOV BL," + temp.getSecond());
                    asmCode.add("      CMP BL," + temp.getThird());
                    asmCode.add("JA " + liveStatu.getQt().get(i + 1).getFourth());
                    asmJump[i] = asmCount;
                    asmCount += 3;
                } else {
                    String s = getIsActive(RDL, liveStatu);
                    if (s.equals("Active")) {
                        if (RDL.contains("[")) {
                            //如果是数组，则需要特殊处理
                            String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                            int arrayLength = getArrayInfo[1].length();
                            String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                            asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                            asmCode.add("     ADD BX," + arrayPos);
                            asmCode.add("     MOV [BX],AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JA " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 3;
                            asmCount += 6;
                        } else {
                            asmCode.add("     MOV " + RDL + ",AL");
                            RDL = "";
                            asmCode.add("      MOV BL," + temp.getSecond());
                            asmCode.add("      CMP BL," + temp.getThird());
                            asmCode.add("JA " + liveStatu.getQt().get(i + 1).getFourth());
                            asmJump[i] = asmCount + 1;
                            asmCount += 4;
                        }
                    }
                    RDL = "";
                }
            } else if (temp.getFirst().equals("ie") || temp.getFirst().equals("wh")) {
                String s = getIsActive(RDL, liveStatu);
                if (s.equals("Active")) {
                    if (RDL.contains("[")) {
                        //如果是数组，则需要特殊处理
                        String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                        int arrayLength = getArrayInfo[1].length();
                        String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                        asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                        asmCode.add("     ADD BX," + arrayPos);
                        asmCode.add("     MOV [BX],AL");
                        RDL = "";
                        asmJump[i] = asmCount + 3;
                        asmCount += 3;
                    } else {
                        asmCode.add("      MOV " + RDL + ", AL");
                        RDL = "";
                        asmJump[i] = asmCount + 1;
                        asmCount += 1;
                    }
                } else {
                    asmJump[i] = asmCount;
                }
            } else if (temp.getFirst().equals("el") || temp.getFirst().equals("we")) {
                String s = getIsActive(RDL, liveStatu);
                if (s.equals("Active")) {
                    if (RDL.contains("[")) {
                        //如果是数组，则需要特殊处理
                        String[] getArrayInfo = RDL.split("\\[");//用[分隔开
                        int arrayLength = getArrayInfo[1].length();
                        String arrayPos = getArrayInfo[1].substring(0, arrayLength - 1);//获取数组内数字
                        asmCode.add("     MOV BX,OFFSET " + getArrayInfo[0]);
                        asmCode.add("     ADD BX," + arrayPos);
                        asmCode.add("     MOV [BX],AL");
                        RDL = "";
                        asmCode.add("JMP " + temp.getFourth());
                        asmJump[i] = asmCount + 3;
                        asmCount += 4;
                    } else {
                        asmCode.add("      MOV " + RDL + ", AL");
                        RDL = "";
                        asmCode.add("JMP " + temp.getFourth());
                        asmJump[i] = asmCount + 1;
                        asmCount += 2;
                    }

                } else {
                    asmCode.add("JMP " + temp.getFourth());
                    asmJump[i] = asmCount;
                    asmCount += 1;
                }
            }
        }
        asmJump[liveStatu.getQt().size()] = asmCode.size();
        asmCode.add("      MOV AH,4CH");
        asmCode.add("      INT 21H");
        asmCode.add("      CODE ENDS");
        asmCode.add("      END START");

        int jumpCount = 0;//记录跳转位置
        for (int j = 0; j < asmCode.size(); j++) {
            if (asmCode.get(j).charAt(0) == 'J') {
                //如果是跳转指令
                String[] getJumpNum = asmCode.get(j).split(" ");
                String toWhere = getJumpNum[1];
                if (asmCode.get(asmJump[Integer.parseInt(toWhere)]).charAt(0) == 'T') {//如果该处已经填有标号
                    String[] getWhere = asmCode.get(asmJump[Integer.parseInt(toWhere)]).split(":");
                    asmCode.set(j, getJumpNum[0] + " " + getWhere[0]);
                } else {//如果该处没有标号
                    String temp = asmCode.get(asmJump[Integer.parseInt(toWhere)]);
                    asmCode.set(asmJump[Integer.parseInt(toWhere)], "TURN" + jumpCount + ": " + temp);
                    asmCode.set(j, getJumpNum[0] + " TURN" + jumpCount);
                    jumpCount++;
                }
            }
        }


    }

    private String getIsActive(String newRDL, LiveStatu liveStatu) {
        int findQt;
        int qtWhere = 0;//first对应1 second对应2 以此类推
        for (findQt = liveStatu.getQt().size() - 1; findQt >= 0; findQt--) {  //逆序遍历四元式
            if (liveStatu.getQt().get(findQt).getSecond().equals(newRDL)) {
                qtWhere = 2;
                break;
            } else if (liveStatu.getQt().get(findQt).getThird().equals(newRDL)) {
                qtWhere = 3;
                break;
            } else if (liveStatu.getQt().get(findQt).getFourth().equals(newRDL)) {
                qtWhere = 4;
                break;
            }
        }
        String s = "";
        if (qtWhere == 2) {
            s = liveStatu.getActiveLable().get(findQt).getSecond();
        } else if (qtWhere == 3) {
            s = liveStatu.getActiveLable().get(findQt).getThird();
        } else if (qtWhere == 4) {
            s = liveStatu.getActiveLable().get(findQt).getFourth();
        }
        return s;
    }
}


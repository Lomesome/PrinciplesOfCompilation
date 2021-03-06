package pers.lomesome.compliation.tool.finalattr;

/*
    定义各种常量，避免程序内出现魔法数值
 */

public final class Constants {
    public final static int STATE_BEGIN = 0;
    public final static int STATE_1 = 1;
    public final static int STATE_2 = 2;
    public final static int STATE_3 = 3;
    public final static int STATE_4 = 4;
    public final static int STATE_5 = 5;
    public final static int STATE_6 = 6;
    public final static int STATE_7 = 7;
    public final static int STATE_8 = 8;
    public final static int STATE_9 = 9;
    public final static int STATE_10 = 10;
    public final static int STATE_11 = 11;
    public final static int STATE_12 = 12;
    public final static int STATE_13 = 13;
    public final static int STATE_14 = 14;
    public final static int STATE_15 = 15;
    public final static int STATE_16 = 16;
    public final static int STATE_17 = 17;
    public final static int STATE_18 = 18;
    public final static int STATE_19 = 19;
    public final static int STATE_20 = 20;
    public final static int STATE_21 = 21;
    public final static int STATE_22 = 22;
    public final static int STATE_23 = 23;
    public final static int STATE_24 = 24;
    public final static int STATE_25 = 25;
    public final static int STATE_26 = 26;
    public final static int STATE_27 = 27;
    public final static int STATE_28 = 28;
    public final static int STATE_29 = 29;
    public final static int STATE_30 = 30;
    public final static int STATE_31 = 31;
    public final static int STATE_32 = 32;
    public final static int STATE_33 = 33;
    public final static int STATE_34 = 34;
    public final static int STATE_35 = 35;
    public final static int STATE_36 = 36;
    public final static int STATE_37 = 37;
    public final static int STATE_38 = 38;
    public final static int STATE_39 = 39;
    public final static int STATE_40 = 40;
    public final static int STATE_41 = 41;
    public final static int STATE_42 = 42;
    public final static int STATE_43 = 43;
    public final static int STATE_44 = 44;
    public final static int STATE_45 = 45;
    public final static int STATE_46 = 46;
    public final static int STATE_47 = 47;
    public final static int STATE_48 = 48;
    public final static int STATE_49 = 49;
    public final static int STATE_50 = 50;
    public final static int STATE_51 = 51;
    public final static int STATE_52 = 52;
    public final static int STATE_53 = 53;
    public final static int STATE_54 = 54;
    public final static int STATE_55 = 55;
    public final static int STATE_56 = 56;
    public final static int STATE_57 = 57;
    public final static int STATE_58 = 58;
    public final static int STATE_59 = 59;
    public final static int STATE_60 = 60;
    public final static int STATE_61 = 61;
    public final static int STATE_62 = 62;
    public final static int STATE_63 = 63;
    public final static int STATE_64 = 64;
    public final static int STATE_65 = 65;
    public final static int STATE_66 = 66;
    public final static int STATE_67 = 67;
    public final static int STATE_68 = 68;
    public final static int STATE_69 = 69;
    public final static int STATE_70 = 70;
    public final static int STATE_71 = 71;
    public final static int STATE_72 = 72;
    public final static int STATE_73 = 73;
    public final static int STATE_74 = 74;
    public final static int STATE_75 = 75;
    public final static int STATE_76 = 76;
    public final static int STATE_77 = 77;
    public final static int STATE_78 = 78;
    public final static int STATE_79 = 79;
    public final static int STATE_80 = 80;
    public final static int STATE_81 = 81;
    public final static int STATE_82 = 82;
    public final static int STATE_83 = 83;
    public final static int STATE_84 = 84;
    public final static int STATE_85 = 85;
    public final static int STATE_EOF = 999;

    public final static String KEYWORD = "关键字";
    public final static String DELIMITER =  "界符";
    public final static String INTEGER = "整数";
    public final static String CHARACTER = "字符";
    public final static String STRING = "字符串";
    public final static String IDENTIFIER = "标识符";
    public final static String REALNUMBER = "实数";
    public final static String OPERATOR = "运算符";

    public final static int INTEGER_TOKEN = 400;
    public final static int CHARACTER_TOKEN = 500;
    public final static int STRING_TOKEN = 600;
    public final static int IDENTIFIER_TOKEN = 700;
    public final static int REALNUMBER_TOKEN = 800;
    public final static int ERROR_TOKEN = 404;

    public static String INT = "int";
    public static String UNSIGNED = "unsigned";
    public static String LONG = "long";
    public static String LONG_UNSIGNED = "long_unsigned";
    public static String FLOAT = "float";
    public static String DOUBLE = "double";
    public static String LONG_DOUBLE = "long_double";
    public static String CHAR = "char";
    public static String STRING_ = "string";
    public static String CONST = "const";

    public final static String PUSH = "PUSH";
    public final static String GEQA = "GEQA";
    public final static String GEQS = "GEQS";
    public final static String GEQM = "GEQM";
    public final static String GEQD = "GEQD";
    public final static String ASSI = "ASSI";
    public final static String GREA = "GREA";
    public final static String LESS = "LESS";
    public final static String EQUA = "GEQE";
    public final static String GREQ = "GREQ";
    public final static String LEEQ = "LEEQ";
    public final static String NOEQ = "NOEQ";
    public final static String IF = "IF";
    public final static String EL = "EL";
    public final static String IEFIR = "IEFIR";
    public final static String IESEC = "IESEC";
    public final static String DO = "DO";
    public final static String DOW = "DOW";
    public final static String WE = "WE";
    public final static String PUSHNUM = "PUSHNUM";
    public final static String LEVELA = "LEVELA";
    public final static String LEVELS = "LEVELS";
    public final static String ADDFUN = "ADDFUN";
    public final static String CALLFUN = "CALLFUN";
    public final static String ADDARG = "ADDARG";
    public final static String RE = "RE";
    public final static String RET = "RET";
    public final static String CALLFUNARG = "CALLFUNARG";
    public final static String PUSHARG = "PUSHARG";
    public final static String CALLS = "CALLS";
    public final static String PRINT = "PRINT";
    public final static String INDE = "INDE";


    public final static String GRAMMAR = "S\n" +
            "\t: function functions\n" +
            "\t;\n" +
            "\n" +
            "function\n" +
            "\t: func_type funcname '(' args ')' program\n" +
            "\t;\n" +
            "\n" +
            "func_type\n" +
            "\t: type\n" +
            "\t| 'void'\n" +
            "\t;\n" +
            "\n" +
            "functions\n" +
            "\t: function functions\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "funcname\n" +
            "\t: id\n" +
            "\t| 'main'\n" +
            "\t;\n" +
            "\n" +
            "args\n" +
            "\t: type id arg\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "arg\n" +
            "\t: , type id arg\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "program\n" +
            "\t: '{' expmaker '}'\n" +
            "\t;\n" +
            "\n" +
            "expmaker\n" +
            "\t: type id idassignment ';' expmaker\n" +
            "\t| id grop exp ';' expmaker\n" +
            "\t| 'if' '(' boolexp ')' '{' expmaker '}' iselse expmaker\n" +
            "\t| 'struct' id '{' expmaker '}' ';' expmaker\n" +
            "\t| 'while' '(' boolexp ')' stmt\n" +
            "\t| 'for' '(' isnull_exp ';' isnull_exp ';' isnull_exp ')' stmt\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "stmt\n" +
            "\t: '{' expmaker '}' expmaker\n" +
            "\t;\n" +
            "\n" +
            "isnull_exp\n" +
            "\t: exp\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "iselse\n" +
            "\t: 'else' elif\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "elif\n" +
            "\t: '{' expmaker '}'\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "grop\n" +
            "\t: '='\n" +
            "\t| '[' int_const ']' '='\n" +
            "\t;\n" +
            "\n" +
            "idassignment\n" +
            "\t: '=' exp\n" +
            "\t| '[' int_const ']'\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "boolexp\n" +
            "\t: beforexp afterexp\n" +
            "\t;\n" +
            "\n" +
            "beforexp\n" +
            "\t: float_const\n" +
            "\t| int_const\n" +
            "\t| id\n" +
            "\t;\n" +
            "\n" +
            "value\n" +
            "\t: float_const\n" +
            "\t| int_const\n" +
            "\t| id\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "afterexp\n" +
            "\t: '>' value\n" +
            "\t| '<' value\n" +
            "\t| '==' value\n" +
            "\t| '>=' value\n" +
            "\t| '<=' value\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "exp\n" +
            "\t: assignment_exp additive_exp\n" +
            "\t;\n" +
            "\n" +
            "additive_exp\n" +
            "\t: '+' assignment_exp additive_exp\n" +
            "\t| '-' assignment_exp additive_exp\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "assignment_exp\n" +
            "\t: primary_exp mult_exp\n" +
            "\t;\n" +
            "\n" +
            "mult_exp\n" +
            "\t: '*' primary_exp mult_exp\n" +
            "\t| '/' primary_exp mult_exp\n" +
            "\t| ε\n" +
            "\t;\n" +
            "\n" +
            "primary_exp\n" +
            "\t: id\n" +
            "\t| float_const\n" +
            "\t| int_const\n" +
            "\t| string\n" +
            "\t| char_const\n" +
            "\t| '(' exp ')'\n" +
            "\t;\n" +
            "\n" +
            "type\n" +
            "\t: 'int'\n" +
            "\t| 'char'\n" +
            "\t| 'float'\n" +
            "\t| 'string'\n" +
            "\t;";

    public final static int DISTANCE = 50;
    public final static int RADIUS = 15;
}

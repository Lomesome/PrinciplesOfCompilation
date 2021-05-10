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

    public final static String GRAMMAR = "primary_expression\n" +
            "\t: IDENTIFIER\n" +
            "\t| CONSTANT\n" +
            "\t| STRING_LITERAL\n" +
            "\t| '(' expression ')'\n" +
            "\t;\n" +
            "\n" +
            "postfix_expression\n" +
            "\t: primary_expression\n" +
            "\t| postfix_expression '[' expression ']'\n" +
            "\t| postfix_expression '(' ')'\n" +
            "\t| postfix_expression '(' argument_expression_list ')'\n" +
            "\t| postfix_expression '.' IDENTIFIER\n" +
            "\t| postfix_expression PTR_OP IDENTIFIER\n" +
            "\t| postfix_expression INC_OP\n" +
            "\t| postfix_expression DEC_OP\n" +
            "\t;\n" +
            "\n" +
            "argument_expression_list\n" +
            "\t: assignment_expression\n" +
            "\t| argument_expression_list ',' assignment_expression\n" +
            "\t;\n" +
            "\n" +
            "unary_expression\n" +
            "\t: postfix_expression\n" +
            "\t| INC_OP unary_expression\n" +
            "\t| DEC_OP unary_expression\n" +
            "\t| unary_operator cast_expression\n" +
            "\t| SIZEOF unary_expression\n" +
            "\t| SIZEOF '(' type_name ')'\n" +
            "\t;\n" +
            "\n" +
            "unary_operator\n" +
            "\t: '&'\n" +
            "\t| '*'\n" +
            "\t| '+'\n" +
            "\t| '-'\n" +
            "\t| '~'\n" +
            "\t| '!'\n" +
            "\t;\n" +
            "\n" +
            "cast_expression\n" +
            "\t: unary_expression\n" +
            "\t| '(' type_name ')' cast_expression\n" +
            "\t;\n" +
            "\n" +
            "multiplicative_expression\n" +
            "\t: cast_expression\n" +
            "\t| multiplicative_expression '*' cast_expression\n" +
            "\t| multiplicative_expression '/' cast_expression\n" +
            "\t| multiplicative_expression '%' cast_expression\n" +
            "\t;\n" +
            "\n" +
            "additive_expression\n" +
            "\t: multiplicative_expression\n" +
            "\t| additive_expression '+' multiplicative_expression\n" +
            "\t| additive_expression '-' multiplicative_expression\n" +
            "\t;\n" +
            "\n" +
            "shift_expression\n" +
            "\t: additive_expression\n" +
            "\t| shift_expression LEFT_OP additive_expression\n" +
            "\t| shift_expression RIGHT_OP additive_expression\n" +
            "\t;\n" +
            "\n" +
            "relational_expression\n" +
            "\t: shift_expression\n" +
            "\t| relational_expression '<' shift_expression\n" +
            "\t| relational_expression '>' shift_expression\n" +
            "\t| relational_expression LE_OP shift_expression\n" +
            "\t| relational_expression GE_OP shift_expression\n" +
            "\t;\n" +
            "\n" +
            "equality_expression\n" +
            "\t: relational_expression\n" +
            "\t| equality_expression EQ_OP relational_expression\n" +
            "\t| equality_expression NE_OP relational_expression\n" +
            "\t;\n" +
            "\n" +
            "and_expression\n" +
            "\t: equality_expression\n" +
            "\t| and_expression '&' equality_expression\n" +
            "\t;\n" +
            "\n" +
            "exclusive_or_expression\n" +
            "\t: and_expression\n" +
            "\t| exclusive_or_expression '^' and_expression\n" +
            "\t;\n" +
            "\n" +
            "inclusive_or_expression\n" +
            "\t: exclusive_or_expression\n" +
            "\t| inclusive_or_expression '|' exclusive_or_expression\n" +
            "\t;\n" +
            "\n" +
            "logical_and_expression\n" +
            "\t: inclusive_or_expression\n" +
            "\t| logical_and_expression AND_OP inclusive_or_expression\n" +
            "\t;\n" +
            "\n" +
            "logical_or_expression\n" +
            "\t: logical_and_expression\n" +
            "\t| logical_or_expression OR_OP logical_and_expression\n" +
            "\t;\n" +
            "\n" +
            "conditional_expression\n" +
            "\t: logical_or_expression\n" +
            "\t| logical_or_expression '?' expression ':' conditional_expression\n" +
            "\t;\n" +
            "\n" +
            "assignment_expression\n" +
            "\t: conditional_expression\n" +
            "\t| unary_expression assignment_operator assignment_expression\n" +
            "\t;\n" +
            "\n" +
            "assignment_operator\n" +
            "\t: '='\n" +
            "\t| MUL_ASSIGN\n" +
            "\t| DIV_ASSIGN\n" +
            "\t| MOD_ASSIGN\n" +
            "\t| ADD_ASSIGN\n" +
            "\t| SUB_ASSIGN\n" +
            "\t| LEFT_ASSIGN\n" +
            "\t| RIGHT_ASSIGN\n" +
            "\t| AND_ASSIGN\n" +
            "\t| XOR_ASSIGN\n" +
            "\t| OR_ASSIGN\n" +
            "\t;\n" +
            "\n" +
            "expression\n" +
            "\t: assignment_expression\n" +
            "\t| expression ',' assignment_expression\n" +
            "\t;\n" +
            "\n" +
            "constant_expression\n" +
            "\t: conditional_expression\n" +
            "\t;\n" +
            "\n" +
            "declaration\n" +
            "\t: declaration_specifiers ';'\n" +
            "\t| declaration_specifiers init_declarator_list ';'\n" +
            "\t;\n" +
            "\n" +
            "declaration_specifiers\n" +
            "\t: storage_class_specifier\n" +
            "\t| storage_class_specifier declaration_specifiers\n" +
            "\t| type_specifier\n" +
            "\t| type_specifier declaration_specifiers\n" +
            "\t| type_qualifier\n" +
            "\t| type_qualifier declaration_specifiers\n" +
            "\t;\n" +
            "\n" +
            "init_declarator_list\n" +
            "\t: init_declarator\n" +
            "\t| init_declarator_list ',' init_declarator\n" +
            "\t;\n" +
            "\n" +
            "init_declarator\n" +
            "\t: declarator\n" +
            "\t| declarator '=' initializer\n" +
            "\t;\n" +
            "\n" +
            "storage_class_specifier\n" +
            "\t: TYPEDEF\n" +
            "\t| EXTERN\n" +
            "\t| STATIC\n" +
            "\t| AUTO\n" +
            "\t| REGISTER\n" +
            "\t;\n" +
            "\n" +
            "type_specifier\n" +
            "\t: VOID\n" +
            "\t| CHAR\n" +
            "\t| SHORT\n" +
            "\t| INT\n" +
            "\t| LONG\n" +
            "\t| FLOAT\n" +
            "\t| DOUBLE\n" +
            "\t| SIGNED\n" +
            "\t| UNSIGNED\n" +
            "\t| struct_or_union_specifier\n" +
            "\t| enum_specifier\n" +
            "\t| TYPE_NAME\n" +
            "\t;\n" +
            "\n" +
            "struct_or_union_specifier\n" +
            "\t: struct_or_union IDENTIFIER '{' struct_declaration_list '}'\n" +
            "\t| struct_or_union '{' struct_declaration_list '}'\n" +
            "\t| struct_or_union IDENTIFIER\n" +
            "\t;\n" +
            "\n" +
            "struct_or_union\n" +
            "\t: STRUCT\n" +
            "\t| UNION\n" +
            "\t;\n" +
            "\n" +
            "struct_declaration_list\n" +
            "\t: struct_declaration\n" +
            "\t| struct_declaration_list struct_declaration\n" +
            "\t;\n" +
            "\n" +
            "struct_declaration\n" +
            "\t: specifier_qualifier_list struct_declarator_list ';'\n" +
            "\t;\n" +
            "\n" +
            "specifier_qualifier_list\n" +
            "\t: type_specifier specifier_qualifier_list\n" +
            "\t| type_specifier\n" +
            "\t| type_qualifier specifier_qualifier_list\n" +
            "\t| type_qualifier\n" +
            "\t;\n" +
            "\n" +
            "struct_declarator_list\n" +
            "\t: struct_declarator\n" +
            "\t| struct_declarator_list ',' struct_declarator\n" +
            "\t;\n" +
            "\n" +
            "struct_declarator\n" +
            "\t: declarator\n" +
            "\t| ':' constant_expression\n" +
            "\t| declarator ':' constant_expression\n" +
            "\t;\n" +
            "\n" +
            "enum_specifier\n" +
            "\t: ENUM '{' enumerator_list '}'\n" +
            "\t| ENUM IDENTIFIER '{' enumerator_list '}'\n" +
            "\t| ENUM IDENTIFIER\n" +
            "\t;\n" +
            "\n" +
            "enumerator_list\n" +
            "\t: enumerator\n" +
            "\t| enumerator_list ',' enumerator\n" +
            "\t;\n" +
            "\n" +
            "enumerator\n" +
            "\t: IDENTIFIER\n" +
            "\t| IDENTIFIER '=' constant_expression\n" +
            "\t;\n" +
            "\n" +
            "type_qualifier\n" +
            "\t: CONST\n" +
            "\t| VOLATILE\n" +
            "\t;\n" +
            "\n" +
            "declarator\n" +
            "\t: pointer direct_declarator\n" +
            "\t| direct_declarator\n" +
            "\t;\n" +
            "\n" +
            "direct_declarator\n" +
            "\t: IDENTIFIER\n" +
            "\t| '(' declarator ')'\n" +
            "\t| direct_declarator '[' constant_expression ']'\n" +
            "\t| direct_declarator '[' ']'\n" +
            "\t| direct_declarator '(' parameter_type_list ')'\n" +
            "\t| direct_declarator '(' identifier_list ')'\n" +
            "\t| direct_declarator '(' ')'\n" +
            "\t;\n" +
            "\n" +
            "pointer\n" +
            "\t: '*'\n" +
            "\t| '*' type_qualifier_list\n" +
            "\t| '*' pointer\n" +
            "\t| '*' type_qualifier_list pointer\n" +
            "\t;\n" +
            "\n" +
            "type_qualifier_list\n" +
            "\t: type_qualifier\n" +
            "\t| type_qualifier_list type_qualifier\n" +
            "\t;\n" +
            "\n" +
            "\n" +
            "parameter_type_list\n" +
            "\t: parameter_list\n" +
            "\t| parameter_list ',' ELLIPSIS\n" +
            "\t;\n" +
            "\n" +
            "parameter_list\n" +
            "\t: parameter_declaration\n" +
            "\t| parameter_list ',' parameter_declaration\n" +
            "\t;\n" +
            "\n" +
            "parameter_declaration\n" +
            "\t: declaration_specifiers declarator\n" +
            "\t| declaration_specifiers abstract_declarator\n" +
            "\t| declaration_specifiers\n" +
            "\t;\n" +
            "\n" +
            "identifier_list\n" +
            "\t: IDENTIFIER\n" +
            "\t| identifier_list ',' IDENTIFIER\n" +
            "\t;\n" +
            "\n" +
            "type_name\n" +
            "\t: specifier_qualifier_list\n" +
            "\t| specifier_qualifier_list abstract_declarator\n" +
            "\t;\n" +
            "\n" +
            "abstract_declarator\n" +
            "\t: pointer\n" +
            "\t| direct_abstract_declarator\n" +
            "\t| pointer direct_abstract_declarator\n" +
            "\t;\n" +
            "\n" +
            "direct_abstract_declarator\n" +
            "\t: '(' abstract_declarator ')'\n" +
            "\t| '[' ']'\n" +
            "\t| '[' constant_expression ']'\n" +
            "\t| direct_abstract_declarator '[' ']'\n" +
            "\t| direct_abstract_declarator '[' constant_expression ']'\n" +
            "\t| '(' ')'\n" +
            "\t| '(' parameter_type_list ')'\n" +
            "\t| direct_abstract_declarator '(' ')'\n" +
            "\t| direct_abstract_declarator '(' parameter_type_list ')'\n" +
            "\t;\n" +
            "\n" +
            "initializer\n" +
            "\t: assignment_expression\n" +
            "\t| '{' initializer_list '}'\n" +
            "\t| '{' initializer_list ',' '}'\n" +
            "\t;\n" +
            "\n" +
            "initializer_list\n" +
            "\t: initializer\n" +
            "\t| initializer_list ',' initializer\n" +
            "\t;\n" +
            "\n" +
            "statement\n" +
            "\t: labeled_statement\n" +
            "\t| compound_statement\n" +
            "\t| expression_statement\n" +
            "\t| selection_statement\n" +
            "\t| iteration_statement\n" +
            "\t| jump_statement\n" +
            "\t;\n" +
            "\n" +
            "labeled_statement\n" +
            "\t: IDENTIFIER ':' statement\n" +
            "\t| CASE constant_expression ':' statement\n" +
            "\t| DEFAULT ':' statement\n" +
            "\t;\n" +
            "\n" +
            "compound_statement\n" +
            "\t: '{' '}'\n" +
            "\t| '{' statement_list '}'\n" +
            "\t| '{' declaration_list '}'\n" +
            "\t| '{' declaration_list statement_list '}'\n" +
            "\t;\n" +
            "\n" +
            "declaration_list\n" +
            "\t: declaration\n" +
            "\t| declaration_list declaration\n" +
            "\t;\n" +
            "\n" +
            "statement_list\n" +
            "\t: statement\n" +
            "\t| statement_list statement\n" +
            "\t;\n" +
            "\n" +
            "expression_statement\n" +
            "\t: ';'\n" +
            "\t| expression ';'\n" +
            "\t;\n" +
            "\n" +
            "selection_statement\n" +
            "\t: IF '(' expression ')' statement\n" +
            "\t| IF '(' expression ')' statement ELSE statement\n" +
            "\t| SWITCH '(' expression ')' statement\n" +
            "\t;\n" +
            "\n" +
            "iteration_statement\n" +
            "\t: WHILE '(' expression ')' statement\n" +
            "\t| DO statement WHILE '(' expression ')' ';'\n" +
            "\t| FOR '(' expression_statement expression_statement ')' statement\n" +
            "\t| FOR '(' expression_statement expression_statement expression ')' statement\n" +
            "\t;\n" +
            "\n" +
            "jump_statement\n" +
            "\t: GOTO IDENTIFIER ';'\n" +
            "\t| CONTINUE ';'\n" +
            "\t| BREAK ';'\n" +
            "\t| RETURN ';'\n" +
            "\t| RETURN expression ';'\n" +
            "\t;\n" +
            "\n" +
            "translation_unit\n" +
            "\t: external_declaration\n" +
            "\t| translation_unit external_declaration\n" +
            "\t;\n" +
            "\n" +
            "external_declaration\n" +
            "\t: function_definition\n" +
            "\t| declaration\n" +
            "\t;\n" +
            "\n" +
            "function_definition\n" +
            "\t: declaration_specifiers declarator declaration_list compound_statement\n" +
            "\t| declaration_specifiers declarator compound_statement\n" +
            "\t| declarator declaration_list compound_statement\n" +
            "\t| declarator compound_statement\n" +
            "\t;";
}

package pers.lomesome.compliation.tool;
// Technische Universitaet Muenchen
// Fakultaet fuer Informatik 
// Michael Petter

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.tool.finalattr.Constants;
import pers.lomesome.compliation.model.Word;
import java.util.ArrayList;
import java.util.List;

%%
%public
%class Lexer
%cup
%char
%line
%column

%{
    StringBuffer string = new StringBuffer();
    private final List<Word> words = new ArrayList<>();
    private final List<Word> errorMsgList = new ArrayList<>();

    public List<Word> getWords() {
            return words;
    }
    public List<Word> getErrorMsgList() {
            return errorMsgList;
        }
%}

IDENTIFIER = [a-zA-Z$_] [a-zA-Z0-9$_]*
DIGIT = [0-9]
INTEGER =  [1-9]{DIGIT}* | 0
FLOAT = [1-9][0-9]*[.][0-9]+([eE][+-]?[0-9]*|[0])?
HEXADECIMAL = (0x|0X)[a-fA-F0-9]+
OCTONARY = 0[0-7]+
new_line = \r|\n|\r\n;
white_space = {new_line} | [ \t\f]
NOTES = \/\/[^\n]* | "/*"([^\*]|(\*)*[^\*/])*(\*)*"*/"
DELIMITER = [\;\,\{\}]
S_OPERATOR = [\(\)\[\]\!\*\/\%\+\-\<\>\=\&\|\~\^\.]
D_OPERATOR = "++" | "--" | "<=" | ">=" | "==" | "!=" | "&&" | "||" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|=" | "^=" | ">>=" | "<<=" | "<<" | ">>"
OPERATOR = {S_OPERATOR} | {D_OPERATOR}
ERROEFLOAT = ([1-9])([0-9])* ([^\.Ee\r\n\t\f {S_OPERATOR}{DELIMITER}])

%state STRING
%state CHAR
%%

<YYINITIAL>{
    \"    {string.setLength(0);yybegin(STRING) ;}
    \'    {string.setLength(0);yybegin(CHAR) ;}

    {IDENTIFIER}
            {
              if (FinalAttribute.findToken(yytext()) == Constants.IDENTIFIER_TOKEN) {
                  words.add(new Word(Constants.IDENTIFIER_TOKEN, yytext(), Constants.IDENTIFIER, yyline+1, yycolumn+1));
              } else {
                  words.add(new Word(yytext(), Constants.KEYWORD, yyline+1, yycolumn+1));
              }
           }

    {INTEGER} | {HEXADECIMAL} | {OCTONARY} { words.add(new Word(Constants.INTEGER_TOKEN, yytext(), Constants.INTEGER, yyline+1, yycolumn+1));}

    {FLOAT}  {words.add(new Word(Constants.REALNUMBER_TOKEN, yytext(), Constants.REALNUMBER, yyline+1, yycolumn+1));}

    {ERROEFLOAT} {errorMsgList.add(new Word(Constants.ERROR_TOKEN, yytext(), "error: 数值类型错误 ", yyline+1, yycolumn+1));}

    {DELIMITER} {words.add(new Word(yytext(), Constants.DELIMITER, yyline+1, yycolumn+1));}

    {OPERATOR} {words.add(new Word(yytext(), Constants.OPERATOR, yyline+1, yycolumn+1));}

    {white_space}     { /* ignore */ }

    {NOTES} {/* ignore */}

}

<STRING> {
  \"                             { yybegin(YYINITIAL);words.add(new Word(Constants.STRING_TOKEN, string.toString(), Constants.STRING, yyline+1, yycolumn+1));}
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \n                             { yybegin(YYINITIAL);errorMsgList.add(new Word(Constants.ERROR_TOKEN, string.toString(), "error: 缺少双引号 ", yyline+1, yycolumn+1)); }
  \\r                            { string.append('\r'); }
  \"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

<CHAR> {
    .\' {yybegin(YYINITIAL);words.add(new Word(yytext(), Constants.CHARACTER, yyline+1, yycolumn+1));}
    \n | .[^\'] {yybegin(YYINITIAL);yypushback(1);errorMsgList.add(new Word(Constants.ERROR_TOKEN, yytext(), "error: 缺少单引号 ", yyline+1, yycolumn+1));}
}

[^]   {errorMsgList.add(new Word(Constants.ERROR_TOKEN, yytext(), "error: 不能识别的字符 ", yyline+1, yycolumn+1)); }
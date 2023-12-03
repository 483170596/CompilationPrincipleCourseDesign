package lexer;

import grammar.SymbolType;

import java.util.concurrent.BrokenBarrierException;

public class Lexer {
    private static Lexer instance;            //单实例
    private String string = null;            //要分析的字符串
    private int curPos;                        //当前位置
    private int line;                        //行
    private StringBuffer lexeme = null;        //符号暂存处

    public static Lexer getInstance() {
        if (instance == null) {
            instance = new Lexer();
        }
        return instance;
    }

    public void init(String s) {
        instance.string = s;
        instance.curPos = 0;
        instance.line = 1;
        instance.lexeme = new StringBuffer();
    }

    //取得词法记号，并重置状态变量
    private Token getToken(SymbolType type) {
        String t = lexeme.toString();
        lexeme.setLength(0);
        return new Token(type, t, line);
    }


    //获取下一个token
    public Token nextToken() {
        Token token = null;
        //已经到达结尾
        if (curPos >= string.length())
            return null;
        //去掉空白字符，换行，回车，制表符
        char ch = string.charAt(curPos++);
        while (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') {
            if (ch == '\n' || ch == '\r') line++;
            if (curPos >= string.length())
                return null;
            ch = string.charAt(curPos++);
        }

        // TODO 去掉注释
        if (ch == '/') {
            boolean preExitsMultiLineComments = false;
            if (string.charAt(curPos) == '/') {
                this.lexeme.append(ch);
                ch = string.charAt(curPos++);
                while (ch != '\n') {
                    this.lexeme.append(ch);
                    ch = string.charAt(curPos++);
                }
                line++;
                return this.getToken(SymbolType.NOTE);
            } else if (string.charAt(curPos) == '*') {
                this.lexeme.append(ch);
                ch = string.charAt(curPos++);
                this.lexeme.append(ch);
                ch = string.charAt(curPos++);
                while (!(preExitsMultiLineComments && (ch == '/'))) {
                    if (ch == '*') {
                        preExitsMultiLineComments = true;
                    }
                    if (preExitsMultiLineComments && (ch != '*')) {
                        preExitsMultiLineComments = false;
                    }
                    this.lexeme.append(ch);
                    ch = string.charAt(curPos++);
                }
                this.lexeme.append(ch);
                return this.getToken(SymbolType.NOTE);
            }
        }

        //整型常量
        if (Character.isDigit(ch)) {
            this.lexeme.append(ch);
            while (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (Character.isDigit(ch))
                    this.lexeme.append(ch);
                else {
                    curPos--;
                    break;
                }
            }
            return this.getToken(SymbolType.INTCON);
        }

        // TODO ! 和 !=
        if (ch == '!') {
            this.lexeme.append(ch);
            if (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (ch == '=') {
                    this.lexeme.append(ch);
                } else {
                    curPos--;
                    return this.getToken(SymbolType.NOT);
                }
            }
            return this.getToken(SymbolType.NEQ);
        }

        // TODO &&
        if (ch == '&') {
            this.lexeme.append(ch);
            if (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (ch == '&') {
                    this.lexeme.append(ch);
                } else {
                    curPos--;
                }
            }
            return this.getToken(SymbolType.AND);
        }

        // TODO ||
        if (ch == '|') {
            this.lexeme.append(ch);
            if (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (ch == '|') {
                    this.lexeme.append(ch);
                } else {
                    curPos--;
                }
            }
            return this.getToken(SymbolType.OR);
        }

        // TODO +
        if (ch == '+') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.PLUS);
        }

        // TODO -
        if (ch == '-') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.MINU);
        }

        // TODO *
        if (ch == '*') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.MULT);
        }

        // TODO /
        if (ch == '/') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.DIV);
        }

        // TODO %
        if (ch == '%') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.MOD);
        }

        // TODO < 和 <=
        if (ch == '<') {
            this.lexeme.append(ch);
            if (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (ch == '=') {
                    this.lexeme.append(ch);
                } else {
                    curPos--;
                    return this.getToken(SymbolType.LSS);
                }
            }
            return this.getToken(SymbolType.LEQ);
        }

        // TODO > 和 >=
        if (ch == '>') {
            this.lexeme.append(ch);
            if (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (ch == '=') {
                    this.lexeme.append(ch);
                } else {
                    curPos--;
                    return this.getToken(SymbolType.GRE);
                }
            }
            return this.getToken(SymbolType.GEQ);
        }

        // TODO = 和 ==
        if (ch == '=') {
            this.lexeme.append(ch);
            if (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (ch == '=') {
                    this.lexeme.append(ch);
                } else {
                    curPos--;
                    return this.getToken(SymbolType.ASSIGN);
                }
            }
            return this.getToken(SymbolType.EQL);
        }

        // TODO ;
        if (ch == ';') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.SEMICN);
        }

        // TODO ,
        if (ch == ',') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.COMMA);
        }

        // TODO (
        if (ch == '(') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.LPARENT);
        }

        // TODO )
        if (ch == ')') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.RPARENT);
        }

        // TODO [
        if (ch == '[') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.LBRACK);
        }

        // TODO ]
        if (ch == ']') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.RBRACK);
        }

        // TODO {
        if (ch == '{') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.LBRACE);
        }

        // TODO }
        if (ch == '}') {
            this.lexeme.append(ch);
            return this.getToken(SymbolType.RBRACE);
        }

        // TODO 字符串常量
        if (ch == '"') {
            this.lexeme.append(ch);
            ch = string.charAt(curPos++);
            while (ch != '"') {
                this.lexeme.append(ch);
                ch = string.charAt(curPos++);
            }
            this.lexeme.append(ch);
            return this.getToken(SymbolType.STRCON);
        }

        // TODO 标识符
        // TODO 保留字
        if (Character.isLetter(ch) || ch == '_') {
            this.lexeme.append(ch);
            while (curPos < string.length()) {
                ch = string.charAt(curPos++);
                if (Character.isLetter(ch) || ch == '_' || Character.isDigit(ch)) {
                    this.lexeme.append(ch);
                } else {
                    curPos--;
                    break;
                }
            }
            switch (this.lexeme.toString()) {
                case "main": {
                    return this.getToken(SymbolType.MAINTK);
                }
                case "const": {
                    return this.getToken(SymbolType.CONSTTK);
                }
                case "int": {
                    return this.getToken(SymbolType.INTTK);
                }
                case "break": {
                    return this.getToken(SymbolType.BREAKTK);
                }
                case "continue": {
                    return this.getToken(SymbolType.CONTINUETK);
                }
                case "if": {
                    return this.getToken(SymbolType.IFTK);
                }
                case "else": {
                    return this.getToken(SymbolType.ELSETK);
                }
                case "while": {
                    return this.getToken(SymbolType.WHILETK);
                }
                case "getint": {
                    return this.getToken(SymbolType.GETINTTK);
                }
                case "printf": {
                    return this.getToken(SymbolType.PRINTFTK);
                }
                case "return": {
                    return this.getToken(SymbolType.RETURNTK);
                }
                case "void": {
                    return this.getToken(SymbolType.VOIDTK);
                }
                default: {
                    return this.getToken(SymbolType.IDENFR);
                }
            }
        }

        this.getToken(SymbolType.UNKNOWN);
        return this.nextToken();
    }

    private void error() {
        //输出错误信息
        System.out.println("Error in line " + line);
        while (true) ;
    }
}

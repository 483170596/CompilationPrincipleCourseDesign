package intercode;

import grammar.CFGBlock;
import grammar.Production;
import grammar.SymbolType;
import lexer.Token;
import parser.LRTable;
import parser.LRTableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class InterCode {

    //多符号表——作用域栈
    private final Stack<ArrayList<symbolItem>> symbolTableStack = new Stack<>();

    //S属性文法 翻译模式 要初始化
    private final Rule[] SAG = new Rule[47];

    //LR1分析表 要初始化
    private final LRTable lr1 = new LRTable();

    //文法B 要初始化
    private final CFGBlock CFG = new CFGBlock();

    //状态栈
    private final Stack<Integer> stateStack = new Stack<>();

    //假出口
    private final Stack<Integer> falseStack = new Stack<>();

    //while回填
    private final Stack<Integer> whileBackStack = new Stack<>();

    //while真出口
    private final Stack<Integer> whileGoStack = new Stack<>();

    //if真出口
    private final Stack<Integer> ifGoStack = new Stack<>();

    //出if作用域
    private boolean ifFlag = false;

    //符号栈
    private final Stack<SymbolType> symbolStack = new Stack<>();

    //
    private final Stack<Token> tokenStack = new Stack<>();

    //打印语句的参数
    private final List<Quaternion> printArgs = new ArrayList<>();

    //词法分析结果
    private final List<Token> tokens;

    //中间代码
    private final List<Quaternion> interCode = new ArrayList<>();

    //
    private final StringBuffer buffer = new StringBuffer();//存语法翻译时候栈顶的移入的变化

    //
    private final StringBuffer syntaxBuffer = new StringBuffer();//存生成的中间代码供打印 语义

    //临时变量标号
    private int tempCounter = 1;

    //中间代码标号
    private int codeCounter = 0;

    //
    private int printfNums = 1;//用来生成打印语句的个数

    //
    private boolean printfFirstFlag = true;//用来判断是否进行连续打印

    //
    private boolean writeEmptyFlag = false;

    //
    private boolean whileFlag = false;//用来判断是否进入了else语句的flag

    //构造方法
    public InterCode(List<Token> tokens) {
        this.tokens = tokens;
        int line = tokens.get(tokens.size() - 1).getLine();
        line++;
        tokens.add(new Token(SymbolType.EOF, "#", line));
        CFG.init();
        lr1.init_B();
        SAGInit();
        // 初始化状态栈和符号栈
        stateStack.push(0); // 初始状态
        symbolStack.push(SymbolType.EOF); // 初始符号
    }

    /*TODO 分析*/
    public void semanticAnalysisAndIntermediateCodeGeneration() {
        int currentIndex = 0; // 当前处理的输入符号索引
        while (currentIndex < tokens.size()) {
            //状态栈栈顶
            int currentState = stateStack.peek();
            //当前输入符号
            Token index = tokens.get(currentIndex);
            SymbolType currentSymbol = tokens.get(currentIndex).getType();
            String lexeme = tokens.get(currentIndex).getLexeme();
            if (currentSymbol.equals(SymbolType.ELSETK) && symbolStack.peek().equals(SymbolType.RBRACE)) {
                backPatch(falseStack, interCode.size() + 1);
                falseStack.push(interCode.size());
                ifGoStack.push(1);
                interCode.add(new Quaternion("j", "", "", ""));
            } else if (!currentSymbol.equals(SymbolType.ELSETK) && symbolStack.peek().equals(SymbolType.RBRACE)) {
                if (!falseStack.empty()) {
                    if (!whileBackStack.empty()) {
                        if (falseStack.peek().equals(whileGoStack.peek())) {
                            backPatch(falseStack, interCode.size() + 1);
                            whileGoStack.pop();
                        } else {
                            backPatch(falseStack, interCode.size());
                        }
                    } else {
                        backPatch(falseStack, interCode.size());
                    }
                }
            }
            LRTableEntry entry = lr1.get(currentState, currentSymbol);
            if (entry == null) {
                // 处理错误情况
                // 输出错误信息，可以抛出异常或采取其他处理方式
                System.out.println("Syntax error");
                break;
            } else {
                if (entry.getAction() == 's') {
                    int nextState = entry.getState();
                    stateStack.push(nextState);
                    symbolStack.push(currentSymbol);
                    tokenStack.push(index);
                    buffer.append(currentSymbol).append(" ").append(lexeme).append("\n");
                    currentIndex++;
                    if (currentSymbol.equals(SymbolType.WHILETK)) {
                        whileFlag = true;
                    }
                } else if (entry.getAction() == 'r') {
                    // 规约操作
                    int productionNum = entry.getState(); // 使用产生式编号
                    Production production = CFG.getProduction(productionNum);
                    // 获取产生式左侧非终结符
                    SymbolType leftSymbol = production.getLeft();
                    int reduceCount = production.getRight().size(); // 弹出的符号数
                    //如果产生式最右侧是空串，则不进行弹出操作
                    if (!production.getRight().contains(SymbolType.EPSILON)) {
                        for (int i = 0; i < reduceCount; i++) {
                            stateStack.pop();
                            symbolStack.pop();
                        }
                    }
                    //执行产生中间代码
                    SAG[productionNum].gen(leftSymbol);
                    // 执行状态转移
                    int newState = stateStack.peek();
                    stateStack.push(lr1.get(newState, leftSymbol).getState());
                    symbolStack.push(leftSymbol);
                    buffer.append(leftSymbol).append("\n");
                    System.out.println(leftSymbol);
                } else if (entry.getAction() == 'a') {
                    // 接受操作
                    SAG[0].gen(SymbolType.CompUnit);
                    System.out.println("Accepted");
                    break;
                }
            }
        }
        Production firstproduction = CFG.getProduction(0);
        SymbolType leftSymbol = firstproduction.getLeft();
        buffer.append(leftSymbol).append("\n");
    }

    public StringBuffer getInterCode() {
        for (Quaternion quaternion : interCode) {
            if (quaternion.getOp().equals("j")) {
                if (!quaternion.getResult().equals("")) {
                    if (interCode.get(Integer.parseInt(quaternion.getResult())).getOp().equals("j")) {
                        quaternion.setResult(interCode.get(Integer.parseInt(quaternion.getResult())).getResult());
                    }
                }
            }
        }
        for (int i = 0; i < interCode.size(); i++) {
            syntaxBuffer.append(i).append(":\t").append(interCode.get(i).toTAC()).append('\n');
        }
        return syntaxBuffer;
    }

    //生成临时变量
    private String newTemp() {
        return "T" + tempCounter++;
    }

    //获取临时变量
    private String getTempCounter() {
        return String.valueOf(tempCounter - 1);
    }

    //代码回填
    private void backPatch(Stack<Integer> Stack, int target) {
        if (!Stack.isEmpty()) {
            int n = ifGoStack.pop();
            for (int i = 0; i < n; i++) {
                int position = Stack.pop();
                Quaternion code = interCode.get(position);
                code.setResult(String.valueOf(target));
            }
        } else {
            // 处理栈为空的情况，例如，抛出异常或记录错误。
            System.err.println("错误：尝试从空栈中弹出元素。");
        }
    }

    //符号表项
    private static class symbolItem {
        String name;
        String kind;
        String type;

        public symbolItem() {
        }

        public symbolItem(String name, String kind, String type) {
            this.name = name;
            this.kind = kind;
            this.type = type;
        }

        /**
         * 获取
         *
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * 设置
         *
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 获取
         *
         * @return kind
         */
        public String getKind() {
            return kind;
        }

        /**
         * 设置
         *
         * @param kind
         */
        public void setKind(String kind) {
            this.kind = kind;
        }

        /**
         * 获取
         *
         * @return type
         */
        public String getType() {
            return type;
        }

        /**
         * 设置
         *
         * @param type
         */
        public void setType(String type) {
            this.type = type;
        }

        public String toString() {
            return "symbolItem{name = " + name + ", kind = " + kind + ", type = " + type + "}";
        }
    }

    //规则接口
    private interface Rule {
        void gen(SymbolType leftSymbol);
    }

    //翻译模式 初始化
    private void SAGInit() {
        SAG[0] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                interCode.add(new Quaternion("halt", "", "", ""));
            }
        };
        SAG[1] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                tokenStack.pop();
                tokenStack.pop();
                tokenStack.pop();
            }
        };
        SAG[2] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                ;
            }
        };
        SAG[3] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                ;
            }
        };
        SAG[4] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                ;
            }
        };
        SAG[5] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                ;
            }
        };
        SAG[6] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                ;
            }
        };
        SAG[7] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                ;
            }
        };
        SAG[8] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[9] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[10] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(), op = tokenStack.pop(), left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                interCode.add(new Quaternion(op.getLexeme(), right.getLexeme(), "", left.getLexeme()));
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[11] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                tokenStack.pop();//出分号
                Token right = tokenStack.pop(), op = tokenStack.pop(), left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                interCode.add(new Quaternion(op.getLexeme(), right.getLexeme(), "", left.getLexeme()));
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[12] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {

            }
        };
        SAG[13] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {

            }
        };
        SAG[14] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {

            }
        };
        SAG[15] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {

            }
        };
        SAG[16] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {

            }
        };
        SAG[17] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token semicolon = tokenStack.pop(),
                        rBracket = tokenStack.pop(),
                        firstArg = tokenStack.pop(),
                        lBracket = tokenStack.pop(),
                        fun = tokenStack.pop();
                String strCode = fun.getLexeme() + lBracket.getLexeme() + firstArg.getLexeme() + rBracket.getLexeme() + semicolon.getLexeme();
                interCode.add(new Quaternion("j", "", "", String.valueOf(whileBackStack.pop())));
                tokenStack.push(new Token(leftSymbol, strCode, fun.getLine()));
            }
        };
        SAG[18] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                tokenStack.pop();
                tokenStack.pop();
                tokenStack.pop();
                Token firstArg = tokenStack.pop(),
                        equalSign = tokenStack.pop(),
                        fun = tokenStack.pop();
                String strCode = fun.getLexeme() + equalSign.getLexeme() + firstArg.getLexeme() + "()";
                interCode.add(new Quaternion(equalSign.getLexeme(), firstArg.getLexeme() + "()", "", fun.getLexeme()));
                tokenStack.push(new Token(leftSymbol, strCode, fun.getLine()));
            }
        };
        SAG[19] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token firstArg, fun;
                if (writeEmptyFlag) {
                    tokenStack.pop();
                    tokenStack.pop();
                    firstArg = tokenStack.pop();
                    tokenStack.pop();
                    fun = tokenStack.pop();
                } else {
                    tokenStack.pop();
                    tokenStack.pop();
                    tokenStack.pop();
                    firstArg = tokenStack.pop();
                    tokenStack.pop();
                    fun = tokenStack.pop();
                }
                interCode.add(new Quaternion("param", "", "", firstArg.getLexeme()));
                interCode.addAll(printArgs);
                // 清空 printArgs
                printArgs.clear();
                interCode.add(new Quaternion("call", fun.getLexeme(), "", String.valueOf(printfNums)));
                printfNums = 1;
                printfFirstFlag = true;
            }
        };
        SAG[20] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                if (writeEmptyFlag) {
                    writeEmptyFlag = false;
                }
                printfNums++;
                if (printfFirstFlag) {
                    printfFirstFlag = false;
                    Token lin2 = tokenStack.pop();
                    Token lin1 = tokenStack.pop();
                    String strCode = "param " + lin2.getLexeme();
                    printArgs.add(new Quaternion("param", "", "", lin2.getLexeme()));
                    tokenStack.push(new Token(leftSymbol, strCode, lin1.getLine()));

                } else {
                    Token lin3 = tokenStack.pop();
                    Token lin2 = tokenStack.pop();
                    Token lin1 = tokenStack.pop();
                    String strCode = "param " + lin3.getLexeme();
                    printArgs.add(new Quaternion("param", "", "", lin3.getLexeme()));
                    tokenStack.push(new Token(leftSymbol, strCode, lin1.getLine()));
                }
            }
        };
        SAG[21] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                writeEmptyFlag = true;
            }
        };
        SAG[22] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[23] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                ifFlag = false;
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[24] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        local = tokenStack.pop(),
                        left = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[25] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[26] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[27] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[28] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[29] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                interCode.add(new Quaternion(op.getLexeme(), left.getLexeme(), right.getLexeme(), newTemp()));
                tokenStack.push(new Token(leftSymbol, "T" + getTempCounter(), left.getLine()));
            }
        };
        SAG[30] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                interCode.add(new Quaternion(op.getLexeme(), left.getLexeme(), right.getLexeme(), newTemp()));
                tokenStack.push(new Token(leftSymbol, "T" + getTempCounter(), left.getLine()));
            }
        };
        SAG[31] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                interCode.add(new Quaternion(op.getLexeme(), left.getLexeme(), right.getLexeme(), newTemp()));
                tokenStack.push(new Token(leftSymbol, "T" + getTempCounter(), left.getLine()));
            }
        };
        SAG[32] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[33] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                interCode.add(new Quaternion(op.getLexeme(), left.getLexeme(), right.getLexeme(), newTemp()));
                tokenStack.push(new Token(leftSymbol, "T" + getTempCounter(), left.getLine()));
            }
        };
        SAG[34] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                interCode.add(new Quaternion(op.getLexeme(), left.getLexeme(), right.getLexeme(), newTemp()));
                tokenStack.push(new Token(leftSymbol, "T" + getTempCounter(), left.getLine()));
            }
        };
        SAG[35] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[36] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[37] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[38] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[39] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[40] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[41] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[42] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[43] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                if (!ifFlag) {
                    ifFlag = true;
                    ifGoStack.push(1);
                    System.out.println(ifGoStack);
                }
                if (whileFlag) {
                    whileBackStack.push(interCode.size());
                }
                Token local = tokenStack.pop();
                interCode.add(new Quaternion("jq", local.getLexeme(), "", String.valueOf(interCode.size() + 2)));
                if (whileFlag) {
                    whileGoStack.push(interCode.size());
                    whileFlag = false;
                }
                falseStack.push(interCode.size());
                interCode.add(new Quaternion("j", "", "", ""));
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[44] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                if (ifFlag) {
                    int n = ifGoStack.pop() + 1;
                    ifGoStack.push(n);
                }

                Token right = tokenStack.pop();

                interCode.add(new Quaternion("jq", right.getLexeme(), "", String.valueOf(interCode.size() + 2)));
                falseStack.push(interCode.size());
                interCode.add(new Quaternion("j", "", "", ""));


                Token op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
        SAG[45] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                Token local = tokenStack.pop();
                tokenStack.push(new Token(leftSymbol, local.getLexeme(), local.getLine()));
            }
        };
        SAG[46] = new Rule() {
            @Override
            public void gen(SymbolType leftSymbol) {
                if (ifFlag) {
                    int n = ifGoStack.pop() + 1;
                    ifGoStack.push(n);
                }
                Token right = tokenStack.pop(),
                        op = tokenStack.pop(),
                        left = tokenStack.pop();
                String strCode = left.getLexeme() + op.getLexeme() + right.getLexeme();
                tokenStack.push(new Token(leftSymbol, strCode, left.getLine()));
            }
        };
    }

    //四元式
    private static class Quaternion {
        private String op;
        private String arg1;
        private String arg2;
        private String result;

        public String toTAC() {
            if (op.equals("=")) {
                return result + "=" + arg1;
            } else if (op.equals("<")) {
                return result + "<" + arg1;
            } else if (op.equals(">")) {
                return result + ">" + arg1;
            } else if (op.equals("<=")) {
                return result + "<=" + arg1;
            } else if (op.equals(">=")) {
                return result + ">=" + arg1;
            } else if (op.equals("==")) {
                return result + "==" + arg1;
            } else if (op.equals("!=")) {
                return result + "!=" + arg1;
            } else if (op.equals("&&")) {
                return result + "&&" + arg1;
            } else if (op.equals("||")) {
                return result + "||" + arg1;
            } else if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("%")) {
                return result + "=" + arg1 + op + arg2;

            } else if (op.equals("j")) {
                return "goto " + result;
            } else if (op.equals("jq")) {
                return "if " + arg1 + " goto " + result;
            } else if (op.equals("param")) {
                return "param " + result;
            } else if (op.equals("call")) {
                return "call " + arg1 + "," + result;
            } else if (op.equals("halt")) {
                return "halt";
            }
            throw new IllegalArgumentException("Unsupported operation: " + op);
        }

        public Quaternion() {
        }

        public Quaternion(String op, String arg1, String arg2, String result) {
            this.op = op;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.result = result;
        }

        /**
         * 获取
         *
         * @return op
         */
        public String getOp() {
            return op;
        }

        /**
         * 设置
         *
         * @param op
         */
        public void setOp(String op) {
            this.op = op;
        }

        /**
         * 获取
         *
         * @return arg1
         */
        public String getArg1() {
            return arg1;
        }

        /**
         * 设置
         *
         * @param arg1
         */
        public void setArg1(String arg1) {
            this.arg1 = arg1;
        }

        /**
         * 获取
         *
         * @return arg2
         */
        public String getArg2() {
            return arg2;
        }

        /**
         * 设置
         *
         * @param arg2
         */
        public void setArg2(String arg2) {
            this.arg2 = arg2;
        }

        /**
         * 获取
         *
         * @return result
         */
        public String getResult() {
            return result;
        }

        /**
         * 设置
         *
         * @param result
         */
        public void setResult(String result) {
            this.result = result;
        }

        public String toString() {
            return "Quaternion{op = " + op + ", arg1 = " + arg1 + ", arg2 = " + arg2 + ", result = " + result + "}";
        }
    }
}

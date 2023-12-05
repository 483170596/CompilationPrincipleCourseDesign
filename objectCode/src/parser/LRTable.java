package parser;

import grammar.*;
import lexer.Token;

import java.io.*;
import java.util.*;

public class LRTable {
    private HashMap<Integer, HashMap<SymbolType, LRTableEntry>> table;

    private LR1Automata lr1Automata = new LR1Automata();

    private CFG grammar;

    public LRTable() {
        this.table = new HashMap<>();
    }

    /*automatic construct LRTable for CFGBlock Here*/
    private void init() {
		/* TODO LR(1)分析表
		[1,<Stmt>]=2
		[1,EOF]=acc
		[1,PRINTFTK]=s3
		[1,IDENFR]=s4
		*/
        //you can you addItem function to insert an item of LRtable
        //see LRTableEntry for further information
		/*
		addItem(1, SymbolType.Stmt, new LRTableEntry('g',2));
		addItem(1, SymbolType.EOF, new LRTableEntry('a',0));
		addItem(1, SymbolType.PRINTFTK, new LRTableEntry('s',3));
		addItem(1, SymbolType.IDENFR, new LRTableEntry('s',4));
		'r',
		*/
//        LR1Automata lr1Automata = new LR1Automata();
        lr1Automata.Init();
        this.grammar = lr1Automata.grammar;
        // TODO 由自动机lr1Automata求LR(1)分析表
        for (Map.Entry<Integer, LR1Automata.State> stateEntry : lr1Automata.getAutomata().entrySet()) {
            for (Map.Entry<SymbolType, HashMap<SymbolType, Integer>> goEntry : stateEntry.getValue().go.entrySet()) {
                for (Map.Entry<SymbolType, Integer> entry : goEntry.getValue().entrySet()) {
                    char c = ' ';
                    if (goEntry.getKey().isACC()) {
                        c = 'a';
                    } else if (goEntry.getKey().isREDUCE()) {
                        c = 'r';
                    } else if (goEntry.getKey().isGOTO()) {
                        c = 'g';
                    } else if (goEntry.getKey().isSHIFT()) {
                        c = 's';
                    }
                    addItem(stateEntry.getKey(), entry.getKey(), new LRTableEntry(c, entry.getValue()));
                }
            }
        }
    }

    public void init_B() {
//        // 获取当前工作目录
//        String workingDirectory = System.getProperty("user.dir");
//        // 打印当前工作目录
//        System.out.println("当前工作目录：" + workingDirectory);

        this.grammar = new CFGBlock();
        grammar.init();
        int row, state;
        char action;
        SymbolType column;

        String[] lines = tableString.split("\n");
        for (String line : lines) {
            if (line.equals("EOF")) {
                break;
            }
            String[] words = line.split(" ");
            row = Integer.parseInt(words[0]);
            column = SymbolType.valueOf(words[1]);
            action = words[2].charAt(0);
            state = Integer.parseInt(words[3]);
            addItem(row, column, new LRTableEntry(action, state));
        }

/*        // 使用 try-with-resources 语句确保在操作完成后自动关闭资源
        try (BufferedReader reader = new BufferedReader(new FileReader("src/parser/LR_table.txt"))) {
            // 逐行读取文件内容
            String line;
            while (!(line = reader.readLine()).equals("EOF")) {
                String[] words = line.split(" ");
//                System.out.println(Arrays.toString(words));
//                System.exit(0);
                row = Integer.parseInt(words[0]);
                column = SymbolType.valueOf(words[1]);
                action = words[2].charAt(0);
                state = Integer.parseInt(words[3]);
                addItem(row, column, new LRTableEntry(action, state));
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }*/
    }

    private void init_A() {
        this.grammar = new CFGA();
        grammar.init();
        addItem(0, SymbolType.EOF, new LRTableEntry('r', 2));
        addItem(0, SymbolType.PRINTFTK, new LRTableEntry('r', 2));
        addItem(0, SymbolType.IDENFR, new LRTableEntry('r', 2));
        addItem(0, SymbolType.BlockItemList, new LRTableEntry('g', 1));
        addItem(1, SymbolType.Stmt, new LRTableEntry('g', 2));
        addItem(1, SymbolType.EOF, new LRTableEntry('a', 0));
        addItem(1, SymbolType.PRINTFTK, new LRTableEntry('s', 3));
        addItem(1, SymbolType.IDENFR, new LRTableEntry('s', 4));
        addItem(2, SymbolType.EOF, new LRTableEntry('r', 1));
        addItem(2, SymbolType.PRINTFTK, new LRTableEntry('r', 1));
        addItem(2, SymbolType.IDENFR, new LRTableEntry('r', 1));
        addItem(3, SymbolType.LPARENT, new LRTableEntry('s', 5));
        addItem(4, SymbolType.ASSIGN, new LRTableEntry('s', 6));
        addItem(5, SymbolType.STRCON, new LRTableEntry('s', 7));
        addItem(6, SymbolType.PrimaryExp, new LRTableEntry('g', 8));
        addItem(6, SymbolType.MulExp, new LRTableEntry('g', 9));
        addItem(6, SymbolType.LPARENT, new LRTableEntry('s', 10));
        addItem(6, SymbolType.IDENFR, new LRTableEntry('s', 11));
        addItem(6, SymbolType.AddExp, new LRTableEntry('g', 12));
        addItem(6, SymbolType.INTCON, new LRTableEntry('s', 13));
        addItem(7, SymbolType.COMMA, new LRTableEntry('s', 14));
        addItem(8, SymbolType.SEMICN, new LRTableEntry('r', 8));
        addItem(8, SymbolType.PLUS, new LRTableEntry('r', 8));
        addItem(8, SymbolType.MULT, new LRTableEntry('r', 8));
        addItem(9, SymbolType.SEMICN, new LRTableEntry('r', 6));
        addItem(9, SymbolType.PLUS, new LRTableEntry('r', 6));
        addItem(9, SymbolType.MULT, new LRTableEntry('s', 15));
        addItem(10, SymbolType.PrimaryExp, new LRTableEntry('g', 16));
        addItem(10, SymbolType.MulExp, new LRTableEntry('g', 17));
        addItem(10, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(10, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(10, SymbolType.AddExp, new LRTableEntry('g', 20));
        addItem(10, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(11, SymbolType.SEMICN, new LRTableEntry('r', 10));
        addItem(11, SymbolType.PLUS, new LRTableEntry('r', 10));
        addItem(11, SymbolType.MULT, new LRTableEntry('r', 10));
        addItem(12, SymbolType.SEMICN, new LRTableEntry('s', 22));
        addItem(12, SymbolType.PLUS, new LRTableEntry('s', 23));
        addItem(13, SymbolType.SEMICN, new LRTableEntry('r', 11));
        addItem(13, SymbolType.PLUS, new LRTableEntry('r', 11));
        addItem(13, SymbolType.MULT, new LRTableEntry('r', 11));
        addItem(14, SymbolType.IDENFR, new LRTableEntry('s', 24));
        addItem(15, SymbolType.PrimaryExp, new LRTableEntry('g', 25));
        addItem(15, SymbolType.LPARENT, new LRTableEntry('s', 10));
        addItem(15, SymbolType.IDENFR, new LRTableEntry('s', 11));
        addItem(15, SymbolType.INTCON, new LRTableEntry('s', 13));
        addItem(16, SymbolType.PLUS, new LRTableEntry('r', 8));
        addItem(16, SymbolType.RPARENT, new LRTableEntry('r', 8));
        addItem(16, SymbolType.MULT, new LRTableEntry('r', 8));
        addItem(17, SymbolType.PLUS, new LRTableEntry('r', 6));
        addItem(17, SymbolType.RPARENT, new LRTableEntry('r', 6));
        addItem(17, SymbolType.MULT, new LRTableEntry('s', 26));
        addItem(18, SymbolType.PrimaryExp, new LRTableEntry('g', 16));
        addItem(18, SymbolType.MulExp, new LRTableEntry('g', 17));
        addItem(18, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(18, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(18, SymbolType.AddExp, new LRTableEntry('g', 27));
        addItem(18, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(19, SymbolType.PLUS, new LRTableEntry('r', 10));
        addItem(19, SymbolType.RPARENT, new LRTableEntry('r', 10));
        addItem(19, SymbolType.MULT, new LRTableEntry('r', 10));
        addItem(20, SymbolType.PLUS, new LRTableEntry('s', 28));
        addItem(20, SymbolType.RPARENT, new LRTableEntry('s', 29));
        addItem(21, SymbolType.PLUS, new LRTableEntry('r', 11));
        addItem(21, SymbolType.RPARENT, new LRTableEntry('r', 11));
        addItem(21, SymbolType.MULT, new LRTableEntry('r', 11));
        addItem(22, SymbolType.EOF, new LRTableEntry('r', 3));
        addItem(22, SymbolType.PRINTFTK, new LRTableEntry('r', 3));
        addItem(22, SymbolType.IDENFR, new LRTableEntry('r', 3));
        addItem(23, SymbolType.PrimaryExp, new LRTableEntry('g', 8));
        addItem(23, SymbolType.MulExp, new LRTableEntry('g', 30));
        addItem(23, SymbolType.LPARENT, new LRTableEntry('s', 10));
        addItem(23, SymbolType.IDENFR, new LRTableEntry('s', 11));
        addItem(23, SymbolType.INTCON, new LRTableEntry('s', 13));
        addItem(24, SymbolType.RPARENT, new LRTableEntry('s', 31));
        addItem(25, SymbolType.SEMICN, new LRTableEntry('r', 7));
        addItem(25, SymbolType.PLUS, new LRTableEntry('r', 7));
        addItem(25, SymbolType.MULT, new LRTableEntry('r', 7));
        addItem(26, SymbolType.PrimaryExp, new LRTableEntry('g', 32));
        addItem(26, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(26, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(26, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(27, SymbolType.PLUS, new LRTableEntry('s', 28));
        addItem(27, SymbolType.RPARENT, new LRTableEntry('s', 33));
        addItem(28, SymbolType.PrimaryExp, new LRTableEntry('g', 16));
        addItem(28, SymbolType.MulExp, new LRTableEntry('g', 34));
        addItem(28, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(28, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(28, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(29, SymbolType.SEMICN, new LRTableEntry('r', 9));
        addItem(29, SymbolType.PLUS, new LRTableEntry('r', 9));
        addItem(29, SymbolType.MULT, new LRTableEntry('r', 9));
        addItem(30, SymbolType.SEMICN, new LRTableEntry('r', 5));
        addItem(30, SymbolType.PLUS, new LRTableEntry('r', 5));
        addItem(30, SymbolType.MULT, new LRTableEntry('s', 15));
        addItem(31, SymbolType.SEMICN, new LRTableEntry('s', 35));
        addItem(32, SymbolType.PLUS, new LRTableEntry('r', 7));
        addItem(32, SymbolType.RPARENT, new LRTableEntry('r', 7));
        addItem(32, SymbolType.MULT, new LRTableEntry('r', 7));
        addItem(33, SymbolType.PLUS, new LRTableEntry('r', 9));
        addItem(33, SymbolType.RPARENT, new LRTableEntry('r', 9));
        addItem(33, SymbolType.MULT, new LRTableEntry('r', 9));
        addItem(34, SymbolType.PLUS, new LRTableEntry('r', 5));
        addItem(34, SymbolType.RPARENT, new LRTableEntry('r', 5));
        addItem(34, SymbolType.MULT, new LRTableEntry('s', 26));
        addItem(35, SymbolType.EOF, new LRTableEntry('r', 4));
        addItem(35, SymbolType.PRINTFTK, new LRTableEntry('r', 4));
        addItem(35, SymbolType.IDENFR, new LRTableEntry('r', 4));
    }

    public StringBuffer syntaxAnalysis(LinkedList<Token> tokens) {
        tokens.offer(new Token(SymbolType.EOF, "#", tokens.size() + 1));
        init_B();
        StringBuffer syntaxBuffer = new StringBuffer();

        //状态栈
        Stack<Integer> stateStack = new Stack<>();
        stateStack.push(0);
        //符号栈
        Stack<SymbolType> symbolStack = new Stack<>();
        symbolStack.push(SymbolType.EOF);

        Token nextToken = tokens.poll();
        while (nextToken != null) {
            LRTableEntry newEntry = get(stateStack.peek(), nextToken.getType());
            if (newEntry == null) {
                throw new NullPointerException(stateStack + " " + symbolStack + "\n---\n" + syntaxBuffer);
            }
            System.out.println(stateStack + " " + symbolStack + " " + nextToken + " " + newEntry + "\n---\n");
            /*
            m为状态栈顶，a为当前输入符号，$|ε| = 0$
            1. *初始*：0入状态栈，#入符号栈
            2. $Action[m, a]$ =$S_i$：状态栈压入$i$，符号栈压入$a$
            3. $Action[m, a] = r_j$：状态栈弹出$|r_j|$个状态，符号栈弹出$|r_j|$个符号
            符号栈压入$r_j$的左部符号A，状态栈压入Goto(k,A),其中k为状态栈顶
            4. $Action[m, a] = acc$：分析成功
            5. $Action[m, a] = error$：分析出错*/
            if (newEntry.getAction() == 's') {
                stateStack.push(newEntry.getState());
                symbolStack.push(nextToken.getType());
                syntaxBuffer.append(nextToken + "\n");
                nextToken = tokens.poll();
                continue;
            }
            if (newEntry.getAction() == 'r') {
                Production thisProduct = this.grammar.getProduction(newEntry.getState());
                int length = thisProduct.getRight().size();
                if (thisProduct.getRight().size() == 1 && thisProduct.getRight().get(0).isEpsilon()) {
                    length = 0;
                }
                for (int i = 0; i < length; i++) {
                    stateStack.pop();
                    symbolStack.pop();
                }
                symbolStack.push(thisProduct.getLeft());
                syntaxBuffer.append(thisProduct.getLeft() + "\n");
                try {
                    stateStack.push(get(stateStack.peek(), symbolStack.peek()).getState());
                } catch (NullPointerException e) {
                    System.out.println(thisProduct.getRight().size());
                    System.out.println(stateStack + " " + symbolStack + "\n------------\n" + syntaxBuffer + "\n->" + nextToken);
                    System.exit(1);
                }
                continue;
            }
            if (newEntry.getAction() == 'a') {
                syntaxBuffer.append(SymbolType.CompUnit);
                break;
            }
        }
        return syntaxBuffer;
    }

    public void addItem(int row, SymbolType column, LRTableEntry entry) {
        HashMap<SymbolType, LRTableEntry> tmp = null;
        tmp = table.get(row);
        if (tmp == null) tmp = new HashMap<SymbolType, LRTableEntry>();
        tmp.put(column, entry);
        table.put(row, tmp);
    }

    //从分析表获取[row,column]
    public LRTableEntry get(int row, SymbolType column) {
        HashMap<SymbolType, LRTableEntry> tmp = null;
        tmp = table.get(row);
        if (tmp == null) return null;
        return tmp.get(column);
    }

    private final String tableString = "0 LBRACE s 1\n" +
            "0 Block g 2\n" +
            "1 INTCON r 3\n" +
            "1 IFTK r 3\n" +
            "1 WHILETK r 3\n" +
            "1 BlockItemList g 3\n" +
            "1 LBRACE r 3\n" +
            "1 PRINTFTK r 3\n" +
            "1 SEMICN r 3\n" +
            "1 IDENFR r 3\n" +
            "1 RBRACE r 3\n" +
            "1 INTTK r 3\n" +
            "1 LPARENT r 3\n" +
            "2 EOF a 0\n" +
            "3 WHILETK s 21\n" +
            "3 MulExp g 6\n" +
            "3 LBRACE s 11\n" +
            "3 PRINTFTK s 7\n" +
            "3 SEMICN s 4\n" +
            "3 Stmt g 13\n" +
            "3 IDENFR s 9\n" +
            "3 RBRACE s 22\n" +
            "3 INTTK s 16\n" +
            "3 BlockItem g 14\n" +
            "3 Block g 8\n" +
            "3 LPARENT s 19\n" +
            "3 UnaryExp g 10\n" +
            "3 INTCON s 18\n" +
            "3 IFTK s 17\n" +
            "3 VarDecl g 5\n" +
            "3 PrimaryExp g 20\n" +
            "3 Exp g 15\n" +
            "3 AddExp g 12\n" +
            "4 INTCON r 13\n" +
            "4 IFTK r 13\n" +
            "4 WHILETK r 13\n" +
            "4 LBRACE r 13\n" +
            "4 PRINTFTK r 13\n" +
            "4 SEMICN r 13\n" +
            "4 IDENFR r 13\n" +
            "4 RBRACE r 13\n" +
            "4 INTTK r 13\n" +
            "4 LPARENT r 13\n" +
            "5 INTCON r 4\n" +
            "5 IFTK r 4\n" +
            "5 WHILETK r 4\n" +
            "5 LBRACE r 4\n" +
            "5 PRINTFTK r 4\n" +
            "5 SEMICN r 4\n" +
            "5 IDENFR r 4\n" +
            "5 RBRACE r 4\n" +
            "5 INTTK r 4\n" +
            "5 LPARENT r 4\n" +
            "6 MOD s 24\n" +
            "6 MULT s 25\n" +
            "6 MINU r 32\n" +
            "6 SEMICN r 32\n" +
            "6 DIV s 23\n" +
            "6 PLUS r 32\n" +
            "7 LPARENT s 26\n" +
            "8 INTCON r 14\n" +
            "8 IFTK r 14\n" +
            "8 WHILETK r 14\n" +
            "8 LBRACE r 14\n" +
            "8 PRINTFTK r 14\n" +
            "8 SEMICN r 14\n" +
            "8 IDENFR r 14\n" +
            "8 RBRACE r 14\n" +
            "8 INTTK r 14\n" +
            "8 LPARENT r 14\n" +
            "9 MOD r 25\n" +
            "9 MULT r 25\n" +
            "9 MINU r 25\n" +
            "9 SEMICN r 25\n" +
            "9 DIV r 25\n" +
            "9 PLUS r 25\n" +
            "9 ASSIGN s 27\n" +
            "10 MOD r 28\n" +
            "10 MULT r 28\n" +
            "10 MINU r 28\n" +
            "10 SEMICN r 28\n" +
            "10 DIV r 28\n" +
            "10 PLUS r 28\n" +
            "11 INTCON r 3\n" +
            "11 IFTK r 3\n" +
            "11 WHILETK r 3\n" +
            "11 BlockItemList g 28\n" +
            "11 LBRACE r 3\n" +
            "11 PRINTFTK r 3\n" +
            "11 SEMICN r 3\n" +
            "11 IDENFR r 3\n" +
            "11 RBRACE r 3\n" +
            "11 INTTK r 3\n" +
            "11 LPARENT r 3\n" +
            "12 MINU s 30\n" +
            "12 SEMICN r 22\n" +
            "12 PLUS s 29\n" +
            "13 INTCON r 5\n" +
            "13 IFTK r 5\n" +
            "13 WHILETK r 5\n" +
            "13 LBRACE r 5\n" +
            "13 PRINTFTK r 5\n" +
            "13 SEMICN r 5\n" +
            "13 IDENFR r 5\n" +
            "13 RBRACE r 5\n" +
            "13 INTTK r 5\n" +
            "13 LPARENT r 5\n" +
            "14 INTCON r 2\n" +
            "14 IFTK r 2\n" +
            "14 WHILETK r 2\n" +
            "14 LBRACE r 2\n" +
            "14 PRINTFTK r 2\n" +
            "14 SEMICN r 2\n" +
            "14 IDENFR r 2\n" +
            "14 RBRACE r 2\n" +
            "14 INTTK r 2\n" +
            "14 LPARENT r 2\n" +
            "15 SEMICN s 31\n" +
            "16 VarDef g 32\n" +
            "16 IDENFR s 34\n" +
            "16 VarDeclList g 33\n" +
            "17 LPARENT s 35\n" +
            "18 MOD r 26\n" +
            "18 MULT r 26\n" +
            "18 MINU r 26\n" +
            "18 SEMICN r 26\n" +
            "18 DIV r 26\n" +
            "18 PLUS r 26\n" +
            "19 INTCON s 41\n" +
            "19 MulExp g 40\n" +
            "19 IDENFR s 37\n" +
            "19 LPARENT s 38\n" +
            "19 PrimaryExp g 42\n" +
            "19 Exp g 36\n" +
            "19 AddExp g 43\n" +
            "19 UnaryExp g 39\n" +
            "20 MOD r 27\n" +
            "20 MULT r 27\n" +
            "20 MINU r 27\n" +
            "20 SEMICN r 27\n" +
            "20 DIV r 27\n" +
            "20 PLUS r 27\n" +
            "21 LPARENT s 44\n" +
            "22 EOF r 1\n" +
            "23 INTCON s 18\n" +
            "23 IDENFR s 46\n" +
            "23 PrimaryExp g 20\n" +
            "23 LPARENT s 19\n" +
            "23 UnaryExp g 45\n" +
            "24 INTCON s 18\n" +
            "24 IDENFR s 46\n" +
            "24 PrimaryExp g 20\n" +
            "24 LPARENT s 19\n" +
            "24 UnaryExp g 47\n" +
            "25 INTCON s 18\n" +
            "25 IDENFR s 46\n" +
            "25 PrimaryExp g 20\n" +
            "25 LPARENT s 19\n" +
            "25 UnaryExp g 48\n" +
            "26 STRCON s 49\n" +
            "27 INTCON s 18\n" +
            "27 MulExp g 6\n" +
            "27 IDENFR s 46\n" +
            "27 GETINTTK s 51\n" +
            "27 LPARENT s 19\n" +
            "27 PrimaryExp g 20\n" +
            "27 Exp g 50\n" +
            "27 AddExp g 12\n" +
            "27 UnaryExp g 10\n" +
            "28 WHILETK s 21\n" +
            "28 MulExp g 6\n" +
            "28 LBRACE s 11\n" +
            "28 PRINTFTK s 7\n" +
            "28 SEMICN s 4\n" +
            "28 Stmt g 13\n" +
            "28 IDENFR s 9\n" +
            "28 RBRACE s 52\n" +
            "28 INTTK s 16\n" +
            "28 BlockItem g 14\n" +
            "28 Block g 8\n" +
            "28 LPARENT s 19\n" +
            "28 UnaryExp g 10\n" +
            "28 INTCON s 18\n" +
            "28 IFTK s 17\n" +
            "28 VarDecl g 5\n" +
            "28 PrimaryExp g 20\n" +
            "28 Exp g 15\n" +
            "28 AddExp g 12\n" +
            "29 INTCON s 18\n" +
            "29 MulExp g 53\n" +
            "29 IDENFR s 46\n" +
            "29 LPARENT s 19\n" +
            "29 PrimaryExp g 20\n" +
            "29 UnaryExp g 10\n" +
            "30 INTCON s 18\n" +
            "30 MulExp g 54\n" +
            "30 IDENFR s 46\n" +
            "30 LPARENT s 19\n" +
            "30 PrimaryExp g 20\n" +
            "30 UnaryExp g 10\n" +
            "31 INTCON r 12\n" +
            "31 IFTK r 12\n" +
            "31 WHILETK r 12\n" +
            "31 LBRACE r 12\n" +
            "31 PRINTFTK r 12\n" +
            "31 SEMICN r 12\n" +
            "31 IDENFR r 12\n" +
            "31 RBRACE r 12\n" +
            "31 INTTK r 12\n" +
            "31 LPARENT r 12\n" +
            "32 COMMA r 8\n" +
            "32 SEMICN r 8\n" +
            "33 COMMA s 56\n" +
            "33 SEMICN s 55\n" +
            "34 COMMA r 9\n" +
            "34 SEMICN r 9\n" +
            "34 ASSIGN s 57\n" +
            "35 INTCON s 64\n" +
            "35 MulExp g 60\n" +
            "35 RelExp g 58\n" +
            "35 LOrExp g 69\n" +
            "35 EqExp g 63\n" +
            "35 IDENFR s 67\n" +
            "35 LPARENT s 66\n" +
            "35 PrimaryExp g 68\n" +
            "35 LAndExp g 59\n" +
            "35 Cond g 65\n" +
            "35 AddExp g 62\n" +
            "35 UnaryExp g 61\n" +
            "36 RPARENT s 70\n" +
            "37 MOD r 25\n" +
            "37 MULT r 25\n" +
            "37 MINU r 25\n" +
            "37 RPARENT r 25\n" +
            "37 DIV r 25\n" +
            "37 PLUS r 25\n" +
            "38 INTCON s 41\n" +
            "38 MulExp g 40\n" +
            "38 IDENFR s 37\n" +
            "38 LPARENT s 38\n" +
            "38 PrimaryExp g 42\n" +
            "38 Exp g 71\n" +
            "38 AddExp g 43\n" +
            "38 UnaryExp g 39\n" +
            "39 MOD r 28\n" +
            "39 MULT r 28\n" +
            "39 MINU r 28\n" +
            "39 RPARENT r 28\n" +
            "39 DIV r 28\n" +
            "39 PLUS r 28\n" +
            "40 MOD s 74\n" +
            "40 MULT s 72\n" +
            "40 MINU r 32\n" +
            "40 RPARENT r 32\n" +
            "40 DIV s 73\n" +
            "40 PLUS r 32\n" +
            "41 MOD r 26\n" +
            "41 MULT r 26\n" +
            "41 MINU r 26\n" +
            "41 RPARENT r 26\n" +
            "41 DIV r 26\n" +
            "41 PLUS r 26\n" +
            "42 MOD r 27\n" +
            "42 MULT r 27\n" +
            "42 MINU r 27\n" +
            "42 RPARENT r 27\n" +
            "42 DIV r 27\n" +
            "42 PLUS r 27\n" +
            "43 MINU s 76\n" +
            "43 RPARENT r 22\n" +
            "43 PLUS s 75\n" +
            "44 INTCON s 64\n" +
            "44 MulExp g 60\n" +
            "44 RelExp g 58\n" +
            "44 LOrExp g 69\n" +
            "44 EqExp g 63\n" +
            "44 IDENFR s 67\n" +
            "44 LPARENT s 66\n" +
            "44 PrimaryExp g 68\n" +
            "44 LAndExp g 59\n" +
            "44 Cond g 77\n" +
            "44 AddExp g 62\n" +
            "44 UnaryExp g 61\n" +
            "45 MOD r 30\n" +
            "45 MULT r 30\n" +
            "45 MINU r 30\n" +
            "45 SEMICN r 30\n" +
            "45 DIV r 30\n" +
            "45 PLUS r 30\n" +
            "46 MOD r 25\n" +
            "46 MULT r 25\n" +
            "46 MINU r 25\n" +
            "46 SEMICN r 25\n" +
            "46 DIV r 25\n" +
            "46 PLUS r 25\n" +
            "47 MOD r 31\n" +
            "47 MULT r 31\n" +
            "47 MINU r 31\n" +
            "47 SEMICN r 31\n" +
            "47 DIV r 31\n" +
            "47 PLUS r 31\n" +
            "48 MOD r 29\n" +
            "48 MULT r 29\n" +
            "48 MINU r 29\n" +
            "48 SEMICN r 29\n" +
            "48 DIV r 29\n" +
            "48 PLUS r 29\n" +
            "49 COMMA r 21\n" +
            "49 RPARENT r 21\n" +
            "49 ExpList g 78\n" +
            "50 SEMICN s 79\n" +
            "51 LPARENT s 80\n" +
            "52 INTCON r 1\n" +
            "52 IFTK r 1\n" +
            "52 WHILETK r 1\n" +
            "52 LBRACE r 1\n" +
            "52 PRINTFTK r 1\n" +
            "52 SEMICN r 1\n" +
            "52 IDENFR r 1\n" +
            "52 RBRACE r 1\n" +
            "52 INTTK r 1\n" +
            "52 LPARENT r 1\n" +
            "53 MOD s 24\n" +
            "53 MULT s 25\n" +
            "53 MINU r 33\n" +
            "53 SEMICN r 33\n" +
            "53 DIV s 23\n" +
            "53 PLUS r 33\n" +
            "54 MOD s 24\n" +
            "54 MULT s 25\n" +
            "54 MINU r 34\n" +
            "54 SEMICN r 34\n" +
            "54 DIV s 23\n" +
            "54 PLUS r 34\n" +
            "55 INTCON r 6\n" +
            "55 IFTK r 6\n" +
            "55 WHILETK r 6\n" +
            "55 LBRACE r 6\n" +
            "55 PRINTFTK r 6\n" +
            "55 SEMICN r 6\n" +
            "55 IDENFR r 6\n" +
            "55 RBRACE r 6\n" +
            "55 INTTK r 6\n" +
            "55 LPARENT r 6\n" +
            "56 VarDef g 81\n" +
            "56 IDENFR s 34\n" +
            "57 INTCON s 85\n" +
            "57 MulExp g 82\n" +
            "57 IDENFR s 83\n" +
            "57 LPARENT s 87\n" +
            "57 PrimaryExp g 88\n" +
            "57 Exp g 89\n" +
            "57 AddExp g 86\n" +
            "57 UnaryExp g 84\n" +
            "58 GEQ s 92\n" +
            "58 OR r 40\n" +
            "58 RPARENT r 40\n" +
            "58 NEQ r 40\n" +
            "58 LSS s 90\n" +
            "58 EQL r 40\n" +
            "58 AND r 40\n" +
            "58 LEQ s 91\n" +
            "58 GRE s 93\n" +
            "59 OR r 45\n" +
            "59 RPARENT r 45\n" +
            "59 AND s 94\n" +
            "60 MOD s 95\n" +
            "60 MULT s 97\n" +
            "60 GEQ r 32\n" +
            "60 OR r 32\n" +
            "60 RPARENT r 32\n" +
            "60 LSS r 32\n" +
            "60 PLUS r 32\n" +
            "60 EQL r 32\n" +
            "60 AND r 32\n" +
            "60 GRE r 32\n" +
            "60 MINU r 32\n" +
            "60 NEQ r 32\n" +
            "60 DIV s 96\n" +
            "60 LEQ r 32\n" +
            "61 MOD r 28\n" +
            "61 MULT r 28\n" +
            "61 GEQ r 28\n" +
            "61 OR r 28\n" +
            "61 RPARENT r 28\n" +
            "61 LSS r 28\n" +
            "61 PLUS r 28\n" +
            "61 EQL r 28\n" +
            "61 AND r 28\n" +
            "61 GRE r 28\n" +
            "61 MINU r 28\n" +
            "61 NEQ r 28\n" +
            "61 DIV r 28\n" +
            "61 LEQ r 28\n" +
            "62 GEQ r 35\n" +
            "62 OR r 35\n" +
            "62 MINU s 98\n" +
            "62 RPARENT r 35\n" +
            "62 NEQ r 35\n" +
            "62 LSS r 35\n" +
            "62 PLUS s 99\n" +
            "62 EQL r 35\n" +
            "62 AND r 35\n" +
            "62 LEQ r 35\n" +
            "62 GRE r 35\n" +
            "63 OR r 43\n" +
            "63 RPARENT r 43\n" +
            "63 NEQ s 100\n" +
            "63 EQL s 101\n" +
            "63 AND r 43\n" +
            "64 MOD r 26\n" +
            "64 MULT r 26\n" +
            "64 GEQ r 26\n" +
            "64 OR r 26\n" +
            "64 RPARENT r 26\n" +
            "64 LSS r 26\n" +
            "64 PLUS r 26\n" +
            "64 EQL r 26\n" +
            "64 AND r 26\n" +
            "64 GRE r 26\n" +
            "64 MINU r 26\n" +
            "64 NEQ r 26\n" +
            "64 DIV r 26\n" +
            "64 LEQ r 26\n" +
            "65 RPARENT s 102\n" +
            "66 INTCON s 41\n" +
            "66 MulExp g 40\n" +
            "66 IDENFR s 37\n" +
            "66 LPARENT s 38\n" +
            "66 PrimaryExp g 42\n" +
            "66 Exp g 103\n" +
            "66 AddExp g 43\n" +
            "66 UnaryExp g 39\n" +
            "67 MOD r 25\n" +
            "67 MULT r 25\n" +
            "67 GEQ r 25\n" +
            "67 OR r 25\n" +
            "67 RPARENT r 25\n" +
            "67 LSS r 25\n" +
            "67 PLUS r 25\n" +
            "67 EQL r 25\n" +
            "67 AND r 25\n" +
            "67 GRE r 25\n" +
            "67 MINU r 25\n" +
            "67 NEQ r 25\n" +
            "67 DIV r 25\n" +
            "67 LEQ r 25\n" +
            "68 MOD r 27\n" +
            "68 MULT r 27\n" +
            "68 GEQ r 27\n" +
            "68 OR r 27\n" +
            "68 RPARENT r 27\n" +
            "68 LSS r 27\n" +
            "68 PLUS r 27\n" +
            "68 EQL r 27\n" +
            "68 AND r 27\n" +
            "68 GRE r 27\n" +
            "68 MINU r 27\n" +
            "68 NEQ r 27\n" +
            "68 DIV r 27\n" +
            "68 LEQ r 27\n" +
            "69 OR s 104\n" +
            "69 RPARENT r 23\n" +
            "70 MOD r 24\n" +
            "70 MULT r 24\n" +
            "70 MINU r 24\n" +
            "70 SEMICN r 24\n" +
            "70 DIV r 24\n" +
            "70 PLUS r 24\n" +
            "71 RPARENT s 105\n" +
            "72 INTCON s 41\n" +
            "72 IDENFR s 37\n" +
            "72 LPARENT s 38\n" +
            "72 PrimaryExp g 42\n" +
            "72 UnaryExp g 106\n" +
            "73 INTCON s 41\n" +
            "73 IDENFR s 37\n" +
            "73 LPARENT s 38\n" +
            "73 PrimaryExp g 42\n" +
            "73 UnaryExp g 107\n" +
            "74 INTCON s 41\n" +
            "74 IDENFR s 37\n" +
            "74 LPARENT s 38\n" +
            "74 PrimaryExp g 42\n" +
            "74 UnaryExp g 108\n" +
            "75 INTCON s 41\n" +
            "75 MulExp g 109\n" +
            "75 IDENFR s 37\n" +
            "75 LPARENT s 38\n" +
            "75 PrimaryExp g 42\n" +
            "75 UnaryExp g 39\n" +
            "76 INTCON s 41\n" +
            "76 MulExp g 110\n" +
            "76 IDENFR s 37\n" +
            "76 LPARENT s 38\n" +
            "76 PrimaryExp g 42\n" +
            "76 UnaryExp g 39\n" +
            "77 RPARENT s 111\n" +
            "78 COMMA s 113\n" +
            "78 RPARENT s 112\n" +
            "79 INTCON r 11\n" +
            "79 IFTK r 11\n" +
            "79 WHILETK r 11\n" +
            "79 LBRACE r 11\n" +
            "79 PRINTFTK r 11\n" +
            "79 SEMICN r 11\n" +
            "79 IDENFR r 11\n" +
            "79 RBRACE r 11\n" +
            "79 INTTK r 11\n" +
            "79 LPARENT r 11\n" +
            "80 RPARENT s 114\n" +
            "81 COMMA r 7\n" +
            "81 SEMICN r 7\n" +
            "82 COMMA r 32\n" +
            "82 MOD s 115\n" +
            "82 MULT s 116\n" +
            "82 MINU r 32\n" +
            "82 SEMICN r 32\n" +
            "82 DIV s 117\n" +
            "82 PLUS r 32\n" +
            "83 COMMA r 25\n" +
            "83 MOD r 25\n" +
            "83 MULT r 25\n" +
            "83 MINU r 25\n" +
            "83 SEMICN r 25\n" +
            "83 DIV r 25\n" +
            "83 PLUS r 25\n" +
            "84 COMMA r 28\n" +
            "84 MOD r 28\n" +
            "84 MULT r 28\n" +
            "84 MINU r 28\n" +
            "84 SEMICN r 28\n" +
            "84 DIV r 28\n" +
            "84 PLUS r 28\n" +
            "85 COMMA r 26\n" +
            "85 MOD r 26\n" +
            "85 MULT r 26\n" +
            "85 MINU r 26\n" +
            "85 SEMICN r 26\n" +
            "85 DIV r 26\n" +
            "85 PLUS r 26\n" +
            "86 COMMA r 22\n" +
            "86 MINU s 119\n" +
            "86 SEMICN r 22\n" +
            "86 PLUS s 118\n" +
            "87 INTCON s 41\n" +
            "87 MulExp g 40\n" +
            "87 IDENFR s 37\n" +
            "87 LPARENT s 38\n" +
            "87 PrimaryExp g 42\n" +
            "87 Exp g 120\n" +
            "87 AddExp g 43\n" +
            "87 UnaryExp g 39\n" +
            "88 COMMA r 27\n" +
            "88 MOD r 27\n" +
            "88 MULT r 27\n" +
            "88 MINU r 27\n" +
            "88 SEMICN r 27\n" +
            "88 DIV r 27\n" +
            "88 PLUS r 27\n" +
            "89 COMMA r 10\n" +
            "89 SEMICN r 10\n" +
            "90 INTCON s 64\n" +
            "90 MulExp g 60\n" +
            "90 IDENFR s 67\n" +
            "90 PrimaryExp g 68\n" +
            "90 LPARENT s 66\n" +
            "90 AddExp g 121\n" +
            "90 UnaryExp g 61\n" +
            "91 INTCON s 64\n" +
            "91 MulExp g 60\n" +
            "91 IDENFR s 67\n" +
            "91 PrimaryExp g 68\n" +
            "91 LPARENT s 66\n" +
            "91 AddExp g 122\n" +
            "91 UnaryExp g 61\n" +
            "92 INTCON s 64\n" +
            "92 MulExp g 60\n" +
            "92 IDENFR s 67\n" +
            "92 PrimaryExp g 68\n" +
            "92 LPARENT s 66\n" +
            "92 AddExp g 123\n" +
            "92 UnaryExp g 61\n" +
            "93 INTCON s 64\n" +
            "93 MulExp g 60\n" +
            "93 IDENFR s 67\n" +
            "93 PrimaryExp g 68\n" +
            "93 LPARENT s 66\n" +
            "93 AddExp g 124\n" +
            "93 UnaryExp g 61\n" +
            "94 INTCON s 64\n" +
            "94 MulExp g 60\n" +
            "94 RelExp g 58\n" +
            "94 EqExp g 125\n" +
            "94 IDENFR s 67\n" +
            "94 LPARENT s 66\n" +
            "94 PrimaryExp g 68\n" +
            "94 AddExp g 62\n" +
            "94 UnaryExp g 61\n" +
            "95 INTCON s 64\n" +
            "95 IDENFR s 67\n" +
            "95 LPARENT s 66\n" +
            "95 PrimaryExp g 68\n" +
            "95 UnaryExp g 126\n" +
            "96 INTCON s 64\n" +
            "96 IDENFR s 67\n" +
            "96 LPARENT s 66\n" +
            "96 PrimaryExp g 68\n" +
            "96 UnaryExp g 127\n" +
            "97 INTCON s 64\n" +
            "97 IDENFR s 67\n" +
            "97 LPARENT s 66\n" +
            "97 PrimaryExp g 68\n" +
            "97 UnaryExp g 128\n" +
            "98 INTCON s 64\n" +
            "98 MulExp g 129\n" +
            "98 IDENFR s 67\n" +
            "98 PrimaryExp g 68\n" +
            "98 LPARENT s 66\n" +
            "98 UnaryExp g 61\n" +
            "99 INTCON s 64\n" +
            "99 MulExp g 130\n" +
            "99 IDENFR s 67\n" +
            "99 PrimaryExp g 68\n" +
            "99 LPARENT s 66\n" +
            "99 UnaryExp g 61\n" +
            "100 INTCON s 64\n" +
            "100 MulExp g 60\n" +
            "100 RelExp g 131\n" +
            "100 IDENFR s 67\n" +
            "100 LPARENT s 66\n" +
            "100 PrimaryExp g 68\n" +
            "100 AddExp g 62\n" +
            "100 UnaryExp g 61\n" +
            "101 INTCON s 64\n" +
            "101 MulExp g 60\n" +
            "101 RelExp g 132\n" +
            "101 IDENFR s 67\n" +
            "101 LPARENT s 66\n" +
            "101 PrimaryExp g 68\n" +
            "101 AddExp g 62\n" +
            "101 UnaryExp g 61\n" +
            "102 LBRACE s 134\n" +
            "102 Block g 133\n" +
            "103 RPARENT s 135\n" +
            "104 INTCON s 64\n" +
            "104 MulExp g 60\n" +
            "104 RelExp g 58\n" +
            "104 EqExp g 63\n" +
            "104 IDENFR s 67\n" +
            "104 LPARENT s 66\n" +
            "104 PrimaryExp g 68\n" +
            "104 LAndExp g 136\n" +
            "104 AddExp g 62\n" +
            "104 UnaryExp g 61\n" +
            "105 MOD r 24\n" +
            "105 MULT r 24\n" +
            "105 MINU r 24\n" +
            "105 RPARENT r 24\n" +
            "105 DIV r 24\n" +
            "105 PLUS r 24\n" +
            "106 MOD r 29\n" +
            "106 MULT r 29\n" +
            "106 MINU r 29\n" +
            "106 RPARENT r 29\n" +
            "106 DIV r 29\n" +
            "106 PLUS r 29\n" +
            "107 MOD r 30\n" +
            "107 MULT r 30\n" +
            "107 MINU r 30\n" +
            "107 RPARENT r 30\n" +
            "107 DIV r 30\n" +
            "107 PLUS r 30\n" +
            "108 MOD r 31\n" +
            "108 MULT r 31\n" +
            "108 MINU r 31\n" +
            "108 RPARENT r 31\n" +
            "108 DIV r 31\n" +
            "108 PLUS r 31\n" +
            "109 MOD s 74\n" +
            "109 MULT s 72\n" +
            "109 MINU r 33\n" +
            "109 RPARENT r 33\n" +
            "109 DIV s 73\n" +
            "109 PLUS r 33\n" +
            "110 MOD s 74\n" +
            "110 MULT s 72\n" +
            "110 MINU r 34\n" +
            "110 RPARENT r 34\n" +
            "110 DIV s 73\n" +
            "110 PLUS r 34\n" +
            "111 WHILETK s 21\n" +
            "111 MulExp g 6\n" +
            "111 LBRACE s 11\n" +
            "111 PRINTFTK s 7\n" +
            "111 SEMICN s 4\n" +
            "111 Stmt g 137\n" +
            "111 IDENFR s 9\n" +
            "111 Block g 8\n" +
            "111 LPARENT s 19\n" +
            "111 UnaryExp g 10\n" +
            "111 INTCON s 18\n" +
            "111 IFTK s 17\n" +
            "111 PrimaryExp g 20\n" +
            "111 Exp g 15\n" +
            "111 AddExp g 12\n" +
            "112 SEMICN s 138\n" +
            "113 INTCON s 143\n" +
            "113 MulExp g 142\n" +
            "113 IDENFR s 139\n" +
            "113 LPARENT s 140\n" +
            "113 PrimaryExp g 144\n" +
            "113 Exp g 146\n" +
            "113 AddExp g 145\n" +
            "113 UnaryExp g 141\n" +
            "114 SEMICN s 147\n" +
            "115 INTCON s 85\n" +
            "115 IDENFR s 83\n" +
            "115 PrimaryExp g 88\n" +
            "115 LPARENT s 87\n" +
            "115 UnaryExp g 148\n" +
            "116 INTCON s 85\n" +
            "116 IDENFR s 83\n" +
            "116 PrimaryExp g 88\n" +
            "116 LPARENT s 87\n" +
            "116 UnaryExp g 149\n" +
            "117 INTCON s 85\n" +
            "117 IDENFR s 83\n" +
            "117 PrimaryExp g 88\n" +
            "117 LPARENT s 87\n" +
            "117 UnaryExp g 150\n" +
            "118 INTCON s 85\n" +
            "118 MulExp g 151\n" +
            "118 IDENFR s 83\n" +
            "118 LPARENT s 87\n" +
            "118 PrimaryExp g 88\n" +
            "118 UnaryExp g 84\n" +
            "119 INTCON s 85\n" +
            "119 MulExp g 152\n" +
            "119 IDENFR s 83\n" +
            "119 LPARENT s 87\n" +
            "119 PrimaryExp g 88\n" +
            "119 UnaryExp g 84\n" +
            "120 RPARENT s 153\n" +
            "121 GEQ r 36\n" +
            "121 OR r 36\n" +
            "121 MINU s 98\n" +
            "121 RPARENT r 36\n" +
            "121 NEQ r 36\n" +
            "121 LSS r 36\n" +
            "121 PLUS s 99\n" +
            "121 EQL r 36\n" +
            "121 AND r 36\n" +
            "121 LEQ r 36\n" +
            "121 GRE r 36\n" +
            "122 GEQ r 38\n" +
            "122 OR r 38\n" +
            "122 MINU s 98\n" +
            "122 RPARENT r 38\n" +
            "122 NEQ r 38\n" +
            "122 LSS r 38\n" +
            "122 PLUS s 99\n" +
            "122 EQL r 38\n" +
            "122 AND r 38\n" +
            "122 LEQ r 38\n" +
            "122 GRE r 38\n" +
            "123 GEQ r 39\n" +
            "123 OR r 39\n" +
            "123 MINU s 98\n" +
            "123 RPARENT r 39\n" +
            "123 NEQ r 39\n" +
            "123 LSS r 39\n" +
            "123 PLUS s 99\n" +
            "123 EQL r 39\n" +
            "123 AND r 39\n" +
            "123 LEQ r 39\n" +
            "123 GRE r 39\n" +
            "124 GEQ r 37\n" +
            "124 OR r 37\n" +
            "124 MINU s 98\n" +
            "124 RPARENT r 37\n" +
            "124 NEQ r 37\n" +
            "124 LSS r 37\n" +
            "124 PLUS s 99\n" +
            "124 EQL r 37\n" +
            "124 AND r 37\n" +
            "124 LEQ r 37\n" +
            "124 GRE r 37\n" +
            "125 OR r 44\n" +
            "125 RPARENT r 44\n" +
            "125 NEQ s 100\n" +
            "125 EQL s 101\n" +
            "125 AND r 44\n" +
            "126 MOD r 31\n" +
            "126 MULT r 31\n" +
            "126 GEQ r 31\n" +
            "126 OR r 31\n" +
            "126 RPARENT r 31\n" +
            "126 LSS r 31\n" +
            "126 PLUS r 31\n" +
            "126 EQL r 31\n" +
            "126 AND r 31\n" +
            "126 GRE r 31\n" +
            "126 MINU r 31\n" +
            "126 NEQ r 31\n" +
            "126 DIV r 31\n" +
            "126 LEQ r 31\n" +
            "127 MOD r 30\n" +
            "127 MULT r 30\n" +
            "127 GEQ r 30\n" +
            "127 OR r 30\n" +
            "127 RPARENT r 30\n" +
            "127 LSS r 30\n" +
            "127 PLUS r 30\n" +
            "127 EQL r 30\n" +
            "127 AND r 30\n" +
            "127 GRE r 30\n" +
            "127 MINU r 30\n" +
            "127 NEQ r 30\n" +
            "127 DIV r 30\n" +
            "127 LEQ r 30\n" +
            "128 MOD r 29\n" +
            "128 MULT r 29\n" +
            "128 GEQ r 29\n" +
            "128 OR r 29\n" +
            "128 RPARENT r 29\n" +
            "128 LSS r 29\n" +
            "128 PLUS r 29\n" +
            "128 EQL r 29\n" +
            "128 AND r 29\n" +
            "128 GRE r 29\n" +
            "128 MINU r 29\n" +
            "128 NEQ r 29\n" +
            "128 DIV r 29\n" +
            "128 LEQ r 29\n" +
            "129 MOD s 95\n" +
            "129 MULT s 97\n" +
            "129 GEQ r 34\n" +
            "129 OR r 34\n" +
            "129 RPARENT r 34\n" +
            "129 LSS r 34\n" +
            "129 PLUS r 34\n" +
            "129 EQL r 34\n" +
            "129 AND r 34\n" +
            "129 GRE r 34\n" +
            "129 MINU r 34\n" +
            "129 NEQ r 34\n" +
            "129 DIV s 96\n" +
            "129 LEQ r 34\n" +
            "130 MOD s 95\n" +
            "130 MULT s 97\n" +
            "130 GEQ r 33\n" +
            "130 OR r 33\n" +
            "130 RPARENT r 33\n" +
            "130 LSS r 33\n" +
            "130 PLUS r 33\n" +
            "130 EQL r 33\n" +
            "130 AND r 33\n" +
            "130 GRE r 33\n" +
            "130 MINU r 33\n" +
            "130 NEQ r 33\n" +
            "130 DIV s 96\n" +
            "130 LEQ r 33\n" +
            "131 GEQ s 92\n" +
            "131 OR r 42\n" +
            "131 RPARENT r 42\n" +
            "131 NEQ r 42\n" +
            "131 LSS s 90\n" +
            "131 EQL r 42\n" +
            "131 AND r 42\n" +
            "131 LEQ s 91\n" +
            "131 GRE s 93\n" +
            "132 GEQ s 92\n" +
            "132 OR r 41\n" +
            "132 RPARENT r 41\n" +
            "132 NEQ r 41\n" +
            "132 LSS s 90\n" +
            "132 EQL r 41\n" +
            "132 AND r 41\n" +
            "132 LEQ s 91\n" +
            "132 GRE s 93\n" +
            "133 INTCON r 15\n" +
            "133 IFTK r 15\n" +
            "133 WHILETK r 15\n" +
            "133 LBRACE r 15\n" +
            "133 PRINTFTK r 15\n" +
            "133 SEMICN r 15\n" +
            "133 IDENFR r 15\n" +
            "133 RBRACE r 15\n" +
            "133 ELSETK s 154\n" +
            "133 INTTK r 15\n" +
            "133 LPARENT r 15\n" +
            "134 INTCON r 3\n" +
            "134 IFTK r 3\n" +
            "134 WHILETK r 3\n" +
            "134 BlockItemList g 155\n" +
            "134 LBRACE r 3\n" +
            "134 PRINTFTK r 3\n" +
            "134 SEMICN r 3\n" +
            "134 IDENFR r 3\n" +
            "134 RBRACE r 3\n" +
            "134 INTTK r 3\n" +
            "134 LPARENT r 3\n" +
            "135 MOD r 24\n" +
            "135 MULT r 24\n" +
            "135 GEQ r 24\n" +
            "135 OR r 24\n" +
            "135 RPARENT r 24\n" +
            "135 LSS r 24\n" +
            "135 PLUS r 24\n" +
            "135 EQL r 24\n" +
            "135 AND r 24\n" +
            "135 GRE r 24\n" +
            "135 MINU r 24\n" +
            "135 NEQ r 24\n" +
            "135 DIV r 24\n" +
            "135 LEQ r 24\n" +
            "136 OR r 46\n" +
            "136 RPARENT r 46\n" +
            "136 AND s 94\n" +
            "137 INTCON r 17\n" +
            "137 IFTK r 17\n" +
            "137 WHILETK r 17\n" +
            "137 LBRACE r 17\n" +
            "137 PRINTFTK r 17\n" +
            "137 SEMICN r 17\n" +
            "137 IDENFR r 17\n" +
            "137 RBRACE r 17\n" +
            "137 INTTK r 17\n" +
            "137 LPARENT r 17\n" +
            "138 INTCON r 19\n" +
            "138 IFTK r 19\n" +
            "138 WHILETK r 19\n" +
            "138 LBRACE r 19\n" +
            "138 PRINTFTK r 19\n" +
            "138 SEMICN r 19\n" +
            "138 IDENFR r 19\n" +
            "138 RBRACE r 19\n" +
            "138 INTTK r 19\n" +
            "138 LPARENT r 19\n" +
            "139 COMMA r 25\n" +
            "139 MOD r 25\n" +
            "139 MULT r 25\n" +
            "139 MINU r 25\n" +
            "139 RPARENT r 25\n" +
            "139 DIV r 25\n" +
            "139 PLUS r 25\n" +
            "140 INTCON s 41\n" +
            "140 MulExp g 40\n" +
            "140 IDENFR s 37\n" +
            "140 LPARENT s 38\n" +
            "140 PrimaryExp g 42\n" +
            "140 Exp g 156\n" +
            "140 AddExp g 43\n" +
            "140 UnaryExp g 39\n" +
            "141 COMMA r 28\n" +
            "141 MOD r 28\n" +
            "141 MULT r 28\n" +
            "141 MINU r 28\n" +
            "141 RPARENT r 28\n" +
            "141 DIV r 28\n" +
            "141 PLUS r 28\n" +
            "142 COMMA r 32\n" +
            "142 MOD s 158\n" +
            "142 MULT s 157\n" +
            "142 MINU r 32\n" +
            "142 RPARENT r 32\n" +
            "142 DIV s 159\n" +
            "142 PLUS r 32\n" +
            "143 COMMA r 26\n" +
            "143 MOD r 26\n" +
            "143 MULT r 26\n" +
            "143 MINU r 26\n" +
            "143 RPARENT r 26\n" +
            "143 DIV r 26\n" +
            "143 PLUS r 26\n" +
            "144 COMMA r 27\n" +
            "144 MOD r 27\n" +
            "144 MULT r 27\n" +
            "144 MINU r 27\n" +
            "144 RPARENT r 27\n" +
            "144 DIV r 27\n" +
            "144 PLUS r 27\n" +
            "145 COMMA r 22\n" +
            "145 MINU s 161\n" +
            "145 RPARENT r 22\n" +
            "145 PLUS s 160\n" +
            "146 COMMA r 20\n" +
            "146 RPARENT r 20\n" +
            "147 INTCON r 18\n" +
            "147 IFTK r 18\n" +
            "147 WHILETK r 18\n" +
            "147 LBRACE r 18\n" +
            "147 PRINTFTK r 18\n" +
            "147 SEMICN r 18\n" +
            "147 IDENFR r 18\n" +
            "147 RBRACE r 18\n" +
            "147 INTTK r 18\n" +
            "147 LPARENT r 18\n" +
            "148 COMMA r 31\n" +
            "148 MOD r 31\n" +
            "148 MULT r 31\n" +
            "148 MINU r 31\n" +
            "148 SEMICN r 31\n" +
            "148 DIV r 31\n" +
            "148 PLUS r 31\n" +
            "149 COMMA r 29\n" +
            "149 MOD r 29\n" +
            "149 MULT r 29\n" +
            "149 MINU r 29\n" +
            "149 SEMICN r 29\n" +
            "149 DIV r 29\n" +
            "149 PLUS r 29\n" +
            "150 COMMA r 30\n" +
            "150 MOD r 30\n" +
            "150 MULT r 30\n" +
            "150 MINU r 30\n" +
            "150 SEMICN r 30\n" +
            "150 DIV r 30\n" +
            "150 PLUS r 30\n" +
            "151 COMMA r 33\n" +
            "151 MOD s 115\n" +
            "151 MULT s 116\n" +
            "151 MINU r 33\n" +
            "151 SEMICN r 33\n" +
            "151 DIV s 117\n" +
            "151 PLUS r 33\n" +
            "152 COMMA r 34\n" +
            "152 MOD s 115\n" +
            "152 MULT s 116\n" +
            "152 MINU r 34\n" +
            "152 SEMICN r 34\n" +
            "152 DIV s 117\n" +
            "152 PLUS r 34\n" +
            "153 COMMA r 24\n" +
            "153 MOD r 24\n" +
            "153 MULT r 24\n" +
            "153 MINU r 24\n" +
            "153 SEMICN r 24\n" +
            "153 DIV r 24\n" +
            "153 PLUS r 24\n" +
            "154 LBRACE s 11\n" +
            "154 Block g 162\n" +
            "155 WHILETK s 21\n" +
            "155 MulExp g 6\n" +
            "155 LBRACE s 11\n" +
            "155 PRINTFTK s 7\n" +
            "155 SEMICN s 4\n" +
            "155 Stmt g 13\n" +
            "155 IDENFR s 9\n" +
            "155 RBRACE s 163\n" +
            "155 INTTK s 16\n" +
            "155 BlockItem g 14\n" +
            "155 Block g 8\n" +
            "155 LPARENT s 19\n" +
            "155 UnaryExp g 10\n" +
            "155 INTCON s 18\n" +
            "155 IFTK s 17\n" +
            "155 VarDecl g 5\n" +
            "155 PrimaryExp g 20\n" +
            "155 Exp g 15\n" +
            "155 AddExp g 12\n" +
            "156 RPARENT s 164\n" +
            "157 INTCON s 143\n" +
            "157 IDENFR s 139\n" +
            "157 LPARENT s 140\n" +
            "157 PrimaryExp g 144\n" +
            "157 UnaryExp g 165\n" +
            "158 INTCON s 143\n" +
            "158 IDENFR s 139\n" +
            "158 LPARENT s 140\n" +
            "158 PrimaryExp g 144\n" +
            "158 UnaryExp g 166\n" +
            "159 INTCON s 143\n" +
            "159 IDENFR s 139\n" +
            "159 LPARENT s 140\n" +
            "159 PrimaryExp g 144\n" +
            "159 UnaryExp g 167\n" +
            "160 INTCON s 143\n" +
            "160 MulExp g 168\n" +
            "160 IDENFR s 139\n" +
            "160 LPARENT s 140\n" +
            "160 PrimaryExp g 144\n" +
            "160 UnaryExp g 141\n" +
            "161 INTCON s 143\n" +
            "161 MulExp g 169\n" +
            "161 IDENFR s 139\n" +
            "161 LPARENT s 140\n" +
            "161 PrimaryExp g 144\n" +
            "161 UnaryExp g 141\n" +
            "162 INTCON r 16\n" +
            "162 IFTK r 16\n" +
            "162 WHILETK r 16\n" +
            "162 LBRACE r 16\n" +
            "162 PRINTFTK r 16\n" +
            "162 SEMICN r 16\n" +
            "162 IDENFR r 16\n" +
            "162 RBRACE r 16\n" +
            "162 INTTK r 16\n" +
            "162 LPARENT r 16\n" +
            "163 INTCON r 1\n" +
            "163 IFTK r 1\n" +
            "163 WHILETK r 1\n" +
            "163 LBRACE r 1\n" +
            "163 PRINTFTK r 1\n" +
            "163 SEMICN r 1\n" +
            "163 IDENFR r 1\n" +
            "163 RBRACE r 1\n" +
            "163 ELSETK r 1\n" +
            "163 INTTK r 1\n" +
            "163 LPARENT r 1\n" +
            "164 COMMA r 24\n" +
            "164 MOD r 24\n" +
            "164 MULT r 24\n" +
            "164 MINU r 24\n" +
            "164 RPARENT r 24\n" +
            "164 DIV r 24\n" +
            "164 PLUS r 24\n" +
            "165 COMMA r 29\n" +
            "165 MOD r 29\n" +
            "165 MULT r 29\n" +
            "165 MINU r 29\n" +
            "165 RPARENT r 29\n" +
            "165 DIV r 29\n" +
            "165 PLUS r 29\n" +
            "166 COMMA r 31\n" +
            "166 MOD r 31\n" +
            "166 MULT r 31\n" +
            "166 MINU r 31\n" +
            "166 RPARENT r 31\n" +
            "166 DIV r 31\n" +
            "166 PLUS r 31\n" +
            "167 COMMA r 30\n" +
            "167 MOD r 30\n" +
            "167 MULT r 30\n" +
            "167 MINU r 30\n" +
            "167 RPARENT r 30\n" +
            "167 DIV r 30\n" +
            "167 PLUS r 30\n" +
            "168 COMMA r 33\n" +
            "168 MOD s 158\n" +
            "168 MULT s 157\n" +
            "168 MINU r 33\n" +
            "168 RPARENT r 33\n" +
            "168 DIV s 159\n" +
            "168 PLUS r 33\n" +
            "169 COMMA r 34\n" +
            "169 MOD s 158\n" +
            "169 MULT s 157\n" +
            "169 MINU r 34\n" +
            "169 RPARENT r 34\n" +
            "169 DIV s 159\n" +
            "169 PLUS r 34\n" +
            "EOF";

    public String toString() {
        if (table == null) return null;
        StringBuffer buf = new StringBuffer();
        for (int row : table.keySet()) {
            HashMap<SymbolType, LRTableEntry> columns = table.get(row);
            for (SymbolType symbol : columns.keySet()) {
                buf.append("[" + row + "," + symbol + "]=" + columns.get(symbol));
                buf.append("\n");
            }
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        LRTable lrTable = new LRTable();
//        String currentDirectory = System.getProperty("user.dir");
//        System.out.println("Current working directory: " + currentDirectory);
        lrTable.init_B();
        System.out.println(lrTable);
    }
}

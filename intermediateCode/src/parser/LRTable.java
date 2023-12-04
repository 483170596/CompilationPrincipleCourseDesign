package parser;

import grammar.*;
import lexer.Token;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        this.grammar = new CFGBlock();
        grammar.init();
        int row, state;
        char action;
        SymbolType column;
        // 使用 try-with-resources 语句确保在操作完成后自动关闭资源
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
        }
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

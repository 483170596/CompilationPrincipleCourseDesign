package parser;

import grammar.CFGBlock;
import grammar.Production;
import grammar.SymbolType;
import lexer.Token;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private static Parser instance;                     //单实例

    private final CFGBlock grammar = new CFGBlock();

    private final ArrayList<SymbolType> string = new ArrayList<>();     //输入串

    private final LRTable lrTable = new LRTable();            //状态分析表

    private final Stack<SymbolType> character = new Stack<>();    //符号栈

    private final Stack<Integer> state = new Stack<>();        //状态栈

    public final StringBuffer buffer = new StringBuffer();

    /**
     * 获取
     * @return instance
     */
    public static Parser getInstance() {
        if (instance == null) {
            instance = new Parser();
        }
        return instance;
    }

    /**
     * 设置
     * @param instance
     */
    public static void setInstance(Parser instance) {
        Parser.instance = instance;
    }


    public void analysis(ArrayList<Token> tokens) {
        lrTable.init_B();
        grammar.init();
        state.push(0);
        character.push(SymbolType.EOF);

        //初始化输入串
        for (Token each : tokens)
            string.add(each.getType());
        string.add(SymbolType.EOF);

        LRTableEntry entry;
        int curPos = 0;
        while (true) {
            SymbolType str = string.get(curPos);
            entry = lrTable.get(state.peek(), str);
            System.out.print("\n状态栈: " + state + "\n符号栈" + character + "\n[输入串] " + str + "\t[操作] " + entry);

            if (entry.getAction() == 's') {
                shift(entry.getState(), str);
                buffer.append(tokens.get(curPos)).append("\n");
                curPos++;

            } else if (entry.getAction() == 'r') {
                reduce(entry.getState());

            } else {
                buffer.append(grammar.getStartSymbol()).append("\n"); //偷懒写法
//                System.out.println(buffer);
                break;
            }
        }
    }

    private void shift(int state, SymbolType str) {
        this.state.push(state);
        this.character.push(str);
        System.out.println();
    }

    private void reduce(int number) {
        Production production = this.grammar.getProduction(number);
        System.out.println("\t[产生式] " + production);
        ArrayList<SymbolType> right = production.getRight();
        SymbolType left = production.getLeft();

        if (right.size() == 1 && right.get(0) == SymbolType.EPSILON) {
            character.push(left);
            int gotoState = lrTable.get(state.peek(), left).getState();
            state.push(gotoState);

            buffer.append(left).append("\n");
            return;
        }

        //符号栈出栈
        for (int i = 0; i < right.size(); i++) {
            character.pop();
            state.pop();
        }
        character.push(left);

        if (state.isEmpty()) {
            state.push(0);
        }

        int gotoState = lrTable.get(state.peek(), left).getState();
        state.push(gotoState);

        buffer.append(left).append("\n");
    }

    public String toString() {
        return "Parser{instance = " + instance + ", grammar = " + grammar + ", string = " + string + ", lrTable = " + lrTable + ", character = " + character + ", state = " + state + ", buffer = " + buffer + "}";
    }
}

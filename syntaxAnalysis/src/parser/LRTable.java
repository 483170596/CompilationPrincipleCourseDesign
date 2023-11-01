package parser;

import java.util.*;

import grammar.CFGBlock;
import grammar.Production;
import grammar.SymbolType;

public class LRTable {
    private HashMap<Integer, HashMap<SymbolType, LRTableEntry>> table = null;

    public LRTable() {
        this.table = new HashMap<Integer, HashMap<SymbolType, LRTableEntry>>();
    }

    /*automatic construct LRTable for CFGBlock Here*/
    public void init() {
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
		*/
        CFGBlock grammar = new CFGBlock();
        grammar.init();

    }


    public void addItem(int row, SymbolType column, LRTableEntry entry) {
        HashMap<SymbolType, LRTableEntry> tmp = null;
        tmp = table.get(row);
        if (tmp == null) tmp = new HashMap<SymbolType, LRTableEntry>();
        tmp.put(column, entry);
        table.put(row, tmp);
    }

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
}

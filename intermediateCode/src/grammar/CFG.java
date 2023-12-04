package grammar;

import java.util.*;

public abstract class CFG {
    private SymbolType startSymbol;
    private ArrayList<Production> productions;

    public CFG() {
        this.startSymbol = null;
        this.productions = new ArrayList<Production>();
    }

    public void setStartSymbol(SymbolType symbol) {
        this.startSymbol = symbol;
    }

    public SymbolType getStartSymbol() {
        return this.startSymbol;
    }

    public ArrayList<Production> getProductions() {
        return this.productions;
    }

    public Production getProduction(int index) {
        return this.productions.get(index);
    }

    public void addProduction(Production p) {
        this.productions.add(p);
    }

    public abstract void init();

    public String toString() {
        if (productions == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < productions.size(); i++) {
            buf.append("[" + i + "]\t" + productions.get(i).toString() + "\n");
        }
        //remove the last character : '\n'
        buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }
}

package grammar;
import java.util.*;

public class Production {
	SymbolType left;
	ArrayList<SymbolType> right;
	public Production(SymbolType left, SymbolType... right) {
		this.left = left;
		this.right = new ArrayList<SymbolType>();
		this.right.addAll(Arrays.asList(right));
	}
	public Production() {
		this.left = null;
		this.right = new ArrayList<SymbolType>();
	}
	public SymbolType getLeft() {
		return this.left;
	}
	public ArrayList<SymbolType> getRight() {
		return this.right;
	}

	public void setLeft(SymbolType left) {
		this.left = left;
	}
	public void setRight(ArrayList<SymbolType> right) {
		this.right = right;
	}
	
	public void addRightSymbol(SymbolType symbol) {
		this.right.add(symbol);
	}
	
	public String toString() {
		String str = this.left + "->";
		for(SymbolType symbol : this.right) {
			str += symbol + " ";
		}
		return str;
	}
	public static void main(String[] args) {
		//test: constructor 1
		Production p1 = new Production(SymbolType.AddExp, SymbolType.AddExp, SymbolType.MINU, SymbolType.MulExp);
		System.out.println(p1.toString());
		//test: constructor 2
		Production p2 = new Production();
		p2.setLeft(SymbolType.AddExp);
		p2.addRightSymbol(SymbolType.AddExp);
		p2.addRightSymbol(SymbolType.PLUS);
		p2.addRightSymbol(SymbolType.MulExp);
		System.out.println(p2.toString());
	}
}

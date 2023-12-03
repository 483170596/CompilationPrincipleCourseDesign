package parser;
public class LRTableEntry {
	private char action;	//'s', shift移进;  'r',reduce规约;	'g',goto;  'a',acc
							//if action = acc, state will be ignored
	private int state;		//state for shift and goto, product No. for reduce
	public LRTableEntry(char action, int state) {
		this.action = action;
		this.state = state;
	}
	public char getAction() {
		return this.action;
	}
	public int getState() {
		return this.state;
	}
	public String toString() {
		if('a' == this.action) return "acc";
		if('g' == this.action) return this.state + "";
		return this.action + "" + this.state;
	}
}

package grammar;

public class CFGA extends CFG{
	public void init() {
		/*
		0)  CompUnit → BlockItemList
		1)  BlockItemList →  BlockItemList Stmt 
		2)  BlockItemList →  EPSILON
		3) Stmt → Ident '=' AddExp ';' 
		4) Stmt → 'printf''('FormatString Ident ')'';' 
		5) AddExp → AddExp '+' MulExp
		6) AddExp → MulExp
		7) MulExp → MulExp '*' PrimaryExp
		8) MulExp → PrimaryExp 
		9) PrimaryExp → '(' AddExp ')'
		10) PrimaryExp → Ident
		11) PrimaryExp → IntConst
		*/
		this.setStartSymbol(SymbolType.CompUnit);
		Production p = null;
		//(0)CompUnit → BlockItemList
		p = new Production(SymbolType.CompUnit, SymbolType.BlockItemList);
		addProduction(p);
		//(1) BlockItemList →  BlockItemList Stmt 
		p = new Production(SymbolType.BlockItemList, SymbolType.BlockItemList, SymbolType.Stmt);
		addProduction(p);
		//(2) BlockItemList →  EPSILON
		p = new Production(SymbolType.BlockItemList, SymbolType.EPSILON);
		addProduction(p);
		//(3) Stmt → Ident '=' AddExp ';' 
		p = new Production(SymbolType.Stmt, SymbolType.IDENFR, SymbolType.ASSIGN, SymbolType.AddExp, SymbolType.SEMICN);
		addProduction(p);
		//(4) Stmt → 'printf''('FormatString ',' Ident ')'';'  
		p = new Production(SymbolType.Stmt, SymbolType.PRINTFTK, SymbolType.LPARENT, SymbolType.STRCON, SymbolType.COMMA, SymbolType.IDENFR, SymbolType.RPARENT, SymbolType.SEMICN);
		addProduction(p);
		//(5) AddExp → AddExp '+' MulExp
		p = new Production(SymbolType.AddExp, SymbolType.AddExp, SymbolType.PLUS, SymbolType.MulExp);
		addProduction(p);
		//(6) AddExp → MulExp
		p = new Production(SymbolType.AddExp, SymbolType.MulExp);
		addProduction(p);
		//(7)MulExp → MulExp * PrimaryExp
		p = new Production(SymbolType.MulExp, SymbolType.MulExp, SymbolType.MULT, SymbolType.PrimaryExp);
		addProduction(p);
		//(8)MulExp → PrimaryExp
		p = new Production(SymbolType.MulExp, SymbolType.PrimaryExp);
		addProduction(p);
		//(9)PrimaryExp → ( AddExp )
		p = new Production(SymbolType.PrimaryExp, SymbolType.LPARENT, SymbolType.AddExp, SymbolType.RPARENT);
		addProduction(p);
		//(10)PrimaryExp → id
		p = new Production(SymbolType.PrimaryExp, SymbolType.IDENFR);
		addProduction(p);
		//(11)PrimaryExp → intConst
		p = new Production(SymbolType.PrimaryExp, SymbolType.INTCON);
		addProduction(p);
	}
	public static void main(String[] args) {
		CFGA grammar = new CFGA();
		grammar.init();
		System.out.println(grammar.toString());
	}
}

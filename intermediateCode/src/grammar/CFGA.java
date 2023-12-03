package grammar;

public class CFGA extends CFG{
	public void init() {
		/*
		0)  CompUnit → BlockItemList
		1)  BlockItemList →  BlockItemList Stmt 
		2)  BlockItemList →  EPSILON
		3) Stmt → Ident '=' AddExp ';'  				{S.code = E.code || gen(id.palce, ‘=’, E.name)}
		4) Stmt → 'printf''('FormatString Ident ')'';' 	{S.code = gen(param FormatString.place) || gen(param Ident.name)}
		5) AddExp → AddExp '+' MulExp					{E.palce= newtemp;	E.code = E1.code || T.code || gen(E.palce‘=’ E1.palce‘+’ T.palce)}
		6) AddExp → MulExp								{E.code = T.code; E.palce= T.palce}
		7) MulExp → MulExp '*' PrimaryExp				{T.palce= newtemp;	T.code = T1.code || F.code || gen(T.palce‘=’ T1.palce‘*’ F.palce)}
		8) MulExp → PrimaryExp							{T.code = F.code; T.palce= F.palce}
		9) PrimaryExp → '(' AddExp ')'					{F.code = E1.code; F.palce= E1.palce}
		10) PrimaryExp → Ident							{F.palce= id.palce; F.code = ‘’}
		11) PrimaryExp → IntConst						{F.place=newtemp; F.code=gen(F.place ‘=’ num.val)}
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

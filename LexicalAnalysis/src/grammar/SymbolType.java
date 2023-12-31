package grammar;

public enum SymbolType {
    // terminals
    IDENFR("IDENFR"),	//identifier
    INTCON("INTCON"),	//int const
    STRCON("STRCON"),	//string const
    MAINTK("MAINTK"),	//main
    CONSTTK("CONSTTK"),	//const
    INTTK("INTTK"),		//int
    BREAKTK("BREAKTK"),	//break
    CONTINUETK("CONTINUETK"),	//continue
    IFTK("IFTK"),		//if
    ELSETK("ELSETK"),	//else
    NOT("NOT"),			//!
    AND("AND"),			//&&
    OR("OR"),			//||
    WHILETK("WHILETK"),	//while
    GETINTTK("GETINTTK"),//getint
    PRINTFTK("PRINTFTK"),//printf
    RETURNTK("RETURNTK"),//return
    PLUS("PLUS"),		//+
    MINU("MINU"),		//-
    VOIDTK("VOIDTK"),	//void
    MULT("MULT"),		//*
    DIV("DIV"),			// /
    MOD("MOD"),			//%
    LSS("LSS"),			//<
    LEQ("LEQ"),			//<=
    GRE("GRE"),			//>
    GEQ("GEQ"),			//>=
    EQL("EQL"),			//==
    NEQ("NEQ"),			//!=
    ASSIGN("ASSIGN"),	//=
    SEMICN("SEMICN"),	//;
    COMMA("COMMA"),		//,
    LPARENT("LPARENT"),	//(
    RPARENT("RPARENT"),	//)
    LBRACK("LBRACK"),	//[
    RBRACK("RBRACK"),	//]
    LBRACE("LBRACE"),	//{
    RBRACE("RBRACE"),	//}
    EOF("EOF"), 		//#, end of file
    NOTE("NOTE"),		//comment
    UNKNOWN("UNKNOWN"), //unkonwn character
    
    
    //epsilon string
    EPSILON("EPSILON","epsilon"),
    
    
    //nonterminals    
    CompUnit("<CompUnit>","NonTerminal"),
    Decl("<Decl>","NonTerminal"),
    ConstDecl("<ConstDecl>","NonTerminal"),
    BType("<BType>","NonTerminal"),
    ConstDef("<ConstDef>","NonTerminal"),
    ConstDefList("<ConstDefList>","NonTerminal"),
    ConstInitVal("<ConstInitVal>","NonTerminal"),
    VarDecl("<VarDecl>","NonTerminal"),
    VarDeclList("<VarDeclList>","NonTerminal"),
    VarDef("<VarDef>","NonTerminal"),
    InitVal("<InitVal>","NonTerminal"),
    FuncDef("<FuncDef>","NonTerminal"),
    MainFuncDef("<MainFuncDef>","NonTerminal"),
    FuncType("<FuncType>","NonTerminal"),
    FuncFParams("<FuncFParams>","NonTerminal"),
    FuncFParam("<FuncFParam>","NonTerminal"),
    Block("<Block>","NonTerminal"),
    BlockItem("<BlockItem>","NonTerminal"),
    BlockItemList("<BlockItemList>","NonTerminal"),
    Stmt("<Stmt>","NonTerminal"),
    OptionalElse("<OptionalElse>","NonTerminal"),
    Exp("<Exp>","NonTerminal"),
    ExpList("<ExpList>","NonTerminal"),
    Cond("<Cond>","NonTerminal"),
    LVal("<LVal>","NonTerminal"),
    PrimaryExp("<PrimaryExp>","NonTerminal"),
    Number("<Number>","NonTerminal"),
    UnaryExp("<UnaryExp>","NonTerminal"),
    UnaryOp("<UnaryOp>","NonTerminal"),
    FuncRParams("<FuncRParams>","NonTerminal"),
    MulExp("<MulExp>","NonTerminal"),
    AddExp("<AddExp>","NonTerminal"),
    RelExp("<RelExp>","NonTerminal"),
    EqExp("<EqExp>","NonTerminal"),
    LAndExp("<LAndExp>","NonTerminal"),
    LOrExp("<LOrExp>","NonTerminal"),
    ConstExp("<ConstExp>","NonTerminal"),
    ;
	
    private String symbol;	//用于输出的符号字符串，例如'<ConstExp>'
    private String type;	//terminal,NonTerminal,epsilon
    
    private SymbolType(String string) {
        this.symbol = string;
        this.type = "terminal";
    }
    private SymbolType(String string, String type) {
        this.symbol = string;
        this.type = type;
    }
    
    public boolean isTerminal() {
    	return this.type.equalsIgnoreCase("terminal");
    }
    
    public boolean isNonTerminal() {
    	return this.type.equalsIgnoreCase("NonTerminal");
    }
    
    public boolean isEpsilon() {
    	return this.type.equalsIgnoreCase("epsilon");
    }
    
    public String toString() {
        return this.symbol;
    }
}

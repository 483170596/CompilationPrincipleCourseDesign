package grammar;

public enum SymbolType {
    // terminals
    IDENFR("IDENFR"),    //Ident
    INTCON("INTCON"),    //IntConst
    STRCON("STRCON"),    //FormatString
    MAINTK("MAINTK"),    //main
    CONSTTK("CONSTTK"),    //const
    INTTK("INTTK"),        //int
    BREAKTK("BREAKTK"),    //break
    CONTINUETK("CONTINUETK"),    //continue
    IFTK("IFTK"),        //if
    ELSETK("ELSETK"),    //else
    NOT("NOT"),            //!
    AND("AND"),            //&&
    OR("OR"),            //||
    WHILETK("WHILETK"),    //while
    GETINTTK("GETINTTK"),//getint
    PRINTFTK("PRINTFTK"),//printf
    RETURNTK("RETURNTK"),//return
    PLUS("PLUS"),        //+
    MINU("MINU"),        //-
    VOIDTK("VOIDTK"),    //void
    MULT("MULT"),        //*
    DIV("DIV"),            ///
    MOD("MOD"),            //%
    LSS("LSS"),            //<
    LEQ("LEQ"),            //<=
    GRE("GRE"),            //>
    GEQ("GEQ"),            //>=
    EQL("EQL"),            //==
    NEQ("NEQ"),            //!=
    ASSIGN("ASSIGN"),    //=
    SEMICN("SEMICN"),    //;
    COMMA("COMMA"),        //,
    LPARENT("LPARENT"),    //(
    RPARENT("RPARENT"),    //)
    LBRACK("LBRACK"),    //[
    RBRACK("RBRACK"),    //]
    LBRACE("LBRACE"),    //{
    RBRACE("RBRACE"),    //}
    EOF("EOF"),        //#


    NOTE("NOTE"),        //comment
    UNKNOWN("UNKNOWN"), //unkonwn character


    //epsilon string
    EPSILON("EPSILON", "epsilon"),


    //nonterminals    
    CompUnit("<CompUnit>", "NonTerminal"),
    Decl("<Decl>", "NonTerminal"),
    ConstDecl("<ConstDecl>", "NonTerminal"),
    BType("<BType>", "NonTerminal"),
    ConstDef("<ConstDef>", "NonTerminal"),
    ConstDefList("<ConstDefList>", "NonTerminal"),
    ConstInitVal("<ConstInitVal>", "NonTerminal"),
    VarDecl("<VarDecl>", "NonTerminal"),
    VarDeclList("<VarDeclList>", "NonTerminal"),
    VarDef("<VarDef>", "NonTerminal"),
    InitVal("<InitVal>", "NonTerminal"),
    FuncDef("<FuncDef>", "NonTerminal"),
    MainFuncDef("<MainFuncDef>", "NonTerminal"),
    FuncType("<FuncType>", "NonTerminal"),
    FuncFParams("<FuncFParams>", "NonTerminal"),
    FuncFParam("<FuncFParam>", "NonTerminal"),
    Block("<Block>", "NonTerminal"),
    BlockItem("<BlockItem>", "NonTerminal"),
    BlockItemList("<BlockItemList>", "NonTerminal"),
    Stmt("<Stmt>", "NonTerminal"),
    OptionalElse("<OptionalElse>", "NonTerminal"),
    Exp("<Exp>", "NonTerminal"),
    ExpList("<ExpList>", "NonTerminal"),
    Cond("<Cond>", "NonTerminal"),
    LVal("<LVal>", "NonTerminal"),
    PrimaryExp("<PrimaryExp>", "NonTerminal"),
    Number("<Number>", "NonTerminal"),
    UnaryExp("<UnaryExp>", "NonTerminal"),
    UnaryOp("<UnaryOp>", "NonTerminal"),
    FuncRParams("<FuncRParams>", "NonTerminal"),
    MulExp("<MulExp>", "NonTerminal"),
    AddExp("<AddExp>", "NonTerminal"),
    RelExp("<RelExp>", "NonTerminal"),
    EqExp("<EqExp>", "NonTerminal"),
    LAndExp("<LAndExp>", "NonTerminal"),
    LOrExp("<LOrExp>", "NonTerminal"),
    ConstExp("<ConstExp>", "NonTerminal"),

    //test
    E_("<E'>", "NonTerminal"),
    E("<E>", "NonTerminal"),
    T("<T>", "NonTerminal"),
    F("<F>", "NonTerminal"),
    LEFT("("),    //(
    RIGHT(")"),    //)
    ADD("+"),
    MUL("*"),

    //test1
    S_("S'", "NonTerminal"),
    S("S", "NonTerminal"),
    A("A", "NonTerminal"),
    a("a"),
    b("b"),
    c("c"),
    d("d"),
    e("e"),


    //acc
    ACC("ACC", "acc"),
    //规约
    REDUCE("REDUCE", "reduce"),
    //action，终
    SHIFT("SHIFT", "shift"),
    //goto 非
    GOTO("GOTO", "goto"),
    ;

    private String symbol;    //the symbol string for output, such as '<ConstExp>'
    private String type;    //终结符terminal,非终结符NonTerminal,空epsilon

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

    public boolean isACC() {
        return this.type.equalsIgnoreCase("acc");
    }

    public boolean isREDUCE() {
        return this.type.equalsIgnoreCase("reduce");
    }

    public boolean isSHIFT() {
        return this.type.equalsIgnoreCase("shift");
    }

    public boolean isGOTO() {
        return this.type.equalsIgnoreCase("goto");
    }

    public String toString() {
        return this.symbol;
    }
}

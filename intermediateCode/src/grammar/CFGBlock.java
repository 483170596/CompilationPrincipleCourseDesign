package grammar;

public class CFGBlock extends CFG {

    /*
    0)  CompUnit → Block
    1)  Block → '{' BlockItemList '}'
    2)  BlockItemList →  BlockItemList BlockItem
    3)  BlockItemList →  EPSILON
    4)  BlockItem → VarDecl
    5)  BlockItem → Stmt
    6)  VarDecl → 'int' VarDeclList ';'
    7)  VarDeclList → VarDeclList  ',' VarDef
    8)  VarDeclList → VarDef
    9)  VarDef → Ident
    10) VarDef → Ident '=' Exp
    11) Stmt → Ident '=' Exp ';'
    12) Stmt → Exp ';'
    13) Stmt → ';'
    14) Stmt → Block
    15) Stmt → 'if' '(' Cond ')' Block
    16) Stmt → 'if' '(' Cond ')' Block 'else' Block
    17) Stmt → 'while' '(' Cond ')' Block
    18) Stmt → Ident '=' 'getint''('')'';'
    19) Stmt → 'printf''('FormatString ExpList ')'';'
    20) ExpList → ExpList ',' Exp
    21) ExpList → EPSILON
    22) Exp → AddExp
    23) Cond → LOrExp
    24) PrimaryExp → '(' Exp ')'
    25) PrimaryExp → Ident
    26) PrimaryExp → IntConst
    27) UnaryExp → PrimaryExp
    28) MulExp → UnaryExp
    29) MulExp → MulExp '*' UnaryExp
    30) MulExp → MulExp '/' UnaryExp
    31) MulExp → MulExp '%' UnaryExp
    32) AddExp → MulExp
    33) AddExp → AddExp '+' MulExp
    34) AddExp → AddExp '-' MulExp
    35) RelExp → AddExp
    36) RelExp → RelExp '<' AddExp
    37) RelExp → RelExp '>' AddExp
    38) RelExp → RelExp '<=' AddExp
    39) RelExp → RelExp '>=' AddExp
    40) EqExp → RelExp
    41) EqExp → EqExp '==' RelExp
    42) EqExp → EqExp '!=' RelExp
    43) LAndExp → EqExp
    44) LAndExp → LAndExp '&&' EqExp
    45) LOrExp → LAndExp
    46) LOrExp → LOrExp '||' LAndExp
     */
    public void init() {
        this.setStartSymbol(SymbolType.CompUnit);
        Production p = null;

        //0) CompUnit → Block
        p = new Production(SymbolType.CompUnit, SymbolType.Block);
        addProduction(p);
        //1)  Block → '{' BlockItemList '}'
        p = new Production(SymbolType.Block, SymbolType.LBRACE, SymbolType.BlockItemList, SymbolType.RBRACE);
        addProduction(p);
        //2)  BlockItemList →  BlockItemList BlockItem
        p = new Production(SymbolType.BlockItemList, SymbolType.BlockItemList, SymbolType.BlockItem);
        addProduction(p);
        //3)  BlockItemList →  EPSILON
        p = new Production(SymbolType.BlockItemList, SymbolType.EPSILON);
        addProduction(p);
        //4)  BlockItem → VarDecl
        p = new Production(SymbolType.BlockItem, SymbolType.VarDecl);
        addProduction(p);
        //5)  BlockItem → Stmt
        p = new Production(SymbolType.BlockItem, SymbolType.Stmt);
        addProduction(p);
        //6)  VarDecl → 'int' VarDeclList ';'
        p = new Production(SymbolType.VarDecl, SymbolType.INTTK, SymbolType.VarDeclList, SymbolType.SEMICN);
        addProduction(p);
        //7)  VarDeclList → VarDeclList  ',' VarDef
        p = new Production(SymbolType.VarDeclList, SymbolType.VarDeclList, SymbolType.COMMA, SymbolType.VarDef);
        addProduction(p);
        //8)  VarDeclList → VarDef
        p = new Production(SymbolType.VarDeclList, SymbolType.VarDef);
        addProduction(p);
        //9)  VarDef → Ident
        p = new Production(SymbolType.VarDef, SymbolType.IDENFR);
        addProduction(p);

        //10) VarDef → Ident '=' Exp
        p = new Production(SymbolType.VarDef, SymbolType.IDENFR, SymbolType.ASSIGN, SymbolType.Exp);
        addProduction(p);
        //11) Stmt → Ident '=' Exp ';'
        p = new Production(SymbolType.Stmt, SymbolType.IDENFR, SymbolType.ASSIGN, SymbolType.Exp, SymbolType.SEMICN);
        addProduction(p);
        //12) Stmt → Exp ';'
        p = new Production(SymbolType.Stmt, SymbolType.Exp, SymbolType.SEMICN);
        addProduction(p);
        //13) Stmt → ';'
        p = new Production(SymbolType.Stmt, SymbolType.SEMICN);
        addProduction(p);
        //14) Stmt → Block
        p = new Production(SymbolType.Stmt, SymbolType.Block);
        addProduction(p);
        //15) Stmt → 'if' '(' Cond ')' Block
        p = new Production(SymbolType.Stmt, SymbolType.IFTK, SymbolType.LPARENT, SymbolType.Cond, SymbolType.RPARENT, SymbolType.Block);
        addProduction(p);
        //16) Stmt → 'if' '(' Cond ')' Block 'else' Block
        p = new Production(SymbolType.Stmt, SymbolType.IFTK, SymbolType.LPARENT, SymbolType.Cond, SymbolType.RPARENT, SymbolType.Block, SymbolType.ELSETK, SymbolType.Block);
        addProduction(p);
        //17) Stmt → 'while' '(' Cond ')' Stmt
        p = new Production(SymbolType.Stmt, SymbolType.WHILETK, SymbolType.LPARENT, SymbolType.Cond, SymbolType.RPARENT, SymbolType.Stmt);
        addProduction(p);
        //18) Stmt → Ident '=' 'getint''('')'';'
        p = new Production(SymbolType.Stmt, SymbolType.IDENFR, SymbolType.ASSIGN, SymbolType.GETINTTK, SymbolType.LPARENT, SymbolType.RPARENT, SymbolType.SEMICN);
        addProduction(p);
        //19) Stmt → 'printf''('FormatString ExpList ')'';'
        p = new Production(SymbolType.Stmt, SymbolType.PRINTFTK, SymbolType.LPARENT, SymbolType.STRCON, SymbolType.ExpList, SymbolType.RPARENT, SymbolType.SEMICN);
        addProduction(p);

        //20) ExpList → ExpList ',' Exp
        p = new Production(SymbolType.ExpList, SymbolType.ExpList, SymbolType.COMMA, SymbolType.Exp);
        addProduction(p);
        //21) ExpList → EPSILON
        p = new Production(SymbolType.ExpList, SymbolType.EPSILON);
        addProduction(p);
        //22) Exp → AddExp
        p = new Production(SymbolType.Exp, SymbolType.AddExp);
        addProduction(p);

        //23) Cond → LOrExp
        /* Cond→Or		{Cond.truelist=Or.truelist;Cond.falselist=Or.falselist;}*/
        p = new Production(SymbolType.Cond, SymbolType.LOrExp);
        addProduction(p);

        //24) PrimaryExp → '(' Exp ')'
        p = new Production(SymbolType.PrimaryExp, SymbolType.LPARENT, SymbolType.Exp, SymbolType.RPARENT);
        addProduction(p);
        //25) PrimaryExp → Ident
        p = new Production(SymbolType.PrimaryExp, SymbolType.IDENFR);
        addProduction(p);
        //26) PrimaryExp → IntConst
        p = new Production(SymbolType.PrimaryExp, SymbolType.INTCON);
        addProduction(p);
        //27) UnaryExp → PrimaryExp
        p = new Production(SymbolType.UnaryExp, SymbolType.PrimaryExp);
        addProduction(p);
        //28) MulExp → UnaryExp
        p = new Production(SymbolType.MulExp, SymbolType.UnaryExp);
        addProduction(p);
        //29) MulExp → MulExp '*' UnaryExp
        p = new Production(SymbolType.MulExp, SymbolType.MulExp, SymbolType.MULT, SymbolType.UnaryExp);
        addProduction(p);

        //30) MulExp → MulExp '/' UnaryExp
        p = new Production(SymbolType.MulExp, SymbolType.MulExp, SymbolType.DIV, SymbolType.UnaryExp);
        addProduction(p);
        //31) MulExp → MulExp '%' UnaryExp
        p = new Production(SymbolType.MulExp, SymbolType.MulExp, SymbolType.MOD, SymbolType.UnaryExp);
        addProduction(p);
        //32) AddExp → MulExp
        p = new Production(SymbolType.AddExp, SymbolType.MulExp);
        addProduction(p);
        //33) AddExp → AddExp '+' MulExp
        p = new Production(SymbolType.AddExp, SymbolType.AddExp, SymbolType.PLUS, SymbolType.MulExp);
        addProduction(p);
        //34) AddExp → AddExp '-' MulExp
        p = new Production(SymbolType.AddExp, SymbolType.AddExp, SymbolType.MINU, SymbolType.MulExp);
        addProduction(p);

        //35) RelExp → AddExp
        /*Rel→E	{Rel.place=E.place;}*/
        p = new Production(SymbolType.RelExp, SymbolType.AddExp);
        addProduction(p);

        //36) RelExp → RelExp '<' AddExp
        /*Rel→Rel1 < E
        {Rel.truelist=makelist(nextstmt);
        Rel.falselist=makelist(nextstmt+1);
        gen(‘if’ Rel1.place < E.place ‘goto -’);
        gen(‘goto -’);}*/
        p = new Production(SymbolType.RelExp, SymbolType.RelExp, SymbolType.LSS, SymbolType.AddExp);
        addProduction(p);
        //37) RelExp → RelExp '>' AddExp
        /*Rel→Rel1 > E
        {Rel.truelist=makelist(nextstmt);
        Rel.falselist=makelist(nextstmt+1);
        gen(‘if’ Rel1.place > E.place ‘goto -’);
        gen(‘goto -’);}*/
        p = new Production(SymbolType.RelExp, SymbolType.RelExp, SymbolType.GRE, SymbolType.AddExp);
        addProduction(p);
        //38) RelExp → RelExp '<=' AddExp
        /*Rel→Rel1 <= E
        {Rel.truelist=makelist(nextstmt);
        Rel.falselist=makelist(nextstmt+1);
        gen(‘if’ Rel1.place <= E.place ‘goto -’);
        gen(‘goto -’);}*/
        p = new Production(SymbolType.RelExp, SymbolType.RelExp, SymbolType.LEQ, SymbolType.AddExp);
        addProduction(p);
        //39) RelExp → RelExp '>=' AddExp
        /*Rel→Rel1 >= E
        {Rel.truelist=makelist(nextstmt);
        Rel.falselist=makelist(nextstmt+1);
        gen(‘if’ Rel1.place >= E.place ‘goto -’);
        gen(‘goto -’);}*/
        p = new Production(SymbolType.RelExp, SymbolType.RelExp, SymbolType.GEQ, SymbolType.AddExp);
        addProduction(p);

        //40) EqExp → RelExp
        /*Eq→Rel
        {Eq.truelist=Rel.truelist;
        Eq.falselist=Rel.falselist;
        Eq.place = Rel.place;}*/
        p = new Production(SymbolType.EqExp, SymbolType.RelExp);
        addProduction(p);

        //41) EqExp → EqExp '==' RelExp
        /*Eq→Eq1 == Rel
        {Eq.truelist=makelist(nextstmt);
        Eq.falselist=makelist(nextstmt+1);
        gen(‘if’ Eq1.place == Rel.place ‘goto -’);
        gen(‘goto -’);}*/
        p = new Production(SymbolType.EqExp, SymbolType.EqExp, SymbolType.EQL, SymbolType.RelExp);
        addProduction(p);

        //42) EqExp → EqExp '!=' RelExp
        /*Rel→Rel1 != E
        {Rel.truelist=makelist(nextstmt);
        Rel.falselist=makelist(nextstmt+1);
        gen(‘if’ Rel1.place != E.place ‘goto -’);
        gen(‘goto -’);}*/
        p = new Production(SymbolType.EqExp, SymbolType.EqExp, SymbolType.NEQ, SymbolType.RelExp);
        addProduction(p);

        //43) LAndExp → EqExp
        /*And→Eq
        {And.truelist=Eq.truelist;
        And.falselist=Eq.falselist;}*/
        p = new Production(SymbolType.LAndExp, SymbolType.EqExp);
        addProduction(p);

        //44) LAndExp → LAndExp '&&' EqExp
        /*
        * And→And1 && M Eq
          {backpatch(And1.truelist, M.gotostmt);
          And.truelist=Eq.truelist;
          And.falselist=merge(And1.falselist,Eq.falselist);}*/
        p = new Production(SymbolType.LAndExp, SymbolType.LAndExp, SymbolType.AND, SymbolType.EqExp);
        addProduction(p);

        //45) LOrExp → LAndExp
        /*Or→And
        {Or.truelist=And.truelist;
        Or.falselist=And.falselist;}
        */
        p = new Production(SymbolType.LOrExp, SymbolType.LAndExp);
        addProduction(p);

        //46) LOrExp → LOrExp '||' LAndExp
        /* Or→Or1 ‘||’  M And
        {backpatch(Or1.falselist, M.gotostmt);
        Or.truelist=merge(Or1.truelist,And.truelist);
        Or.falselist=And.falselist;}
        */
        p = new Production(SymbolType.LOrExp, SymbolType.LOrExp, SymbolType.OR, SymbolType.LAndExp);
        addProduction(p);
    }

    public void initTest() {
        this.setStartSymbol(SymbolType.CompUnit);
        Production p = null;

        //0) E'->E
        p = new Production(SymbolType.E_, SymbolType.E);
        addProduction(p);

        //2) E->E+T
        p = new Production(SymbolType.E, SymbolType.E, SymbolType.ADD, SymbolType.T);
        addProduction(p);

        //3)E->T
        p = new Production(SymbolType.E, SymbolType.T);
        addProduction(p);

        //4)T->TF
        p = new Production(SymbolType.T, SymbolType.T, SymbolType.F);
        addProduction(p);

        //5) T->F
        p = new Production(SymbolType.T, SymbolType.F);
        addProduction(p);

        //6) F->F*
        p = new Production(SymbolType.F, SymbolType.F, SymbolType.MUL);
        addProduction(p);

        //7) F->(E)
        p = new Production(SymbolType.F, SymbolType.LEFT, SymbolType.E, SymbolType.RIGHT);
        addProduction(p);

        //8)F->a
        p = new Production(SymbolType.F, SymbolType.a);
        addProduction(p);

        //9)F->b
        p = new Production(SymbolType.F, SymbolType.b);
        addProduction(p);
    }

    public void initTest1() {
        this.setStartSymbol(SymbolType.CompUnit);
        Production p = null;

        //0) S'->S
        p = new Production(SymbolType.S_, SymbolType.S);
        addProduction(p);

        //1)S->aAd
        p = new Production(SymbolType.S, SymbolType.a, SymbolType.A, SymbolType.d);
        addProduction(p);

        //2)S->bAc
        p = new Production(SymbolType.S, SymbolType.b, SymbolType.A, SymbolType.c);
        addProduction(p);

        //3)S->aec
        p = new Production(SymbolType.S, SymbolType.a, SymbolType.e, SymbolType.c);
        addProduction(p);

        //4)S->bed
        p = new Production(SymbolType.S, SymbolType.b, SymbolType.e, SymbolType.d);
        addProduction(p);

        //A->e
        p = new Production(SymbolType.A, SymbolType.e);
        addProduction(p);
    }

    public static void main(String[] args) {

        // TODO 生成该文法
        CFGBlock grammar = new CFGBlock();
        grammar.init();
        System.out.println(grammar.toString());

    }
}

package parser;

import grammar.*;
import lexer.Token;

import java.io.*;
import java.util.*;

public class LRTable {
    private HashMap<Integer, HashMap<SymbolType, LRTableEntry>> table;

    private LR1Automata lr1Automata = new LR1Automata();

    private CFG grammar;

    public LRTable() {
        this.table = new HashMap<>();
    }

    /*automatic construct LRTable for CFGBlock Here*/
    private void init() {
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
		'r',
		*/
//        LR1Automata lr1Automata = new LR1Automata();
        lr1Automata.Init();
        this.grammar = lr1Automata.grammar;
        // TODO 由自动机lr1Automata求LR(1)分析表
        for (Map.Entry<Integer, LR1Automata.State> stateEntry : lr1Automata.getAutomata().entrySet()) {
            for (Map.Entry<SymbolType, HashMap<SymbolType, Integer>> goEntry : stateEntry.getValue().go.entrySet()) {
                for (Map.Entry<SymbolType, Integer> entry : goEntry.getValue().entrySet()) {
                    char c = ' ';
                    if (goEntry.getKey().isACC()) {
                        c = 'a';
                    } else if (goEntry.getKey().isREDUCE()) {
                        c = 'r';
                    } else if (goEntry.getKey().isGOTO()) {
                        c = 'g';
                    } else if (goEntry.getKey().isSHIFT()) {
                        c = 's';
                    }
                    addItem(stateEntry.getKey(), entry.getKey(), new LRTableEntry(c, entry.getValue()));
                }
            }
        }
    }

    public void init_B() {
//        // 获取当前工作目录
//        String workingDirectory = System.getProperty("user.dir");
//        // 打印当前工作目录
//        System.out.println("当前工作目录：" + workingDirectory);

        this.grammar = new CFGBlock();
        grammar.init();
        int row, state;
        char action;
        SymbolType column;

        String tableString = """
                0 LBRACE s 1
                0 Block g 2
                1 INTCON r 3
                1 IFTK r 3
                1 WHILETK r 3
                1 BlockItemList g 3
                1 LBRACE r 3
                1 PRINTFTK r 3
                1 SEMICN r 3
                1 IDENFR r 3
                1 RBRACE r 3
                1 INTTK r 3
                1 LPARENT r 3
                2 EOF a 0
                3 WHILETK s 21
                3 MulExp g 6
                3 LBRACE s 11
                3 PRINTFTK s 7
                3 SEMICN s 4
                3 Stmt g 13
                3 IDENFR s 9
                3 RBRACE s 22
                3 INTTK s 16
                3 BlockItem g 14
                3 Block g 8
                3 LPARENT s 19
                3 UnaryExp g 10
                3 INTCON s 18
                3 IFTK s 17
                3 VarDecl g 5
                3 PrimaryExp g 20
                3 Exp g 15
                3 AddExp g 12
                4 INTCON r 13
                4 IFTK r 13
                4 WHILETK r 13
                4 LBRACE r 13
                4 PRINTFTK r 13
                4 SEMICN r 13
                4 IDENFR r 13
                4 RBRACE r 13
                4 INTTK r 13
                4 LPARENT r 13
                5 INTCON r 4
                5 IFTK r 4
                5 WHILETK r 4
                5 LBRACE r 4
                5 PRINTFTK r 4
                5 SEMICN r 4
                5 IDENFR r 4
                5 RBRACE r 4
                5 INTTK r 4
                5 LPARENT r 4
                6 MOD s 24
                6 MULT s 25
                6 MINU r 32
                6 SEMICN r 32
                6 DIV s 23
                6 PLUS r 32
                7 LPARENT s 26
                8 INTCON r 14
                8 IFTK r 14
                8 WHILETK r 14
                8 LBRACE r 14
                8 PRINTFTK r 14
                8 SEMICN r 14
                8 IDENFR r 14
                8 RBRACE r 14
                8 INTTK r 14
                8 LPARENT r 14
                9 MOD r 25
                9 MULT r 25
                9 MINU r 25
                9 SEMICN r 25
                9 DIV r 25
                9 PLUS r 25
                9 ASSIGN s 27
                10 MOD r 28
                10 MULT r 28
                10 MINU r 28
                10 SEMICN r 28
                10 DIV r 28
                10 PLUS r 28
                11 INTCON r 3
                11 IFTK r 3
                11 WHILETK r 3
                11 BlockItemList g 28
                11 LBRACE r 3
                11 PRINTFTK r 3
                11 SEMICN r 3
                11 IDENFR r 3
                11 RBRACE r 3
                11 INTTK r 3
                11 LPARENT r 3
                12 MINU s 30
                12 SEMICN r 22
                12 PLUS s 29
                13 INTCON r 5
                13 IFTK r 5
                13 WHILETK r 5
                13 LBRACE r 5
                13 PRINTFTK r 5
                13 SEMICN r 5
                13 IDENFR r 5
                13 RBRACE r 5
                13 INTTK r 5
                13 LPARENT r 5
                14 INTCON r 2
                14 IFTK r 2
                14 WHILETK r 2
                14 LBRACE r 2
                14 PRINTFTK r 2
                14 SEMICN r 2
                14 IDENFR r 2
                14 RBRACE r 2
                14 INTTK r 2
                14 LPARENT r 2
                15 SEMICN s 31
                16 VarDef g 32
                16 IDENFR s 34
                16 VarDeclList g 33
                17 LPARENT s 35
                18 MOD r 26
                18 MULT r 26
                18 MINU r 26
                18 SEMICN r 26
                18 DIV r 26
                18 PLUS r 26
                19 INTCON s 41
                19 MulExp g 40
                19 IDENFR s 37
                19 LPARENT s 38
                19 PrimaryExp g 42
                19 Exp g 36
                19 AddExp g 43
                19 UnaryExp g 39
                20 MOD r 27
                20 MULT r 27
                20 MINU r 27
                20 SEMICN r 27
                20 DIV r 27
                20 PLUS r 27
                21 LPARENT s 44
                22 EOF r 1
                23 INTCON s 18
                23 IDENFR s 46
                23 PrimaryExp g 20
                23 LPARENT s 19
                23 UnaryExp g 45
                24 INTCON s 18
                24 IDENFR s 46
                24 PrimaryExp g 20
                24 LPARENT s 19
                24 UnaryExp g 47
                25 INTCON s 18
                25 IDENFR s 46
                25 PrimaryExp g 20
                25 LPARENT s 19
                25 UnaryExp g 48
                26 STRCON s 49
                27 INTCON s 18
                27 MulExp g 6
                27 IDENFR s 46
                27 GETINTTK s 51
                27 LPARENT s 19
                27 PrimaryExp g 20
                27 Exp g 50
                27 AddExp g 12
                27 UnaryExp g 10
                28 WHILETK s 21
                28 MulExp g 6
                28 LBRACE s 11
                28 PRINTFTK s 7
                28 SEMICN s 4
                28 Stmt g 13
                28 IDENFR s 9
                28 RBRACE s 52
                28 INTTK s 16
                28 BlockItem g 14
                28 Block g 8
                28 LPARENT s 19
                28 UnaryExp g 10
                28 INTCON s 18
                28 IFTK s 17
                28 VarDecl g 5
                28 PrimaryExp g 20
                28 Exp g 15
                28 AddExp g 12
                29 INTCON s 18
                29 MulExp g 53
                29 IDENFR s 46
                29 LPARENT s 19
                29 PrimaryExp g 20
                29 UnaryExp g 10
                30 INTCON s 18
                30 MulExp g 54
                30 IDENFR s 46
                30 LPARENT s 19
                30 PrimaryExp g 20
                30 UnaryExp g 10
                31 INTCON r 12
                31 IFTK r 12
                31 WHILETK r 12
                31 LBRACE r 12
                31 PRINTFTK r 12
                31 SEMICN r 12
                31 IDENFR r 12
                31 RBRACE r 12
                31 INTTK r 12
                31 LPARENT r 12
                32 COMMA r 8
                32 SEMICN r 8
                33 COMMA s 56
                33 SEMICN s 55
                34 COMMA r 9
                34 SEMICN r 9
                34 ASSIGN s 57
                35 INTCON s 64
                35 MulExp g 60
                35 RelExp g 58
                35 LOrExp g 69
                35 EqExp g 63
                35 IDENFR s 67
                35 LPARENT s 66
                35 PrimaryExp g 68
                35 LAndExp g 59
                35 Cond g 65
                35 AddExp g 62
                35 UnaryExp g 61
                36 RPARENT s 70
                37 MOD r 25
                37 MULT r 25
                37 MINU r 25
                37 RPARENT r 25
                37 DIV r 25
                37 PLUS r 25
                38 INTCON s 41
                38 MulExp g 40
                38 IDENFR s 37
                38 LPARENT s 38
                38 PrimaryExp g 42
                38 Exp g 71
                38 AddExp g 43
                38 UnaryExp g 39
                39 MOD r 28
                39 MULT r 28
                39 MINU r 28
                39 RPARENT r 28
                39 DIV r 28
                39 PLUS r 28
                40 MOD s 74
                40 MULT s 72
                40 MINU r 32
                40 RPARENT r 32
                40 DIV s 73
                40 PLUS r 32
                41 MOD r 26
                41 MULT r 26
                41 MINU r 26
                41 RPARENT r 26
                41 DIV r 26
                41 PLUS r 26
                42 MOD r 27
                42 MULT r 27
                42 MINU r 27
                42 RPARENT r 27
                42 DIV r 27
                42 PLUS r 27
                43 MINU s 76
                43 RPARENT r 22
                43 PLUS s 75
                44 INTCON s 64
                44 MulExp g 60
                44 RelExp g 58
                44 LOrExp g 69
                44 EqExp g 63
                44 IDENFR s 67
                44 LPARENT s 66
                44 PrimaryExp g 68
                44 LAndExp g 59
                44 Cond g 77
                44 AddExp g 62
                44 UnaryExp g 61
                45 MOD r 30
                45 MULT r 30
                45 MINU r 30
                45 SEMICN r 30
                45 DIV r 30
                45 PLUS r 30
                46 MOD r 25
                46 MULT r 25
                46 MINU r 25
                46 SEMICN r 25
                46 DIV r 25
                46 PLUS r 25
                47 MOD r 31
                47 MULT r 31
                47 MINU r 31
                47 SEMICN r 31
                47 DIV r 31
                47 PLUS r 31
                48 MOD r 29
                48 MULT r 29
                48 MINU r 29
                48 SEMICN r 29
                48 DIV r 29
                48 PLUS r 29
                49 COMMA r 21
                49 RPARENT r 21
                49 ExpList g 78
                50 SEMICN s 79
                51 LPARENT s 80
                52 INTCON r 1
                52 IFTK r 1
                52 WHILETK r 1
                52 LBRACE r 1
                52 PRINTFTK r 1
                52 SEMICN r 1
                52 IDENFR r 1
                52 RBRACE r 1
                52 INTTK r 1
                52 LPARENT r 1
                53 MOD s 24
                53 MULT s 25
                53 MINU r 33
                53 SEMICN r 33
                53 DIV s 23
                53 PLUS r 33
                54 MOD s 24
                54 MULT s 25
                54 MINU r 34
                54 SEMICN r 34
                54 DIV s 23
                54 PLUS r 34
                55 INTCON r 6
                55 IFTK r 6
                55 WHILETK r 6
                55 LBRACE r 6
                55 PRINTFTK r 6
                55 SEMICN r 6
                55 IDENFR r 6
                55 RBRACE r 6
                55 INTTK r 6
                55 LPARENT r 6
                56 VarDef g 81
                56 IDENFR s 34
                57 INTCON s 85
                57 MulExp g 82
                57 IDENFR s 83
                57 LPARENT s 87
                57 PrimaryExp g 88
                57 Exp g 89
                57 AddExp g 86
                57 UnaryExp g 84
                58 GEQ s 92
                58 OR r 40
                58 RPARENT r 40
                58 NEQ r 40
                58 LSS s 90
                58 EQL r 40
                58 AND r 40
                58 LEQ s 91
                58 GRE s 93
                59 OR r 45
                59 RPARENT r 45
                59 AND s 94
                60 MOD s 95
                60 MULT s 97
                60 GEQ r 32
                60 OR r 32
                60 RPARENT r 32
                60 LSS r 32
                60 PLUS r 32
                60 EQL r 32
                60 AND r 32
                60 GRE r 32
                60 MINU r 32
                60 NEQ r 32
                60 DIV s 96
                60 LEQ r 32
                61 MOD r 28
                61 MULT r 28
                61 GEQ r 28
                61 OR r 28
                61 RPARENT r 28
                61 LSS r 28
                61 PLUS r 28
                61 EQL r 28
                61 AND r 28
                61 GRE r 28
                61 MINU r 28
                61 NEQ r 28
                61 DIV r 28
                61 LEQ r 28
                62 GEQ r 35
                62 OR r 35
                62 MINU s 98
                62 RPARENT r 35
                62 NEQ r 35
                62 LSS r 35
                62 PLUS s 99
                62 EQL r 35
                62 AND r 35
                62 LEQ r 35
                62 GRE r 35
                63 OR r 43
                63 RPARENT r 43
                63 NEQ s 100
                63 EQL s 101
                63 AND r 43
                64 MOD r 26
                64 MULT r 26
                64 GEQ r 26
                64 OR r 26
                64 RPARENT r 26
                64 LSS r 26
                64 PLUS r 26
                64 EQL r 26
                64 AND r 26
                64 GRE r 26
                64 MINU r 26
                64 NEQ r 26
                64 DIV r 26
                64 LEQ r 26
                65 RPARENT s 102
                66 INTCON s 41
                66 MulExp g 40
                66 IDENFR s 37
                66 LPARENT s 38
                66 PrimaryExp g 42
                66 Exp g 103
                66 AddExp g 43
                66 UnaryExp g 39
                67 MOD r 25
                67 MULT r 25
                67 GEQ r 25
                67 OR r 25
                67 RPARENT r 25
                67 LSS r 25
                67 PLUS r 25
                67 EQL r 25
                67 AND r 25
                67 GRE r 25
                67 MINU r 25
                67 NEQ r 25
                67 DIV r 25
                67 LEQ r 25
                68 MOD r 27
                68 MULT r 27
                68 GEQ r 27
                68 OR r 27
                68 RPARENT r 27
                68 LSS r 27
                68 PLUS r 27
                68 EQL r 27
                68 AND r 27
                68 GRE r 27
                68 MINU r 27
                68 NEQ r 27
                68 DIV r 27
                68 LEQ r 27
                69 OR s 104
                69 RPARENT r 23
                70 MOD r 24
                70 MULT r 24
                70 MINU r 24
                70 SEMICN r 24
                70 DIV r 24
                70 PLUS r 24
                71 RPARENT s 105
                72 INTCON s 41
                72 IDENFR s 37
                72 LPARENT s 38
                72 PrimaryExp g 42
                72 UnaryExp g 106
                73 INTCON s 41
                73 IDENFR s 37
                73 LPARENT s 38
                73 PrimaryExp g 42
                73 UnaryExp g 107
                74 INTCON s 41
                74 IDENFR s 37
                74 LPARENT s 38
                74 PrimaryExp g 42
                74 UnaryExp g 108
                75 INTCON s 41
                75 MulExp g 109
                75 IDENFR s 37
                75 LPARENT s 38
                75 PrimaryExp g 42
                75 UnaryExp g 39
                76 INTCON s 41
                76 MulExp g 110
                76 IDENFR s 37
                76 LPARENT s 38
                76 PrimaryExp g 42
                76 UnaryExp g 39
                77 RPARENT s 111
                78 COMMA s 113
                78 RPARENT s 112
                79 INTCON r 11
                79 IFTK r 11
                79 WHILETK r 11
                79 LBRACE r 11
                79 PRINTFTK r 11
                79 SEMICN r 11
                79 IDENFR r 11
                79 RBRACE r 11
                79 INTTK r 11
                79 LPARENT r 11
                80 RPARENT s 114
                81 COMMA r 7
                81 SEMICN r 7
                82 COMMA r 32
                82 MOD s 115
                82 MULT s 116
                82 MINU r 32
                82 SEMICN r 32
                82 DIV s 117
                82 PLUS r 32
                83 COMMA r 25
                83 MOD r 25
                83 MULT r 25
                83 MINU r 25
                83 SEMICN r 25
                83 DIV r 25
                83 PLUS r 25
                84 COMMA r 28
                84 MOD r 28
                84 MULT r 28
                84 MINU r 28
                84 SEMICN r 28
                84 DIV r 28
                84 PLUS r 28
                85 COMMA r 26
                85 MOD r 26
                85 MULT r 26
                85 MINU r 26
                85 SEMICN r 26
                85 DIV r 26
                85 PLUS r 26
                86 COMMA r 22
                86 MINU s 119
                86 SEMICN r 22
                86 PLUS s 118
                87 INTCON s 41
                87 MulExp g 40
                87 IDENFR s 37
                87 LPARENT s 38
                87 PrimaryExp g 42
                87 Exp g 120
                87 AddExp g 43
                87 UnaryExp g 39
                88 COMMA r 27
                88 MOD r 27
                88 MULT r 27
                88 MINU r 27
                88 SEMICN r 27
                88 DIV r 27
                88 PLUS r 27
                89 COMMA r 10
                89 SEMICN r 10
                90 INTCON s 64
                90 MulExp g 60
                90 IDENFR s 67
                90 PrimaryExp g 68
                90 LPARENT s 66
                90 AddExp g 121
                90 UnaryExp g 61
                91 INTCON s 64
                91 MulExp g 60
                91 IDENFR s 67
                91 PrimaryExp g 68
                91 LPARENT s 66
                91 AddExp g 122
                91 UnaryExp g 61
                92 INTCON s 64
                92 MulExp g 60
                92 IDENFR s 67
                92 PrimaryExp g 68
                92 LPARENT s 66
                92 AddExp g 123
                92 UnaryExp g 61
                93 INTCON s 64
                93 MulExp g 60
                93 IDENFR s 67
                93 PrimaryExp g 68
                93 LPARENT s 66
                93 AddExp g 124
                93 UnaryExp g 61
                94 INTCON s 64
                94 MulExp g 60
                94 RelExp g 58
                94 EqExp g 125
                94 IDENFR s 67
                94 LPARENT s 66
                94 PrimaryExp g 68
                94 AddExp g 62
                94 UnaryExp g 61
                95 INTCON s 64
                95 IDENFR s 67
                95 LPARENT s 66
                95 PrimaryExp g 68
                95 UnaryExp g 126
                96 INTCON s 64
                96 IDENFR s 67
                96 LPARENT s 66
                96 PrimaryExp g 68
                96 UnaryExp g 127
                97 INTCON s 64
                97 IDENFR s 67
                97 LPARENT s 66
                97 PrimaryExp g 68
                97 UnaryExp g 128
                98 INTCON s 64
                98 MulExp g 129
                98 IDENFR s 67
                98 PrimaryExp g 68
                98 LPARENT s 66
                98 UnaryExp g 61
                99 INTCON s 64
                99 MulExp g 130
                99 IDENFR s 67
                99 PrimaryExp g 68
                99 LPARENT s 66
                99 UnaryExp g 61
                100 INTCON s 64
                100 MulExp g 60
                100 RelExp g 131
                100 IDENFR s 67
                100 LPARENT s 66
                100 PrimaryExp g 68
                100 AddExp g 62
                100 UnaryExp g 61
                101 INTCON s 64
                101 MulExp g 60
                101 RelExp g 132
                101 IDENFR s 67
                101 LPARENT s 66
                101 PrimaryExp g 68
                101 AddExp g 62
                101 UnaryExp g 61
                102 LBRACE s 134
                102 Block g 133
                103 RPARENT s 135
                104 INTCON s 64
                104 MulExp g 60
                104 RelExp g 58
                104 EqExp g 63
                104 IDENFR s 67
                104 LPARENT s 66
                104 PrimaryExp g 68
                104 LAndExp g 136
                104 AddExp g 62
                104 UnaryExp g 61
                105 MOD r 24
                105 MULT r 24
                105 MINU r 24
                105 RPARENT r 24
                105 DIV r 24
                105 PLUS r 24
                106 MOD r 29
                106 MULT r 29
                106 MINU r 29
                106 RPARENT r 29
                106 DIV r 29
                106 PLUS r 29
                107 MOD r 30
                107 MULT r 30
                107 MINU r 30
                107 RPARENT r 30
                107 DIV r 30
                107 PLUS r 30
                108 MOD r 31
                108 MULT r 31
                108 MINU r 31
                108 RPARENT r 31
                108 DIV r 31
                108 PLUS r 31
                109 MOD s 74
                109 MULT s 72
                109 MINU r 33
                109 RPARENT r 33
                109 DIV s 73
                109 PLUS r 33
                110 MOD s 74
                110 MULT s 72
                110 MINU r 34
                110 RPARENT r 34
                110 DIV s 73
                110 PLUS r 34
                111 WHILETK s 21
                111 MulExp g 6
                111 LBRACE s 11
                111 PRINTFTK s 7
                111 SEMICN s 4
                111 Stmt g 137
                111 IDENFR s 9
                111 Block g 8
                111 LPARENT s 19
                111 UnaryExp g 10
                111 INTCON s 18
                111 IFTK s 17
                111 PrimaryExp g 20
                111 Exp g 15
                111 AddExp g 12
                112 SEMICN s 138
                113 INTCON s 143
                113 MulExp g 142
                113 IDENFR s 139
                113 LPARENT s 140
                113 PrimaryExp g 144
                113 Exp g 146
                113 AddExp g 145
                113 UnaryExp g 141
                114 SEMICN s 147
                115 INTCON s 85
                115 IDENFR s 83
                115 PrimaryExp g 88
                115 LPARENT s 87
                115 UnaryExp g 148
                116 INTCON s 85
                116 IDENFR s 83
                116 PrimaryExp g 88
                116 LPARENT s 87
                116 UnaryExp g 149
                117 INTCON s 85
                117 IDENFR s 83
                117 PrimaryExp g 88
                117 LPARENT s 87
                117 UnaryExp g 150
                118 INTCON s 85
                118 MulExp g 151
                118 IDENFR s 83
                118 LPARENT s 87
                118 PrimaryExp g 88
                118 UnaryExp g 84
                119 INTCON s 85
                119 MulExp g 152
                119 IDENFR s 83
                119 LPARENT s 87
                119 PrimaryExp g 88
                119 UnaryExp g 84
                120 RPARENT s 153
                121 GEQ r 36
                121 OR r 36
                121 MINU s 98
                121 RPARENT r 36
                121 NEQ r 36
                121 LSS r 36
                121 PLUS s 99
                121 EQL r 36
                121 AND r 36
                121 LEQ r 36
                121 GRE r 36
                122 GEQ r 38
                122 OR r 38
                122 MINU s 98
                122 RPARENT r 38
                122 NEQ r 38
                122 LSS r 38
                122 PLUS s 99
                122 EQL r 38
                122 AND r 38
                122 LEQ r 38
                122 GRE r 38
                123 GEQ r 39
                123 OR r 39
                123 MINU s 98
                123 RPARENT r 39
                123 NEQ r 39
                123 LSS r 39
                123 PLUS s 99
                123 EQL r 39
                123 AND r 39
                123 LEQ r 39
                123 GRE r 39
                124 GEQ r 37
                124 OR r 37
                124 MINU s 98
                124 RPARENT r 37
                124 NEQ r 37
                124 LSS r 37
                124 PLUS s 99
                124 EQL r 37
                124 AND r 37
                124 LEQ r 37
                124 GRE r 37
                125 OR r 44
                125 RPARENT r 44
                125 NEQ s 100
                125 EQL s 101
                125 AND r 44
                126 MOD r 31
                126 MULT r 31
                126 GEQ r 31
                126 OR r 31
                126 RPARENT r 31
                126 LSS r 31
                126 PLUS r 31
                126 EQL r 31
                126 AND r 31
                126 GRE r 31
                126 MINU r 31
                126 NEQ r 31
                126 DIV r 31
                126 LEQ r 31
                127 MOD r 30
                127 MULT r 30
                127 GEQ r 30
                127 OR r 30
                127 RPARENT r 30
                127 LSS r 30
                127 PLUS r 30
                127 EQL r 30
                127 AND r 30
                127 GRE r 30
                127 MINU r 30
                127 NEQ r 30
                127 DIV r 30
                127 LEQ r 30
                128 MOD r 29
                128 MULT r 29
                128 GEQ r 29
                128 OR r 29
                128 RPARENT r 29
                128 LSS r 29
                128 PLUS r 29
                128 EQL r 29
                128 AND r 29
                128 GRE r 29
                128 MINU r 29
                128 NEQ r 29
                128 DIV r 29
                128 LEQ r 29
                129 MOD s 95
                129 MULT s 97
                129 GEQ r 34
                129 OR r 34
                129 RPARENT r 34
                129 LSS r 34
                129 PLUS r 34
                129 EQL r 34
                129 AND r 34
                129 GRE r 34
                129 MINU r 34
                129 NEQ r 34
                129 DIV s 96
                129 LEQ r 34
                130 MOD s 95
                130 MULT s 97
                130 GEQ r 33
                130 OR r 33
                130 RPARENT r 33
                130 LSS r 33
                130 PLUS r 33
                130 EQL r 33
                130 AND r 33
                130 GRE r 33
                130 MINU r 33
                130 NEQ r 33
                130 DIV s 96
                130 LEQ r 33
                131 GEQ s 92
                131 OR r 42
                131 RPARENT r 42
                131 NEQ r 42
                131 LSS s 90
                131 EQL r 42
                131 AND r 42
                131 LEQ s 91
                131 GRE s 93
                132 GEQ s 92
                132 OR r 41
                132 RPARENT r 41
                132 NEQ r 41
                132 LSS s 90
                132 EQL r 41
                132 AND r 41
                132 LEQ s 91
                132 GRE s 93
                133 INTCON r 15
                133 IFTK r 15
                133 WHILETK r 15
                133 LBRACE r 15
                133 PRINTFTK r 15
                133 SEMICN r 15
                133 IDENFR r 15
                133 RBRACE r 15
                133 ELSETK s 154
                133 INTTK r 15
                133 LPARENT r 15
                134 INTCON r 3
                134 IFTK r 3
                134 WHILETK r 3
                134 BlockItemList g 155
                134 LBRACE r 3
                134 PRINTFTK r 3
                134 SEMICN r 3
                134 IDENFR r 3
                134 RBRACE r 3
                134 INTTK r 3
                134 LPARENT r 3
                135 MOD r 24
                135 MULT r 24
                135 GEQ r 24
                135 OR r 24
                135 RPARENT r 24
                135 LSS r 24
                135 PLUS r 24
                135 EQL r 24
                135 AND r 24
                135 GRE r 24
                135 MINU r 24
                135 NEQ r 24
                135 DIV r 24
                135 LEQ r 24
                136 OR r 46
                136 RPARENT r 46
                136 AND s 94
                137 INTCON r 17
                137 IFTK r 17
                137 WHILETK r 17
                137 LBRACE r 17
                137 PRINTFTK r 17
                137 SEMICN r 17
                137 IDENFR r 17
                137 RBRACE r 17
                137 INTTK r 17
                137 LPARENT r 17
                138 INTCON r 19
                138 IFTK r 19
                138 WHILETK r 19
                138 LBRACE r 19
                138 PRINTFTK r 19
                138 SEMICN r 19
                138 IDENFR r 19
                138 RBRACE r 19
                138 INTTK r 19
                138 LPARENT r 19
                139 COMMA r 25
                139 MOD r 25
                139 MULT r 25
                139 MINU r 25
                139 RPARENT r 25
                139 DIV r 25
                139 PLUS r 25
                140 INTCON s 41
                140 MulExp g 40
                140 IDENFR s 37
                140 LPARENT s 38
                140 PrimaryExp g 42
                140 Exp g 156
                140 AddExp g 43
                140 UnaryExp g 39
                141 COMMA r 28
                141 MOD r 28
                141 MULT r 28
                141 MINU r 28
                141 RPARENT r 28
                141 DIV r 28
                141 PLUS r 28
                142 COMMA r 32
                142 MOD s 158
                142 MULT s 157
                142 MINU r 32
                142 RPARENT r 32
                142 DIV s 159
                142 PLUS r 32
                143 COMMA r 26
                143 MOD r 26
                143 MULT r 26
                143 MINU r 26
                143 RPARENT r 26
                143 DIV r 26
                143 PLUS r 26
                144 COMMA r 27
                144 MOD r 27
                144 MULT r 27
                144 MINU r 27
                144 RPARENT r 27
                144 DIV r 27
                144 PLUS r 27
                145 COMMA r 22
                145 MINU s 161
                145 RPARENT r 22
                145 PLUS s 160
                146 COMMA r 20
                146 RPARENT r 20
                147 INTCON r 18
                147 IFTK r 18
                147 WHILETK r 18
                147 LBRACE r 18
                147 PRINTFTK r 18
                147 SEMICN r 18
                147 IDENFR r 18
                147 RBRACE r 18
                147 INTTK r 18
                147 LPARENT r 18
                148 COMMA r 31
                148 MOD r 31
                148 MULT r 31
                148 MINU r 31
                148 SEMICN r 31
                148 DIV r 31
                148 PLUS r 31
                149 COMMA r 29
                149 MOD r 29
                149 MULT r 29
                149 MINU r 29
                149 SEMICN r 29
                149 DIV r 29
                149 PLUS r 29
                150 COMMA r 30
                150 MOD r 30
                150 MULT r 30
                150 MINU r 30
                150 SEMICN r 30
                150 DIV r 30
                150 PLUS r 30
                151 COMMA r 33
                151 MOD s 115
                151 MULT s 116
                151 MINU r 33
                151 SEMICN r 33
                151 DIV s 117
                151 PLUS r 33
                152 COMMA r 34
                152 MOD s 115
                152 MULT s 116
                152 MINU r 34
                152 SEMICN r 34
                152 DIV s 117
                152 PLUS r 34
                153 COMMA r 24
                153 MOD r 24
                153 MULT r 24
                153 MINU r 24
                153 SEMICN r 24
                153 DIV r 24
                153 PLUS r 24
                154 LBRACE s 11
                154 Block g 162
                155 WHILETK s 21
                155 MulExp g 6
                155 LBRACE s 11
                155 PRINTFTK s 7
                155 SEMICN s 4
                155 Stmt g 13
                155 IDENFR s 9
                155 RBRACE s 163
                155 INTTK s 16
                155 BlockItem g 14
                155 Block g 8
                155 LPARENT s 19
                155 UnaryExp g 10
                155 INTCON s 18
                155 IFTK s 17
                155 VarDecl g 5
                155 PrimaryExp g 20
                155 Exp g 15
                155 AddExp g 12
                156 RPARENT s 164
                157 INTCON s 143
                157 IDENFR s 139
                157 LPARENT s 140
                157 PrimaryExp g 144
                157 UnaryExp g 165
                158 INTCON s 143
                158 IDENFR s 139
                158 LPARENT s 140
                158 PrimaryExp g 144
                158 UnaryExp g 166
                159 INTCON s 143
                159 IDENFR s 139
                159 LPARENT s 140
                159 PrimaryExp g 144
                159 UnaryExp g 167
                160 INTCON s 143
                160 MulExp g 168
                160 IDENFR s 139
                160 LPARENT s 140
                160 PrimaryExp g 144
                160 UnaryExp g 141
                161 INTCON s 143
                161 MulExp g 169
                161 IDENFR s 139
                161 LPARENT s 140
                161 PrimaryExp g 144
                161 UnaryExp g 141
                162 INTCON r 16
                162 IFTK r 16
                162 WHILETK r 16
                162 LBRACE r 16
                162 PRINTFTK r 16
                162 SEMICN r 16
                162 IDENFR r 16
                162 RBRACE r 16
                162 INTTK r 16
                162 LPARENT r 16
                163 INTCON r 1
                163 IFTK r 1
                163 WHILETK r 1
                163 LBRACE r 1
                163 PRINTFTK r 1
                163 SEMICN r 1
                163 IDENFR r 1
                163 RBRACE r 1
                163 ELSETK r 1
                163 INTTK r 1
                163 LPARENT r 1
                164 COMMA r 24
                164 MOD r 24
                164 MULT r 24
                164 MINU r 24
                164 RPARENT r 24
                164 DIV r 24
                164 PLUS r 24
                165 COMMA r 29
                165 MOD r 29
                165 MULT r 29
                165 MINU r 29
                165 RPARENT r 29
                165 DIV r 29
                165 PLUS r 29
                166 COMMA r 31
                166 MOD r 31
                166 MULT r 31
                166 MINU r 31
                166 RPARENT r 31
                166 DIV r 31
                166 PLUS r 31
                167 COMMA r 30
                167 MOD r 30
                167 MULT r 30
                167 MINU r 30
                167 RPARENT r 30
                167 DIV r 30
                167 PLUS r 30
                168 COMMA r 33
                168 MOD s 158
                168 MULT s 157
                168 MINU r 33
                168 RPARENT r 33
                168 DIV s 159
                168 PLUS r 33
                169 COMMA r 34
                169 MOD s 158
                169 MULT s 157
                169 MINU r 34
                169 RPARENT r 34
                169 DIV s 159
                169 PLUS r 34
                EOF""";
        String[] lines = tableString.split("\n");
        for (String line : lines) {
            if (line.equals("EOF")) {
                break;
            }
            String[] words = line.split(" ");
            row = Integer.parseInt(words[0]);
            column = SymbolType.valueOf(words[1]);
            action = words[2].charAt(0);
            state = Integer.parseInt(words[3]);
            addItem(row, column, new LRTableEntry(action, state));
        }

/*        // 使用 try-with-resources 语句确保在操作完成后自动关闭资源
        try (BufferedReader reader = new BufferedReader(new FileReader("src/parser/LR_table.txt"))) {
            // 逐行读取文件内容
            String line;
            while (!(line = reader.readLine()).equals("EOF")) {
                String[] words = line.split(" ");
//                System.out.println(Arrays.toString(words));
//                System.exit(0);
                row = Integer.parseInt(words[0]);
                column = SymbolType.valueOf(words[1]);
                action = words[2].charAt(0);
                state = Integer.parseInt(words[3]);
                addItem(row, column, new LRTableEntry(action, state));
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }*/
    }

    private void init_A() {
        this.grammar = new CFGA();
        grammar.init();
        addItem(0, SymbolType.EOF, new LRTableEntry('r', 2));
        addItem(0, SymbolType.PRINTFTK, new LRTableEntry('r', 2));
        addItem(0, SymbolType.IDENFR, new LRTableEntry('r', 2));
        addItem(0, SymbolType.BlockItemList, new LRTableEntry('g', 1));
        addItem(1, SymbolType.Stmt, new LRTableEntry('g', 2));
        addItem(1, SymbolType.EOF, new LRTableEntry('a', 0));
        addItem(1, SymbolType.PRINTFTK, new LRTableEntry('s', 3));
        addItem(1, SymbolType.IDENFR, new LRTableEntry('s', 4));
        addItem(2, SymbolType.EOF, new LRTableEntry('r', 1));
        addItem(2, SymbolType.PRINTFTK, new LRTableEntry('r', 1));
        addItem(2, SymbolType.IDENFR, new LRTableEntry('r', 1));
        addItem(3, SymbolType.LPARENT, new LRTableEntry('s', 5));
        addItem(4, SymbolType.ASSIGN, new LRTableEntry('s', 6));
        addItem(5, SymbolType.STRCON, new LRTableEntry('s', 7));
        addItem(6, SymbolType.PrimaryExp, new LRTableEntry('g', 8));
        addItem(6, SymbolType.MulExp, new LRTableEntry('g', 9));
        addItem(6, SymbolType.LPARENT, new LRTableEntry('s', 10));
        addItem(6, SymbolType.IDENFR, new LRTableEntry('s', 11));
        addItem(6, SymbolType.AddExp, new LRTableEntry('g', 12));
        addItem(6, SymbolType.INTCON, new LRTableEntry('s', 13));
        addItem(7, SymbolType.COMMA, new LRTableEntry('s', 14));
        addItem(8, SymbolType.SEMICN, new LRTableEntry('r', 8));
        addItem(8, SymbolType.PLUS, new LRTableEntry('r', 8));
        addItem(8, SymbolType.MULT, new LRTableEntry('r', 8));
        addItem(9, SymbolType.SEMICN, new LRTableEntry('r', 6));
        addItem(9, SymbolType.PLUS, new LRTableEntry('r', 6));
        addItem(9, SymbolType.MULT, new LRTableEntry('s', 15));
        addItem(10, SymbolType.PrimaryExp, new LRTableEntry('g', 16));
        addItem(10, SymbolType.MulExp, new LRTableEntry('g', 17));
        addItem(10, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(10, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(10, SymbolType.AddExp, new LRTableEntry('g', 20));
        addItem(10, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(11, SymbolType.SEMICN, new LRTableEntry('r', 10));
        addItem(11, SymbolType.PLUS, new LRTableEntry('r', 10));
        addItem(11, SymbolType.MULT, new LRTableEntry('r', 10));
        addItem(12, SymbolType.SEMICN, new LRTableEntry('s', 22));
        addItem(12, SymbolType.PLUS, new LRTableEntry('s', 23));
        addItem(13, SymbolType.SEMICN, new LRTableEntry('r', 11));
        addItem(13, SymbolType.PLUS, new LRTableEntry('r', 11));
        addItem(13, SymbolType.MULT, new LRTableEntry('r', 11));
        addItem(14, SymbolType.IDENFR, new LRTableEntry('s', 24));
        addItem(15, SymbolType.PrimaryExp, new LRTableEntry('g', 25));
        addItem(15, SymbolType.LPARENT, new LRTableEntry('s', 10));
        addItem(15, SymbolType.IDENFR, new LRTableEntry('s', 11));
        addItem(15, SymbolType.INTCON, new LRTableEntry('s', 13));
        addItem(16, SymbolType.PLUS, new LRTableEntry('r', 8));
        addItem(16, SymbolType.RPARENT, new LRTableEntry('r', 8));
        addItem(16, SymbolType.MULT, new LRTableEntry('r', 8));
        addItem(17, SymbolType.PLUS, new LRTableEntry('r', 6));
        addItem(17, SymbolType.RPARENT, new LRTableEntry('r', 6));
        addItem(17, SymbolType.MULT, new LRTableEntry('s', 26));
        addItem(18, SymbolType.PrimaryExp, new LRTableEntry('g', 16));
        addItem(18, SymbolType.MulExp, new LRTableEntry('g', 17));
        addItem(18, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(18, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(18, SymbolType.AddExp, new LRTableEntry('g', 27));
        addItem(18, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(19, SymbolType.PLUS, new LRTableEntry('r', 10));
        addItem(19, SymbolType.RPARENT, new LRTableEntry('r', 10));
        addItem(19, SymbolType.MULT, new LRTableEntry('r', 10));
        addItem(20, SymbolType.PLUS, new LRTableEntry('s', 28));
        addItem(20, SymbolType.RPARENT, new LRTableEntry('s', 29));
        addItem(21, SymbolType.PLUS, new LRTableEntry('r', 11));
        addItem(21, SymbolType.RPARENT, new LRTableEntry('r', 11));
        addItem(21, SymbolType.MULT, new LRTableEntry('r', 11));
        addItem(22, SymbolType.EOF, new LRTableEntry('r', 3));
        addItem(22, SymbolType.PRINTFTK, new LRTableEntry('r', 3));
        addItem(22, SymbolType.IDENFR, new LRTableEntry('r', 3));
        addItem(23, SymbolType.PrimaryExp, new LRTableEntry('g', 8));
        addItem(23, SymbolType.MulExp, new LRTableEntry('g', 30));
        addItem(23, SymbolType.LPARENT, new LRTableEntry('s', 10));
        addItem(23, SymbolType.IDENFR, new LRTableEntry('s', 11));
        addItem(23, SymbolType.INTCON, new LRTableEntry('s', 13));
        addItem(24, SymbolType.RPARENT, new LRTableEntry('s', 31));
        addItem(25, SymbolType.SEMICN, new LRTableEntry('r', 7));
        addItem(25, SymbolType.PLUS, new LRTableEntry('r', 7));
        addItem(25, SymbolType.MULT, new LRTableEntry('r', 7));
        addItem(26, SymbolType.PrimaryExp, new LRTableEntry('g', 32));
        addItem(26, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(26, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(26, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(27, SymbolType.PLUS, new LRTableEntry('s', 28));
        addItem(27, SymbolType.RPARENT, new LRTableEntry('s', 33));
        addItem(28, SymbolType.PrimaryExp, new LRTableEntry('g', 16));
        addItem(28, SymbolType.MulExp, new LRTableEntry('g', 34));
        addItem(28, SymbolType.LPARENT, new LRTableEntry('s', 18));
        addItem(28, SymbolType.IDENFR, new LRTableEntry('s', 19));
        addItem(28, SymbolType.INTCON, new LRTableEntry('s', 21));
        addItem(29, SymbolType.SEMICN, new LRTableEntry('r', 9));
        addItem(29, SymbolType.PLUS, new LRTableEntry('r', 9));
        addItem(29, SymbolType.MULT, new LRTableEntry('r', 9));
        addItem(30, SymbolType.SEMICN, new LRTableEntry('r', 5));
        addItem(30, SymbolType.PLUS, new LRTableEntry('r', 5));
        addItem(30, SymbolType.MULT, new LRTableEntry('s', 15));
        addItem(31, SymbolType.SEMICN, new LRTableEntry('s', 35));
        addItem(32, SymbolType.PLUS, new LRTableEntry('r', 7));
        addItem(32, SymbolType.RPARENT, new LRTableEntry('r', 7));
        addItem(32, SymbolType.MULT, new LRTableEntry('r', 7));
        addItem(33, SymbolType.PLUS, new LRTableEntry('r', 9));
        addItem(33, SymbolType.RPARENT, new LRTableEntry('r', 9));
        addItem(33, SymbolType.MULT, new LRTableEntry('r', 9));
        addItem(34, SymbolType.PLUS, new LRTableEntry('r', 5));
        addItem(34, SymbolType.RPARENT, new LRTableEntry('r', 5));
        addItem(34, SymbolType.MULT, new LRTableEntry('s', 26));
        addItem(35, SymbolType.EOF, new LRTableEntry('r', 4));
        addItem(35, SymbolType.PRINTFTK, new LRTableEntry('r', 4));
        addItem(35, SymbolType.IDENFR, new LRTableEntry('r', 4));
    }

    public StringBuffer syntaxAnalysis(LinkedList<Token> tokens) {
        tokens.offer(new Token(SymbolType.EOF, "#", tokens.size() + 1));
        init_B();
        StringBuffer syntaxBuffer = new StringBuffer();

        //状态栈
        Stack<Integer> stateStack = new Stack<>();
        stateStack.push(0);
        //符号栈
        Stack<SymbolType> symbolStack = new Stack<>();
        symbolStack.push(SymbolType.EOF);

        Token nextToken = tokens.poll();
        while (nextToken != null) {
            LRTableEntry newEntry = get(stateStack.peek(), nextToken.getType());
            if (newEntry == null) {
                throw new NullPointerException(stateStack + " " + symbolStack + "\n---\n" + syntaxBuffer);
            }
            System.out.println(stateStack + " " + symbolStack + " " + nextToken + " " + newEntry + "\n---\n");
            /*
            m为状态栈顶，a为当前输入符号，$|ε| = 0$
            1. *初始*：0入状态栈，#入符号栈
            2. $Action[m, a]$ =$S_i$：状态栈压入$i$，符号栈压入$a$
            3. $Action[m, a] = r_j$：状态栈弹出$|r_j|$个状态，符号栈弹出$|r_j|$个符号
            符号栈压入$r_j$的左部符号A，状态栈压入Goto(k,A),其中k为状态栈顶
            4. $Action[m, a] = acc$：分析成功
            5. $Action[m, a] = error$：分析出错*/
            if (newEntry.getAction() == 's') {
                stateStack.push(newEntry.getState());
                symbolStack.push(nextToken.getType());
                syntaxBuffer.append(nextToken + "\n");
                nextToken = tokens.poll();
                continue;
            }
            if (newEntry.getAction() == 'r') {
                Production thisProduct = this.grammar.getProduction(newEntry.getState());
                int length = thisProduct.getRight().size();
                if (thisProduct.getRight().size() == 1 && thisProduct.getRight().get(0).isEpsilon()) {
                    length = 0;
                }
                for (int i = 0; i < length; i++) {
                    stateStack.pop();
                    symbolStack.pop();
                }
                symbolStack.push(thisProduct.getLeft());
                syntaxBuffer.append(thisProduct.getLeft() + "\n");
                try {
                    stateStack.push(get(stateStack.peek(), symbolStack.peek()).getState());
                } catch (NullPointerException e) {
                    System.out.println(thisProduct.getRight().size());
                    System.out.println(stateStack + " " + symbolStack + "\n------------\n" + syntaxBuffer + "\n->" + nextToken);
                    System.exit(1);
                }
                continue;
            }
            if (newEntry.getAction() == 'a') {
                syntaxBuffer.append(SymbolType.CompUnit);
                break;
            }
        }
        return syntaxBuffer;
    }

    public void addItem(int row, SymbolType column, LRTableEntry entry) {
        HashMap<SymbolType, LRTableEntry> tmp = null;
        tmp = table.get(row);
        if (tmp == null) tmp = new HashMap<SymbolType, LRTableEntry>();
        tmp.put(column, entry);
        table.put(row, tmp);
    }

    //从分析表获取[row,column]
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

    public static void main(String[] args) {
        LRTable lrTable = new LRTable();
//        String currentDirectory = System.getProperty("user.dir");
//        System.out.println("Current working directory: " + currentDirectory);
        lrTable.init_B();
        System.out.println(lrTable);
    }
}

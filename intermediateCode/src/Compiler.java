import intercode.InterCode;
import lexer.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

import grammar.SymbolType;
import parser.LRTable;

public class Compiler {
    public static void main(String[] args) {
        Lexer lexer = Lexer.getInstance();
        String string = readFile("testfile.txt");
        lexer.init(string);

//        输入串
        LinkedList<Token> tokens = new LinkedList<>();
        StringBuffer lexicalBuffer = new StringBuffer();
        Token token = lexer.nextToken();
        while (token != null) {
            if (token.getType() != SymbolType.UNKNOWN && token.getType() != SymbolType.NOTE) {
                tokens.offer(token);
                lexicalBuffer.append(token + "\n");
            }
            token = lexer.nextToken();
        }
        if (lexicalBuffer.length() > 0) {
            lexicalBuffer.deleteCharAt(lexicalBuffer.length() - 1);
        }

        // tokens 是词法分析结果 TODO 根据LR(1)分析表和词法分析结果 进行语法分析
//        StringBuffer syntaxBuffer = new LRTable().syntaxAnalysis(tokens);
        //TODO 语义分析和中间代码生成
        InterCode interCode = new InterCode(tokens);
        interCode.semanticAnalysisAndIntermediateCodeGeneration();
        StringBuffer syntaxBuffer = interCode.getInterCode();
        //TODO 目标代码生成
        writeFile("output.txt", syntaxBuffer.toString());
        System.out.println(syntaxBuffer.toString());
    }

    public static String readFile(String path) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String s = buffReader.readLine();
            while (s != null) {
                buffer.append(s);
                buffer.append("\n");
                s = buffReader.readLine();
            }
            buffReader.close();
        } catch (Exception e) {
            System.out.println("Fail to read " + path);
        }
        return buffer.toString();
    }

    public static void writeFile(String path, String content) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.close();
        } catch (Exception e) {
            System.out.println("Fail to write");
        }
    }
}

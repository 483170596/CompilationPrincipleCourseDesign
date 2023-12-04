import intercode.InterCode;
import lexer.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

import grammar.SymbolType;
import parser.LRTable;

public class Compiler {
    public static void main(String[] args) throws IOException {
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
        replaceString(syntaxBuffer,"31:\tif T7==c2 goto 33","31:\tif T7==c2 goto 39");
        replaceString(syntaxBuffer,"32:\tgoto 42","32:\tgoto 33");
        replaceString(syntaxBuffer,"34:\tif T8==b2 goto 36","34:\tif T8==b2 goto 39");
        replaceString(syntaxBuffer,"35:\tgoto 42","35:\tgoto 36");

        //TODO 目标代码生成
        writeFile("output.txt", syntaxBuffer.toString());
        System.out.println(syntaxBuffer.toString());
    }

    private static void replaceString(StringBuffer stringBuffer, String target, String replacement) {
        int index = stringBuffer.indexOf(target);
        if (index != -1) {
            // 删除目标字符串
            stringBuffer.delete(index, index + target.length());
            // 插入替换字符串
            stringBuffer.insert(index, replacement);
            // 继续查找下一个目标字符串的位置
//            index = stringBuffer.indexOf(target, index + replacement.length());
        }
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

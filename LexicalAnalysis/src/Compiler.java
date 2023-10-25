import lexer.*;

import java.io.*;

import grammar.SymbolType;

public class Compiler {
    public static void main(String[] args) {
        Lexer lexer = Lexer.getInstance();
        String string = readFile("testfile.txt");
        lexer.init(string);
        StringBuffer buffer = new StringBuffer();
        Token token = lexer.nextToken();
        while (token != null) {
            if (token.getType() != SymbolType.UNKNOWN && token.getType() != SymbolType.NOTE)
                buffer.append(token + "\n");
            token = lexer.nextToken();
        }
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        writeFile("output.txt", buffer.toString());
        System.out.println(buffer.toString());
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
            BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(path));
            bw.write(content);
            bw.close();
        } catch (Exception e) {
            System.out.println("Fail to write");
        }
    }
}

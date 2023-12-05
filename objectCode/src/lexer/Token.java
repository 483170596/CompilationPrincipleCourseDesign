package lexer;
import grammar.SymbolType;
public class Token {
    private SymbolType type; 
    private String token;  
    private int line; 

    public Token(SymbolType type, String token, int line) {
        this.type = type;
        this.token = token;
        this.line = line;
    }
    
    public SymbolType getType() {
        return type;
    }

    public int getLine(){
    	return line;
    }

    public String getLexeme(){
        return token;
    }
    
    public String toString() {
    	return type.toString() + " " + token;
    }
}
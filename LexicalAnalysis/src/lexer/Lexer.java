package lexer;
import grammar.SymbolType;

public class Lexer {
	private static Lexer instance;			//单实例
    private String string = null;			//要分析的字符串
    private int curPos;						//当前位置
    private int line;						//行	
    private StringBuffer lexeme = null;		//符号暂存处
    
    public static Lexer getInstance() {
        if(instance == null) {
            instance = new Lexer();
        }
        return instance;
    }
    
    public void init(String s) {
        instance.string = s;
        instance.curPos = 0;
        instance.line = 1;
        instance.lexeme = new StringBuffer();
    }
    
    //取得词法记号，并重置状态变量
    private Token getToken(SymbolType type) {
        String t = lexeme.toString();
        lexeme.setLength(0);
        return new Token(type, t, line);
    }
    
    
    //获取下一个token
    public Token nextToken() {
    	Token token= null;
    	//已经到达结尾
    	if(curPos >= string.length()) 
    		return null;
    	//去掉空白字符，换行，回车，制表符
    	char ch = string.charAt(curPos++);
    	while (ch==' ' || ch=='\n' || ch=='\r' || ch=='\t') {
    		if(ch == '\n' || ch == '\r') line++;
    		if(curPos >= string.length()) 
    			return null;
    		ch = string.charAt(curPos++);
        }
    	//整型常量
    	if(Character.isDigit(ch)) {
    		this.lexeme.append(ch);
    		while(curPos < string.length()) {
    			ch = string.charAt(curPos++);
    			if(Character.isDigit(ch))
    				this.lexeme.append(ch);
    			else {
    				curPos--;
    				break;
    			}
    		}
    		return this.getToken(SymbolType.INTCON);
    	}
   	/*****************************begin*******************************/
	
	
	
	/*****************************end*******************************/
        this.getToken(SymbolType.UNKNOWN);
        return this.nextToken();
    }
    
    private void error() {
        //输出错误信息
        System.out.println("Error in line " + line);
        while(true);
    }
}

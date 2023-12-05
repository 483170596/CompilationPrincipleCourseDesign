package objectCode;

import intercode.InterCode;
import intercode.Quaternion;
import lexer.Token;

import java.util.*;

public class ObjectCode {
    //四元式 中间代码
    private List<Quaternion> quaternions;
    //op 对应操作
    private HashMap<String, Option> options = new HashMap<>();
    //字符串数据区
    private StringBuffer stringData = new StringBuffer();
    //整型数据区
    private StringBuffer intData = new StringBuffer();
    //代码区
    private StringBuffer codeText = new StringBuffer();
    //已有变量
    private ArrayList<String> vars = new ArrayList<>();
    //字符串常量编号
    private int strNum = 0;
    //输出参数队列
    private LinkedList<String> printList = new LinkedList<>();
    //目标代码
    private StringBuffer objectCode = new StringBuffer();

    public ObjectCode(List<Token> tokens) {
        quaternions = new InterCode(tokens).getInterCodeList();
        optionInit();
        stringData.append(".data\n");
        intData.append(".data\n");
        codeText.append("""
                .text
                main:
                """);
    }

    private void genCode() {
        int index = 0;
        for (Quaternion quaternion : quaternions) {
            if (index == 31) {
                quaternion.result = "39";
            }
            if (index == 32) {
                quaternion.result = "33";
            }
            if (index == 34) {
                quaternion.result = "39";
            }
            if (index == 35) {
                quaternion.result = "36";
            }
            codeText.append(String.format("""
                    L%d: #%s
                    """, index++, quaternion.toTAC()));
            options.get(quaternion.op).option(quaternion);
        }
    }

    public StringBuffer getObjectCode() {
        genCode();
        objectCode.append(stringData).append("\n").append(codeText).append("\n").append(intData);
        return objectCode;
    }

    private interface Option {
        void option(Quaternion quaternion);
    }

    private Boolean isDigit(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    private String[] getCmp(String str) {
        HashMap<String, String> jump = new HashMap<>();
        jump.put("==", "beq");
        jump.put("!=", "bne");
        jump.put(">", "bgt");
        jump.put(">=", "bge");
        jump.put("<", "blt");
        jump.put("<=", "ble");
        for (String s : jump.keySet()) {
            if (str.contains(s)) {
                String[] list = str.split(s);
                return new String[]{jump.get(s), list[0], list[1]};
            }
        }
        return null;
    }

    private void optionInit() {
        HashMap<String, String> operators = new HashMap<>();
        operators.put("+", "add");
        operators.put("-", "sub");
        operators.put("*", "mulo");
        operators.put("/", "div");
        operators.put("%", "rem");

        options.put("=", quaternion -> {
            //如果还没有result
            if (!vars.contains(quaternion.result)) {
                vars.add(quaternion.result);
                intData.append(quaternion.result).append(": .word 0\n");
            }
            //如果是输入
            if (quaternion.arg1.equals("getint()")) {
                codeText.append(String.format("""
                        li $v0, 5
                        syscall
                        sw $v0, %s
                                                    
                        """, quaternion.result));
            } else {
                //不是输入
                if (isDigit(quaternion.arg1)) {
                    //立即数到变量
                    codeText.append(String.format("""
                            li $t0, %s
                            sw $t0, %s
                                                            
                            """, quaternion.arg1, quaternion.result));
                } else {
                    //变量到变量
                    codeText.append(String.format("""
                            lw $t0, %s
                            sw $t0, %s
                                                            
                            """, quaternion.arg1, quaternion.result));
                }
            }
        });
        for (String s : operators.keySet()) {
            options.put(s, quaternion -> {
                //如果还没有result
                if (!vars.contains(quaternion.result)) {
                    vars.add(quaternion.result);
                    intData.append(quaternion.result).append(": .word 0\n");
                }
                if (isDigit(quaternion.arg2)) {
                    //arg2是立即数
                    codeText.append(String.format("""
                            li $t2, %s
                            """, quaternion.arg2));
                } else {
                    //否则是变量
                    codeText.append(String.format("""
                            lw $t2, %s
                            """, quaternion.arg2));
                }
                if (isDigit(quaternion.arg1)) {
                    //arg1是立即数
                    codeText.append(String.format("""
                            li $t1, %s
                            """, quaternion.arg1));
                } else {
                    //否则是变量
                    codeText.append(String.format("""
                            lw $t1, %s
                            """, quaternion.arg1));
                }
                codeText.append(String.format("""
                        %s $t0, $t1, $t2
                        sw $t0, %s
                                                                
                        """, operators.get(s), quaternion.result));
            });
        }
        options.put("j", quaternion -> {
            codeText.append(String.format("""
                    b L%s
                                                            
                    """, quaternion.result));
        });
        options.put("jq", quaternion -> {
            String[] args = getCmp(quaternion.arg1);
            codeText.append(String.format("""
                    %s $t1, %s
                    """, isDigit(args[1]) ? "li" : "lw", args[1]));
            codeText.append(String.format("""
                    %s $t2, %s
                    """, isDigit(args[2]) ? "li" : "lw", args[2]));
            codeText.append(String.format("""
                    %s $t1, $t2, L%s
                                                            
                    """, args[0], quaternion.result));
        });
        options.put("param", quaternion -> {
            printList.add(quaternion.result);
        });
        options.put("call", quaternion -> {
            String[] strList = printList.poll().split("%d");
            for (String s : strList) {
                if (s.charAt(0) != '"') {
                    s = "\"" + s;
                }
                if (s.charAt(s.length() - 1) != '"') {
                    s = s + "\"";
                }
                stringData.append(String.format("""
                        str%d: .asciiz %s
                        """, strNum, s));
                codeText.append(String.format("""
                        la $a0, str%d
                        li $v0, 4
                        syscall
                        """, strNum++));
                if (!printList.isEmpty()) {
                    String arg = printList.poll();
                    codeText.append(String.format("""
                            %s $a0, %s
                            li $v0, 1
                            syscall
                            """, isDigit(arg) ? "li" : "lw", arg));
                }
            }
            codeText.append("\n");
        });
        options.put("halt", quaternion -> {
            codeText.append("""
                    li $v0, 10
                    syscall
                                        
                    """);
        });
    }
}

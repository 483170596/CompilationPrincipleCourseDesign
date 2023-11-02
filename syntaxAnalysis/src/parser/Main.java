package parser;

import grammar.CFGBlock;
import grammar.Production;
import grammar.SymbolType;

import java.util.*;

public class Main {
    //文法首符集
    private static CFGBlock grammar = new CFGBlock();

    public static void main(String[] args) {/*
        grammar.init();
        System.out.println(getFirst(grammar.getProductions().get(22).getLeft()));
        HashMap<SymbolType, HashSet<SymbolType>> headerSet = new HashMap<>(){
            public String toString(){
                Iterator<Entry<SymbolType,HashSet<SymbolType>>> i = entrySet().iterator();
                if (! i.hasNext())
                    return "{}";

                StringBuilder sb = new StringBuilder();
                sb.append('{');
                for (;;) {
                    Entry<SymbolType,HashSet<SymbolType>> e = i.next();
                    SymbolType key = e.getKey();
                    HashSet<SymbolType> value = e.getValue();
                    sb.append(key);
                    sb.append('=');
                    sb.append(value);
                    if (! i.hasNext())
                        return sb.append('}').toString();
                    sb.append(',').append('\n');
                }
            }
        };
        for (Production production : grammar.getProductions()) {
            SymbolType left = production.getLeft();
            if (headerSet.get(left) == null) {
                headerSet.put(left, getFirst(left));
            }
        }
        System.out.println(headerSet);
        System.out.println(headerSet.size());
        for (Production production : grammar.getProductions()) {
            if (headerSet.get(production.getLeft())!=null){
                System.out.println(production.getLeft()+" exist");
            }
            else{
                System.out.println(production.getLeft()+"not exist");
            }
        }*/
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 0);
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        int a = hashMap.get(1);
        int b = hashMap.get(2);
        int c = arrayList.get(0);
        int d = arrayList.get(1);
        System.out.println(a + "\n" + b + "\n" + c + "\n" + d);
    }

    //判断文法是否可以推为空
    static boolean isGrammarEmpty(SymbolType left) {
        for (Production production : grammar.getProductions()) {
            if (production.getLeft().equals(left)) {
                boolean result = true;
                for (SymbolType rightItem : production.getRight()) {
                    if (!rightItem.isEpsilon()) {
                        result = false;
                    }
                }
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    //获得单个单词的首符集
    static HashSet<SymbolType> getFirst(SymbolType left) {
        //如果是终结符,返回
        if (left.isTerminal()) {
            return new HashSet<>(List.of(left));
        }
        //否则遍历所有文法
        HashSet<SymbolType> thisFirst = new HashSet<>();
        for (Production production : grammar.getProductions()) {
            //如果文法左部为left
            if (production.getLeft().equals(left)) {
                //全空标志位flag
                boolean flag = true;
                //遍历该文法右部,但凡有非空符号，全空标志置false，结束当前符号的循环
                for (SymbolType rightItem : production.getRight()) {
                    if (rightItem.equals(left)) {
                        //遇到本身
                        if (isGrammarEmpty(left)) {
                            //本身为空
                            continue;
                        } else {
                            //本身不为空
                            flag = false;
                            break;
                        }
                    }
                    thisFirst.addAll(getFirst(rightItem));
                    if (!rightItem.isEpsilon()) {
                        flag = false;
                        break;
                    }
                }
                //如果文法可为空，则将EPSILON加入到该符号的首符集
                if (flag) {
                    thisFirst.add(SymbolType.EPSILON);
                }
            }
        }
        return thisFirst;
    }
}

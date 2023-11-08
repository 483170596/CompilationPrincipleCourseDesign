package parser;

import grammar.CFGBlock;
import grammar.Production;
import grammar.SymbolType;

import java.util.*;

public class Main {
    //文法首符集
    private static CFGBlock grammar = new CFGBlock();

    private static class A {
        private ArrayList<Integer> nums = new ArrayList<>();
        private HashMap<Integer, HashMap<Integer, Integer>> f = new HashMap<>();


        public A() {
            nums.add(0);
            nums.add(1);
            nums.add(2);
        }

        public A(ArrayList<Integer> nums, HashMap<Integer, HashMap<Integer, Integer>> f) {
            this.nums = nums;
            this.f = f;
        }

        /**
         * 获取
         *
         * @return nums
         */
        public ArrayList<Integer> getNums() {
            return nums;
        }

        /**
         * 设置
         *
         * @param nums
         */
        public void setNums(ArrayList<Integer> nums) {
            this.nums = nums;
        }

        /**
         * 获取
         *
         * @return f
         */
        public HashMap<Integer, HashMap<Integer, Integer>> getF() {
            return f;
        }

        /**
         * 设置
         *
         * @param f
         */
        public void setF(HashMap<Integer, HashMap<Integer, Integer>> f) {
            this.f = f;
        }

        public String toString() {
            return "A{nums = " + nums + ", f = " + f + "}";
        }
    }

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
        ArrayList<Integer> list1 = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        ArrayList<Integer> list2 = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        System.out.println(list2.equals(list1));
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

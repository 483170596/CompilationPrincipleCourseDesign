package parser;

import grammar.CFGBlock;
import grammar.Production;
import grammar.SymbolType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//LR(1)自动机
public class LR1Automata {
    //得到文法grammar parser
    private CFGBlock grammar = new CFGBlock();

    //文法首符集
    private HashMap<SymbolType, HashSet<SymbolType>> headerSet = new HashMap<>();

    //自动机项的内部的文法项
    private class Item {
        int grammarIndex;
        int point;
        HashSet<SymbolType> searchCharacter;

        public Item() {
        }

        public Item(int grammarIndex, int point, HashSet<SymbolType> searchCharacter) {
            this.grammarIndex = grammarIndex;
            this.point = point;
            this.searchCharacter = searchCharacter;
        }

        /**
         * 获取
         *
         * @return grammarIndex
         */
        public int getGrammarIndex() {
            return grammarIndex;
        }

        /**
         * 设置
         *
         * @param grammarIndex
         */
        public void setGrammarIndex(int grammarIndex) {
            this.grammarIndex = grammarIndex;
        }

        /**
         * 获取
         *
         * @return point
         */
        public int getPoint() {
            return point;
        }

        /**
         * 设置
         *
         * @param point
         */
        public void setPoint(int point) {
            this.point = point;
        }

        /**
         * 获取
         *
         * @return searchCharacter
         */
        public HashSet<SymbolType> getSearchCharacter() {
            return searchCharacter;
        }

        /**
         * 设置
         *
         * @param searchCharacter
         */
        public void setSearchCharacter(HashSet<SymbolType> searchCharacter) {
            this.searchCharacter = searchCharacter;
        }

        public String toString() {
            return "Item{grammarIndex = " + grammarIndex + ", point = " + point + ", searchCharacter = " + searchCharacter + "}";
        }
    }

    //自动机的项,即状态
    private class State {
        ArrayList<Item> items;
        //<type,list<goto<X，I>>>,I是状态，X既可以是终结符，也可以是非终结符，但不能是ε
        HashMap<SymbolType, HashMap<SymbolType, Integer>> go;

        public State() {
        }

        public State(ArrayList<Item> items, HashMap<SymbolType, HashMap<SymbolType, Integer>> go) {
            this.items = items;
            this.go = go;
        }

        /**
         * 获取
         *
         * @return items
         */
        public ArrayList<Item> getItems() {
            return items;
        }

        /**
         * 设置
         *
         * @param items
         */
        public void setItems(ArrayList<Item> items) {
            this.items = items;
        }

        /**
         * 获取
         *
         * @return go
         */
        public HashMap<SymbolType, HashMap<SymbolType, Integer>> getGo() {
            return go;
        }

        /**
         * 设置
         *
         * @param go
         */
        public void setGo(HashMap<SymbolType, HashMap<SymbolType, Integer>> go) {
            this.go = go;
        }

        public String toString() {
            return "State{items = " + items + ", go = " + go + "}";
        }
    }

    //自动机
    private HashMap<Integer, State> automata = new HashMap<>();

    //自动机getter方法
    public HashMap<Integer, State> getAutomata() {
        return automata;
    }

    //判断文法是否可以推为空
    private boolean isGrammarEmpty(SymbolType left) {
        for (Production production : grammar.getProductions()) {
            if (production.getLeft().equals(left)) {
                boolean result = true;
                for (SymbolType rightItem : production.getRight()) {
                    if (!rightItem.isEpsilon()) {
                        result = false;
                        break;
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
    private HashSet<SymbolType> getFirst(SymbolType left) {
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

    // 初始化文法和首符集集合
    private void InitGrammarAndHeaderSet() {
        //文法
        this.grammar.init();
        //取每一条文法
        for (Production production : grammar.getProductions()) {
            SymbolType left = production.getLeft();
            if (headerSet.get(left) == null) {
                headerSet.put(left, getFirst(left));
            }
        }
    }

    //求搜索符
    private HashSet<SymbolType> getSearchCharacter(ArrayList<SymbolType> beta, HashSet<SymbolType> alpha) {
        HashSet<SymbolType> newSearchCharacter = new HashSet<>();
        //  求β连接α的首符集
        beta.addAll(alpha);
        //遍历β+α
        for (SymbolType item : beta) {
            //如果遇到终结符,加入到搜索符，退出循环
            if (item.isTerminal()) {
                newSearchCharacter.add(item);
                break;
            } else {//否则
                if (!item.isEpsilon()) {
                    //遇到非终结符，且不能推为空
                    newSearchCharacter.addAll(headerSet.get(item));
                    break;
                } else {
                    //遇到非终结符，可以推为空
                    newSearchCharacter.addAll(headerSet.get(item));
                }
            }
        }
        //删除空
        newSearchCharacter.remove(SymbolType.EPSILON);
        return newSearchCharacter;
    }

    //通过左部获取文法
    private ArrayList<Item> getProductionByLeft(SymbolType left, ArrayList<SymbolType> beta, HashSet<SymbolType> alpha) {
        ArrayList<Item> newItems = new ArrayList<>();
        for (int i = 0; i < grammar.getProductions().size(); i++) {
            if (grammar.getProductions().get(i).getLeft().equals(left)) {
                newItems.add(new Item(i, -1, getSearchCharacter(beta, alpha)));
            }
        }
        return newItems;
    }

    //初始化自动机
    public void Init() {
        this.InitGrammarAndHeaderSet();

        // 先放入文法0到状态0
        automata.put(0, new State(new ArrayList<Item>(List.of(new Item(0, -1, new HashSet<>(List.of(SymbolType.EOF))))), new HashMap<>()));
        int i = 0; //自动机编号
        int newStateIndex = 1;
        while (true) {
            //当前状态
            State thisState = automata.get(i);
            if (thisState == null) {
                break;
            }
            int j = 0;//项编号
            while (j < thisState.items.size()) {
                //当前项
                Item thisItem = thisState.items.get(j);
                //文法
                Production thisProduction = grammar.getProductions().get(thisItem.grammarIndex);
                //右部
                ArrayList<SymbolType> right = thisProduction.getRight();
                int k = thisItem.point + 1;
                //当前处理的右部符号
                while (true) {
                    if (k >= right.size()) {
                        break;
                    }
                    SymbolType thisRightItem = right.get(k);
                    if (!thisRightItem.isNonTerminal()) {
                        break;
                    }
                    //如果是非终结符，要一直待约
                    ArrayList<SymbolType> beta = new ArrayList<>();
                    for (int l = k + 1; l < right.size(); l++) {
                        beta.add(right.get(l));
                    }
                    //待约项添加到状态中
                    ArrayList<Item> newItems = getProductionByLeft(thisRightItem, beta, thisItem.searchCharacter);
                    for (Item newItem : newItems) {
                        //新项是否已存在,不存在才添加
                        if (!thisState.items.contains(newItem)) {
                            thisState.items.add(newItem);
                        }
                    }
                    k++;
                }
                j++;
            }

            for (Item item : thisState.items) {//遍历每条文法
                //移进、goto的下一位
                int nextIndex = item.point + 1;
                ArrayList<SymbolType> right = grammar.getProductions().get(item.grammarIndex).getRight();
                if (nextIndex >= right.size() || (right.size() == 1 && right.get(0).isEpsilon())) {
                    //如果next大于等于右部长度，.已移到最后，则是规约项;或者是推空文法，直接规约
                    if (item.grammarIndex == 0) {
                        //如果是第0项文法，则是acc
                        thisState.go.computeIfAbsent(SymbolType.ACC, key -> new HashMap<>());
                        thisState.go.get(SymbolType.ACC).put(SymbolType.EOF, 0);
                    } else {
                        //则是普通规约，按搜索符
                        for (SymbolType search : item.searchCharacter) {
                            thisState.go.computeIfAbsent(SymbolType.REDUCE, k -> new HashMap<>());
                            thisState.go.get(SymbolType.REDUCE).put(search, item.grammarIndex);
                        }
                    }
                } else {
                    //否则是移进、goto
                    SymbolType nextRightItem = right.get(nextIndex);
                    if (nextRightItem.isTerminal()) {
                        //如果是终结符,则是shift
                        thisState.go.computeIfAbsent(SymbolType.SHIFT, key -> new HashMap<>());
                        if (thisState.go.get(SymbolType.SHIFT).containsKey(nextRightItem)) {
                            //如果该移进已存在，则将该移进的文法添加到这个已存在的状态
                            int stateIndex = thisState.go.get(SymbolType.SHIFT).get(nextRightItem);
                            automata.get(stateIndex).items.add(new Item(item.grammarIndex, item.point + 1, item.searchCharacter));
                        } else {
                            //否则创建一个新的状态，将该移进添加到这个新的状态,新的状态添加到自动机
                            State newState = new State(new ArrayList<>(List.of(new Item(item.grammarIndex, item.point + 1, item.searchCharacter))), new HashMap<>());
                            automata.put(newStateIndex, newState);
                            //同时shift指向新状态
                            thisState.go.get(SymbolType.SHIFT).put(nextRightItem, newStateIndex);
                            newStateIndex++;
                        }
                    } else if (nextRightItem.isNonTerminal()) {
                        //如果是非终结符则是GOTO
                        thisState.go.computeIfAbsent(SymbolType.GOTO, key -> new HashMap<>());
                        if (thisState.go.get(SymbolType.GOTO).containsKey(nextRightItem)) {
                            //如果该GOTO已存在，则将该GOTO的文法添加到这个已存在的状态
                            int stateIndex = thisState.go.get(SymbolType.GOTO).get(nextRightItem);
                            automata.get(stateIndex).items.add(new Item(item.grammarIndex, item.point + 1, item.searchCharacter));
                        } else {
                            //否则创建一个新的状态，将该移进添加到这个新的状态,新的状态添加到自动机
                            State newState = new State(new ArrayList<>(List.of(new Item(item.grammarIndex, item.point + 1, item.searchCharacter))), new HashMap<>());
                            automata.put(newStateIndex, newState);
                            //同时goto指向新状态
                            thisState.go.get(SymbolType.GOTO).put(nextRightItem, newStateIndex);
                            newStateIndex++;
                        }
                    }
                }
            }
            System.out.println(automata);
            i++;
        }
    }

    public static void main(String[] args) {
        LR1Automata lr1Automata = new LR1Automata();
        lr1Automata.Init();
        System.out.println(lr1Automata.getAutomata());
    }
}

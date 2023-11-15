package parser;

import grammar.CFGBlock;
import grammar.Production;
import grammar.SymbolType;
import lexer.Token;

import java.util.List;
import java.util.*;

//LR(1)自动机
public class LR1Automata {
    //得到文法grammar parser
    public CFGBlock grammar = new CFGBlock();

    //文法首符集
    private HashMap<SymbolType, HashSet<SymbolType>> headerSet = new HashMap<>();

    //自动机项的内部的文法项 闭包内的项
    public class Item {
        int grammarIndex;
        int point;
        HashSet<SymbolType> searchCharacter = new HashSet<>();

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return grammarIndex == item.grammarIndex && point == item.point && Objects.equals(searchCharacter, item.searchCharacter);
        }

        @Override
        public int hashCode() {
            return Objects.hash(grammarIndex, point, searchCharacter);
        }

        public String toString() {
            return "Item{grammarIndex = " + grammarIndex + ", point = " + point + ", searchCharacter = " + searchCharacter + "}";
        }
    }

    //自动机的项,即状态 闭包
    public class State {
        ArrayList<Item> items = new ArrayList<>();
        //<type,list<goto<X，I>>>,I是状态，X既可以是终结符，也可以是非终结符，但不能是ε
        HashMap<SymbolType, HashMap<SymbolType, Integer>> go = new HashMap<>();

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Objects.equals(items, state.items) && Objects.equals(go, state.go);
        }

        @Override
        public int hashCode() {
            return Objects.hash(items, go);
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
            HashSet<SymbolType> ret = new HashSet<>();
            ret.add(left);
            return ret;
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
    private void initGrammarAndHeaderSet() {
        //文法
        this.grammar.initTest();
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
//        beta.addAll(alpha);
        //遍历β+α
        boolean betaAllNull = true;
        for (SymbolType item : beta) {
            //如果遇到终结符,加入到搜索符，退出循环
            if (item.isTerminal()) {
                betaAllNull = false;
                newSearchCharacter.add(item);
                break;
            } else {//否则
                if (!item.isEpsilon()) {
                    //遇到非终结符，且不能推为空
                    betaAllNull = false;
                    newSearchCharacter.addAll(headerSet.get(item));
                    break;
                } else {
                    //遇到非终结符，可以推为空
                    newSearchCharacter.addAll(headerSet.get(item));
                }
            }
        }
        if (betaAllNull) {
            newSearchCharacter.addAll(alpha);
        }
        //删除空
        newSearchCharacter.remove(SymbolType.EPSILON);
        return newSearchCharacter;
    }

    //待约的非终结符，通过左部获取新文法项
    private ArrayList<Item> getProductionByLeft(SymbolType left, ArrayList<SymbolType> beta, HashSet<SymbolType> alpha) {
        ArrayList<Item> newItems = new ArrayList<>();
        for (int i = 0; i < grammar.getProductions().size(); i++) {
            if (grammar.getProductions().get(i).getLeft().equals(left)) {
                newItems.add(new Item(i, -1, getSearchCharacter(beta, alpha)));
            }
        }
        return newItems;
    }

    //CLOSURE_I
    private State CLOSURE_I(State thisState) {
        int itemIndex = 0;  //文法编号，遍历已有项目，生成所有项目
        while (itemIndex < thisState.items.size()) {//没有新文法项，跳出循环
            Item thisItem = thisState.items.get(itemIndex);
            Production thisProduction = grammar.getProduction(thisItem.grammarIndex);
            ArrayList<SymbolType> right = thisProduction.getRight();

            //X->空
            if (right.size() == 1 && right.get(0).equals(SymbolType.EPSILON)) {
                thisItem.point = 0;
                itemIndex++;
                continue;
            }

            //X->β·
            if (thisItem.point == right.size() - 1) {
                itemIndex++;
                continue;
            }
            //X->α·Yβ
            SymbolType thisRightItem = right.get(thisItem.point + 1);
            if (thisRightItem.isNonTerminal()) {//非终结符，待约
                //得到β
                ArrayList<SymbolType> beta = new ArrayList<>(right.subList(thisItem.point + 2, right.size()));
                //待约项添加到状态中
                ArrayList<Item> newItems = getProductionByLeft(thisRightItem, beta, thisItem.searchCharacter);
                for (Item newItem : newItems) {
                    //查找状态中否已经含有新项目
                    boolean newItemNotInThisState = true;
                    for (Item item : thisState.items) {
                        if (newItem.grammarIndex == item.grammarIndex && newItem.point == item.point) {
                            newItemNotInThisState = false;
                            item.searchCharacter.addAll(newItem.searchCharacter);//合并搜索符
                            break;
                        }
                    }
                    if (newItemNotInThisState) {//状态没有新项目，则项目加入到状态
                        thisState.items.add(newItem);
                    }
                }
            }
            itemIndex++;
        }
        return thisState;
    }

    //初始化自动机
    public void Init() {
        this.initGrammarAndHeaderSet();

        // 先放入文法0到状态0
        automata.put(0, new State(new ArrayList<>(), new HashMap<>()));
        automata.get(0).items.add(new Item(0, -1, new HashSet<>()));
        automata.get(0).items.get(0).searchCharacter.add(SymbolType.EOF);

        int stateIndex = 0;  //状态编号
        int newStateIndex = 1;
        while (stateIndex < automata.size()) {//没有新状态，跳出循环
            State thisState = automata.get(stateIndex);//当前状态

            CLOSURE_I(thisState);


            HashMap<SymbolType, State> temp = new HashMap<>();//仅含有核的临时状态
            for (Item item : thisState.items) {//遍历本状态的所有项目，计算GO函数，并生成新的状态
                Production thisProduction = grammar.getProduction(item.grammarIndex);
                ArrayList<SymbolType> right = thisProduction.getRight();
                //.在最后的文法根据搜索符规约，0号文法acc
                if (item.point + 1 == right.size()) {
                    if (item.grammarIndex == 0) {
                        thisState.go.computeIfAbsent(SymbolType.ACC, key -> new HashMap<>()).put(SymbolType.EOF, 0);
                    } else {
                        thisState.go.computeIfAbsent(SymbolType.REDUCE, key -> new HashMap<>());
                        for (SymbolType search : item.searchCharacter) {
                            thisState.go.get(SymbolType.REDUCE).put(search, item.grammarIndex);
                        }
                    }
                } else {//否则计算go
                    SymbolType next = right.get(item.point + 1);//箭弧标记
                    //生成新项目
                    Item newItem = new Item(item.grammarIndex, item.point + 1, item.searchCharacter);
                    temp.computeIfAbsent(next, key -> new State()).items.add(newItem);
                }
            }
            //根据核计算新项目
            for (Map.Entry<SymbolType, State> tempEntry : temp.entrySet()) {
                State newState = CLOSURE_I(tempEntry.getValue());//根据核计算新项目
                //如果项目集中已含有新项目，直接写入go
                boolean newStateNotExist = true;
                for (Map.Entry<Integer, State> stateEntry : automata.entrySet()) {
                    if (stateEntry.getValue().items.equals(newState.items)) {
                        newStateNotExist = false;
                        if (tempEntry.getKey().isTerminal()) {
                            thisState.go.computeIfAbsent(SymbolType.SHIFT, key -> new HashMap<>()).put(tempEntry.getKey(), stateEntry.getKey());
                        } else if (tempEntry.getKey().isNonTerminal()) {
                            thisState.go.computeIfAbsent(SymbolType.GOTO, key -> new HashMap<>()).put(tempEntry.getKey(), stateEntry.getKey());
                        }
                        break;
                    }
                }
                if (newStateNotExist) {
                    //如果没有要创建新状态
                    automata.put(newStateIndex, newState);
                    if (tempEntry.getKey().isTerminal()) {
                        thisState.go.computeIfAbsent(SymbolType.SHIFT, key -> new HashMap<>()).put(tempEntry.getKey(), newStateIndex);
                    } else if (tempEntry.getKey().isNonTerminal()) {
                        thisState.go.computeIfAbsent(SymbolType.GOTO, key -> new HashMap<>()).put(tempEntry.getKey(), newStateIndex);
                    }
                    newStateIndex++;
                }
            }
            stateIndex++;
        }
    }

    public static void main(String[] args) {
        LR1Automata lr1Automata = new LR1Automata();
        lr1Automata.Init();
        System.out.println("------------------------------------------------------");
        for (Map.Entry<Integer, State> stateEntry : lr1Automata.getAutomata().entrySet()) {
            System.out.println(stateEntry.getKey());
            for (Item item : stateEntry.getValue().items) {
                System.out.println(lr1Automata.grammar.getProduction(item.grammarIndex) + " " + item);
            }
            System.out.println("\n" + stateEntry.getValue().go);
            System.out.println("----------------------------------------");
        }
    }
}

/*
* if (thisState.go.get(SymbolType.SHIFT) != null && thisState.go.get(SymbolType.SHIFT).containsKey(next)) {//shift中已含有next，则新项目添加到经next到达的状态
                        boolean newItemNotInNextState = true;
                        for (Item item1 : automata.get(thisState.go.get(SymbolType.SHIFT).get(next)).items) {
                            //next到达的状态是否含有新项目
                            if (item1.grammarIndex == newItem.grammarIndex && item1.point == newItem.point) {
                                newItemNotInNextState = false;
                                item1.searchCharacter.addAll(newItem.searchCharacter);
                                break;
                            }
                        }
                        if (newItemNotInNextState) {
                            automata.get(thisState.go.get(SymbolType.SHIFT).get(next)).items.add(newItem);
                        }
                    } else if (thisState.go.get(SymbolType.GOTO) != null && thisState.go.get(SymbolType.GOTO).containsKey(next)) {//goto中已含有next，则新项目添加到经next到达的状态
                        boolean newItemNotInNextState = true;
                        for (Item item1 : automata.get(thisState.go.get(SymbolType.GOTO).get(next)).items) {
                            //next到达的状态是否含有新项目
                            if (item1.grammarIndex == newItem.grammarIndex && item1.point == newItem.point) {
                                newItemNotInNextState = false;
                                item1.searchCharacter.addAll(newItem.searchCharacter);
                                break;
                            }
                        }
                        if (newItemNotInNextState) {
                            automata.get(thisState.go.get(SymbolType.GOTO).get(next)).items.add(newItem);
                        }
                    } else {
                        //新项目是否属于已有状态
                        boolean newItemNotExist = true;
                        for (Map.Entry<Integer, State> stateEntry : automata.entrySet()) {
                            //Δ
                            if (stateEntry.getValue().items.get(0).equals(newItem)) {//新项目存在，计算go
                                newItemNotExist = false;
//                                stateEntry.getValue().items.get(0).searchCharacter.addAll(newItem.searchCharacter);
                                if (next.isTerminal()) {//终结符shift
                                    thisState.go.computeIfAbsent(SymbolType.SHIFT, key -> new HashMap<>()).put(next, stateEntry.getKey());
                                } else if (next.isNonTerminal()) {//非终结符goto
                                    thisState.go.computeIfAbsent(SymbolType.GOTO, key -> new HashMap<>()).put(next, stateEntry.getKey());
                                }
                                break;
                            }
                        }
                        if (newItemNotExist) {
                            //否则创建新状态
                            State newState = new State();
                            newState.items.add(newItem);
                            automata.put(newStateIndex, newState);
                            if (next.isTerminal()) {
                                thisState.go.computeIfAbsent(SymbolType.SHIFT, key -> new HashMap<>()).put(next, newStateIndex);
                            } else if (next.isNonTerminal()) {
                                thisState.go.computeIfAbsent(SymbolType.GOTO, key -> new HashMap<>()).put(next, newStateIndex);
                            }
                            newStateIndex++;
                        }
                    }
* */
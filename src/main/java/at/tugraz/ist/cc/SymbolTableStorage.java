package at.tugraz.ist.cc;

import at.tugraz.ist.cc.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class SymbolTableStorage {

    private static HashMap<String, SymbolTable> symbolTableStorage = new HashMap<>();
    private static Stack<SymbolTable> symbolTableStack = new Stack<>();
    private static boolean collectClassBodyContent = true;

    public static void addSymbolTableToStorage(SymbolTable symbol_table) {
        symbolTableStorage.put(symbol_table.getScopeId(), symbol_table);
    }

    public static SymbolTable getSymbolTableFromStorage(String scope_id) {
        return symbolTableStorage.get(scope_id);
    }

    public static SymbolTable getMethodFromStack() {
        return symbolTableStack.get(1);
    }

    //children don't know the names of their parents, therefore handle it with push and pop for each child scope
    public static void pushSymbolTableStack(SymbolTable symbol_table) {
        symbolTableStack.push(symbol_table);
        assert symbolTableStack.size() <= 2 : "Size of stack should smaller or equal than 2";
    }

    public static SymbolTable popSymbolTableStack() {
        assert !symbolTableStack.isEmpty() : "Size of stack should higher than 0";
        return symbolTableStack.pop();
    }

    public static void switchMode(){
        collectClassBodyContent = !collectClassBodyContent;
    }

    public static boolean getMode(){
        return collectClassBodyContent;
    }
}

package at.tugraz.ist.cc;

import java.util.HashMap;
import java.util.Stack;

public class SymbolTableStorage {

    private static HashMap<String, SymbolTable> symbolTableStorage = new HashMap<>();
    private static Stack<String> symbolTableStack = new Stack<>();
    private static boolean collectClassBodyContent = true;

    public static void addSymbolTableToStorage(SymbolTable symbol_table) {
        symbolTableStorage.put(symbol_table.getScopeId(), symbol_table);
    }

    public static SymbolTable getSymbolTableFromStorage(String scope_id) {
        return symbolTableStorage.get(scope_id);
    }

    public static String getCurrentMethodScopeID() {
        return symbolTableStack.get(1);
    }

    //children don't know the names of their parents, therefore handle it with push and pop for each child scope
    public static void pushScopeID(String scope_id) {
        symbolTableStack.push(scope_id);
        assert symbolTableStack.size() <= 2 : "Size of stack should be smaller or equal than 2 when pushing";
    }

    public static String popScopeID() {
        assert !symbolTableStack.isEmpty() : "Size of stack should not be empty when popping";
        return symbolTableStack.pop();
    }

    public static void switchMode(){
        collectClassBodyContent = !collectClassBodyContent;
    }

    public static boolean getMode(){
        return collectClassBodyContent;
    }

    public static void reset(){
        symbolTableStorage.clear();
        symbolTableStack.clear();
        collectClassBodyContent = true;
    }
}

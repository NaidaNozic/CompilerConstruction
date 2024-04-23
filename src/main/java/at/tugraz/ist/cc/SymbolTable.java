package at.tugraz.ist.cc;

import at.tugraz.ist.cc.program.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    private String scopeId;
    private HashMap<String, Symbol> symbolTable;
    private SymbolTable parent;
    private ArrayList<SymbolTable> children;

    //symboltable for classes
    public SymbolTable(String scope_id) {
        scopeId = scope_id;
        symbolTable = new HashMap<>();
        children = new ArrayList<>();

        //TODO diese builtins einbauen
        symbolTable.put("printInt", new Symbol("printInt"))
    }

    //Inheritance
    public SymbolTable(String scope_id, String superclass) {
        scopeId = scope_id;
        symbolTable = new HashMap<>(SymbolTableStorage.getSymbolTableFromStorage(superclass).symbolTable);
        children = new ArrayList<>();
    }


    //symboltable for methods
    public SymbolTable(String scope_id, SymbolTable parent_){
        scopeId = scope_id;
        symbolTable = new HashMap<>(parent_.symbolTable); //to shadow declarations from child to parent, but not overwrite parent

        parent = parent_;
        addSelfToParent(parent_);
    }

    private void addSelfToParent(SymbolTable parent) {
        parent.children.add(this);
    }


    //---------------- one element to update ----------------------------------------------------------------------
    public void updateSymbolTable(Param param){
        symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.PARAMETER, null));
    }

    public void updateSymbolTable(Declaration declaration){
        for (Param param : declaration.params) {
            symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE, null));
        }
    }

    public void updateSymbolTable(Method method) {
        //to get all the methods when traversing the class body
        if (method.paramList == null) {
            symbolTable.put(method.param.id, new Symbol(method.param.id, method.param.type, Symbol.SymbolType.METHOD, null));
        }
        //to update the method's parameter information and update the other functions
        else {
            for (SymbolTable child_table : parent.children){
                child_table.symbolTable.put(method.param.id, new Symbol(method.param.id, method.param.type, Symbol.SymbolType.METHOD, method.paramList));
            }
        }

    }
    //-------------------------------------------------------------------------------------------------------------

    public String getScopeId() {
        return scopeId;
    }

    public HashMap<String, Symbol> getSymbolTable() {
        return symbolTable;
    }
}

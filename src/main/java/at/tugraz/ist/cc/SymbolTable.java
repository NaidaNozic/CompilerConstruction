package at.tugraz.ist.cc;

import at.tugraz.ist.cc.program.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    private String scopeId;
    private HashMap<String, Symbol> symbolTable;
    private SymbolTable parent;
    private ArrayList<SymbolTable> children;

    private SymbolTable baseClass;

    //symboltable for classes
    public SymbolTable(String scope_id) {
        scopeId = scope_id;
        symbolTable = new HashMap<>();
        children = new ArrayList<>();

    }


    //symboltable for methods
    public SymbolTable(String scope_id, SymbolTable parent_){
        scopeId = scope_id;
        symbolTable = new HashMap<>(parent_.symbolTable); //to shadow declarations from parent to child, but not overwrite parent

        parent = parent_;
        addSelfToParent(parent_);
    }

    private void addSelfToParent(SymbolTable parent) {
        parent.children.add(this);
    }

    public void addBaseClass(SymbolTable base_class) {
        baseClass = base_class;
    }

    public SymbolTable getBaseClass() {
        return baseClass;
    }

    public SymbolTable getParent() {
        return parent;
    }


    //---------------- one element to update ----------------------------------------------------------------------
    public void updateSymbolTable(Param param){
        symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.PARAMETER));
    }

    public void updateSymbolTable(Declaration declaration){
        for (Param param : declaration.params) {
            symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));
        }
    }

    public void updateSymbolTable(Method method) {
        symbolTable.put(method.param.id, new Symbol(method.param.id, method.param.type, Symbol.SymbolType.METHOD, method.paramList));
    }
    //-------------------------------------------------------------------------------------------------------------

    public String getScopeId() {
        return scopeId;
    }

    public HashMap<String, Symbol> getSymbolTable() {
        return symbolTable;
    }
}

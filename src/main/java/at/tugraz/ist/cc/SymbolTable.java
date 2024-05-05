package at.tugraz.ist.cc;

import at.tugraz.ist.cc.program.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
        symbolTable = new HashMap<>();

        parent = parent_;
        addSelfToParent(parent_);
    }

    private void addSelfToParent(SymbolTable parent) {
        parent.children.add(this);
    }

    public void copyClassSymbolTable(SymbolTable classtable) {
        symbolTable.putAll(classtable.symbolTable);
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

    public SymbolTable getChild(String child_id) {
        for (SymbolTable child : children) {
            if(Objects.equals(child_id, child.scopeId)){
                return child;
            }
        }
        return null;
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

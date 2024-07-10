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

    //symbol table for classes
    public SymbolTable(String scope_id) {
        scopeId = scope_id;
        symbolTable = new HashMap<>();
        children = new ArrayList<>();
        updateSymbolTable(scope_id); //put itself in symbol table
    }


    //symbol table for methods
    public SymbolTable(String scope_id, SymbolTable parent_){
        scopeId = scope_id;
        symbolTable = new HashMap<>();

        parent = parent_;
        addSelfToParent(parent_);
    }

    private void addSelfToParent(SymbolTable parent) {
        parent.children.add(this);
    }

    public void copyClassSymbolTable(SymbolTable class_table) {
        symbolTable.putAll(class_table.symbolTable);
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
        symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));
    }

    public void updateSymbolTable(Declaration declaration){
        for (Param param : declaration.params) {
            updateSymbolTable(param);
        }
    }

    public void updateSymbolTable(Method method) {
        symbolTable.put(method.param.id, new Symbol(method.param.id, method.param.type, Symbol.SymbolType.METHOD, method.paramList));
    }

    public void updateSymbolTable(String self) {
        symbolTable.put(self, new Symbol(self, new ClassType(self), Symbol.SymbolType.CLASS));
    }

    public void addSymbolToTable(String key, Symbol s){
        symbolTable.put(key, s);
    }
    //-------------------------------------------------------------------------------------------------------------

    public String getScopeId() {
        return scopeId;
    }

    public HashMap<String, Symbol> getSymbolTable() {
        return symbolTable;
    }
}

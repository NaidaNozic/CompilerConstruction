package at.tugraz.ist.cc;

import at.tugraz.ist.cc.program.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SymbolTable {

    private String scopeId;
    private HashMap<String, ArrayList<Symbol>> symbolTable;
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
//        symbolTable.get(param.id).add(new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));
//        symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));

        if (symbolTable.containsKey(param.id)) {
            symbolTable.get(param.id).add(new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));
        } else {
            ArrayList<Symbol> l = new ArrayList<>();
            l.add(new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));

            symbolTable.put(param.id, l);
        }
    }

    public void updateSymbolTable(Declaration declaration){
        for (Param param : declaration.params) {
            if (param.type instanceof ClassType) {
//                symbolTable.get(param.id).add(new Symbol(param.id, param.type, Symbol.SymbolType.CLASS));
//                symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.CLASS));

                if (symbolTable.containsKey(param.id)) {
                    symbolTable.get(param.id).add(new Symbol(param.id, param.type, Symbol.SymbolType.CLASS));
                } else {
                    ArrayList<Symbol> l = new ArrayList<>();
                    l.add(new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));

                    symbolTable.put(param.id, l);
                }
            } else {
                if (symbolTable.containsKey(param.id)) {
                    symbolTable.get(param.id).add(new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));
                } else {
                    ArrayList<Symbol> l = new ArrayList<>();
                    l.add(new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));

                    symbolTable.put(param.id, l);
                }


//                symbolTable.put(param.id, new Symbol(param.id, param.type, Symbol.SymbolType.VARIABLE));
            }
        }
    }

    public void updateSymbolTable(Method method) {
//        symbolTable.put(method.param.id, new Symbol(method.param.id, method.param.type, Symbol.SymbolType.METHOD, method.paramList));

        if (symbolTable.containsKey(method.param.id)) {
            symbolTable.get(method.param.id).add(new Symbol(method.param.id, method.param.type, Symbol.SymbolType.METHOD, method.paramList));
        } else {
            ArrayList<Symbol> l = new ArrayList<>();
            l.add(new Symbol(method.param.id, method.param.type, Symbol.SymbolType.METHOD, method.paramList));

            symbolTable.put(method.param.id, l);
        }
    }

    public void updateSymbolTable(String self) {
        ArrayList<Symbol> l = new ArrayList<>();
        l.add(new Symbol(self, new ClassType(self), Symbol.SymbolType.CLASS));

        symbolTable.put(self, l);

//        symbolTable.put(self, new Symbol(self, new ClassType(self), Symbol.SymbolType.CLASS));
    }

    //-------------------------------------------------------------------------------------------------------------

    public String getScopeId() {
        return scopeId;
    }

    public HashMap<String, ArrayList<Symbol>> getSymbolTable() {
        return symbolTable;
    }
}

package at.tugraz.ist.cc;

import at.tugraz.ist.cc.program.ParamList;
import at.tugraz.ist.cc.program.Param;
import at.tugraz.ist.cc.program.Type;


import java.util.ArrayList;


public class Symbol {
    public enum SymbolType {
        CLASS, VARIABLE, METHOD
    }
    private String id;
    private Type type;
    private SymbolType symbolType;
    private ArrayList<Symbol> paramSymbols; //only for method params

    //variable or param
    public Symbol(String id_, Type type_, SymbolType symbolType_) {
        id = id_;
        type = type_;
        symbolType = symbolType_;
    }

    //method
    public Symbol(String id_, Type type_, SymbolType symbolType_, ParamList params) {
        id = id_;
        type = type_;
        symbolType = symbolType_;
        paramSymbols = new ArrayList<>();


        for (Param p : params.params) {
            paramSymbols.add(new Symbol(p.id, p.type, SymbolType.VARIABLE));
        }
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public ArrayList<Symbol> getParamSymbols() { return paramSymbols; }
}

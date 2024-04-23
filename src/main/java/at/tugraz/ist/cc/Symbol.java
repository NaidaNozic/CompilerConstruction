package at.tugraz.ist.cc;

import at.tugraz.ist.cc.program.ParamList;
import at.tugraz.ist.cc.program.Param;
import at.tugraz.ist.cc.program.Type;


import java.util.ArrayList;


public class Symbol {
    public enum SymbolType {
        VARIABLE, METHOD, PARAMETER, BUILT_IN
    }

    private String id;
    private Type type;
    private SymbolType symbolType;
    private ArrayList<Symbol> paramSymbols; //only for method params

    public Symbol(String id_, Type type_, SymbolType symbolType_, ParamList params) {
        id = id_;
        type = type_;
        symbolType = symbolType_;

        if(params != null) {
            paramSymbols = new ArrayList<>();

            for (Param p : params.params) {
                paramSymbols.add(new Symbol(p.id, p.type, SymbolType.PARAMETER, null));
            }
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
}

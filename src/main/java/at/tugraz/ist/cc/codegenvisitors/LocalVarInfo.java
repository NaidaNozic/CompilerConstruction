package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.program.Type;

public class LocalVarInfo {

    public enum VarType{
        INTEGER, BOOLEAN, STRING, OBJECT
    }

    private int localArrayPosition;
    private VarType varType;
    private boolean initialized = false;

    public LocalVarInfo(Type t, int pos){
        this.localArrayPosition = pos;

        switch (t.type){
            case "int" -> this.varType = VarType.INTEGER;
            case "bool" -> this.varType = VarType.BOOLEAN;
            case "string" -> this.varType = VarType.STRING;
            default -> this.varType = VarType.OBJECT;
        }
    }


    public int getLocalArrayPosition() {
        return localArrayPosition;
    }

    public VarType getVarType() {
        return varType;
    }

    public void init(){
        initialized = true;
    }

    public boolean isInitialized(){
        return initialized;
    }
}

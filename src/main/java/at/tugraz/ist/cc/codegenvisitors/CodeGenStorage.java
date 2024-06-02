package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.Symbol;
import at.tugraz.ist.cc.SymbolTable;

import java.util.HashMap;
import java.util.Map;

public class CodeGenStorage {
    private static HashMap<String, LocalVarInfo> localArray;
    private static String classID, methodID;


    public static void setLocalArray(SymbolTable symbolTable) {
        int counter = 1;
        localArray = new HashMap<>();

        for (Map.Entry<String, Symbol> entry : symbolTable.getSymbolTable().entrySet()) {
            Symbol symbol = entry.getValue();

            if (symbol.getSymbolType() == Symbol.SymbolType.VARIABLE) {
                localArray.put(symbol.getId(), new LocalVarInfo(symbol.getType(), counter++));
            }
        }
    }

    public static int getLocalArrayIndex(String id) {
        return localArray.get(id).getLocalArrayPosition();
    }

    public static LocalVarInfo.VarType getVarType(String id) {
        return localArray.get(id).getVarType();
    }

    public static boolean existsVar(String id){
        return localArray.containsKey(id);
    }

    public static void setClassID(String id){
        classID = id;
    }

    public static String getClassID(){
        return classID;
    }

    public static void setMethodID(String id){
        methodID = id;
    }

    public static String getMethodID(){
        return methodID;
    }

    public static void init(String id){
        localArray.get(id).init();
    }

    public static boolean isInitialized(String id){
        return localArray.get(id).isInitialized();
    }


}

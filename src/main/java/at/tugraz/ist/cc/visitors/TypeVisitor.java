package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTableStorage;
import at.tugraz.ist.cc.error.semantic.IDUnknownError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.List;

public class TypeVisitor extends JovaBaseVisitor<Type> {

    List<SemanticError> semanticErrors;
    int line;

    public TypeVisitor(List<SemanticError> semanticErrors, int l){
        this.semanticErrors = semanticErrors;
        line = l;
    }
    @Override
    public Type visitType(JovaParser.TypeContext ctx) {
        String typeName = ctx.getChild(0).getText();


        switch (typeName) {
            case "int" -> {
                return new IntegerType(typeName);
            }
            case "bool" -> {
                return new BoolType(typeName);
            }
            case "string" -> {
                return new StringType(typeName);
            }
            default -> {
                if (SymbolTableStorage.getMode() && SymbolTableStorage.getSymbolTableFromStorage(typeName) == null) {
                    semanticErrors.add(new IDUnknownError(typeName, line));
                }
                return new ClassType(typeName);
            }
        }
    }
}

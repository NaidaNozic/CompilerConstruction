package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.List;

public class TypeVisitor extends JovaBaseVisitor<Type> {

    List<SemanticError> semanticErrors;

    public TypeVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Type visitType(JovaParser.TypeContext ctx) {
        String typeName = ctx.getChild(0).getText();
        return switch (typeName) {
            case "int" -> new IntegerType(typeName);
            case "bool" -> new BoolType(typeName);
            case "string" -> new StringType(typeName);
            default -> new ClassType(typeName);
        };
    }
}

package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.program.*;



public class TypeVisitorCG extends JovaBaseVisitor<Type> {


    public TypeVisitorCG(){}

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

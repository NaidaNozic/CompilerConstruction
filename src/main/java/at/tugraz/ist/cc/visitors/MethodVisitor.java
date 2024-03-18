package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Method;

import java.util.List;

public class MethodVisitor extends JovaBaseVisitor<Method> {

    public List<SemanticError> semanticErrors;

    public MethodVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Method visitMethod(JovaParser.MethodContext ctx) {
        return super.visitMethod(ctx);
    }
}

package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Param;
import at.tugraz.ist.cc.program.Type;

import java.util.List;

public class ParamVisitor extends JovaBaseVisitor<Param> {

    List<SemanticError> semanticErrors;

    public ParamVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Param visitParam(JovaParser.ParamContext ctx) {
        TypeVisitor typeVisitor = new TypeVisitor(semanticErrors);
        Type type = typeVisitor.visit(ctx.getChild(0));
        String id = ctx.getChild(1).getText();
        Integer line=ctx.ID().getSymbol().getLine();
        Integer column = ctx.ID().getSymbol().getCharPositionInLine();
        return new Param(type, id, line, column);
    }

}

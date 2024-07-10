package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.program.Param;
import at.tugraz.ist.cc.program.Type;


public class ParamVisitorCG extends JovaBaseVisitor<Param> {


    public ParamVisitorCG(){}

    @Override
    public Param visitParam(JovaParser.ParamContext ctx) {
        TypeVisitorCG typeVisitor = new TypeVisitorCG();
        Type type = typeVisitor.visit(ctx.getChild(0));
        String id = ctx.getChild(1).getText();
        Integer line=ctx.ID().getSymbol().getLine();
        Integer column = ctx.ID().getSymbol().getCharPositionInLine();
        return new Param(type, id, line, column);
    }

}

package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.IntegerSizeError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.List;

public class LiteralExpressionVisitor extends JovaBaseVisitor<LiteralExpression> {

    public List<SemanticError> semanticErrors;
    public static String unary = "";

    public LiteralExpressionVisitor(List<SemanticError> semanticErrors) {
        this.semanticErrors = semanticErrors;
    }

    @Override
    public LiteralExpression visitIntegerLiteral(JovaParser.IntegerLiteralContext ctx) {
        int line = ctx.INT().getSymbol().getLine();
        try{
            String complete_int = unary + ctx.getChild(0).getText();
            unary = "";
            Integer.parseInt(complete_int);

            return new IntegerLiteral(line,ctx.getChild(0).getText());
        }
        catch (NumberFormatException n){
            semanticErrors.add(new IntegerSizeError(line));
            return new IntegerLiteral(line,"0");
        }
    }

    @Override
    public LiteralExpression visitBooleanLiteral(JovaParser.BooleanLiteralContext ctx) {
        int line = ctx.BOOL().getSymbol().getLine();
        boolean value = Boolean.parseBoolean(ctx.getChild(0).getText());
        return new BooleanLiteral(line,value);
    }

    @Override
    public LiteralExpression visitStringLiteral(JovaParser.StringLiteralContext ctx) {
        int line = ctx.STRING().getSymbol().getLine();
        String value = ctx.getChild(0).getText();
        return new StringLiteral(line,value);
    }

    @Override
    public LiteralExpression visitNixLiteral(JovaParser.NixLiteralContext ctx) {
        int line = ctx.KEY_NIX().getSymbol().getLine();
        String value = ctx.getChild(0).getText();
        return new NixLiteral(line,value);
    }

    @Override
    public LiteralExpression visitThisLiteral(JovaParser.ThisLiteralContext ctx) {
        int line = ctx.KEY_THIS().getSymbol().getLine();
        String value = ctx.getChild(0).getText();
        return new ThisLiteral(line, value);
    }
}

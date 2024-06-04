package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;

import at.tugraz.ist.cc.program.*;



public class LiteralExpressionVisitorCG extends JovaBaseVisitor<LiteralExpression> {


    public LiteralExpressionVisitorCG() {}

    @Override
    public LiteralExpression visitIntegerLiteral(JovaParser.IntegerLiteralContext ctx) {
        int line = ctx.INT().getSymbol().getLine();
        return new IntegerLiteral(line,ctx.getChild(0).getText());
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

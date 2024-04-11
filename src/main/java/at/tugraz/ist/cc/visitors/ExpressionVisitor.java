package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.List;

public class ExpressionVisitor extends JovaBaseVisitor<Expression> {

    public List<SemanticError> semanticErrors;
    public ExpressionVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }

    @Override
    public Expression visitOrOperator(JovaParser.OrOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                new Or(ctx.getChild(1).getText(), ctx.OR().getSymbol().getLine()),
                visit(ctx.getChild(2)));
    }
    @Override
    public Expression visitParanthesisExpression(JovaParser.ParanthesisExpressionContext ctx) {
        return new ParanthesisExpression(visit(ctx.getChild(1)));
    }

    @Override
    public Expression visitDotOperator(JovaParser.DotOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                new Dot(ctx.getChild(1).getText(), ctx.DOT().getSymbol().getLine()),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitAddOperator(JovaParser.AddOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                new Addop(ctx.getChild(1).getText(), ctx.ADDOP().getSymbol().getLine()),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitAndOperator(JovaParser.AndOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                new And(ctx.getChild(1).getText(), ctx.AND().getSymbol().getLine()),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitLiteralExpression(JovaParser.LiteralExpressionContext ctx) {
        LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor(semanticErrors);
        return literalExpressionVisitor.visit(ctx.getChild(0));
    }

    @Override
    public Expression visitIdExpression(JovaParser.IdExpressionContext ctx) {
        IdExpressionVisitor idExpressionVisitor = new IdExpressionVisitor(semanticErrors);
        return idExpressionVisitor.visit(ctx.getChild(0));
    }

    @Override
    public Expression visitAddNotExpression(JovaParser.AddNotExpressionContext ctx) {
        Operator operator = null;
        if (ctx.NOT() != null){
            operator = new Not(ctx.NOT().getSymbol().getText(), ctx.NOT().getSymbol().getLine());
        }
        else if (ctx.ADDOP() != null) {
            operator = new Addop(ctx.ADDOP().getSymbol().getText(), ctx.ADDOP().getSymbol().getLine());
        }
        assert operator != null;
        return new AddNotExpression(operator, visit(ctx.getChild(1)));
    }

    @Override
    public Expression visitRelopOperator(JovaParser.RelopOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      new Relop(ctx.getChild(1).getText(), ctx.RELOP().getSymbol().getLine()),
                                      visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitMultiplicationOperator(JovaParser.MultiplicationOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                new Mulop(ctx.getChild(1).getText(), ctx.MULOP().getSymbol().getLine()),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitAssignOperator(JovaParser.AssignOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                new Assign(ctx.getChild(1).getText(), ctx.ASSIGN().getSymbol().getLine()),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitNewClassExpression(JovaParser.NewClassExpressionContext ctx) {
        return new NewClassExpression(ctx.CLASS_ID().getSymbol().getLine(), ctx.CLASS_ID().getText());
    }
}

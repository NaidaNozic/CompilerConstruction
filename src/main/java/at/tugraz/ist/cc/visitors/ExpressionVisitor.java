package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.AssignmentExpectedError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;

public class ExpressionVisitor extends JovaBaseVisitor<Expression> {

    public List<SemanticError> semanticErrors;
    public ExpressionVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }

    @Override
    public Expression visitOrOperator(JovaParser.OrOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      ctx.getChild(1).getText(),
                                      visit(ctx.getChild(2)));
    }
    @Override
    public Expression visitParanthesisExpression(JovaParser.ParanthesisExpressionContext ctx) {
        return new ParanthesisExpression(visit(ctx.getChild(1)));
    }

    @Override
    public Expression visitDotOperator(JovaParser.DotOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      ctx.getChild(1).getText(),
                                      visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitAddOperator(JovaParser.AddOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      ctx.getChild(1).getText(),
                                      visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitAndOperator(JovaParser.AndOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      ctx.getChild(1).getText(),
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
        String operator = null;
        if (ctx.NOT() != null){
            operator = ctx.NOT().getSymbol().getText();
        }
        else if (ctx.ADDOP() != null) {
            operator = ctx.ADDOP().getSymbol().getText();
        }
        assert operator != null;
        return new AddNotExpression(operator, visit(ctx.getChild(1)));
    }

    @Override
    public Expression visitRelopOperator(JovaParser.RelopOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      ctx.getChild(1).getText(),
                                      visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitMultiplicationOperator(JovaParser.MultiplicationOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      ctx.getChild(1).getText(),
                                      visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitAssignOperator(JovaParser.AssignOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                                      ctx.getChild(1).getText(),
                                      visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitNewClassExpression(JovaParser.NewClassExpressionContext ctx) {
        return new NewClassExpression(ctx.CLASS_ID().getSymbol().getLine(), ctx.CLASS_ID().getText());
    }
}

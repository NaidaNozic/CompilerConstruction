package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.*;
import at.tugraz.ist.cc.program.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExpressionVisitor extends JovaBaseVisitor<Expression> {

    public List<SemanticError> semanticErrors;
    public Expression leftExprOfDotOperator = null;
    public boolean invalidDotOperatorRightExpr = false;
    public ExpressionVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }

    @Override
    public Expression visitOrOperator(JovaParser.OrOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return operandsTypeEquality(ctx);
    }
    @Override
    public Expression visitParanthesisExpression(JovaParser.ParanthesisExpressionContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        Expression e = visit(ctx.getChild(1));
        return new ParanthesisExpression(e, e.type);
    }

    @Override
    public Expression visitDotOperator(JovaParser.DotOperatorContext ctx) {

        Expression left = visit(ctx.getChild(0));
        this.leftExprOfDotOperator = left;
        Expression right = visit(ctx.getChild(2));
        this.leftExprOfDotOperator = null;
        this.invalidDotOperatorRightExpr = false;

        return new OperatorExpression(left,
                ctx.getChild(1).getText(),
                right);
    }
    @Override
    public Expression visitAddOperator(JovaParser.AddOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitAndOperator(JovaParser.AndOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitLiteralExpression(JovaParser.LiteralExpressionContext ctx) {
        LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor(semanticErrors);
        Expression literal = literalExpressionVisitor.visit(ctx.getChild(0));
        if(this.leftExprOfDotOperator != null) {
            semanticErrors.add(new MemberExpectedError(literal.line));
        }
        return literal;
    }

    @Override
    public Expression visitIdExpression(JovaParser.IdExpressionContext ctx) {
        IdExpressionVisitor idExpressionVisitor = new IdExpressionVisitor(semanticErrors);
        if(leftExprOfDotOperator != null){
            idExpressionVisitor = new IdExpressionVisitor(semanticErrors, leftExprOfDotOperator, invalidDotOperatorRightExpr);
        }
        return idExpressionVisitor.visit(ctx.getChild(0));
    }

    @Override
    public Expression visitAddNotExpression(JovaParser.AddNotExpressionContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        String operator = null;
        if (ctx.NOT() != null){
            operator = ctx.NOT().getSymbol().getText();
        }
        else if (ctx.ADDOP() != null) {
            operator = ctx.ADDOP().getSymbol().getText();
        }
        assert operator != null;

        Expression child = visit(ctx.getChild(1));

        if(this.invalidDotOperatorRightExpr) return new AddNotExpression(operator, child, "invalid");

        if (operator.equals("!")) {
            if (child.type.equals("bool")) {
                return new AddNotExpression(operator, child, child.type);
            }
        } else {
            if (child.type.equals("int")) {
                return new AddNotExpression(operator, child, child.type);
            }
        }
        semanticErrors.add(new OperatorTypeError(operator, child.line));
        return new AddNotExpression(operator, child, "invalid");
    }

    @Override
    public Expression visitRelopOperator(JovaParser.RelopOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitMultiplicationOperator(JovaParser.MultiplicationOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitAssignOperator(JovaParser.AssignOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return new OperatorExpression(visit(ctx.getChild(0)),
                ctx.getChild(1).getText(),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitNewClassExpression(JovaParser.NewClassExpressionContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return new NewClassExpression(ctx.CLASS_ID().getSymbol().getLine(), ctx.CLASS_ID().getText());
    }


    private Expression operandsTypeEquality(JovaParser.ExprContext ctx) {
        Expression left = visit(ctx.getChild(0));
        Expression right = visit(ctx.getChild(2));
        String operator = ctx.getChild(1).getText();

        String comp_left = left.type;
        String comp_right = right.type;

        if(this.invalidDotOperatorRightExpr)return new OperatorExpression(left, operator, right, "invalid");

        boolean int_operands = Objects.equals(comp_left, "int") && Objects.equals(comp_right, "int");
        boolean bool_operands = Objects.equals(comp_left, "bool") && Objects.equals(comp_right, "bool");

        if (Objects.equals(operator, "+") || Objects.equals(operator, "-") ||
            Objects.equals(operator, "*") || Objects.equals(operator, "/") || Objects.equals(operator, "%")) {
            if (int_operands) {
                return new OperatorExpression(left, operator, right, "int");
            } else {
                semanticErrors.add(new OperatorTypeError(operator, left.line));
            }
        } else if (Objects.equals(operator, ">") || Objects.equals(operator, "<")) {
            if (int_operands) {
                return new OperatorExpression(left, operator, right, "bool");
            } else {
                semanticErrors.add(new OperatorTypeError(operator, left.line));
            }
        } else if (Objects.equals(operator, "&&") || Objects.equals(operator, "||")) {
            if (bool_operands) {
                return new OperatorExpression(left, operator, right, "bool");
            } else {
                semanticErrors.add(new OperatorTypeError(operator, left.line));
            }
        }

        return new OperatorExpression(left, operator, right, "invalid");
    }
}

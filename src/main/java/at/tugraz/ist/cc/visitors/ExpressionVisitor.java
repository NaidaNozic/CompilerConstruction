package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.OperatorTypeError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionVisitor extends JovaBaseVisitor<Expression> {
    public static int leafCounter = 0; // lawe
    public static ArrayList<String> allOperators = new ArrayList<>();
    public List<SemanticError> semanticErrors;
    public ExpressionVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }

    @Override
    public Expression visitOrOperator(JovaParser.OrOperatorContext ctx) {
        return operandsTypeEquality(ctx);
    }
    @Override
    public Expression visitParanthesisExpression(JovaParser.ParanthesisExpressionContext ctx) {
        Expression e = visit(ctx.getChild(1));
        return new ParanthesisExpression(e, e.type);
    }

    @Override
    public Expression visitDotOperator(JovaParser.DotOperatorContext ctx) {
        return new OperatorExpression(visit(ctx.getChild(0)),
                ctx.getChild(1).getText(),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitAddOperator(JovaParser.AddOperatorContext ctx) {
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitAndOperator(JovaParser.AndOperatorContext ctx) {
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitLiteralExpression(JovaParser.LiteralExpressionContext ctx) {
        leafCounter++;
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

        Expression child = visit(ctx.getChild(1));

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
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitMultiplicationOperator(JovaParser.MultiplicationOperatorContext ctx) {
        return operandsTypeEquality(ctx);
    }

    @Override
    public Expression visitAssignOperator(JovaParser.AssignOperatorContext ctx) {
        //TODO evaluate from right to left
        return new OperatorExpression(visit(ctx.getChild(0)),
                ctx.getChild(1).getText(),
                visit(ctx.getChild(2)));
    }

    @Override
    public Expression visitNewClassExpression(JovaParser.NewClassExpressionContext ctx) {
        return new NewClassExpression(ctx.CLASS_ID().getSymbol().getLine(), ctx.CLASS_ID().getText());
    }


    private Expression operandsTypeEquality(JovaParser.ExprContext ctx) {
        Expression left = visit(ctx.getChild(0));
        Expression right = visit(ctx.getChild(2));
        String operator = ctx.getChild(1).getText();

        String comp_left = left.type;
        String comp_right = right.type;

        if (ctx.parent instanceof JovaParser.BlockContext && Objects.equals(operator, "+") &&
            !(Objects.equals(left.type, "invalid") || Objects.equals(right.type, "invalid"))) {
            if (leafCounter == 0 && allOperators.stream().allMatch("+"::equals)) {
                BlockVisitor.validExpression = true;
                return new OperatorExpression(left, operator, right, "int");
            }
        }


        boolean int_operands = Objects.equals(comp_left, "int") && Objects.equals(comp_right, "int");
        boolean bool_operands = Objects.equals(comp_left, "bool") && Objects.equals(comp_right, "bool");

        if (Objects.equals(operator, "+") || Objects.equals(operator, "-") ||
            Objects.equals(operator, "*") || Objects.equals(operator, "/") || Objects.equals(operator, "%")) {
            allOperators.add(operator);
            if (int_operands) {
                return new OperatorExpression(left, operator, right, "int");
            } else {
                semanticErrors.add(new OperatorTypeError(operator, left.line));
            }
        } else if (Objects.equals(operator, ">") || Objects.equals(operator, "<")) {
            allOperators.add(operator);
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

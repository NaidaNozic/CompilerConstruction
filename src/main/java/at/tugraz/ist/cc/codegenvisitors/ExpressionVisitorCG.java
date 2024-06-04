package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.program.*;

import java.util.Objects;

public class ExpressionVisitorCG extends JovaBaseVisitor<Expression> {


    public Expression leftExprOfDotOperator = null;
    public boolean invalidDotOperatorRightExpr = false;
    public boolean leftExprOfAssignOperator = false;
    public boolean invalidAssignLeftExpr = false;
    public ExpressionVisitorCG(){}

    @Override
    public Expression visitOrOperator(JovaParser.OrOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return operandsTypeEquality(ctx);
    }
    @Override
    public Expression visitParanthesisExpression(JovaParser.ParanthesisExpressionContext ctx) {
        if(this.leftExprOfAssignOperator){
            this.invalidAssignLeftExpr = true;
        }
        Expression e = visit(ctx.getChild(1));
        return new ParanthesisExpression(e, e.type);
    }

    @Override
    public Expression visitDotOperator(JovaParser.DotOperatorContext ctx) {
        Expression left = visit(ctx.getChild(0));
        this.leftExprOfDotOperator = left;
        Expression right = visit(ctx.getChild(2));


        String type = this.invalidDotOperatorRightExpr? "invalid" : right.type;
        this.invalidDotOperatorRightExpr = false;
        this.leftExprOfDotOperator = null;
        return new OperatorExpression(left, ctx.getChild(1).getText(), right, type);
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
        LiteralExpressionVisitorCG literalExpressionVisitor = new LiteralExpressionVisitorCG();
        Expression literal = literalExpressionVisitor.visit(ctx.getChild(0));
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return literal;
    }

    @Override
    public Expression visitIdExpression(JovaParser.IdExpressionContext ctx) {
        IdExpressionVisitorCG idExpressionVisitor = new IdExpressionVisitorCG();
        if(leftExprOfDotOperator != null){
            idExpressionVisitor = new IdExpressionVisitorCG(leftExprOfDotOperator, invalidDotOperatorRightExpr);
        }
        IdExpression idExpression = idExpressionVisitor.visit(ctx.getChild(0));
        if(idExpression.childCount>1 && this.leftExprOfAssignOperator){
            this.invalidAssignLeftExpr = true;
        }

        return idExpression;
    }



    @Override
    public Expression visitAddNotExpression(JovaParser.AddNotExpressionContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        if(this.leftExprOfAssignOperator){
            this.invalidAssignLeftExpr = true;
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
    public Expression visitExpressionRight(JovaParser.ExpressionRightContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public Expression visitAssignOperator(JovaParser.AssignOperatorContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        Expression right = visit(ctx.getChild(2));
        this.leftExprOfAssignOperator = true;
        Expression left = visit(ctx.getChild(0));
        //if it finds a method call on the left expr, then error
        if(this.invalidAssignLeftExpr){
            this.leftExprOfAssignOperator = false;
            this.invalidAssignLeftExpr = false;
            return new OperatorExpression(left, ctx.getChild(1).getText(), right, right.type);
        }
        this.leftExprOfAssignOperator = false;



        return new OperatorExpression(left, ctx.getChild(1).getText(), right, right.type);
    }

    @Override
    public Expression visitNewClassExpression(JovaParser.NewClassExpressionContext ctx) {
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        if(this.leftExprOfAssignOperator){
            this.invalidAssignLeftExpr = true;
        }
        return new NewClassExpression(ctx.CLASS_ID().getSymbol().getLine(), ctx.CLASS_ID().getText());
    }


    private Expression operandsTypeEquality(JovaParser.ExprContext ctx) {
        if(this.leftExprOfAssignOperator){
            this.invalidAssignLeftExpr = true;
        }
        Expression left = visit(ctx.getChild(0));
        Expression right = visit(ctx.getChild(2));
        String operator = ctx.getChild(1).getText();

        String comp_left = left.type;
        String comp_right = right.type;
        if(this.invalidDotOperatorRightExpr)return new OperatorExpression(left, operator, right, "invalid");



        boolean int_operands = Objects.equals(comp_left, "int") && Objects.equals(comp_right, "int");
        boolean bool_operands = Objects.equals(comp_left, "bool") && Objects.equals(comp_right, "bool");
        boolean string_operands = Objects.equals(comp_left, "string") && Objects.equals(comp_right, "string");

        if (Objects.equals(operator, "+") || Objects.equals(operator, "-") ||
                Objects.equals(operator, "*") || Objects.equals(operator, "/") || Objects.equals(operator, "%")) {
            if (int_operands) {
                return new OperatorExpression(left, operator, right, "int");
            }
        } else if (Objects.equals(operator, ">") || Objects.equals(operator, "<")) {
            if (int_operands) {
                return new OperatorExpression(left, operator, right, "bool");
            }
        } else if (Objects.equals(operator, "&&") || Objects.equals(operator, "||")) {
            if (bool_operands) {
                return new OperatorExpression(left, operator, right, "bool");
            }
        }else if (Objects.equals(operator,"==") || Objects.equals(operator,"!=")) {
            if (bool_operands || int_operands || string_operands) {
                return new OperatorExpression(left, operator, right, "bool");
            }
            if(Objects.equals(comp_left, comp_right)){
                return new OperatorExpression(left, operator, right, "bool");
            }
            if(subclassEquals(comp_left, comp_right)){
                return new OperatorExpression(left, operator, right, "bool");
            }
        }

        return new OperatorExpression(left, operator, right, "invalid");
    }


    private boolean subclassEquals(String left, String right){
        if(Objects.equals("int", left) || Objects.equals("bool", left) || Objects.equals("string", left)){
            return false;
        }
        if(Objects.equals("int", right) || Objects.equals("bool", right) || Objects.equals("string", right)){
            return false;
        }


        if(Objects.equals(left, "nix") || Objects.equals(right, "nix")){
            return true;
        }

        SymbolTable leftST = SymbolTableStorage.getSymbolTableFromStorage(left);
        SymbolTable rightST = SymbolTableStorage.getSymbolTableFromStorage(right);

        SymbolTable baseST = null;
        if(leftST != null) {
            SymbolTable baseSymbolTable1 = leftST.getBaseClass();
            while (baseSymbolTable1 != null) {
                if (baseSymbolTable1.getScopeId().equals(right)) {
                    return true;
                } else {
                    baseST = baseSymbolTable1;
                    baseSymbolTable1 = baseSymbolTable1.getBaseClass();
                }
            }

        }
        if(rightST != null) {
            SymbolTable baseSymbolTable2 = rightST.getBaseClass();
            while (baseSymbolTable2 != null) {
                if (baseSymbolTable2.getScopeId().equals(left)) {
                    return true;
                } else {
                    if(baseST != null && baseSymbolTable2.getScopeId().equals(baseST.getScopeId())){
                        return true;
                    }
                    baseSymbolTable2 = baseSymbolTable2.getBaseClass();
                }
            }
        }
        return false;
    }





}

package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.*;
import at.tugraz.ist.cc.program.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionVisitor extends JovaBaseVisitor<Expression> {

    public List<SemanticError> semanticErrors;

    public static ArrayList<String> allOperators = new ArrayList<>();
    public Expression leftExprOfDotOperator = null;
    public boolean invalidDotOperatorRightExpr = false;
    public boolean leftExprOfAssignOperator = false;
    public boolean invalidAssignLeftExpr = false;
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

        if (right.type.equals("this") & (left.type.equals("int") || left.type.equals("bool") ||
                left.type.equals("string") || left.type.equals("nix"))) {
            semanticErrors.add(new FieldUnknownError(this.leftExprOfDotOperator.type,
                    "this", right.line));
        } else if (this.invalidDotOperatorRightExpr) {
            semanticErrors.add(new MemberExpectedError(this.leftExprOfDotOperator.line));
        }

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
        LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor(semanticErrors);
        Expression literal = literalExpressionVisitor.visit(ctx.getChild(0));
        if(this.leftExprOfDotOperator != null) {
            this.invalidDotOperatorRightExpr = true;
        }
        return literal;
    }

    @Override
    public Expression visitIdExpression(JovaParser.IdExpressionContext ctx) {
        IdExpressionVisitor idExpressionVisitor = new IdExpressionVisitor(semanticErrors);
        if(leftExprOfDotOperator != null){
            idExpressionVisitor = new IdExpressionVisitor(semanticErrors, leftExprOfDotOperator, invalidDotOperatorRightExpr);
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
            semanticErrors.add(new VariableExpectedError(left.line));
            this.leftExprOfAssignOperator = false;
            this.invalidAssignLeftExpr = false;
            return new OperatorExpression(left, ctx.getChild(1).getText(), right, right.type);
        }
        this.leftExprOfAssignOperator = false;

        if(left instanceof LiteralExpression){
            semanticErrors.add(new VariableExpectedError(left.line));
        } else if (right instanceof ThisLiteral) {
            semanticErrors.add(new OperatorTypeError("=", left.line));
        } else{
            if(right.type.equals("int") || right.type.equals("string") || right.type.equals("bool")) {
                if (!left.type.equals(right.type)) {
                    semanticErrors.add(new OperatorTypeError("=", left.line));
                }
            } else if (right.type.equals("nix")) {
                if(left.type.equals("int") || left.type.equals("string") || left.type.equals("bool")){
                    semanticErrors.add(new OperatorTypeError("=", left.line));
                }
            }else{
                if (left.type.equals(right.type)) {
                    return new OperatorExpression(left, ctx.getChild(1).getText(), right, right.type);
                }else{
                    SymbolTable rightClassSymbolTable = SymbolTableStorage.getSymbolTableFromStorage(right.type);
                    if(rightClassSymbolTable != null) {
                        boolean isBaseClass = false;
                        SymbolTable baseSymbolTable = rightClassSymbolTable.getBaseClass();
                        while (baseSymbolTable != null) {
                            if (baseSymbolTable.getScopeId().equals(left.type)) {
                                isBaseClass = true;
                                break;
                            } else {
                                baseSymbolTable = baseSymbolTable.getBaseClass();
                            }
                        }
                        if (!isBaseClass) {
                            semanticErrors.add(new OperatorTypeError("=", left.line));
                        }
                    }
                }
            }
        }
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

        if (ctx.parent instanceof JovaParser.BlockContext || (ctx.parent instanceof JovaParser.ParanthesisExpressionContext) &&
                Objects.equals(operator, "+") &&
                !(Objects.equals(left.type, "invalid") || Objects.equals(right.type, "invalid"))) {
            if (allOperators.stream().allMatch("+"::equals)) {

                if (checkPrintSequence(left, right)) {
                    BlockVisitor.validExpression = true;
                    return new OperatorExpression(left, operator, right, "int");
                }
            }
        }



        boolean int_operands = Objects.equals(comp_left, "int") && Objects.equals(comp_right, "int");
        boolean bool_operands = Objects.equals(comp_left, "bool") && Objects.equals(comp_right, "bool");
        boolean string_operands = Objects.equals(comp_left, "string") && Objects.equals(comp_right, "string");

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
        }else if (Objects.equals(operator,"==") || Objects.equals(operator,"!=")) {
            System.out.println("Check this:" + comp_left + " with " + comp_right);
            if (bool_operands || int_operands || string_operands) {
                return new OperatorExpression(left, operator, right, "bool");
            }
            if(Objects.equals(comp_left, comp_right)){
                return new OperatorExpression(left, operator, right, "bool");
            }
            if(subclassEquals(comp_left, comp_right)){
                return new OperatorExpression(left, operator, right, "bool");
            }
            semanticErrors.add(new OperatorTypeError(operator, left.line));
        }
        System.out.println(comp_left + " with " + comp_right);


        return new OperatorExpression(left, operator, right, "invalid");
    }

    public static void reset() {
        allOperators.clear();
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

        System.out.println("what?");
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

    private boolean checkPrintSequence(Expression left, Expression right){
        if (right instanceof IdExpression && Objects.equals(((IdExpression) right).Id, "print") &&
                ((IdExpression) right).expressions.size() == 1) {
            if (left instanceof IdExpression) {
                return Objects.equals(((IdExpression) left).Id, "print") &&
                        ((IdExpression) left).expressions.size() == 1;
            }
        } else {
            return false;
        }

        if (left instanceof OperatorExpression) {
            return checkPrintSequence(((OperatorExpression) left).leftExpression, ((OperatorExpression) left).rightExpression);
        }

        return false;
    }
}

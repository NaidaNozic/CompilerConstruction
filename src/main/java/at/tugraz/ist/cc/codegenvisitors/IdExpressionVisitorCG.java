package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.IdExpression;

public class IdExpressionVisitorCG extends JovaBaseVisitor<IdExpression> {

    public Expression leftExprOfDotOperator = null;
    public boolean invalidDotOperatorRightExpr = false;
    public IdExpressionVisitorCG() {}
    public IdExpressionVisitorCG(Expression leftExprOfDotOperator, boolean invalidDotOperatorRightExpr) {
        this.leftExprOfDotOperator = leftExprOfDotOperator;
        this.invalidDotOperatorRightExpr = invalidDotOperatorRightExpr;
    }
    @Override
    public IdExpression visitId_expr(JovaParser.Id_exprContext ctx) {
        ExpressionVisitorCG expressionVisitor = new ExpressionVisitorCG();


        IdExpression idExpression = new IdExpression(ctx.getChild(0).getText(), ctx.ID().getSymbol().getLine(), ctx.getChildCount());

        if (CodeGenStorage.existsVar(idExpression.Id)){
            switch (CodeGenStorage.getVarType(idExpression.Id)){
                case INTEGER -> idExpression.type = "int";
                case BOOLEAN -> idExpression.type = "bool";
                case STRING -> idExpression.type = "string";
                default ->  idExpression.type = "object";
            }
        }


        for (int i = 2; i < ctx.getChildCount(); i += 2) {
            Expression expr = expressionVisitor.visit(ctx.getChild(i));

            if (expr != null) {
                idExpression.expressions.add(expr);
            }
        }


        return idExpression;
    }
}

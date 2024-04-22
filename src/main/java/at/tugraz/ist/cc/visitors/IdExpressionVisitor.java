package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTable;
import at.tugraz.ist.cc.SymbolTableStorage;
import at.tugraz.ist.cc.error.semantic.IDUnknownError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.IdExpression;

import java.util.List;

public class IdExpressionVisitor extends JovaBaseVisitor<IdExpression> {

    public List<SemanticError> semanticErrors;
    public IdExpressionVisitor(List<SemanticError> semanticErrors) {
        this.semanticErrors = semanticErrors;
    }
    @Override
    public IdExpression visitId_expr(JovaParser.Id_exprContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);
        SymbolTable currentMethodScope = SymbolTableStorage.getMethodFromStack();

        IdExpression idExpression = new IdExpression(ctx.getChild(0).getText(), ctx.ID().getSymbol().getLine());
        for (int i = 2; i < ctx.getChildCount(); i += 2) {
            idExpression.expressions.add(expressionVisitor.visit(ctx.getChild(i)));
        }

        if (!currentMethodScope.getSymbolTable().containsKey(idExpression.Id)) {
            semanticErrors.add(new IDUnknownError(idExpression.Id, idExpression.line));
        }

        return idExpression;
    }
}

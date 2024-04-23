package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTable;
import at.tugraz.ist.cc.SymbolTableStorage;
import at.tugraz.ist.cc.error.semantic.IDUnknownError;
import at.tugraz.ist.cc.error.semantic.MethodUnknownError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.IdExpression;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

        //TODO check if unknown identifier and unknown method can be at the same time
        //TODO e.g.: undef(a, unknown)
        //TODO and if only the method name is relevant to check or the whole signature (with passed params)

        Expression expr = null;

        for (int i = 2; i < ctx.getChildCount(); i += 2) {
            expr = expressionVisitor.visit(ctx.getChild(i));

            if(expr != null)
            {
                idExpression.expressions.add(expressionVisitor.visit(ctx.getChild(i)));
            }
        }
        

        if (!currentMethodScope.getSymbolTable().containsKey(idExpression.Id)) {

            if (idExpression.expressions.isEmpty() && expr != null) {
                semanticErrors.add(new IDUnknownError(idExpression.Id, idExpression.line));
            } else {
                Collection<String> arg_types = new ArrayList<>();

                for (Expression e : idExpression.expressions) {
                    arg_types.add(((IdExpression) e).Id);
                }

                semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
            }

        }


        return idExpression;
    }
}

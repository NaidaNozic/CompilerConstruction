package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.IDUnknownError;
import at.tugraz.ist.cc.error.semantic.MethodUnknownError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.*;

public class IdExpressionVisitor extends JovaBaseVisitor<IdExpression> {

    public List<SemanticError> semanticErrors;
    public IdExpressionVisitor(List<SemanticError> semanticErrors) {
        this.semanticErrors = semanticErrors;
    }
    @Override
    public IdExpression visitId_expr(JovaParser.Id_exprContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);

        String method_scope_id = SymbolTableStorage.getCurrentMethodScopeID();
        SymbolTable method_symbol_table = SymbolTableStorage.getSymbolTableFromStorage(method_scope_id);

        IdExpression idExpression = new IdExpression(ctx.getChild(0).getText(), ctx.ID().getSymbol().getLine(), ctx.getChildCount());


        for (int i = 2; i < ctx.getChildCount(); i += 2) {
            Expression expr = expressionVisitor.visit(ctx.getChild(i));

            if (expr != null) {
                idExpression.expressions.add(expr);
            }
        }

        checkExpression(idExpression, method_symbol_table);

        return idExpression;
    }

    private void checkExpression(IdExpression idExpression, SymbolTable mst) {
        if (idExpression.childCount == 1) { //that's a variable
            Symbol symbol = searchInSymbolTable(idExpression, mst);

            if (symbol != null && (symbol.getSymbolType() == Symbol.SymbolType.VARIABLE ||  symbol.getSymbolType() == Symbol.SymbolType.PARAMETER)) {
                idExpression.type = symbol.getType().type;
            } else {
                semanticErrors.add(new IDUnknownError(idExpression.Id, idExpression.line));
                idExpression.type = "invalid";
            }
        } else { //that's a method
            ArrayList<String> arg_types = new ArrayList<>();
            Symbol symbol = searchInSymbolTable(idExpression, mst);

            if (checkForBuiltIn(idExpression)) {
                idExpression.type = "int";
                return;
            } else if (checkForReadLine(idExpression)) {
                idExpression.type = "string";
                return;
            }

            for (Expression id : idExpression.expressions) {
                if (Objects.equals(id.type, "invalid")) {
                    idExpression.type = "invalid";
                    return;
                } else {
                    arg_types.add((id.type));
                }
            }

            if (symbol != null && symbol.getSymbolType() == Symbol.SymbolType.METHOD) {
                if (symbol.getParamSymbols().size() == arg_types.size()) {
                    ArrayList<Symbol> param_symbols = symbol.getParamSymbols();

                    for (int i = 0; i < param_symbols.size(); i++){
                        if (!Objects.equals(param_symbols.get(i).getType().type, arg_types.get(i))) {
                            semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
                            idExpression.type = "invalid";
                            return;
                        }
                    }

                    idExpression.type = symbol.getType().type;

                } else {
                    semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
                    idExpression.type = "invalid";
                }
            } else {
                semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
                idExpression.type = "invalid";
            }
        }
    }

    private Symbol searchInSymbolTable(IdExpression idExpression, SymbolTable mst) {
        SymbolTable st_helper = mst.getParent().getBaseClass();

        if (mst.getSymbolTable().containsKey(idExpression.Id)) {
            return mst.getSymbolTable().get(idExpression.Id);
        } else {
            while (st_helper != null) {
                if (st_helper.getSymbolTable().containsKey(idExpression.Id)) {
                    return st_helper.getSymbolTable().get(idExpression.Id);
                } else {
                    st_helper = st_helper.getBaseClass();
                }
            }
        }
        return null;
    }

    private boolean checkForBuiltIn(IdExpression idExpression) {
        if (Objects.equals(idExpression.Id, "print") && idExpression.expressions.size() == 1) {
            return Objects.equals(idExpression.expressions.getFirst().type, "int") ||
                    Objects.equals(idExpression.expressions.getFirst().type, "bool") ||
                    Objects.equals(idExpression.expressions.getFirst().type, "string");
        } else {
            return Objects.equals(idExpression.Id, "readInt") && idExpression.expressions.isEmpty();
        }
    }

    private boolean checkForReadLine(IdExpression idExpression) {
        return Objects.equals(idExpression.Id, "readLine") && idExpression.expressions.isEmpty();

    }
}

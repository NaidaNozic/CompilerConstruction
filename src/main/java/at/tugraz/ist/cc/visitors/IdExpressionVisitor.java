package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.*;
import at.tugraz.ist.cc.program.*;

import java.util.*;

import static org.antlr.v4.analysis.LeftRecursiveRuleAnalyzer.ASSOC.right;

public class IdExpressionVisitor extends JovaBaseVisitor<IdExpression> {

    public List<SemanticError> semanticErrors;
    public Expression leftExprOfDotOperator = null;
    public boolean invalidDotOperatorRightExpr = false;
    public IdExpressionVisitor(List<SemanticError> semanticErrors) {
        this.semanticErrors = semanticErrors;
    }
    public IdExpressionVisitor(List<SemanticError> semanticErrors, Expression leftExprOfDotOperator,
                               boolean invalidDotOperatorRightExpr) {
        this.semanticErrors = semanticErrors;
        this.leftExprOfDotOperator = leftExprOfDotOperator;
        this.invalidDotOperatorRightExpr = invalidDotOperatorRightExpr;
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

        if(leftExprOfDotOperator != null && !invalidDotOperatorRightExpr) {
            checkExpressionWithDotOperator(idExpression, method_symbol_table);
        } else if(invalidDotOperatorRightExpr) {
            idExpression.type = "invalid";
        }else{
            checkExpression(idExpression, method_symbol_table);
        }
        return idExpression;
    }

    private void checkExpressionWithDotOperator(IdExpression rightExpr, SymbolTable mst){
        if(leftExprOfDotOperator.type.equals("invalid")){
            rightExpr.type = "invalid";
            return;
        }
        if (leftExprOfDotOperator.type.equals("int") || leftExprOfDotOperator.type.equals("bool") ||
                leftExprOfDotOperator.type.equals("string") || leftExprOfDotOperator.type.equals("nix")) {
            if (rightExpr.childCount == 1) {
                semanticErrors.add(new FieldUnknownError(leftExprOfDotOperator.type, rightExpr.Id, rightExpr.line));
            } else {
                ArrayList<String> arg_types = getArgTypes(rightExpr.expressions, mst);
                semanticErrors.add(new MemberFunctionUnknownError(leftExprOfDotOperator.type,
                        rightExpr.Id, arg_types, rightExpr.line));
            }
            rightExpr.type = "invalid";
            return;
        }

        if(rightExpr.childCount == 1){
            Symbol symbol = searchInSymbolTableWithDotOperator(rightExpr, mst, null);
            if(symbol != null && (symbol.getSymbolType() != Symbol.SymbolType.METHOD) ){
                rightExpr.type = symbol.getType().type;
            }else{
                semanticErrors.add(new FieldUnknownError(leftExprOfDotOperator.type, rightExpr.Id, rightExpr.line));
                rightExpr.type = "invalid";
            }
        }else{
            ArrayList<String> arg_types = getArgTypes(rightExpr.expressions, mst);
            Symbol symbol = searchInSymbolTableWithDotOperator(rightExpr, mst, Symbol.SymbolType.METHOD);

            if (checkForPrint(rightExpr)) {
                ExpressionVisitor.leafCounter--;
                semanticErrors.add(new MemberFunctionUnknownError(leftExprOfDotOperator.type,
                        rightExpr.Id, arg_types, rightExpr.line));
                rightExpr.type = "int";
                return;
            } else if (checkForReadInt(rightExpr)) {
                ExpressionVisitor.leafCounter++;
                semanticErrors.add(new MemberFunctionUnknownError(leftExprOfDotOperator.type,
                        rightExpr.Id, arg_types, rightExpr.line));
                rightExpr.type = "int";
                return;
            } else if (checkForReadLine(rightExpr)) {
                ExpressionVisitor.leafCounter++;
                semanticErrors.add(new MemberFunctionUnknownError(leftExprOfDotOperator.type,
                        rightExpr.Id, arg_types, rightExpr.line));
                rightExpr.type = "string";
                return;
            }

            ExpressionVisitor.leafCounter++;

            for (Expression id : rightExpr.expressions) {
                if (Objects.equals(id.type, "invalid")) {
                    rightExpr.type = "invalid";
                    return;
                }
            }

            if (symbol != null && symbol.getSymbolType() == Symbol.SymbolType.METHOD) {
                if (symbol.getParamSymbols().size() == arg_types.size()) {
                    ArrayList<Symbol> param_symbols = symbol.getParamSymbols();

                    for (int i = 0; i < param_symbols.size(); i++) {
                        if (!Objects.equals(param_symbols.get(i).getType().type, arg_types.get(i))) {
                            semanticErrors.add(new MemberFunctionUnknownError(leftExprOfDotOperator.type,
                                    rightExpr.Id, arg_types, rightExpr.line));
                            rightExpr.type = "invalid";
                            return;
                        }
                    }
                    rightExpr.type = symbol.getType().type;
                    return;
                }
            }
            semanticErrors.add(new MemberFunctionUnknownError(leftExprOfDotOperator.type,
                    rightExpr.Id, arg_types, rightExpr.line));
            rightExpr.type = "invalid";
        }
    }
    private void checkExpression(IdExpression idExpression, SymbolTable mst) {
        if (idExpression.childCount == 1) { //that's a variable
            Symbol symbol = searchInSymbolTable(idExpression, mst);

            if (symbol != null && (symbol.getSymbolType() != Symbol.SymbolType.METHOD)) {
                ExpressionVisitor.leafCounter++;
                idExpression.type = symbol.getType().type;
            } else {
                semanticErrors.add(new IDUnknownError(idExpression.Id, idExpression.line));
                idExpression.type = "invalid";
            }
        } else { //that's a method
            ArrayList<String> arg_types = new ArrayList<>();
            Symbol symbol = searchInSymbolTable(idExpression, mst);

            if (checkForPrint(idExpression)) {
                ExpressionVisitor.leafCounter--;
                idExpression.type = "int";
                return;
            } else  if (checkForReadInt(idExpression)) {
                ExpressionVisitor.leafCounter++;
                idExpression.type = "int";
                return;
            } else if (checkForReadLine(idExpression)) {
                ExpressionVisitor.leafCounter++;
                idExpression.type = "string";
                return;
            }

            ExpressionVisitor.leafCounter++;

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

                    for (int i = 0; i < param_symbols.size(); i++) {
                        if (!Objects.equals(param_symbols.get(i).getType().type, arg_types.get(i))) {
                            semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
                            idExpression.type = "invalid";
                            return;
                        }
                    }

                    idExpression.type = symbol.getType().type;
                    return;
                }
            }

            semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
            idExpression.type = "invalid";
        }
    }


    private ArrayList<String> getArgTypes(List<Expression> expressions, SymbolTable mst) {
        ArrayList<String> arg_types = new ArrayList<>();
        for (Expression expr : expressions){
            if(expr.type.equals("this")){
                arg_types.add(mst.getParent().getScopeId());
            }else{
                arg_types.add(expr.type);
            }
        }
        return arg_types;
    }

    private Symbol searchInSymbolTableWithDotOperator(IdExpression rightExpr, SymbolTable mst, Symbol.SymbolType type)
    {
        SymbolTable classSymbolTable;
        if(this.leftExprOfDotOperator instanceof ThisLiteral){
            classSymbolTable = mst.getParent();
        }else {
            classSymbolTable = SymbolTableStorage.getSymbolTableFromStorage(leftExprOfDotOperator.type);
        }
        SymbolTable baseSymbolTable = classSymbolTable.getBaseClass();

        HashMap<String, Symbol> st = classSymbolTable.getSymbolTable();
        if(type != null) {
            for (Symbol symbol : st.values()) {
                if (symbol.getId().equals(rightExpr.Id) && symbol.getSymbolType() == type) {
                    return symbol;
                }
            }
        }else{
            if(st.containsKey(rightExpr.Id)){
                return st.get(rightExpr.Id);
            }
        }
        while(baseSymbolTable != null){
            if(type != null) {
                for (Symbol symbol : baseSymbolTable.getSymbolTable().values()) {
                    if (symbol.getId().equals(rightExpr.Id) && symbol.getSymbolType() == type) {
                        return symbol;
                    }
                }
            }else{
                if(baseSymbolTable.getSymbolTable().containsKey(rightExpr.Id)){
                    return baseSymbolTable.getSymbolTable().get(rightExpr.Id);
                }
            }
            baseSymbolTable = baseSymbolTable.getBaseClass();
        }
        return null;
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
    private boolean checkForPrint(IdExpression idExpression) {
        return (Objects.equals(idExpression.Id, "print") && idExpression.expressions.size() == 1) &&
                (Objects.equals(idExpression.expressions.getFirst().type, "int") ||
                        Objects.equals(idExpression.expressions.getFirst().type, "bool") ||
                        Objects.equals(idExpression.expressions.getFirst().type, "string"));
    }

    private boolean checkForReadInt(IdExpression idExpression) {
        return Objects.equals(idExpression.Id, "readInt") && idExpression.expressions.isEmpty();
    }

    private boolean checkForReadLine(IdExpression idExpression) {
        return Objects.equals(idExpression.Id, "readLine") && idExpression.expressions.isEmpty();
    }

}

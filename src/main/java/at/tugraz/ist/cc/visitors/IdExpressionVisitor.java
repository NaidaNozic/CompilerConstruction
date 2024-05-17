package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.*;
import at.tugraz.ist.cc.program.*;

import java.util.*;

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
        String type = leftExprOfDotOperator.type;
        if(type.equals("this")){
            type = mst.getParent().getScopeId();
            if(withinMain(mst)){
                semanticErrors.add(new MainError(leftExprOfDotOperator.line));
            }
        }
        if (leftExprOfDotOperator.type.equals("int") || leftExprOfDotOperator.type.equals("bool") ||
                leftExprOfDotOperator.type.equals("string") || leftExprOfDotOperator.type.equals("nix")) {
            if (rightExpr.childCount == 1) {
                semanticErrors.add(new FieldUnknownError(type, rightExpr.Id, rightExpr.line));
            } else {
                ArrayList<String> arg_types = getArgTypes(rightExpr.expressions, mst);
                semanticErrors.add(new MemberFunctionUnknownError(type,
                        rightExpr.Id, arg_types, rightExpr.line));
            }
            rightExpr.type = "invalid";
            return;
        }

        if(rightExpr.childCount == 1){
            Symbol symbol = searchInSymbolTableWithDotOperator(rightExpr, mst);
            if(symbol != null && (symbol.getSymbolType() != Symbol.SymbolType.METHOD) ){
                rightExpr.type = symbol.getType().type;
            }else{
                semanticErrors.add(new FieldUnknownError(type, rightExpr.Id, rightExpr.line));
                rightExpr.type = "invalid";
            }
        }else{
            ArrayList<String> arg_types = getArgTypes(rightExpr.expressions, mst);
            Symbol symbol = searchInSymbolTableWithDotOperator(rightExpr, mst);

            if (checkForPrint(rightExpr)) {
                semanticErrors.add(new MemberFunctionUnknownError(type,
                        rightExpr.Id, arg_types, rightExpr.line));
                rightExpr.type = "int";
                return;
            } else if (checkForReadInt(rightExpr)) {
                semanticErrors.add(new MemberFunctionUnknownError(type,
                        rightExpr.Id, arg_types, rightExpr.line));
                rightExpr.type = "int";
                return;
            } else if (checkForReadLine(rightExpr)) {
                semanticErrors.add(new MemberFunctionUnknownError(type,
                        rightExpr.Id, arg_types, rightExpr.line));
                rightExpr.type = "string";
                return;
            }


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
                        if (!Objects.equals(param_symbols.get(i).getType().type, arg_types.get(i)) &&
                                !(arg_types.get(i).equals("nix") && !(param_symbols.get(i).getType().type.equals("int") ||
                                        param_symbols.get(i).getType().type.equals("bool")))) {
                            //test if we have a base class and subclass sent to it
                            if(!getBaseAndSubclassComparison(param_symbols.get(i).getType().type, arg_types.get(i))){
                                semanticErrors.add(new MemberFunctionUnknownError(type,
                                        rightExpr.Id, arg_types, rightExpr.line));
                                rightExpr.type = "invalid";
                                return;
                            }
                        }
                    }
                    rightExpr.type = symbol.getType().type;
                    return;
                }
            }
            semanticErrors.add(new MemberFunctionUnknownError(type,
                    rightExpr.Id, arg_types, rightExpr.line));
            rightExpr.type = "invalid";
        }
    }

    private boolean withinMain(SymbolTable mst) {
        if(Objects.equals(mst.getScopeId(), "main")) return true;
        SymbolTable parent = mst.getParent();
        while(parent != null){
            if(Character.isUpperCase(parent.getScopeId().charAt(0))) return false;
            if(Objects.equals(parent.getScopeId(), "main")) return true;
            parent = parent.getParent();
        }
        return false;
    }

    private boolean getBaseAndSubclassComparison(String base, String subclass){

        SymbolTable classSymbolTable = SymbolTableStorage.getSymbolTableFromStorage(subclass);
        if(classSymbolTable == null) return false;
        SymbolTable baseSymbolTable = classSymbolTable.getBaseClass();
        while (baseSymbolTable != null){
            if(baseSymbolTable.getScopeId().equals(base)){
                return true;
            }
            baseSymbolTable = baseSymbolTable.getBaseClass();
        }
        return false;
    }

    private void checkExpression(IdExpression idExpression, SymbolTable mst) {
        if (idExpression.childCount == 1) { //that's a variable
            Symbol symbol = searchInSymbolTable(idExpression, mst);

            if (symbol != null && (symbol.getSymbolType() != Symbol.SymbolType.METHOD)) {
                idExpression.type = symbol.getType().type;
            } else {
                semanticErrors.add(new IDUnknownError(idExpression.Id, idExpression.line));
                idExpression.type = "invalid";
            }
        } else { //that's a method
            ArrayList<String> arg_types = new ArrayList<>();
            Symbol symbol = searchInSymbolTable(idExpression, mst);

            if (checkForPrint(idExpression)) {
                idExpression.type = "int";
                return;
            } else  if (checkForReadInt(idExpression)) {
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

                    for (int i = 0; i < param_symbols.size(); i++) {
                        if (!(Objects.equals(param_symbols.get(i).getType().type, arg_types.get(i)) ||
                                checkClassTypes(param_symbols.get(i).getType().type, arg_types.get(i)))) {
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

    private boolean checkClassTypes(String paramType, String argType){
        SymbolTable arg_type_class = SymbolTableStorage.getSymbolTableFromStorage(argType);

        if (Objects.equals(argType, "nix")) {
            return !(Objects.equals(paramType, "string") || Objects.equals(paramType, "bool") || Objects.equals(paramType, "int"));
        }

        if (arg_type_class != null) {
            SymbolTable base_class = arg_type_class.getBaseClass();

            while (base_class != null) {
                if (Objects.equals(base_class.getScopeId(), paramType)) {
                    return true;
                } else {
                    base_class = base_class.getBaseClass();
                }
            }
        }
        return false;
    }


    private ArrayList<String> getArgTypes(List<Expression> expressions, SymbolTable mst) {
        ArrayList<String> arg_types = new ArrayList<>();
        for (Expression expr : expressions){
            if(expr.type.equals("this")){
                if(withinMain(mst)){
                    semanticErrors.add(new MainError(expr.line));
                }
                arg_types.add(mst.getParent().getScopeId());
            }else{
                arg_types.add(expr.type);
            }
        }
        return arg_types;
    }

    private Symbol searchInSymbolTableWithDotOperator(IdExpression rightExpr, SymbolTable mst)
    {
        SymbolTable classSymbolTable;
        if(this.leftExprOfDotOperator instanceof ThisLiteral){
            classSymbolTable = mst.getParent();
        }else {
            classSymbolTable = SymbolTableStorage.getSymbolTableFromStorage(leftExprOfDotOperator.type);
        }
        SymbolTable baseSymbolTable = classSymbolTable.getBaseClass();

        if (classSymbolTable.getSymbolTable().containsKey(rightExpr.Id)) {
            return classSymbolTable.getSymbolTable().get(rightExpr.Id);
        } else {
            while (baseSymbolTable != null) {
                if (baseSymbolTable.getSymbolTable().containsKey(rightExpr.Id)) {
                    return baseSymbolTable.getSymbolTable().get(rightExpr.Id);
                } else {
                    baseSymbolTable = baseSymbolTable.getBaseClass();
                }
            }
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

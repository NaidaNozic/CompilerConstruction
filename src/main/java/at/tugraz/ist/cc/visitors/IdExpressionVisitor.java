package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.IDUnknownError;
import at.tugraz.ist.cc.error.semantic.MethodUnknownError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.*;

public class IdExpressionVisitor extends JovaBaseVisitor<IdExpression> {

    public List<SemanticError> semanticErrors;
    public static ArrayList<SemanticError> reportErrorLater = new ArrayList<>();
    public static int layer = -1;
    public IdExpressionVisitor(List<SemanticError> semanticErrors) {
        this.semanticErrors = semanticErrors;
    }
    @Override
    public IdExpression visitId_expr(JovaParser.Id_exprContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);
        ArrayList<String> method_param_types = new ArrayList<>();

        String method_scope_id = SymbolTableStorage.getMethodScopeIDFromStack();
        SymbolTable method_symbol_table = SymbolTableStorage.getSymbolTableFromStorage(method_scope_id);

        IdExpression idExpression = new IdExpression(ctx.getChild(0).getText(), ctx.ID().getSymbol().getLine(), ctx.getChildCount());

        layer++;


        for (int i = 2; i < ctx.getChildCount(); i += 2) {
            Expression expr = expressionVisitor.visit(ctx.getChild(i));

            if (expr != null) {
                idExpression.expressions.add(expr);
            }
        }

        if(layer == 0){
            iterateExpressions(idExpression, method_symbol_table);
        }

        layer--;



        System.out.println("break");


        return idExpression;
    }

    private String iterateExpressions(Expression e, SymbolTable mst) {
        if(e instanceof BooleanLiteral) {
            return "bool";
        } else if(e instanceof IntegerLiteral) {
            return "int";
        } else if(e instanceof StringLiteral) {
            return "string";
        } else if (e instanceof IdExpression) {
            return iterateExpressions((IdExpression)e, mst);
        } else if(e instanceof OperatorExpression) {
            iterateExpressions(((OperatorExpression) e).rightExpression, mst); //control of right part
            return iterateExpressions(((OperatorExpression) e).leftExpression, mst);
        }
        return null;
    }

    private String iterateExpressions(IdExpression idExpression, SymbolTable mst) {
        ArrayList<String> arg_types = new ArrayList<>();

        if (idExpression.childCount == 1) {
            Symbol symbol = searchInSymbolTable(idExpression, mst);

            if (symbol != null){
                return symbol.getType().type;
            } else {
                semanticErrors.add(new IDUnknownError(idExpression.Id, idExpression.line));
            }

        } else {
            for (Expression e : idExpression.expressions) {
                if(e instanceof BooleanLiteral) {
                    arg_types.add("bool");
                } else if(e instanceof IntegerLiteral) {
                    arg_types.add("int");
                } else if(e instanceof StringLiteral) {
                    arg_types.add("string");
                } else if (e instanceof IdExpression) {
                    arg_types.add(iterateExpressions((IdExpression) e, mst));
                } else if(e instanceof OperatorExpression) {
                    arg_types.add(iterateExpressions(((OperatorExpression) e).leftExpression, mst));
                    iterateExpressions(((OperatorExpression) e).rightExpression, mst); //control of right part
                }
            }

            Symbol method_symbol = searchInSymbolTable(idExpression, mst);

            if (method_symbol != null && !arg_types.contains(null)) {
                if (method_symbol.getParamSymbols().size() == arg_types.size()) {
                    ArrayList<Symbol> param_symbols = method_symbol.getParamSymbols();

                    for (int i = 0; i < param_symbols.size(); i++){
                        if (!Objects.equals(param_symbols.get(i).getType().type, arg_types.get(i))) {
                            semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
                            return null;
                        }
                    }

                    return method_symbol.getType().type;
                } else {
                    semanticErrors.add(new MethodUnknownError(idExpression.Id, arg_types, idExpression.line));
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


//    private boolean checkCurrentClass(IdExpression idExpression, SymbolTable method_symbol_table, ArrayList<String> method_param_types) {
//        Symbol method_symbol;
//
//        if(method_symbol_table.getSymbolTable().containsKey(idExpression.Id)){
//            method_symbol = method_symbol_table.getSymbolTable().get(idExpression.Id);
//
//            if(method_symbol.getSymbolType() == Symbol.SymbolType.METHOD &&
//                    method_symbol.getParamSymbols().size() == method_param_types.size() &&
//                    reportErrorLater.isEmpty()){
//
//                ArrayList<Symbol> param_symbols = method_symbol.getParamSymbols();
//
//                for (int i = 0; i < method_param_types.size(); i++) {
//
//                    if(!Objects.equals(method_param_types.get(i), param_symbols.get(i).getType().type)) {
//                        return false;
//                    }
//                }
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private boolean checkBaseClass(IdExpression idExpression, SymbolTable method_symbol_table, ArrayList<String> method_param_types){
//        SymbolTable base_class_table;
//        Symbol base_class_method;
//
//        if(method_symbol_table.getParent().getBaseClass() != null) {
//            base_class_table = SymbolTableStorage.getSymbolTableFromStorage(method_symbol_table.getParent().getBaseClass().getScopeId());
//
//            if(base_class_table.getSymbolTable().containsKey(idExpression.Id)){
//                base_class_method = base_class_table.getSymbolTable().get(idExpression.Id);
//
//                if(base_class_method.getSymbolType() == Symbol.SymbolType.METHOD &&
//                        base_class_method.getParamSymbols().size() == method_param_types.size() &&
//                        reportErrorLater.isEmpty()){
//
//                    ArrayList<Symbol> param_symbols = base_class_method.getParamSymbols();
//
//                    for (int i = 0; i < method_param_types.size(); i++) {
//
//                        if(!Objects.equals(method_param_types.get(i), param_symbols.get(i).getType().type)) {
//                            return false;
//                        }
//                    }
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }



}

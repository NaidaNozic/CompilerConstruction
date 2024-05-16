package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.*;
import at.tugraz.ist.cc.program.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class BlockVisitor extends JovaBaseVisitor<Block> {

    private ParamList methodParams;
    public static boolean validExpression = false;
    public List<SemanticError> semanticErrors;
    public BlockVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    public BlockVisitor(List<SemanticError> semanticErrors, ParamList methodParams_){
        this.semanticErrors = semanticErrors;
        methodParams = methodParams_;
    }
    @Override
    public Block visitBlock(JovaParser.BlockContext ctx) {
        Block block = new Block();
        DeclarationVisitor declarationVisitor = new DeclarationVisitor(semanticErrors);
        IfStatementVisitor ifStatementVisitor = new IfStatementVisitor(semanticErrors);
        WhileStatementVisitor whileStatementVisitor = new WhileStatementVisitor(semanticErrors);
        ReturnStatementVisitor returnStatementVisitor = new ReturnStatementVisitor(semanticErrors);
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);

        String method_scope_id = SymbolTableStorage.getCurrentMethodScopeID();
        System.out.println("Hello! " + method_scope_id);
        SymbolTable method_symbol_table = SymbolTableStorage.getSymbolTableFromStorage(method_scope_id);
        SymbolTable parent = method_symbol_table.getParent();
        String method_type = parent.getSymbolTable().get(method_scope_id).getType().type;
        System.out.println("Parent: " + parent.getSymbolTable().get(method_scope_id).getType().type);

        HashSet<String> double_decl_helper = new HashSet<>();

        if (methodParams != null){
            for (Param meth_p : methodParams.params) {
                double_decl_helper.add(meth_p.id);
            }
        }

        for(int i=0; i<ctx.getChildCount(); i++){
            if (ctx.getChild(i) instanceof JovaParser.DeclContext) {
                Declaration declaration = declarationVisitor.visit(ctx.getChild(i));


                if(ctx.parent instanceof JovaParser.If_stmtContext ||
                        ctx.parent instanceof JovaParser.While_stmtContext){
                    semanticErrors.add(new CannotDeclVarError(declaration.params.getFirst().line));
                } else {
                    checkConflicts(declaration, double_decl_helper);
                    block.declarations.add(declaration);

                    method_symbol_table.updateSymbolTable(declaration);
                }

            } else if (ctx.getChild(i) instanceof JovaParser.ExprContext) {


                Expression expression = expressionVisitor.visit(ctx.getChild(i));
                block.expressions.add(expression);




                if (expression instanceof OperatorExpression &&
                        !(((OperatorExpression) expression).operator).equals("=") && !validExpression &&
                        !(((OperatorExpression) expression).operator).equals(".")){
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                } else if (expression instanceof IntegerLiteral ||
                        expression instanceof BooleanLiteral ||
                        expression instanceof StringLiteral) {
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                } else if (expression instanceof IdExpression && isNoMethod((IdExpression) expression, method_symbol_table)) {
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                } else if (expression instanceof ParanthesisExpression && !validExpression) {
                    Expression e_helper = expression;

                    while (e_helper instanceof ParanthesisExpression) {
                        e_helper = ((ParanthesisExpression) e_helper).expression;
                    }

                    if (e_helper instanceof IdExpression && isNoMethod((IdExpression) e_helper, method_symbol_table)) {
                        semanticErrors.add(new AssignmentExpectedError(expression.line));
                    } else if (e_helper instanceof  OperatorExpression || e_helper instanceof IntegerLiteral ||
                            e_helper instanceof BooleanLiteral || e_helper instanceof StringLiteral) {
                        semanticErrors.add(new AssignmentExpectedError(expression.line));
                    }

                }

                ExpressionVisitor.reset();
                validExpression = false;



            } else if (ctx.getChild(i) instanceof JovaParser.If_stmtContext) {

                IfStatement ifStatement = ifStatementVisitor.visit((ctx.getChild(i)));
                block.ifStatements.add(ifStatement);

            } else if (ctx.getChild(i) instanceof JovaParser.While_stmtContext) {

                WhileStatement whileStatement = whileStatementVisitor.visit(ctx.getChild(i));
                block.whileStatements.add(whileStatement);

            } else if (ctx.getChild(i) instanceof  JovaParser.Return_stmtContext) {

                ReturnStatement returnStatement = returnStatementVisitor.visit(ctx.getChild(i));

                if (!(Objects.equals(method_type, returnStatement.expression.type) ||
                        checkClassTypes(method_type, returnStatement.expression.type))) {
                    semanticErrors.add(new ReturnTypeError(returnStatement.line));
                }

            }
        }
        return block;
    }

    private boolean checkClassTypes(String methodType, String retType){
        SymbolTable ret_type_class = SymbolTableStorage.getSymbolTableFromStorage(retType);

        if (Objects.equals(retType, "nix")) {
            return !(Objects.equals(methodType, "string") || Objects.equals(methodType, "bool") || Objects.equals(methodType, "int"));
        }

        if (ret_type_class != null) {
            SymbolTable base_class = ret_type_class.getBaseClass();

            while (base_class != null) {
                if (Objects.equals(base_class.getScopeId(), methodType)) {
                    return true;
                } else {
                    base_class = base_class.getBaseClass();
                }
            }
        }
        return false;
    }



    //new version
    private void checkConflicts(Declaration declaration, HashSet<String> double_decl_helper) {


        for (Param p : declaration.params) {
            if (double_decl_helper.contains(p.id)) {
                semanticErrors.add(new IDDoubleDeclError(p.id, p.line));
            }
            double_decl_helper.add(p.id);
        }
    }


    private boolean isNoMethod(IdExpression idExpression, SymbolTable mst)  {
        Symbol symbol;

        if (mst.getSymbolTable().containsKey(idExpression.Id)){
            symbol = mst.getSymbolTable().get(idExpression.Id);
            return symbol.getSymbolType() != Symbol.SymbolType.METHOD;
        } else {
            SymbolTable st_helper = mst.getParent().getBaseClass();

            while (st_helper != null) {
                if (st_helper.getSymbolTable().containsKey(idExpression.Id)) {
                    symbol = st_helper.getSymbolTable().get(idExpression.Id);
                    return symbol.getSymbolType() != Symbol.SymbolType.METHOD;
                } else {
                    st_helper = st_helper.getBaseClass();
                }
            }
        }

        return false;
    }

}

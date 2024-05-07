package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.AssignmentExpectedError;
import at.tugraz.ist.cc.error.semantic.CannotDeclVarError;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.List;
import java.util.Objects;

public class BlockVisitor extends JovaBaseVisitor<Block> {

    private ParamList methodParams;
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
        SymbolTable method_symbol_table = SymbolTableStorage.getSymbolTableFromStorage(method_scope_id);

        for(int i=0; i<ctx.getChildCount(); i++){
            if (ctx.getChild(i) instanceof JovaParser.DeclContext) {

                Declaration declaration = declarationVisitor.visit(ctx.getChild(i));
                checkConflicts(declaration, block.declarations);
                block.declarations.add(declaration);

                method_symbol_table.updateSymbolTable(declaration);

                if(ctx.parent instanceof JovaParser.If_stmtContext ||
                   ctx.parent instanceof JovaParser.While_stmtContext){
                    semanticErrors.add(new CannotDeclVarError(declaration.params.getFirst().line));
                } else {
                    if (methodParams != null) {
                        for (Param p : declaration.params) {
                            for (Param new_p : methodParams.params) {
                                if (Objects.equals(p.id, new_p.id)) {
                                    semanticErrors.add(new IDDoubleDeclError(p.id, p.line));
                                }
                            }
                        }
                    }
                }

            } else if (ctx.getChild(i) instanceof JovaParser.ExprContext) {


                Expression expression = expressionVisitor.visit(ctx.getChild(i));
                block.expressions.add(expression);

                if (expression instanceof OperatorExpression &&
                        !(((OperatorExpression) expression).operator).equals("=")){
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                } else if (expression instanceof IntegerLiteral ||
                        expression instanceof BooleanLiteral ||
                        expression instanceof StringLiteral) {
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                } else if (expression instanceof IdExpression && isNoMethod((IdExpression) expression, method_symbol_table)) {
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                }



            } else if (ctx.getChild(i) instanceof JovaParser.If_stmtContext) {

                IfStatement ifStatement = ifStatementVisitor.visit((ctx.getChild(i)));
                block.ifStatements.add(ifStatement);

            } else if (ctx.getChild(i) instanceof JovaParser.While_stmtContext) {

                WhileStatement whileStatement = whileStatementVisitor.visit(ctx.getChild(i));
                block.whileStatements.add(whileStatement);

            } else if (ctx.getChild(i) instanceof  JovaParser.Return_stmtContext) {

                ReturnStatement returnStatement = returnStatementVisitor.visit(ctx.getChild(i));
                block.returnStatements.add(returnStatement);

            }
        }
        return block;
    }
    private void checkConflicts(Declaration declaration, List<Declaration> declarations) {

        for (Param currentParam : declaration.params) {
            for (Declaration decl : declarations) {
                for (Param otherParam : decl.params) {
                    if (otherParam.id.equals(currentParam.id)) {
                        semanticErrors.add(new IDDoubleDeclError(currentParam.id, currentParam.line));
                    }
                }
            }
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

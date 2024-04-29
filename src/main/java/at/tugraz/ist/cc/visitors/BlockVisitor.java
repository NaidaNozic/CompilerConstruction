package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTable;
import at.tugraz.ist.cc.SymbolTableStorage;
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

        SymbolTable methodSymboltable = SymbolTableStorage.popSymbolTableStack();

        for(int i=0; i<ctx.getChildCount(); i++){
            if (ctx.getChild(i) instanceof JovaParser.DeclContext) {

                Declaration declaration = declarationVisitor.visit(ctx.getChild(i));
                checkConflicts(declaration, block.declarations);
                block.declarations.add(declaration);

                methodSymboltable.updateSymbolTable(declaration);

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

                SymbolTableStorage.pushSymbolTableStack(methodSymboltable);

                Expression expression = expressionVisitor.visit(ctx.getChild(i));
                block.expressions.add(expression);

                if (expression instanceof OperatorExpression &&
                    !(((OperatorExpression) expression).operator).equals("=")){
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                } else if (expression instanceof IntegerLiteral ||
                           expression instanceof BooleanLiteral ||
                           expression instanceof StringLiteral) {
                    semanticErrors.add(new AssignmentExpectedError(expression.line));
                }


                SymbolTableStorage.popSymbolTableStack();


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

        SymbolTableStorage.pushSymbolTableStack(methodSymboltable);

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

}

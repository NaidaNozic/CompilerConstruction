package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.List;

public class BlockVisitor extends JovaBaseVisitor<Block> {

    public List<SemanticError> semanticErrors;
    public BlockVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Block visitBlock(JovaParser.BlockContext ctx) {
        Block block = new Block();
        DeclarationVisitor declarationVisitor = new DeclarationVisitor(semanticErrors);
        IfStatementVisitor ifStatementVisitor = new IfStatementVisitor(semanticErrors);
        WhileStatementVisitor whileStatementVisitor = new WhileStatementVisitor(semanticErrors);
        ReturnStatementVisitor returnStatementVisitor = new ReturnStatementVisitor(semanticErrors);
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);

        for(int i=0; i<ctx.getChildCount(); i++){
            if (ctx.getChild(i) instanceof JovaParser.DeclContext) {

                Declaration declaration = declarationVisitor.visit(ctx.getChild(i));
                checkConflicts(declaration, block.declarations);
                block.declarations.add(declaration);

            } else if (ctx.getChild(i) instanceof JovaParser.ExprContext) {

                Expression expression = expressionVisitor.visit(ctx.getChild(i));
                block.expressions.add(expression);

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

}

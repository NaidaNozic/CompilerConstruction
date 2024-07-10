package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.WhileStatement;

import java.util.List;

public class WhileStatementVisitorCG extends JovaBaseVisitor<WhileStatement> {
    private boolean visit_expression;

    public List<SemanticError> semanticErrors;
    public WhileStatementVisitorCG(boolean visit_expression){
        this.visit_expression = visit_expression;
    }
    @Override
    public WhileStatement visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        if (this.visit_expression){
            return new WhileStatement(new ExpressionVisitorCG().visit(ctx.getChild(2)), new Block(), 0);
        }

        new BlockVisitorCG().visit(ctx.getChild(4));

        return null;
    }
}

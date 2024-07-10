package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;

import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.IfStatement;




public class IfStatementVisitorCG extends JovaBaseVisitor<IfStatement> {
    private boolean visit_expression;
    private boolean then_block;

    public IfStatementVisitorCG(boolean visit_expression, boolean then_block){
        this.visit_expression = visit_expression;
        this.then_block = then_block;
    }

    @Override
    public IfStatement visitIf_stmt(JovaParser.If_stmtContext ctx) {
        if (this.visit_expression){
            return new IfStatement(new ExpressionVisitorCG().visit(ctx.getChild(2)), new Block(), new Block(), 0);
        } else if (this.then_block){
            new BlockVisitorCG().visit(ctx.getChild(4));
        } else {
            if (ctx.getChildCount()==7){
                new BlockVisitorCG().visit(ctx.getChild(6));
            }
        }

        return null;
    }
}

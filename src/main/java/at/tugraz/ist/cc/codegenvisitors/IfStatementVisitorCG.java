package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.ConditionTypeError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.IfStatement;
import at.tugraz.ist.cc.visitors.BlockVisitor;
import at.tugraz.ist.cc.visitors.ExpressionVisitor;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.List;

public class IfStatementVisitorCG extends JovaBaseVisitor<IfStatement> {

    public IfStatementVisitorCG(){}

    @Override
    public IfStatement visitIf_stmt(JovaParser.If_stmtContext ctx) {

        ExpressionVisitorCG expressionVisitor = new ExpressionVisitorCG();
        BlockVisitorCG thenBlockVisitor = new BlockVisitorCG();
        BlockVisitorCG elseBlockVisitor = new BlockVisitorCG();

        int line = ((TerminalNodeImpl)ctx.getChild(0)).getSymbol().getLine();

        IfStatement ifStatement = new IfStatement(new ExpressionVisitorCG().visit(ctx.getChild(2)), new Block(), new Block(), line);

//        ifStatement.thenBlock = thenBlockVisitor.visit(ctx.getChild(4));
//        if (ctx.getChildCount()==7){
//            ifStatement.elseBlock = elseBlockVisitor.visit(ctx.getChild(6));
//        }

        return ifStatement;
    }
}

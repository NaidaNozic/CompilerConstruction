package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.ConditionTypeError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.WhileStatement;
import at.tugraz.ist.cc.visitors.BlockVisitor;
import at.tugraz.ist.cc.visitors.ExpressionVisitor;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.List;

public class WhileStatementVisitorCG extends JovaBaseVisitor<WhileStatement> {

    public List<SemanticError> semanticErrors;
    public WhileStatementVisitorCG(){}
    @Override
    public WhileStatement visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        int line = ((TerminalNodeImpl)ctx.getChild(0)).getSymbol().getLine();
        WhileStatement whileStatement = new WhileStatement(new ExpressionVisitorCG().visit(ctx.getChild(2)), new Block(), line);

        new BlockVisitorCG().visit(ctx.getChild(4));

        return null;
    }
}

package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.IfStatement;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.List;

public class IfStatementVisitor extends JovaBaseVisitor<IfStatement> {

    public List<SemanticError> semanticErrors;
    public IfStatementVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }

    @Override
    public IfStatement visitIf_stmt(JovaParser.If_stmtContext ctx) {

        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);
        BlockVisitor thenBlockVisitor = new BlockVisitor(semanticErrors);
        BlockVisitor elseBlockVisitor = new BlockVisitor(semanticErrors);

        Expression expression = expressionVisitor.visit(ctx.getChild(2));
        int line = ((TerminalNodeImpl)ctx.getChild(0)).getSymbol().getLine();

        IfStatement ifStatement = new IfStatement(expression, new Block(), new Block(), line);

        ifStatement.thenBlock = thenBlockVisitor.visit(ctx.getChild(4));
        if (ctx.getChildCount()==7){
            ifStatement.elseBlock = elseBlockVisitor.visit(ctx.getChild(6));
        }

        return ifStatement;
    }
}

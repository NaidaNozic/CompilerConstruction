package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.ReturnStatement;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.List;

public class ReturnStatementVisitor extends JovaBaseVisitor<ReturnStatement> {

    public List<SemanticError> semanticErrors;

    public ReturnStatementVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public ReturnStatement visitReturn_stmt(JovaParser.Return_stmtContext ctx) {

        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);
        Expression expression = expressionVisitor.visit(ctx.getChild(1));
        int line = ((TerminalNodeImpl)ctx.getChild(0)).getSymbol().getLine();

        return new ReturnStatement(expression, line);
    }
}

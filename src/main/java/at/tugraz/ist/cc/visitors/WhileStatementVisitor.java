package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.ConditionTypeError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.Expression;
import at.tugraz.ist.cc.program.WhileStatement;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.List;

public class WhileStatementVisitor extends JovaBaseVisitor<WhileStatement> {

    public List<SemanticError> semanticErrors;
    public WhileStatementVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public WhileStatement visitWhile_stmt(JovaParser.While_stmtContext ctx) {

        ExpressionVisitor expressionVisitor = new ExpressionVisitor(semanticErrors);
        BlockVisitor blockVisitor = new BlockVisitor(semanticErrors);
        Expression expression = expressionVisitor.visit(ctx.getChild(2));
        int line = ((TerminalNodeImpl)ctx.getChild(0)).getSymbol().getLine();

        WhileStatement whileStatement = new WhileStatement(expression, new Block(), line);

        whileStatement.block = blockVisitor.visit(ctx.getChild(4));

        if(!expression.type.equals("bool")){
            semanticErrors.add(new ConditionTypeError(expression.line));
        }

        return whileStatement;
    }
}

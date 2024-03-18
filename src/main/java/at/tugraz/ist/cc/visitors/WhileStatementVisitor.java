package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.WhileStatement;

import java.util.List;

public class WhileStatementVisitor extends JovaBaseVisitor<WhileStatement> {

    public List<SemanticError> semanticErrors;
    public WhileStatementVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public WhileStatement visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        return super.visitWhile_stmt(ctx);
    }
}

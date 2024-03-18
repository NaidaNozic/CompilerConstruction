package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.ReturnStatement;

import java.util.List;

public class ReturnStatementVisitor extends JovaBaseVisitor<ReturnStatement> {

    public List<SemanticError> semanticErrors;

    public ReturnStatementVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public ReturnStatement visitReturn_stmt(JovaParser.Return_stmtContext ctx) {
        return super.visitReturn_stmt(ctx);
    }
}

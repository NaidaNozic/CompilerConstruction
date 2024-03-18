package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.IfStatement;

import java.util.List;

public class IfStatementVisitor extends JovaBaseVisitor<IfStatement> {

    public List<SemanticError> semanticErrors;
    public IfStatementVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }

    @Override
    public IfStatement visitIf_stmt(JovaParser.If_stmtContext ctx) {
        return super.visitIf_stmt(ctx);
    }
}

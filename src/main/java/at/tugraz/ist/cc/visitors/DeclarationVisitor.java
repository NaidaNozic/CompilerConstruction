package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Declaration;

import java.util.List;

public class DeclarationVisitor extends JovaBaseVisitor<Declaration> {

    public List<SemanticError> semanticErrors;

    public DeclarationVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Declaration visitDecl(JovaParser.DeclContext ctx) {
        return super.visitDecl(ctx);
    }
}

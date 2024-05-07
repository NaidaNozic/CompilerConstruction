package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Declaration;
import at.tugraz.ist.cc.program.Param;

import java.util.List;

public class DeclarationVisitor extends JovaBaseVisitor<Declaration> {

    public List<SemanticError> semanticErrors;

    public DeclarationVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Declaration visitDecl(JovaParser.DeclContext ctx) {
        Declaration declaration = new Declaration();
        ParamVisitor paramVisitor = new ParamVisitor(semanticErrors);

        if (ctx.getChild(0) instanceof JovaParser.ParamContext) {
            Param param = paramVisitor.visit(ctx.getChild(0));
            declaration.params.add(param);
            for (int i = 1; i < ctx.getChildCount(); i++){
                if (ctx.getChild(i).getText().equals(",")){
                    declaration.params.add(new Param(param.type, ctx.getChild(i+1).getText(), param.line, 0));
                }
            }
        }

        return declaration;
    }
}

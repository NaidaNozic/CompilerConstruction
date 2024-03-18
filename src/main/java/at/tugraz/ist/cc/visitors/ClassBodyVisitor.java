package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.ClassBody;
import at.tugraz.ist.cc.program.Declaration;
import at.tugraz.ist.cc.program.Param;
import org.antlr.v4.codegen.model.decl.Decl;

import java.util.List;

public class ClassBodyVisitor extends JovaBaseVisitor<ClassBody> {

    public List<SemanticError> semanticErrors;

    public ClassBodyVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public ClassBody visitClass_body(JovaParser.Class_bodyContext ctx) {
        ClassBody classBody = new ClassBody();
        DeclarationVisitor declarationVisitor = new DeclarationVisitor(semanticErrors);
        MethodVisitor methodVisitor = new MethodVisitor(semanticErrors);

        for (int i=0; i<ctx.getChildCount(); i++){

            if (ctx.getChild(i) instanceof JovaParser.DeclContext) {

                Declaration declaration = declarationVisitor.visit(ctx.getChild(i));
                checkConflicts(declaration, classBody.declarations);
                classBody.declarations.add(declaration);

            }
        }
        return classBody;
    }
    private void checkConflicts(Declaration declaration, List<Declaration> declarations) {

        for (Param currentParam : declaration.params) {
            for (Declaration decl : declarations) {
                for (Param otherParam : decl.params) {
                    if (otherParam.id.equals(currentParam.id)) {
                        semanticErrors.add(new IDDoubleDeclError(currentParam.id, currentParam.line));
                    }
                }
            }
        }

    }
}


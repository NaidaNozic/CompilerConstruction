package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.ClassDeclaration;
import at.tugraz.ist.cc.program.Program;

import java.util.List;

public class ClassDeclarationVisitor extends JovaBaseVisitor<ClassDeclaration> {
    public List<SemanticError> semanticErrors;

    public ClassDeclarationVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public ClassDeclaration visitClass_decl(JovaParser.Class_declContext ctx) {
        ClassDeclaration classDeclaration;
        ClassBodyVisitor classBodyVisitor = new ClassBodyVisitor(semanticErrors);
        return null;
        //return this.visitChildren(ctx);
    }

}

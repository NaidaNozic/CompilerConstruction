package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassDeclarationVisitor extends JovaBaseVisitor<ClassDeclaration> {
    public List<SemanticError> semanticErrors;
    public ClassDeclarationVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public ClassDeclaration visitClass_decl(JovaParser.Class_declContext ctx) {
        ClassDeclaration classDeclaration;
        ClassBodyVisitor classBodyVisitor = new ClassBodyVisitor(semanticErrors);


        String id= ctx.getChild(0).getText();
        int line =ctx.CLASS_ID(0).getSymbol().getLine();
        if(Objects.equals(ctx.getChild(1).getText(), "{")) {
            ClassBody classBody = classBodyVisitor.visit(ctx.getChild(2));
            List<String> methods = null; //getMethods(classBody);
            List<String> declarations = null; //getDeclarations(classBody);
            classDeclaration = new ClassDeclaration(id, classBody, line);
            return classDeclaration;
        } else {
            ClassBody classBody = classBodyVisitor.visit(ctx.getChild(4));
            String superclass = ctx.getChild(2).getText();
            List<String> methods = null; // getMethods(classBody);
            List<String> declarations = null; // getDeclarations(classBody);
            classDeclaration = new ClassDeclaration(id, superclass, classBody, line);
            return classDeclaration;
        }
    }
}


package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTable;
import at.tugraz.ist.cc.SymbolTableStorage;
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

        if(SymbolTableStorage.getMode()) {
            SymbolTable classTable = new SymbolTable(id);

            SymbolTableStorage.addSymbolTableToStorage(classTable);
            SymbolTableStorage.pushSymbolTableStack(classTable);

            classBodyVisitor.visit(ctx.getChild(2));

            return new ClassDeclaration(id, null, -1);
        }


        if(Objects.equals(ctx.getChild(1).getText(), "{")) {
            SymbolTable classSymbolTable = SymbolTableStorage.getSymbolTableFromStorage(id);
            SymbolTableStorage.pushSymbolTableStack(classSymbolTable);


            ClassBody classBody = classBodyVisitor.visit(ctx.getChild(2));
            List<String> methods = null; //getMethods(classBody);
            List<String> declarations = null; //getDeclarations(classBody);
            classDeclaration = new ClassDeclaration(id, classBody, line);

            SymbolTableStorage.popSymbolTableStack();
            return classDeclaration;
        } else {
            String superclass = ctx.getChild(2).getText();

            SymbolTable classSymbolTable = SymbolTableStorage.getSymbolTableFromStorage(id);
            classSymbolTable.addBaseClass(SymbolTableStorage.getSymbolTableFromStorage(superclass));
            SymbolTableStorage.pushSymbolTableStack(classSymbolTable);


            ClassBody classBody = classBodyVisitor.visit(ctx.getChild(4));
            List<String> methods = null; // getMethods(classBody);
            List<String> declarations = null; // getDeclarations(classBody);
            classDeclaration = new ClassDeclaration(id, superclass, classBody, line);

            return classDeclaration;
        }
    }
}


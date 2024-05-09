package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

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
            SymbolTableStorage.pushScopeID(id);

            if (ctx.getChildCount() == 4)
                classBodyVisitor.visit(ctx.getChild(2));
            else
                classBodyVisitor.visit(ctx.getChild(4)); //inheritance

            return null;
        } else {
            if(Objects.equals(ctx.getChild(1).getText(), "{")) {
                SymbolTableStorage.pushScopeID(id);

                ClassBody classBody = classBodyVisitor.visit(ctx.getChild(2));
                List<String> methods = null; //getMethods(classBody);
                List<String> declarations = null; //getDeclarations(classBody);
                classDeclaration = new ClassDeclaration(id, classBody, line);
            } else {
                String superclass = ctx.getChild(2).getText();
                SymbolTable classSymbolTable = SymbolTableStorage.getSymbolTableFromStorage(id);
                classSymbolTable.addBaseClass(SymbolTableStorage.getSymbolTableFromStorage(superclass));
                SymbolTableStorage.pushScopeID(id);


                ClassBody classBody = classBodyVisitor.visit(ctx.getChild(4));
                List<String> methods = null; // getMethods(classBody);
                List<String> declarations = null; // getDeclarations(classBody);
                classDeclaration = new ClassDeclaration(id, superclass, classBody, line);
            }
            return classDeclaration;
        }
    }
}


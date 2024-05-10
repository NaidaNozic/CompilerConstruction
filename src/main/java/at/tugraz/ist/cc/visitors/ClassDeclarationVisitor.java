package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassDeclarationVisitor extends JovaBaseVisitor<ClassDeclaration> {
    public List<SemanticError> semanticErrors;
    ArrayList<Pair<String, String>> waiting_list;

    public ClassDeclarationVisitor(List<SemanticError> semanticErrors, ArrayList<Pair<String, String>> to_wait){
        this.semanticErrors = semanticErrors;
        this.waiting_list = to_wait;
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

            if (ctx.getChildCount() == 4) {
                classBodyVisitor.visit(ctx.getChild(2));
            }
            else {
                String superclass_id= ctx.getChild(2).getText();
                waiting_list.add(new Pair<>(id, superclass_id));
                classBodyVisitor.visit(ctx.getChild(4)); //inheritance
            }


            return null;
        } else {
            if(Objects.equals(ctx.getChild(1).getText(), "{")) {
                SymbolTableStorage.pushScopeID(id);

                ClassBody classBody = classBodyVisitor.visit(ctx.getChild(2));
                classDeclaration = new ClassDeclaration(id, classBody, line);
            } else {
                String superclass = ctx.getChild(2).getText();
                SymbolTableStorage.pushScopeID(id);


                ClassBody classBody = classBodyVisitor.visit(ctx.getChild(4));
                classDeclaration = new ClassDeclaration(id, superclass, classBody, line);
            }
            return classDeclaration;
        }
    }
}


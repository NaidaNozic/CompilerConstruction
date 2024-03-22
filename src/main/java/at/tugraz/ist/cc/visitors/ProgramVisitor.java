package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.IDUnknownError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProgramVisitor extends JovaBaseVisitor<Program> {
    public List<SemanticError> semanticErrors;
    private Set<String> classNames = new HashSet<>(); // To keep track of declared class names

    public ProgramVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public Program visitProgram(JovaParser.ProgramContext ctx) {
        Program program = new Program();
        ClassDeclarationVisitor classDeclarationVisitor = new ClassDeclarationVisitor(semanticErrors);

        for (int i = 0; i < ctx.getChildCount(); i++){
            ClassDeclaration classDeclaration = classDeclarationVisitor.visit(ctx.getChild(i));
            if (classNames.contains(classDeclaration.id)) {
                semanticErrors.add(new IDDoubleDeclError(classDeclaration.id, classDeclaration.line));
            } else {
                classNames.add(classDeclaration.id);
            }
            program.classDeclarations.add(classDeclaration);

        }

        for (ClassDeclaration classDeclaration : program.classDeclarations){
            if (classDeclaration.classBody == null) return program;
            List<Declaration> declarations = classDeclaration.classBody.declarations;
            List<Method> methods = classDeclaration.classBody.methods;

            if (declarations != null)
                for (Declaration declaration : declarations){
                    if (!declaration.params.isEmpty())
                        checkUndefined(declaration.params.getFirst());
                }

            if (methods != null)
                for (Method method : methods){
                    checkUndefined(method.param);

                    for (Param param1 : method.paramList.params){
                        checkUndefined(param1);
                    }

                    for (Declaration declaration : method.block.declarations){
                        if (!declaration.params.isEmpty())
                            checkUndefined(declaration.params.getFirst());
                    }
                }
        }

        return program;
    }

    public void checkUndefined(Param param) {
        if (param.type instanceof IntegerType
                || param.type instanceof StringType
                || param.type instanceof BoolType)
            return;

        if(!classNames.contains(param.type.type)){
            semanticErrors.add(new IDUnknownError(param.type.type, param.line));
        }
    }
}

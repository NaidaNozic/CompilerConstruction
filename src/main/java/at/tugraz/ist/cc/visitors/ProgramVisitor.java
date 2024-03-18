package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.ClassDeclaration;
import at.tugraz.ist.cc.program.Program;

import java.util.ArrayList;
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

        return program;
    }
}

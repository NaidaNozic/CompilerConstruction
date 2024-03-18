package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.ClassDeclaration;
import at.tugraz.ist.cc.program.Program;

import java.util.ArrayList;
import java.util.List;

public class ProgramVisitor extends JovaBaseVisitor<Program> {

    public List<SemanticError> semanticErrors;
    @Override
    public Program visitProgram(JovaParser.ProgramContext ctx) {
        Program program = new Program();
        semanticErrors = new ArrayList<>();
        ClassDeclarationVisitor classDeclarationVisitor = new ClassDeclarationVisitor(semanticErrors);

        for (int i = 0; i < ctx.getChildCount(); i++){
            program.classDeclarations.add(classDeclarationVisitor.visit(ctx.getChild(i)));
        }

        return program;
    }
}

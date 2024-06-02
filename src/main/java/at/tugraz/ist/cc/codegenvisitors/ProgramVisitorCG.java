package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JasminFileGenerator;
import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;

import at.tugraz.ist.cc.program.*;


public class ProgramVisitorCG extends JovaBaseVisitor<Program> {

    public String fileName;


    public ProgramVisitorCG(String fileName){
        this.fileName = fileName;
    }

    @Override
    public Program visitProgram(JovaParser.ProgramContext ctx) {

        for (int i = 0; i < ctx.getChildCount(); i++) {
            JasminFileGenerator.reset();
            JasminFileGenerator.writeContent(".source " + fileName);
            new ClassDeclVisitorCG().visit(ctx.getChild(i));
            JasminFileGenerator.makeFile();
        }



        return null;
    }

}


package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JasminFileGenerator;
import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;

import at.tugraz.ist.cc.program.*;


public class ProgramVisitorCG extends JovaBaseVisitor<Program> {

    public String fileName;
    private final String outPath;

    //every label is unique in the program
    public static int if_counter;
    public static int while_counter;
    public static int relation_counter;


    public ProgramVisitorCG(String fileName, String outPath){
        this.fileName = fileName;
        this.outPath = outPath;
        if_counter = 0;
        while_counter = 0;
        relation_counter = 0;
    }

    @Override
    public Program visitProgram(JovaParser.ProgramContext ctx) {

        for (int i = 0; i < ctx.getChildCount(); i++) {
            JasminFileGenerator.reset();
            JasminFileGenerator.writeContent(".source " + fileName);
            new ClassDeclVisitorCG().visit(ctx.getChild(i));
            JasminFileGenerator.makeFile(outPath);
        }



        return null;
    }

}


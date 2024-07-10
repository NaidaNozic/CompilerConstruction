package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.program.ClassDeclaration;
import java.util.Objects;

public class ClassDeclVisitorCG extends JovaBaseVisitor<ClassDeclaration> {

    public ClassDeclVisitorCG(){}

    @Override
    public ClassDeclaration visitClass_decl(JovaParser.Class_declContext ctx) {

        String id= ctx.getChild(0).getText();
        CodeGenStorage.setClassID(id);

        if(Objects.equals(ctx.getChild(1).getText(), "{")) {
            JasminFileGenerator.writeContent(".class public " + id);
            JasminFileGenerator.writeContent(".super " + "java/lang/Object");
            JasminFileGenerator.writeContent(".method public <init>()V\n " +
                                             "aload_0\n " +
                                             "invokespecial java/lang/Object/<init>()V\n " +
                                             "return\n" +
                                             ".end method\n");

            new ClassBodyVisitorCG().visit(ctx.getChild(2));

        } else {
            String superclass = ctx.getChild(2).getText();

            JasminFileGenerator.writeContent(".class public " + id);
            JasminFileGenerator.writeContent(".super " + superclass);
            JasminFileGenerator.writeContent(".method public <init>()V\n " +
                                             "aload_0\n " +
                                             "invokespecial " + superclass + "/<init>()V\n " +
                                             "return\n" +
                                             ".end method\n");


            new ClassBodyVisitorCG().visit(ctx.getChild(4));
        }

        return null;
    }
}


package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.program.ClassBody;
import at.tugraz.ist.cc.program.Method;
import at.tugraz.ist.cc.program.Param;

import java.util.Objects;

public class ClassBodyVisitorCG extends JovaBaseVisitor<ClassBody> {


    public ClassBodyVisitorCG(){}

    @Override
    public ClassBody visitClass_body(JovaParser.Class_bodyContext ctx) {


        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof JovaParser.DeclContext) {
                new DeclarationVisitorCG(DeclarationVisitorCG.Location.CLASS_BODY).visit(ctx.getChild(i));

            } else if (ctx.getChild(i) instanceof JovaParser.MethodContext) {

                Method method = new MethodVisitorCG(true).visit(ctx.getChild(i));
                startJasminMethod(method);

                CodeGenStorage.setLocalArray(SymbolTableStorage.getSymbolTableFromStorage(method.param.id));

                new MethodVisitorCG(false).visit(ctx.getChild(i));

                finishJasminMethod(method);

            }
        }
        return null;
    }


    private void startJasminMethod(Method m){
        StringBuilder method_content = new StringBuilder(".method public ");

        CodeGenStorage.setMethodID(m.param.id);

        if (Objects.equals(m.param.id, "main")){
            method_content.append("static main([Ljava/lang/String;)V");
        } else {
            method_content.append(m.param.id).append("(");

            for (Param p : m.paramList.params){
                switch (m.param.type.type){
                    case "int" : method_content.append("I"); break;
                    case "bool" : method_content.append("Z"); break;
                    case "string" : method_content.append("Ljava/lang/String;"); break;
                    default: method_content.append("L").append(p.type.type).append(";"); break;
                }
            }

            method_content.append(")");

            switch (m.param.type.type){
                case "int" : method_content.append("I"); break;
                case "bool" : method_content.append("Z"); break;
                case "string" : method_content.append("Ljava/lang/String;"); break;
                case "nix" : method_content.append("V"); break;
                default: method_content.append("L").append(m.param.type.type).append(";"); break;
            }
        }

        method_content.append("\n.limit stack 10\n.limit locals 20");




        JasminFileGenerator.writeContent(method_content.toString());
    }

    private void finishJasminMethod(Method m) {
        String method_end = "";

        if (!Objects.equals(m.param.id, "main")){
            method_end = switch (m.param.type.type) {
                case "int", "bool" -> "i";
                case "nix" -> "";
                default -> "a";
            };
        }



        method_end += ("return\n.end method");

        JasminFileGenerator.writeContent(method_end);

    }


}


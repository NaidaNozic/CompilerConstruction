package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JasminFileGenerator;
import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.program.Declaration;
import at.tugraz.ist.cc.program.Param;

import java.util.ArrayList;

public class DeclarationVisitorCG extends JovaBaseVisitor<Declaration> {

    public enum Location {
        CLASS_BODY, METHOD_BODY
    }

    private Location location;

    public DeclarationVisitorCG(Location location){
        this.location = location;
    }

    @Override
    public Declaration visitDecl(JovaParser.DeclContext ctx) {
        ArrayList<Param> declarations = new ArrayList<>();


        if (ctx.getChild(0) instanceof JovaParser.ParamContext) {
            Param param = new ParamVisitorCG().visit(ctx.getChild(0));
            declarations.add(param);
            for (int i = 1; i < ctx.getChildCount(); i++){
                if (ctx.getChild(i).getText().equals(",")){
                    declarations.add(new Param(param.type, ctx.getChild(i+1).getText(), param.line, 0));
                }
            }

            if (location == Location.CLASS_BODY) {
                for (Param p : declarations){
                    buildJasminField(p);
                }
            }


        }
        return null;
    }

    private void buildJasminField(Param p){
        String param_content;

        param_content = ".field public " + p.id;

        switch (p.type.type){
            case "int" : param_content += " I"; break;
            case "bool" : param_content += " Z"; break;
            case "string" : param_content += " Ljava/lang/String;"; break;
            default: param_content += (" L" + p.type.type + ";"); break;
        }

        JasminFileGenerator.writeContent(param_content);
    }
}

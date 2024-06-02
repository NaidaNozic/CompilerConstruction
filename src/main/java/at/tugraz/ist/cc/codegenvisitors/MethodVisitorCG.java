package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTable;
import at.tugraz.ist.cc.SymbolTableStorage;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.Method;
import at.tugraz.ist.cc.program.Param;
import at.tugraz.ist.cc.program.ParamList;
import at.tugraz.ist.cc.visitors.BlockVisitor;
import at.tugraz.ist.cc.visitors.ParamVisitor;

import java.util.ArrayList;
import java.util.List;

public class MethodVisitorCG extends JovaBaseVisitor<Method> {
    private boolean getMethodInfo;

    public MethodVisitorCG(boolean getMethodInfo){
        this.getMethodInfo = getMethodInfo;
    }

    @Override
    public Method visitMethod(JovaParser.MethodContext ctx) {
        ParamList param_list = new ParamList();

        for(int children = 0; children < ctx.getChildCount(); children++)
        {
            if(ctx.getChild(children) instanceof JovaParser.Param_listContext){
                for(int params = 0; params < ctx.getChild(children).getChildCount(); params++){

                    if(ctx.getChild(children).getChild(params) instanceof JovaParser.ParamContext){

                        param_list.params.add(new ParamVisitorCG().visit(ctx.getChild(2).getChild(params)));
                    }
                }
            }
        }


        if (!getMethodInfo){
            if(param_list.params.isEmpty()){
                new BlockVisitorCG().visit(ctx.getChild(3));

            } else {
                new BlockVisitorCG().visit(ctx.getChild(4));
            }
        }


        return new Method(new Block(), param_list, new ParamVisitorCG().visit(ctx.getChild(0)));
    }
}

package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.MethodDoubleDefError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.Block;
import at.tugraz.ist.cc.program.Method;
import at.tugraz.ist.cc.program.Param;
import at.tugraz.ist.cc.program.ParamList;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.List;

public class MethodVisitor extends JovaBaseVisitor<Method> {

    public List<SemanticError> semanticErrors;
    public List<String> methodSignatures;

    public MethodVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
        this.methodSignatures = new ArrayList<>();
    }
    @Override
    public Method visitMethod(JovaParser.MethodContext ctx) {
        ParamList param_list = new ParamList();
        List<String> double_decl_list = new ArrayList<>();
        ParamVisitor paramVisitor = new ParamVisitor(semanticErrors);
        BlockVisitor blockVisitor = new BlockVisitor();

        for(int params = 0; params < ctx.getChild(2).getChildCount(); params++){

            if(ctx.getChild(2).getChild(params) instanceof JovaParser.ParamContext){

                Param param = paramVisitor.visit(ctx.getChild(2).getChild(params));

                if(double_decl_list.contains(param.id)){
                    semanticErrors.add(new IDDoubleDeclError(param.id, param.line));
                }
                double_decl_list.add(param.id);

                param_list.params.add(param);
            }
        }



    return new Method(blockVisitor.visit(ctx.getChild(4)),
                      param_list,
                      paramVisitor.visit(ctx.getChild(0)));
    }
}

package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTable;
import at.tugraz.ist.cc.SymbolTableStorage;
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

        if(SymbolTableStorage.getMode()) {
            for(int children = 0; children < ctx.getChildCount(); children++)
            {
                if(ctx.getChild(children) instanceof JovaParser.Param_listContext){
                    for(int params = 0; params < ctx.getChild(children).getChildCount(); params++){

                        if(ctx.getChild(children).getChild(params) instanceof JovaParser.ParamContext){
                            param_list.params.add(paramVisitor.visit(ctx.getChild(2).getChild(params)));

                        }
                    }
                }
            }

            return new Method(null, param_list, paramVisitor.visit(ctx.getChild(0)));
        }
        else {
            String method_scope_id = SymbolTableStorage.getMethodScopeIDFromStack();
            SymbolTable method_symbol_table = SymbolTableStorage.getSymbolTableFromStorage(method_scope_id);

            for(int children = 0; children < ctx.getChildCount(); children++)
            {
                if(ctx.getChild(children) instanceof JovaParser.Param_listContext){
                    for(int params = 0; params < ctx.getChild(children).getChildCount(); params++){

                        if(ctx.getChild(children).getChild(params) instanceof JovaParser.ParamContext){

                            Param param = paramVisitor.visit(ctx.getChild(2).getChild(params));

                            if(double_decl_list.contains(param.id)){
                                semanticErrors.add(new IDDoubleDeclError(param.id, param.line));
                            }
                            double_decl_list.add(param.id);

                            param_list.params.add(param);

                            method_symbol_table.updateSymbolTable(param);

                        }
                    }
                }
            }



            BlockVisitor blockVisitor = new BlockVisitor(semanticErrors, param_list);

            Block block;
            if(param_list.params.isEmpty()){
                block = blockVisitor.visit(ctx.getChild(3));

            } else {
                block = blockVisitor.visit(ctx.getChild(4));

            }


            return new Method(block, param_list, paramVisitor.visit(ctx.getChild(0)));
        }
    }
}

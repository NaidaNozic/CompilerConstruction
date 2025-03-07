package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.SymbolTable;
import at.tugraz.ist.cc.SymbolTableStorage;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.MethodDoubleDefError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.program.*;

import java.util.*;

public class ClassBodyVisitor extends JovaBaseVisitor<ClassBody> {

    public List<SemanticError> semanticErrors;

    public ClassBodyVisitor(List<SemanticError> semanticErrors){
        this.semanticErrors = semanticErrors;
    }
    @Override
    public ClassBody visitClass_body(JovaParser.Class_bodyContext ctx) {
        ClassBody classBody = new ClassBody();
        DeclarationVisitor declarationVisitor = new DeclarationVisitor(semanticErrors);
        MethodVisitor methodVisitor = new MethodVisitor(semanticErrors);

        String class_scope_id = SymbolTableStorage.popScopeID();
        SymbolTable class_symbol_table = SymbolTableStorage.getSymbolTableFromStorage(class_scope_id);

        if (SymbolTableStorage.isCollecting()) {
            for (int i=0; i<ctx.getChildCount(); i++){
                if (ctx.getChild(i) instanceof JovaParser.DeclContext) {
                    Declaration declaration = declarationVisitor.visit(ctx.getChild(i));
                    class_symbol_table.updateSymbolTable(declaration);

                }
                else if (ctx.getChild(i) instanceof JovaParser.MethodContext) {

                    //-------------- only to collect all methods for the symbol table -------------

                    Method method = methodVisitor.visit(ctx.getChild(i));
                    new SymbolTable(method.param.id, class_symbol_table);

                    class_symbol_table.updateSymbolTable(method);
                    //------------------------------------------------------------------------------
                }
            }

            return classBody;
        }
        else {
            for (int i=0; i<ctx.getChildCount(); i++){
                if (ctx.getChild(i) instanceof JovaParser.DeclContext) {
                    Declaration declaration = declarationVisitor.visit(ctx.getChild(i));
                    checkConflicts(declaration, classBody.declarations);
                    classBody.declarations.add(declaration);

                }
                else if (ctx.getChild(i) instanceof JovaParser.MethodContext) {
                    if (ctx.getChild(i) instanceof JovaParser.MethodContext) {
                        SymbolTable methodSymbolTable = class_symbol_table.getChild(ctx.getChild(i).getChild(0).getChild(1).getText());

                        methodSymbolTable.copyClassSymbolTable(class_symbol_table);

                        SymbolTableStorage.addSymbolTableToStorage(methodSymbolTable);

                        SymbolTableStorage.pushScopeID(class_scope_id);
                        SymbolTableStorage.pushScopeID(methodSymbolTable.getScopeId());


                        Method method = methodVisitor.visit(ctx.getChild(i));
                        checkBuiltInFunctions(method);
                        checkMethodConflicts(method, classBody.methods);
                        classBody.methods.add(method);

                        SymbolTableStorage.popScopeID();
                        SymbolTableStorage.popScopeID();
                    }
                }
            }
        }




        return classBody;
    }
    private void checkConflicts(Declaration declaration, List<Declaration> declarations) {

        for (Param currentParam : declaration.params) {
            for (Declaration decl : declarations) {
                for (Param otherParam : decl.params) {
                    if (otherParam.id.equals(currentParam.id)) {
                        semanticErrors.add(new IDDoubleDeclError(currentParam.id, currentParam.line));
                    }
                }
            }
        }

    }

    private void checkMethodConflicts(Method method, List<Method> methods){
        for(Method current_method : methods){
            if(Objects.equals(method.param.id, current_method.param.id)){
                if(method.paramList.params.size() == current_method.paramList.params.size()){
                    boolean all_same = true; //or no parameters passed

                    for(int it = 0; it < method.paramList.params.size(); it++){
                        if(!Objects.equals(method.paramList.params.get(it).type.type, current_method.paramList.params.get(it).type.type)){
                            all_same = false;
                            break;
                        }
                    }

                    if(all_same){
                        //make String Collection
                        List<String> param_types = new ArrayList<>();

                        for(Param param : method.paramList.params)
                        {
                            param_types.add(param.type.type);
                        }
                        semanticErrors.add(new MethodDoubleDefError(method.param.id, param_types, method.param.line));
                    }
                }
            }
        }
    }

    private void checkBuiltInFunctions(Method method){
        if(Objects.equals(method.param.id, "print") && method.paramList.params.size() == 1){
            String type = method.paramList.params.getFirst().type.type;
            if(Objects.equals(type, "int") || Objects.equals(type, "bool") || Objects.equals(type, "string"))
            {
                semanticErrors.add(new MethodDoubleDefError(method.param.id, Collections.singletonList(type), method.param.line));
            }
        }
        else if((Objects.equals(method.param.id, "readInt") || Objects.equals(method.param.id, "readLine")) &&
                method.paramList.params.isEmpty()){

            semanticErrors.add(new MethodDoubleDefError(method.param.id, new ArrayList<>(), method.param.line));
        }
    }

}


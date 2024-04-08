package at.tugraz.ist.cc.visitors;

import at.tugraz.ist.cc.JovaBaseVisitor;
import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.semantic.CyclicInheritanceError;
import at.tugraz.ist.cc.error.semantic.IDDoubleDeclError;
import at.tugraz.ist.cc.error.semantic.IDUnknownError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.error.warning.JovaWarning;
import at.tugraz.ist.cc.error.warning.OverrideWarning;
import at.tugraz.ist.cc.program.*;

import java.util.*;

public class ProgramVisitor extends JovaBaseVisitor<Program> {
    public List<SemanticError> semanticErrors;
    public List<JovaWarning> jovaWarnings;
    private Set<String> classNames = new HashSet<>(); // To keep track of declared class names
    private Map<String, String> inheritance = new HashMap<>(); // To check inherited classes
    //key: class, value: superclass (since there can be only one)

    public ProgramVisitor(List<SemanticError> semanticErrors, List<JovaWarning> jovaWarnings){
        this.semanticErrors = semanticErrors;
        this.jovaWarnings = jovaWarnings;
    }
    @Override
    public Program visitProgram(JovaParser.ProgramContext ctx) {
        Program program = new Program();
        ClassDeclarationVisitor classDeclarationVisitor = new ClassDeclarationVisitor(semanticErrors);

        for (int i = 0; i < ctx.getChildCount(); i++){
            ClassDeclaration classDeclaration = classDeclarationVisitor.visit(ctx.getChild(i));

            if (classNames.contains(classDeclaration.id)) {
                semanticErrors.add(new IDDoubleDeclError(classDeclaration.id, classDeclaration.line));
            } else {
                classNames.add(classDeclaration.id);
            }

            String superclass = classDeclaration.superclass;
            String baseclass = classDeclaration.id;
            Integer errorline = 0;
            if (checkInheritance(superclass, baseclass)){
                for(ClassDeclaration classdec : program.classDeclarations){
                    if(classdec.id.equals(classDeclaration.superclass)) errorline = classdec.line;
                }
                semanticErrors.add(new CyclicInheritanceError(classDeclaration.id, classDeclaration.superclass, errorline));
            }

            program.classDeclarations.add(classDeclaration);
            inheritance.put(baseclass, superclass);

        }

        for (ClassDeclaration classDeclaration : program.classDeclarations){
            if (classDeclaration.classBody == null) return program;
            List<Declaration> declarations = classDeclaration.classBody.declarations;
            List<Method> methods = classDeclaration.classBody.methods;

            if (declarations != null)
                for (Declaration declaration : declarations){
                    if (!declaration.params.isEmpty())
                        checkUndefined(declaration.params.getFirst());
                }

            if (methods != null)
                for (Method method : methods){
                    checkUndefined(method.param);

                    for (Param param1 : method.paramList.params){
                        checkUndefined(param1);
                    }

                    for (Declaration declaration : method.block.declarations){
                        if (!declaration.params.isEmpty())
                            checkUndefined(declaration.params.getFirst());
                    }
                }
            if(classDeclaration.superclass != null){
                Optional<ClassDeclaration> prevClass = program.classDeclarations.stream().filter(x -> x.id.equals(classDeclaration.superclass)).findFirst();
                System.out.println("Tu smo");
                if(prevClass.isPresent()){
                    ClassDeclaration truePrev = prevClass.get();
                    System.out.println("Imamo prijasnju klasu: " + truePrev.id);
                    List<Method> prevMeth = truePrev.classBody.methods;
                    for(Method meth : methods){
                        for(Method prev : prevMeth){
                            System.out.println("Poredimo " + meth.param.id + " i " + prev.param.id);
                            System.out.println("Imaju " + meth.param.type.type+ " i " + prev.param.type.type);
                            if(meth.param.id.equals(prev.param.id) && meth.param.type.type.equals(prev.param.type.type)) {
                                System.out.println("ista metoda " + meth.param.id);
                                checkMeths(meth, prev);
                            }
                        }
                    }
                }
            }
        }

        return program;
    }

    private void checkMeths(Method meth, Method prev) {
        ArrayList<String> errorparams = new ArrayList<>();
        if(meth.paramList.params.size() != prev.paramList.params.size()) return;
        for(int i = 0; i < meth.paramList.params.size(); i++){
            if(!meth.paramList.params.get(i).type.type.equals(prev.paramList.params.get(i).type.type)) return;
            errorparams.add(meth.paramList.params.get(i).type.type);
        }
        jovaWarnings.add(new OverrideWarning(meth.param.id, errorparams, meth.param.line, 3));
        System.out.println("Ista metoda " + meth.param.id);
    }


    private boolean checkInheritance(String superclass, String baseclass) {
        if(inheritance.containsKey(superclass)){
            String checkClass = inheritance.get(superclass);
            if(checkClass == null) return false;
            System.out.println("Checking the new " + checkClass + " and " + baseclass);
            if (checkClass.equals(baseclass)) return true;
            return checkInheritance(checkClass, baseclass);
        } else { return false; }
    }

    public void checkUndefined(Param param) {
        if (param.type instanceof IntegerType
                || param.type instanceof StringType
                || param.type instanceof BoolType)
            return;

        if(!classNames.contains(param.type.type)){
            semanticErrors.add(new IDUnknownError(param.type.type, param.line));
        }
    }
}

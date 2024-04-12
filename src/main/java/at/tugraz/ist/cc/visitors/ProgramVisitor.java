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
import java.util.stream.Collectors;

public class ProgramVisitor extends JovaBaseVisitor<Program> {
    public List<SemanticError> semanticErrors;
    public List<JovaWarning> jovaWarnings;
    private Set<String> classNames = new HashSet<>(); // To keep track of declared class names
    private Map<String, String> inheritance = new HashMap<>(); // To check inherited classes
    //key: class, value: superclass (since there can be only one)
    private ArrayList<ClassDeclaration> checkCycle = new ArrayList<>();

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

            checkCycle.add(classDeclaration);

            String superclass = classDeclaration.superclass;
            String baseclass = classDeclaration.id;
            Integer errorline = 0;
            //if (checkInheritance(superclass, baseclass)){
            //    for(ClassDeclaration classdec : program.classDeclarations){
            //        if(classdec.id.equals(classDeclaration.superclass)) errorline = classdec.line;
            //    }
            //    semanticErrors.add(new CyclicInheritanceError(classDeclaration.id, classDeclaration.superclass, errorline));
            //}
            System.out.println("_________________");
            program.classDeclarations.add(classDeclaration);
            inheritance.put(baseclass, superclass);

        }

        for (ClassDeclaration classDeclaration : program.classDeclarations){

            if (classDeclaration.superclass != null)
                checkUndefinedClassId(classDeclaration.superclass,classDeclaration.line);

            if (classDeclaration.classBody == null) return program;
            List<Declaration> declarations = classDeclaration.classBody.declarations;
            List<Method> methods = classDeclaration.classBody.methods;

            if (declarations != null)
                for (Declaration declaration : declarations){
                    validateDeclarations(declaration);
                }

            if (methods != null)
                for (Method method : methods){
                    checkUndefinedParam(method.param);

                    for (Param param1 : method.paramList.params){
                        checkUndefinedParam(param1);
                    }
                    validateBlock(method.block);
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

            if(newCheckInheritance(checkCycle, classDeclaration)){
                semanticErrors.add(new CyclicInheritanceError(classDeclaration.id, classDeclaration.superclass, classDeclaration.line));
                checkCycle.remove(classDeclaration);
            }
        }

        return program;
    }

    private void validateBlock (Block block){
        // Will be used to find the IDUnknownError
        for (Declaration declaration : block.declarations) {
            validateDeclarations(declaration);
        }
        for (IfStatement ifStatement : block.ifStatements){
            validateExpression(ifStatement.expression);
            validateBlock(ifStatement.thenBlock);
            validateBlock(ifStatement.elseBlock);
        }
        for (WhileStatement whileStatement : block.whileStatements){
            validateExpression(whileStatement.expression);
            validateBlock(whileStatement.block);
        }

        for (ReturnStatement returnStatement : block.returnStatements){
            validateExpression(returnStatement.expression);
        }

        for (Expression expression : block.expressions){
            validateExpression(expression);
        }
    }

    private void validateDeclarations(Declaration declaration){
        // Will be used to find the IDUnknownError
        if (!declaration.params.isEmpty())
            checkUndefinedParam(declaration.params.getFirst());
    }
    private void validateExpression(Expression expression){
        // Will be used to find the IDUnknownError
        if (expression instanceof NewClassExpression) {
            checkUndefinedClassId(((NewClassExpression) expression).classId, expression.line);

        } else if (expression instanceof IdExpression) {
            for (Expression expression1 : ((IdExpression) expression).expressions){
                validateExpression(expression1);
            }

        } else if (expression instanceof OperatorExpression) {
            if (((OperatorExpression) expression).leftExpression instanceof NewClassExpression) {
                checkUndefinedClassId(((NewClassExpression) ((OperatorExpression) expression).leftExpression).classId,
                                       expression.line);
            } else {
                validateExpression(((OperatorExpression) expression).leftExpression);
            }
            if (((OperatorExpression) expression).rightExpression instanceof NewClassExpression) {
                checkUndefinedClassId(((NewClassExpression) ((OperatorExpression) expression).rightExpression).classId,
                                       expression.line);
            } else {
                validateExpression(((OperatorExpression) expression).rightExpression);
            }

        } else if (expression instanceof AddNotExpression) {
            validateExpression(((AddNotExpression) expression).expression);
        } else if (expression instanceof  ParanthesisExpression) {
            validateExpression(((ParanthesisExpression) expression).expression);
        }
    }

    private void checkClass(String supclass, int i){
        //for()
    }

    private void checkMeths(Method meth, Method prev) {
        ArrayList<String> errorparams = new ArrayList<>();
        if(meth.paramList.params.size() != prev.paramList.params.size()) return;
        for(int i = 0; i < meth.paramList.params.size(); i++){
            if(!meth.paramList.params.get(i).type.type.equals(prev.paramList.params.get(i).type.type)) return;
            errorparams.add(meth.paramList.params.get(i).type.type);
        }
        jovaWarnings.add(new OverrideWarning(meth.param.id, errorparams, meth.param.line, meth.param.column));
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

    private boolean newCheckInheritance(ArrayList<ClassDeclaration> checkList, ClassDeclaration baseclass){
        for(ClassDeclaration traverse : checkList){
            if(traverse.id.equals(baseclass.superclass)){
                if(traverse.superclass == null) return false;
                if(traverse.superclass.equals(baseclass.id)) return true;
                ClassDeclaration temp = new ClassDeclaration(baseclass.id, traverse.superclass, baseclass.classBody, baseclass.line);
                ArrayList<ClassDeclaration> removed = new ArrayList<>(checkList.stream().filter(elem -> !elem.id.equals(traverse.id)).toList());
                return newCheckInheritance(removed, temp);
            }
        }
        return false;
    }

    private void checkUndefinedParam(Param param) {
        if (param.type instanceof IntegerType
                || param.type instanceof StringType
                || param.type instanceof BoolType)
            return;

        if(!classNames.contains(param.type.type)){
            semanticErrors.add(new IDUnknownError(param.type.type, param.line));
        }
    }

    private void checkUndefinedClassId(String class_id, int line) {
        if(!classNames.contains(class_id)){
            semanticErrors.add(new IDUnknownError(class_id, line));
        }
    }
}

package at.tugraz.ist.cc.codegenvisitors;

import at.tugraz.ist.cc.*;
import at.tugraz.ist.cc.program.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockVisitorCG extends JovaBaseVisitor<Block> {

    public BlockVisitorCG(){}

    @Override
    public Block visitBlock(JovaParser.BlockContext ctx) {

        for(int i=0; i<ctx.getChildCount(); i++){
            if (ctx.getChild(i) instanceof JovaParser.DeclContext) {
                new DeclarationVisitorCG(DeclarationVisitorCG.Location.METHOD_BODY).visit(ctx.getChild(i));

            } else if (ctx.getChild(i) instanceof JovaParser.ExprContext) {
                Expression e = new ExpressionVisitorCG().visit(ctx.getChild(i));

                buildJasminExpression(e);

            } else if (ctx.getChild(i) instanceof JovaParser.If_stmtContext) {

                int current_if_layer = ProgramVisitorCG.if_counter++;
                ProgramVisitorCG.if_counter++;// every if should have a unique label

                IfStatement ifStatement = new IfStatementVisitorCG(true, true).visit((ctx.getChild(i)));
                buildJasminExpression(ifStatement.expression);

                JasminFileGenerator.writeContent("ifeq IF_" + current_if_layer); //if if_statement evaluates to false...
                new IfStatementVisitorCG(false, true).visit((ctx.getChild(i))); //visit then block
                JasminFileGenerator.writeContent("goto  FI_" + current_if_layer); //skip else
                JasminFileGenerator.writeContent("IF_" + current_if_layer + ":"); //...jump to this label
                new IfStatementVisitorCG(false, false).visit((ctx.getChild(i))); //visit else block or leave if_stmt
                JasminFileGenerator.writeContent("FI_" + current_if_layer + ":"); //skip else

            } else if (ctx.getChild(i) instanceof JovaParser.While_stmtContext) {

                int current_layer_while = ProgramVisitorCG.while_counter;
                ProgramVisitorCG.while_counter++; // every while should have a unique label

                JasminFileGenerator.writeContent("WHILE_" + current_layer_while + ":"); //return to head of while/start of while

                WhileStatement whileStatement = new WhileStatementVisitorCG(true).visit(ctx.getChild(i));
                buildJasminExpression(whileStatement.expression);
                JasminFileGenerator.writeContent("ifeq WHILE_END_" + current_layer_while); //if while evaluates to false...
                new WhileStatementVisitorCG(false).visit(ctx.getChild(i));
                JasminFileGenerator.writeContent("goto WHILE_" + current_layer_while); //go back to start of while evaluation
                JasminFileGenerator.writeContent("WHILE_END_" + current_layer_while + ":"); //...jump to this label


            }
        }
        return null;
    }



    private void buildJasminExpression(Expression e){
        if (e instanceof AddNotExpression addNotExpression){
            buildJasminExpression(addNotExpression.expression);

            int counter = ProgramVisitorCG.relation_counter;
            ProgramVisitorCG.relation_counter++;

            if (Objects.equals(addNotExpression.operator, "-")){
                JasminFileGenerator.writeContent("ineg");
            } else {
                JasminFileGenerator.writeContent("ifeq switch_to_true_" + counter + "\n" +
                                                 "iconst_0\n" +
                                                 "goto escape_" + counter + "\n" +
                                                 "switch_to_true_" + counter + ":\n" +
                                                 "iconst_1\n" +
                                                 "escape_" + counter + ":");
            }
        }
        else if (e instanceof IntegerLiteral integerLiteral){
            JasminFileGenerator.writeContent("ldc " + integerLiteral.integerValue);
        } else if (e instanceof  BooleanLiteral booleanLiteral) {
            if (booleanLiteral.booleanType){
                JasminFileGenerator.writeContent("iconst_1");
            } else {
                JasminFileGenerator.writeContent("iconst_0");
            }
        } else if (e instanceof  StringLiteral stringLiteral) {
            JasminFileGenerator.writeContent("ldc " + stringLiteral.stringType);
        } else if (e instanceof OperatorExpression operatorExpression) {
            if (Objects.equals(operatorExpression.operator, "=")){
                buildJasminExpression(operatorExpression.rightExpression);

                switch (operatorExpression.leftExpression.type){
                    case "int", "bool" -> JasminFileGenerator.writeContent("istore_" + CodeGenStorage.getLocalArrayIndex(((IdExpression)operatorExpression.leftExpression).Id));
                    default -> JasminFileGenerator.writeContent("astore_" + CodeGenStorage.getLocalArrayIndex(((IdExpression)operatorExpression.leftExpression).Id));
                }

                CodeGenStorage.init(((IdExpression) operatorExpression.leftExpression).Id);
            } else {
                buildJasminExpression(operatorExpression.leftExpression);
                buildJasminExpression(operatorExpression.rightExpression);
                doOperation(operatorExpression.operator);
            }

        } else if (e instanceof IdExpression idExpression) {
            buildIdExprJasminCode(idExpression);
        } else if (e instanceof  ParanthesisExpression paranthesisExpression) {
            buildJasminExpression(paranthesisExpression.expression);

        }
    }

    private void buildIdExprJasminCode(IdExpression idExpression){
        if (idExpression.childCount == 1){
            if (CodeGenStorage.isInitialized(idExpression.Id)){
                if (Objects.requireNonNull(CodeGenStorage.getVarType(idExpression.Id)) == LocalVarInfo.VarType.INTEGER ||
                    Objects.requireNonNull(CodeGenStorage.getVarType(idExpression.Id)) == LocalVarInfo.VarType.BOOLEAN) {
                    JasminFileGenerator.writeContent("iload_" + CodeGenStorage.getLocalArrayIndex(idExpression.Id));
                } else {
                    JasminFileGenerator.writeContent("aload_" + CodeGenStorage.getLocalArrayIndex(idExpression.Id));
                }
            }

        } else {
            if (!checkForPrint(idExpression)){
                for (Expression e : idExpression.expressions){
                    buildJasminExpression(e);
                }

                StringBuilder method_call = getStringBuilder(idExpression);
                JasminFileGenerator.writeContent(method_call.toString());
            }

        }
    }

    private static @NotNull StringBuilder getStringBuilder(IdExpression idExpression) {
        StringBuilder method_call = new StringBuilder("invokevirtual " + CodeGenStorage.getClassID() + "/" + CodeGenStorage.getMethodID() + "(");

        for (Expression e : idExpression.expressions){
            switch (e.type){
                case "int" -> method_call.append("I");
                case "bool" -> method_call.append("Z");
                case "string" -> method_call.append("Ljava/lang/String;");
            }
        }

        method_call.append(")");

        switch (idExpression.type){
            case "int" -> method_call.append("I");
            case "bool" -> method_call.append("Z");
            case "string" -> method_call.append("Ljava/lang/String;");
        }
        return method_call;
    }

    private boolean checkForPrint(IdExpression idExpression) {
        String ending = "";

        if (Objects.equals(idExpression.Id, "print") && idExpression.expressions.size() == 1){
            switch (idExpression.expressions.getFirst().type) {
                case "int" -> ending = "I)V";
                case "bool" -> ending = "Z)V";
                case "string" -> ending = "Ljava/lang/String;)V";
                default -> {
                    return false;
                }
            }
        }

        JasminFileGenerator.writeContent("getstatic java/lang/System/out Ljava/io/PrintStream;");

        buildJasminExpression(idExpression.expressions.getFirst());

        String print = ("invokevirtual java/io/PrintStream/print(" + ending);


        JasminFileGenerator.writeContent(print);
        return true;
    }

    private void doOperation(String operator){
        switch (operator){
            case "+" -> JasminFileGenerator.writeContent("iadd");
            case "-" -> JasminFileGenerator.writeContent("isub");
            case "*" -> JasminFileGenerator.writeContent("imul");
            case "/" -> JasminFileGenerator.writeContent("idiv");
            case "%" -> JasminFileGenerator.writeContent("irem");
            case "&&" -> JasminFileGenerator.writeContent("iand");
            case "||" -> JasminFileGenerator.writeContent("ior");
            case ">", "<", "==", "!=" -> assembleRelation(operator);
        }
    }

    private void assembleRelation(String operator){
        int counter = ProgramVisitorCG.relation_counter;
        ProgramVisitorCG.relation_counter++;

        String common_string = "iconst_0\n" +
                               "goto eval_false" + counter + "\n" +
                               "cond_true" + counter + ":\n" +
                               "iconst_1\n" +
                               "eval_false" + counter + ":";

        switch (operator){
            case "<" -> JasminFileGenerator.writeContent("if_icmplt cond_true" + counter + "\n" + common_string);

            case ">" -> JasminFileGenerator.writeContent("if_icmpgt cond_true" + counter + "\n" + common_string);

            case "==" -> JasminFileGenerator.writeContent("if_icmpeq cond_true" + counter + "\n" + common_string);

            case "!=" -> JasminFileGenerator.writeContent("if_icmpne cond_true" + counter + "\n" + common_string);
        }
    }
}

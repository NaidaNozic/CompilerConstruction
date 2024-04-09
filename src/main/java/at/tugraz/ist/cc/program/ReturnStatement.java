package at.tugraz.ist.cc.program;

public class ReturnStatement {

    public Expression expression;
    public  int line;
    public ReturnStatement(Expression expression, int line){
        this.line = line;
        this.expression = expression;
    }

}

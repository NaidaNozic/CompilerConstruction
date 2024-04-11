package at.tugraz.ist.cc.program;

public class OperatorExpression extends Expression{
    public Expression leftExpression; //Addop and Not don't have to have a left expression

    public String operator;
    public Expression rightExpression;
    public OperatorExpression(Expression leftExpression, String operator, Expression rightExpression) {
        super(leftExpression.line);
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }
}

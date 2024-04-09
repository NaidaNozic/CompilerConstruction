package at.tugraz.ist.cc.program;

public class OperatorExpression extends Expression{
    public Expression leftExpression; //Addop and Not don't have to have a left expression

    public Operator operator;
    public Expression rightExpression;
    public OperatorExpression(Expression leftExpression, Operator operator, Expression rightExpression) {
        super(operator.line);
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }
}

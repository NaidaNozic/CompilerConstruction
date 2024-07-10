package at.tugraz.ist.cc.program;

public class OperatorExpression extends Expression{
    public Expression leftExpression; //AddOp and Not don't have to have a left expression
    public Expression rightExpression;
    public String operator;

    public OperatorExpression(Expression leftExpression, String operator, Expression rightExpression, String t) {
        super(leftExpression.line);
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
        this.type = t;
    }

    public OperatorExpression(Expression leftExpression, String operator, Expression rightExpression) { //deprecated
        super(leftExpression.line);
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }
}

package at.tugraz.ist.cc.program;

public class ParanthesisExpression extends Expression{

    public Expression expression;
    public ParanthesisExpression(Expression expression) {
        super(expression.line);
        this.expression = expression;
    }
}

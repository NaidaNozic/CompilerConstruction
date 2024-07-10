package at.tugraz.ist.cc.program;

public class ParanthesisExpression extends Expression{

    public Expression expression;
    public ParanthesisExpression(Expression expression, String t) {
        super(expression.line);
        this.expression = expression;
        this.type = t;
    }
}

package at.tugraz.ist.cc.program;

public class AddNotExpression extends Expression{

    public String operator;
    public Expression expression;

    public AddNotExpression(String operator, Expression expression, String t) {
        super(expression.line);
        this.operator = operator;
        this.expression = expression;
        this.type = t;
    }
}

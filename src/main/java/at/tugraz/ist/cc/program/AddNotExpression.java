package at.tugraz.ist.cc.program;

public class AddNotExpression extends Expression{

    public Operator operator;
    public Expression expression;

    public AddNotExpression(Operator operator, Expression expression) {
        super(operator.line);
        this.operator = operator;
        this.expression = expression;
    }
}

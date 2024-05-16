package at.tugraz.ist.cc.program;

public class IntegerLiteral extends LiteralExpression {
    public String integerValue;
    public IntegerLiteral(Integer line, String integerValue) {
        super(line);
        this.integerValue = integerValue;
        this.type = "int";
    }
}

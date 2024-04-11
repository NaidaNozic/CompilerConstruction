package at.tugraz.ist.cc.program;

public class StringLiteral extends LiteralExpression {

    public String stringType;

    public StringLiteral(Integer line, String stringType) {
        super(line);
        this.stringType = stringType;
    }
}

package at.tugraz.ist.cc.program;

public class BooleanLiteral extends LiteralExpression {
    public boolean booleanType;

    public BooleanLiteral(Integer line, boolean booleanType) {
        super(line);
        this.booleanType = booleanType;
        this.type = "bool";
    }
}

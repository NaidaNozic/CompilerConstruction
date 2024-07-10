package at.tugraz.ist.cc.program;

public class ThisLiteral extends LiteralExpression {
    public String thisType;
    public ThisLiteral(Integer line, String thisType) {
        super(line);
        this.thisType = thisType;
        this.type = "this";
    }
}

package at.tugraz.ist.cc.program;

public class NixLiteral extends LiteralExpression {

    public String nixType;
    public NixLiteral(Integer line, String nixType) {
        super(line);
        this.nixType = nixType;
    }
}

package at.tugraz.ist.cc.program;

public class NixLiteral extends Literal{

    public String nixType;
    public NixLiteral(Integer line, String nixType) {
        super(line);
        this.nixType = nixType;
    }
}

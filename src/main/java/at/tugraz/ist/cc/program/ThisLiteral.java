package at.tugraz.ist.cc.program;

public class ThisLiteral extends Literal{
    public String thisType;
    public ThisLiteral(Integer line, String thisType) {
        super(line);
        this.thisType = thisType;
    }
}

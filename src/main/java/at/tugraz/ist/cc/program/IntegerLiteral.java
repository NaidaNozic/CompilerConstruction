package at.tugraz.ist.cc.program;

public class IntegerLiteral extends Literal{
    public Integer integerType;
    public IntegerLiteral(Integer line, Integer integerType) {
        super(line);
        this.integerType = integerType;
    }
}

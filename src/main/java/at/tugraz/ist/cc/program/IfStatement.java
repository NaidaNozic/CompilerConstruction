package at.tugraz.ist.cc.program;

public class IfStatement {

    public Expression expression;
    public Block thenBlock;
    public Block elseBlock;

    public int line;

    public IfStatement(Expression expression, Block thenBlock, Block elseBlock, int line){
        this.expression = expression;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
        this.line = line;
    }

}

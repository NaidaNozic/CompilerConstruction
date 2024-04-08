package at.tugraz.ist.cc.program;

public class IfStatement {

    public Expression expression;
    public Block thenBlock;
    public Block elseBlock;

    public IfStatement(Expression expression, Block thenBlock){
        this.expression = expression;
        this.thenBlock = thenBlock;
    }

    public IfStatement(Expression expression, Block thenBlock, Block elseBlock){
        this.expression = expression;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

}

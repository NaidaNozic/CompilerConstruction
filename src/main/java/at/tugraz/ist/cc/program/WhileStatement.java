package at.tugraz.ist.cc.program;

public class WhileStatement {
    public Expression expression;
    public Block block;
    public WhileStatement(Expression expression, Block block){
        this.expression = expression;
        this.block = block;
    }
}

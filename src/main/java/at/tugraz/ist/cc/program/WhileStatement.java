package at.tugraz.ist.cc.program;

public class WhileStatement {
    public Expression expression;
    public Block block;
    public int line;
    public WhileStatement(Expression expression, Block block, int line){
        this.expression = expression;
        this.block = block;
        this.line = line;
    }
}

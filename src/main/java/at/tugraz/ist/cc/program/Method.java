package at.tugraz.ist.cc.program;


public class Method {
    public Block block;
    public ParamList paramList;
    public Param param;

    public Method(Block block, ParamList paramList, Param param) {
        this.block = block;
        this.paramList = paramList;
        this.param = param;
    }
}

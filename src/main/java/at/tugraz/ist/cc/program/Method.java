package at.tugraz.ist.cc.program;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.visitors.TypeVisitor;

public class Method {
    public Block block;
    public ParamList paramList;
    public Param param;

    public Method(Block block, ParamList paramList, Param param) {
        this.block = block;
        this.paramList = paramList;
        this.param = param;
    }

    //helper to collect symbol table values
    public Method(String id, Type type) {
        param = new Param(type, id, -1, -1);

    }
}

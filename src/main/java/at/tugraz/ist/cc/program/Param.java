package at.tugraz.ist.cc.program;

public class Param {

    public Type type;
    public String id;
    public Integer line;

    public Param(Type type, String id, Integer line){
        this.type = type;
        this.id = id;
        this.line = line;
    }
}

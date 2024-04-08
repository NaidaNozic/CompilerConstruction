package at.tugraz.ist.cc.program;

public class Param {

    public Type type;
    public String id;
    public Integer line;
    public Integer column;

    public Param(Type type, String id, Integer line, Integer column){
        this.type = type;
        this.id = id;
        this.line = line;
        this.column = column;
    }
}

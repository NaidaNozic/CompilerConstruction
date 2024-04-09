package at.tugraz.ist.cc.program;
public class NewClassExpression extends Expression{
    public String classId;
    public NewClassExpression(Integer line, String classId) {
        super(line);
        this.classId = classId;
    }
}


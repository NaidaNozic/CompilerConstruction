package at.tugraz.ist.cc.program;

public class ClassDeclaration {

    public String id;
    public ClassBody classBody;

    public Integer line;

    public ClassDeclaration(String id, ClassBody classBody, Integer line) {
        this.id = id;
        this.classBody = classBody;
        this.line = line;
    }

}

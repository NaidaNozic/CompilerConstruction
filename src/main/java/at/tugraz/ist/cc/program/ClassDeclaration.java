package at.tugraz.ist.cc.program;


public class ClassDeclaration {

    public String id;
    public String superclass;
    public ClassBody classBody;

    public Integer line;

    public ClassDeclaration(String id, ClassBody classBody, Integer line) {
        this.id = id;
        this.superclass = null;
        this.classBody = classBody;
        this.line = line;
    }

    public ClassDeclaration(String id, String superclass, ClassBody classBody, Integer line) {
        this.id = id;
        this.superclass = superclass;
        this.classBody = classBody;
        this.line = line;
    }

}

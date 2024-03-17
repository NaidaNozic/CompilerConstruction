package at.tugraz.ist.cc.program;

public class ClassDeclaration {

    public StringLiteral id;
    public ClassBody classBody;

    public ClassDeclaration(StringLiteral id, ClassBody classBody) {
        this.id = id;
        this.classBody = classBody;
    }

}

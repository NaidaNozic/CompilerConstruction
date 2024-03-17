package at.tugraz.ist.cc.program;

import java.util.ArrayList;
import java.util.List;

public class ClassBody {

    public List<Declaration> declarations;
    public List<Method> methods;

    public ClassBody(){
        this.declarations = new ArrayList<>();
        this.methods = new ArrayList<>();
    }
}

package at.tugraz.ist.cc.program;

import java.util.ArrayList;
import java.util.List;

public class Declaration {

    public List<Param> params;
    public List<Expression> expressions;

    public Declaration(){
        this.params = new ArrayList<>();
        this.expressions = new ArrayList<>();
    }
}

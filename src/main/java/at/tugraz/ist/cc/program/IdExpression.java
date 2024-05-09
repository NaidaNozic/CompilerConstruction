package at.tugraz.ist.cc.program;

import java.util.ArrayList;
import java.util.List;

public class IdExpression extends Expression{
    public String Id;
    public List<Expression> expressions;

    //needed to know if a method or ID is in the visitor (childCount == 1 then ID, if more -> method)
    public int childCount;


    public IdExpression(String Id, Integer line, int children) {
        super(line);
        this.Id = Id;
        this.expressions = new ArrayList<>();
        childCount = children;
    }
}

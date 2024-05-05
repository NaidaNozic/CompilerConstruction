package at.tugraz.ist.cc.program;

import java.util.ArrayList;
import java.util.List;

public class IdExpression extends Expression{
    public String Id;
    public List<Expression> expressions;
    public int childCount; //needed to know if a method or Id is in the visitor
    public IdExpression(String Id, Integer line, int children) {
        super(line);
        this.Id = Id;
        this.expressions = new ArrayList<>();
        childCount = children;
    }
}

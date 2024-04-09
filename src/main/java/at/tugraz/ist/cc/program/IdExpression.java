package at.tugraz.ist.cc.program;

import java.util.ArrayList;
import java.util.List;

public class IdExpression extends Expression{
    public String Id;
    public List<Expression> expressions;
    public IdExpression(String Id, Integer line) {
        super(line);
        this.Id = Id;
        this.expressions = new ArrayList<>();
    }
}

package at.tugraz.ist.cc.program;

import java.util.ArrayList;
import java.util.List;

public class Block {
    public List<Declaration> declarations;
    public List<IfStatement> ifStatements;
    public List<WhileStatement> whileStatements;
    public List<ReturnStatement> returnStatements;
    public List<Expression> expressions;

    public Block(){
        this.declarations = new ArrayList<>();
        this.ifStatements = new ArrayList<>();
        this.whileStatements = new ArrayList<>();
        this.returnStatements = new ArrayList<>();
        this.expressions = new ArrayList<>();
    }
}

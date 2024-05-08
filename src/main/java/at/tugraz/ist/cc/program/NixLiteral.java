package at.tugraz.ist.cc.program;

public class NixLiteral extends LiteralExpression {

    public NixLiteral(Integer line, String nix_type) {
        super(line);
        this.type = nix_type;
    }
}

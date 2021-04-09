import java.util.ArrayList;
import java.util.List;

public class Leaf extends ASTNode {
    List<Lexeme> childs = new ArrayList<Lexeme>();

    public Leaf(String name) {
        super(name);
    }

    public void addChild(Lexeme lexeme) {
        childs.add(lexeme);
    }
}

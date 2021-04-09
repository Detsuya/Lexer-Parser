import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private String name = null;
    private List<ASTNode> childs = new ArrayList<ASTNode>();

    public ASTNode(String name) {
        this.name = name;
    }

    public void addChild(ASTNode node) {
        childs.add(node);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

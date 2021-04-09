import java.util.List;

public class Parser {
    static List<Lexeme> lexemes = Lexer.getTokenList();

    public static void main(String[] args) throws Exception {
        lang();
    }

    static ASTNode lang() throws Exception {
        ASTNode node = new ASTNode("lang");
        node.addChild(expr());
        while (lexemes.size() > 0 && currToken().matches("VAR|IF_KW")) node.addChild(expr());
        return node;
    }

    static ASTNode expr() throws Exception {
        ASTNode node = new ASTNode("expr");
        switch (currToken()) {
            case ("VAR") -> node.addChild(assign_expr());
            case ("IF_KW") -> node.addChild(if_expr());
            default -> throw new Exception("Error in expr");
        }
        return node;
    }

    static ASTNode assign_expr() throws Exception {
        Leaf node = new Leaf("assign_expr");
        match("VAR", node);
        match("ASSIGN_OP", node);
        node.addChild(value_expr());
        return node;
    }

    static ASTNode value_expr() throws Exception {
        Leaf node = new Leaf("value_expr");
        switch (currToken()) {
            case ("NUMBER"), ("VAR") -> node.addChild(value());
            case ("L_BR") -> {
                match("L_BR", node);
                node.addChild(value_expr());
                match("R_BR", node);
            }
            default -> throw new Exception("Error in value_expr");
        }
        while (currToken().matches("OP")) {
            match("OP", node);
            node.addChild(value_expr());
        }
        return node;
    }
    static ASTNode value() throws Exception {
        Leaf node = new Leaf("value");
        switch (currToken()) {
            case ("NUMBER") -> match("NUMBER", node);
            case ("VAR") -> match("VAR", node);
            default -> throw new Exception("Error in value");
        }
        return node;
    }
    static ASTNode if_expr() throws Exception {
        ASTNode node = new ASTNode("if_expr");
        node.addChild(if_head());
        node.addChild(ifelse_body());
        if (currToken().matches("ELSE_KW")) {
            node.addChild(else_expr());
        }
        return node;
    }

    static ASTNode if_head() throws Exception {
        Leaf node = new Leaf("if_head");
        match("IF_KW", node);
        node.addChild(if_condition());
        return node;
    }

    static ASTNode else_expr() throws Exception {
        Leaf node = new Leaf("else_expr");
        match("ELSE_KW", node);
        node.addChild(ifelse_body());
        return node;
    }

    static ASTNode if_condition() throws Exception {
        Leaf node = new Leaf("if_condition");
        match("L_BR", node);
        node.addChild(logical_expr());
        match("R_BR", node);
        return node;
    }

    static ASTNode logical_expr() throws Exception {
        Leaf node = new Leaf("logical_expr");
        node.addChild(value_expr());
        while (currToken().matches("LOGICAL_OP")) {
            match("LOGICAL_OP", node);
            node.addChild(value_expr());
        }
        return node;
    }

    static ASTNode ifelse_body() throws Exception{
        Leaf node = new Leaf("ifelse_body");
        match("L_S_BR", node);
        node.addChild(expr());
        while (currToken().matches("VAR|IF_KW")) node.addChild(expr());
        match("R_S_BR", node);
        return node;
    }

    private static String currToken() {
        return lexemes.get(0).getTerminal().getIdentifier();
    }

    static void match(String terminal, Leaf currNode) {
        String t = currToken();
        assert (t.equals(terminal)) : "Current Token != " + terminal;
        currNode.addChild(lexemes.get(0));
        lexemes.remove(0);
    }
}

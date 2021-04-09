import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {

    private static final List<Terminal> TERMINALS = List.of(
            new Terminal("VAR", "^[a-zA-Z_]{1}\\w*$"),
            new Terminal("NUMBER", "0|[1-9][0-9]*"),
            new Terminal("ASSIGN_OP", "="),
            new Terminal("LOGICAL_OP", "==|>|<|!="),
            new Terminal("WHILE_KW", "while", 1),
            new Terminal("FOR_KW", "for", 1),
            new Terminal("IF_KW", "if", 1),
            new Terminal("ELSE_KW", "else", 1),
            new Terminal("DO_KW", "do", 1),
            new Terminal("L_BR", "\\("),
            new Terminal("R_BR", "\\)"),
            new Terminal("L_S_BR", "\\{"),
            new Terminal("R_S_BR", "\\}"),
            new Terminal("SC",";"),
            new Terminal("VAR_TYPE", "int|str|float", 1),
            new Terminal("OP", "[+-/*]|\\+\\+|\\-\\-"),
            new Terminal("WS", "\\s+")
    );
    public static List<Lexeme> getTokenList() {
        StringBuilder input = new StringBuilder();
        List<Lexeme> lexemes = new ArrayList<>();
        try {
            FileReader reader = new FileReader("Program.txt");
            int c;
            while ((c=reader.read()) != -1) {
                input.append(Character.toString(c));
            }
            reader.close();
            input.append("$");

            while (input.charAt(0) != '$') {
                Lexeme lexeme = extractNextLexeme(input);
                if (!lexeme.getTerminal().getIdentifier().equals("WS")) lexemes.add(lexeme);
                input.delete(0, lexeme.getValue().length());
            }

        }
        catch(IOException e) {
            System.out.println("Can't read code: " + e.getMessage());
        }
        return lexemes;
    }

    public static void main(String[] args) {
        print(getTokenList());
    }

    private static Lexeme extractNextLexeme(StringBuilder input) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(input.charAt(0));

        if (anyTerminalMatches(buffer)) {
            while (anyTerminalMatches(buffer) && buffer.length() != input.length()) {
                buffer.append(input.charAt(buffer.length()));
            }

            buffer.deleteCharAt(buffer.length() - 1);

            List<Terminal> terminals = lookupTerminals(buffer);

            return new Lexeme(getPrioritizedTerminal(terminals), buffer.toString());
        } else {
            throw new RuntimeException("Unexpected symbol " + buffer);
        }
    }

    private static Terminal getPrioritizedTerminal(List<Terminal> terminals) {
        Terminal prioritizedTerminal = terminals.get(0);

        for (Terminal terminal : terminals) {
            if (terminal.getPriority() > prioritizedTerminal.getPriority()) {
                prioritizedTerminal = terminal;
            }
        }

        return prioritizedTerminal;
    }

    private static boolean anyTerminalMatches(StringBuilder buffer) {
        return lookupTerminals(buffer).size() != 0;
    }

    private static List<Terminal> lookupTerminals(StringBuilder buffer) {
        List<Terminal> terminals = new ArrayList<>();

        for (Terminal terminal : TERMINALS) {
            if (terminal.matches(buffer)) {
                terminals.add(terminal);
            }
        }

        return terminals;
    }

    private static StringBuilder lookupInput(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Input string not found");
        }
        StringBuilder buff = new StringBuilder();
        for (String arg : args) {
            buff.append(arg).append(" ");
        }
        return buff;
    }

    private static void print(List<Lexeme> lexemes) {
        for (Lexeme lexeme : lexemes) {
            System.out.printf("[%s, %s]%n",
                    lexeme.getTerminal().getIdentifier(),
                    lexeme.getValue());
        }
    }

}
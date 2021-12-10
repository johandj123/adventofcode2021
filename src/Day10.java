import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Day10 {
    private static final Map<Character, Character> PAIR = Map.of('(', ')', '[', ']', '{', '}', '<', '>');
    private static final Map<Character, Integer> SCORE1 = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);
    private static final Map<Character, Integer> SCORE2 = Map.of('(', 1, '[', 2, '{', 3, '<', 4);

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input10.txt").toPath());
        first(lines);
    }

    private static void first(List<String> lines) {
        int score1 = 0;
        List<Long> score2 = new ArrayList<>();
        for (String line : lines) {
            boolean stop = false;
            Stack<Character> stack = new Stack<>();
            outer:
            for (char c : line.toCharArray()) {
                switch (c) {
                    case '(':
                    case '[':
                    case '{':
                    case '<':
                        stack.push(c);
                        break;
                    case ')':
                    case ']':
                    case '}':
                    case '>':
                        if (stack.isEmpty() || c != PAIR.get(stack.pop())) {
                            score1 += SCORE1.get(c);
                            stop = true;
                            break outer;
                        }
                        break;
                }
            }
            if (!stop) {
                long lineScore = 0;
                while (!stack.empty()) {
                    char c = stack.pop();
                    lineScore = lineScore * 5L + SCORE2.get(c);
                }
                score2.add(lineScore);
            }
        }
        System.out.println("First: " + score1);
        score2.sort(null);
        System.out.println("Second: " + score2.get(score2.size() / 2));
    }
}

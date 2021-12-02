import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input2.txt").toPath());
        first(lines);
        second(lines);
    }

    private static void first(List<String> lines) {
        int x = 0;
        int depth = 0;
        for (String line : lines) {
            String[] s = line.split(" ");
            int amount = Integer.parseInt(s[1]);
            switch (s[0]) {
                case "forward":
                    x += amount;
                    break;
                case "down":
                    depth += amount;
                    break;
                case "up":
                    depth -= amount;
                    break;
            }
        }
        System.out.println("First: " + (x * depth));
    }

    private static void second(List<String> lines) {
        int x = 0;
        int depth = 0;
        int aim = 0;
        for (String line : lines) {
            String[] s = line.split(" ");
            int amount = Integer.parseInt(s[1]);
            switch (s[0]) {
                case "forward":
                    x += amount;
                    depth += (aim * amount);
                    break;
                case "down":
                    aim += amount;
                    break;
                case "up":
                    aim -= amount;
                    break;
            }
        }
        System.out.println("Second: " + (x * depth));
    }
}

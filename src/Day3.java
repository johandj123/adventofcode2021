import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day3 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input3.txt").toPath());
        first(lines);
        second(lines);
    }

    private static void first(List<String> lines) {
        StringBuilder gammaBuilder = new StringBuilder();
        StringBuilder epsilonBuilder = new StringBuilder();
        for (int x = 0; x < lines.get(0).length(); x++) {
            int[] count = new int[2];
            for (String line : lines) {
                String c = line.substring(x, x + 1);
                count[Integer.parseInt(c)]++;
            }
            int most = (count[0] > count[1]) ? 0 : 1;
            gammaBuilder.append(most);
            epsilonBuilder.append(1 - most);
        }
        int gamma = Integer.parseInt(gammaBuilder.toString(), 2);
        int epsilon = Integer.parseInt(epsilonBuilder.toString(), 2);
        System.out.println("First: " + (gamma * epsilon));
    }

    private static void second(List<String> lines) {
        int oxygen = filterNumber(lines, count -> count[1] >= count[0] ? 1 : 0);
        int co2 = filterNumber(lines, count -> count[0] <= count[1] ? 0 : 1);
        System.out.println("First: " + (oxygen * co2));
    }

    private static int filterNumber(List<String> lines, Function<int[],Integer> selector)
    {
        List<String> current = lines;
        for (int x = 0; x < lines.get(0).length(); x++) {
            int[] count = new int[2];
            for (String line : current) {
                String c = line.substring(x, x + 1);
                count[Integer.parseInt(c)]++;
            }
            int pick = selector.apply(count);
            int xx = x;
            List<String> next = current.stream()
                    .filter(s -> Integer.parseInt(s.substring(xx, xx + 1)) == pick)
                    .collect(Collectors.toList());
            current = next;
            if (current.size() == 1) {
                return Integer.parseInt(current.get(0), 2);
            }
        }
        throw new RuntimeException();
    }
}

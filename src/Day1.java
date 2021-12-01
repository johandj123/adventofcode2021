import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input1.txt").toPath());
        List<Integer> values = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        int increase = 0;
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i) < values.get(i + 1)) {
                increase++;
            }
        }
        System.out.println("First: " + increase);

        increase = 0;
        for (int i = 0; i < values.size() - 3; i++) {
            if (threewindow(values, i) < threewindow(values, i + 1)) {
                increase++;
            }
        }
        System.out.println("Second: " + increase);
    }

    private static int threewindow(List<Integer> values, int i)
    {
        return values.get(i) + values.get(i + 1) + values.get(i + 2);
    }
}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = Arrays.stream(Files.readString(new File("input6.txt").toPath()).trim().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> current = input;
        for (int i = 0; i < 80; i++) {
            current = step(current);
        }
        System.out.println("First: " + current.size());
        long[] stats = init(input);
        for (int i = 0; i < 256; i++) {
            stats = step(stats);
        }
        System.out.println("Second: " + count(stats));
    }

    private static List<Integer> step(List<Integer> input)
    {
        List<Integer> result = new ArrayList<>();
        for (int i : input) {
            if (i == 0) {
                result.add(6);
                result.add(8);
            } else {
                result.add(i - 1);
            }
        }
        return result;
    }

    private static long[] init(List<Integer> input)
    {
        long[] result = new long[9];
        for (int i : input) {
            result[i]++;
        }
        return result;
    }

    private static long[] step(long[] stats)
    {
        long[] result = new long[9];
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = stats[i + 1];
            if (i == 6) {
                result[i] += stats[0];
            }
        }
        result[8] = stats[0];
        return result;
    }

    private static long count(long[] stats)
    {
        return Arrays.stream(stats).sum();
    }
}

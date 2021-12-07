import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = Arrays.stream(Files.readString(new File("input7.txt").toPath()).trim().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        input.sort(null);
        first(input);
        second(input);
    }

    private static void first(List<Integer> input) {
        int mid = input.size() / 2;
        int answer = Math.min(cost(input, input.get(mid)), cost(input, input.get(mid + 1)));
        System.out.println("First: " + answer);
    }

    private static void second(List<Integer> input) {
        int avg = input.stream().mapToInt(x -> x).sum() / input.size();
        int answer = Math.min(secondCost(input, avg), secondCost(input, avg + 1));
        System.out.println("Second: " + answer);
    }

    private static int cost(List<Integer> input, int value)
    {
        return input.stream().mapToInt(x -> Math.abs(x - value)).sum();
    }

    private static int secondCost(List<Integer> input, int value)
    {
        return input.stream().mapToInt(x -> secondCost(x - value)).sum();
    }

    private static int secondCost(int dist)
    {
        dist = Math.abs(dist);
        return (dist * (dist + 1)) / 2;
    }
}

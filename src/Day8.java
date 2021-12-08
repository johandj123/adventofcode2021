import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day8 {
    private static final List<String> DIGITS_LIST = Arrays.asList("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg");
    private static final Set<String> DIGITS_SET = new HashSet<>(DIGITS_LIST);

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input8.txt").toPath());
        first(lines);
        second(lines);
    }

    private static void first(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            int index = line.indexOf("|");
            String right = line.substring(index + 1).trim();
            String[] parts = right.split(" ");
            for (String part : parts) {
                int length = part.length();
                if (length == 2 || length == 4 || length == 3 || length == 7) {
                    count++;
                }
            }
        }
        System.out.println("First: " + count);
    }

    private static void second(List<String> lines) {
        List<String> permutations = findAllPermutations();
        int count = 0;
        for (String line : lines) {
            int index = line.indexOf("|");
            String[] left = line.substring(0, index).trim().split(" ");
            String[] right = line.substring(index + 1).trim().split(" ");
            String permutation = findPermutation(left, permutations);

            String[] permuted = permute(right, permutation);
            StringBuilder sb = new StringBuilder();
            for (String input : permuted) {
                sb.append(findDigit(input));
            }
            int value = Integer.parseInt(sb.toString());
            count += value;
        }
        System.out.println("Second: " + count);
    }

    private static String findDigit(String input)
    {
        for (int i = 0; i < DIGITS_LIST.size(); i++) {
            if (DIGITS_LIST.get(i).equals(input)) {
                return Integer.toString(i);
            }
        }
        throw new RuntimeException("Digit not found");
    }

    private static String findPermutation(String[] left, List<String> permutations) {
        for (String permutation : permutations) {
            String[] permuted = permute(left, permutation);
            if (checkDigits(permuted)) {
                return permutation;
            }
        }
        throw new RuntimeException("No permutation found");
    }

    private static boolean checkDigits(String[] input) {
        Set<String> set = Arrays.stream(input).collect(Collectors.toSet());
        return DIGITS_SET.equals(set);
    }

    private static String[] permute(String[] input, String permutation)
    {
        String[] result = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = permute(input[i], permutation);
        }
        return result;
    }

    private static String permute(String input, String permutation)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int index = input.charAt(i) - 'a';
            sb.append(permutation.charAt(index));
        }

        String original = sb.toString();
        char[] chars =  original.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    private static List<String> findAllPermutations()
    {
        List<String> result = new ArrayList<>();
        add("", "abcdefg", result);
        return result;
    }

    private static void add(String current, String left, List<String> result)
    {
        if (left.isEmpty()) {
            result.add(current);
        } else {
            for (int i = 0; i < left.length(); i++) {
                String nextCurrent = current + left.substring(i, i + 1);
                String nextLeft = left.substring(0, i) + left.substring(i + 1);
                add(nextCurrent, nextLeft, result);
            }
        }
    }
}

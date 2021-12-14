import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day14 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input14.txt").toPath());
        String input = lines.get(0);
        Map<String, String> rules = new HashMap<>();
        for (String line : lines.subList(2, lines.size())) {
            String key = line.substring(0, 2);
            String value = line.substring(6, 7);
            rules.put(key, value);
        }

        first(input, rules);
        second(input, rules);
    }

    private static void first(String input, Map<String, String> rules) {
        String current = input;
        for (int i = 0; i < 10; i++) {
            current = substitute(current, rules);
        }
        System.out.println("First: " + calculateResult(current));
    }

    private static String substitute(String current, Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < current.length() - 1; i++) {
            sb.append(current.charAt(i));
            String key = current.substring(i, i + 2);
            String value = rules.get(key);
            if (value != null) {
                sb.append(value);
            }
        }
        sb.append(current.charAt(current.length() - 1));
        return sb.toString();
    }

    private static int calculateResult(String current) {
        Map<Character, Integer> hist = new HashMap<>();
        for (char c : current.toCharArray()) {
            hist.put(c, 1 + hist.getOrDefault(c, 0));
        }
        int min = hist.values().stream().min(Comparator.naturalOrder()).get();
        int max = hist.values().stream().max(Comparator.naturalOrder()).get();
        return max - min;
    }

    private static void second(String input, Map<String, String> rules) {
        Day14 day14 = new Day14(rules);
        Map<Character, Long> hist = singleHist(input.charAt(input.length() - 1));
        for (int i = 0; i < input.length() - 1; i++) {
            Map<Character, Long> otherHist = day14.determineHistWrap(0, input.charAt(i), input.charAt(i + 1));
            hist = histAdd(hist, otherHist);
        }
        long min = hist.values().stream().min(Comparator.naturalOrder()).get();
        long max = hist.values().stream().max(Comparator.naturalOrder()).get();
        System.out.println("Second: " + (max - min));
    }

    public Day14(Map<String, String> rules) {
        this.rules = rules;
    }

    private Map<String, String> rules;
    private Map<Call, Map<Character, Long>> cache = new HashMap<>();

    private Map<Character, Long> determineHistWrap(int depth,char c1,char c2)
    {
        Call call = new Call(depth, c1, c2);
        if (cache.containsKey(call)) {
            return cache.get(call);
        }
        Map<Character, Long> hist = determineHist(depth, c1, c2);
        cache.put(call, hist);
        return hist;
    }

    private Map<Character, Long> determineHist(int depth,char c1,char c2)
    {
        String value = rules.get(Character.toString(c1) + Character.toString(c2));
        if (depth == 40 || value == null) {
            return singleHist(c1);
        } else {
            char cm = value.charAt(0);
            Map<Character, Long> hist1 = determineHistWrap(1 + depth, c1, cm);
            Map<Character, Long> hist2 = determineHistWrap(1 + depth, cm, c2);
            return histAdd(hist1, hist2);
        }
    }

    private static Map<Character, Long> singleHist(char c1) {
        Map<Character, Long> hist = new HashMap<>();
        hist.put(c1, 1L);
        return hist;
    }

    private static Map<Character, Long> histAdd(Map<Character, Long> hist1, Map<Character, Long> hist2) {
        Map<Character, Long> hist = new HashMap<>(hist1);
        for (Map.Entry<Character, Long> entry : hist2.entrySet()) {
            hist.put(entry.getKey(), hist.getOrDefault(entry.getKey(), 0L) + entry.getValue());
        }
        return hist;
    }

    class Call
    {
        final int depth;
        final char c1;
        final char c2;

        public Call(int depth, char c1, char c2) {
            this.depth = depth;
            this.c1 = c1;
            this.c2 = c2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Call call = (Call) o;
            return depth == call.depth && c1 == call.c1 && c2 == call.c2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(depth, c1, c2);
        }
    }
}

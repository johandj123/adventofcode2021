import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day16 {
    private static final String[] HEXDIGIT = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
            "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    private int versionSum = 0;

    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File("input16.txt").toPath()).trim();
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            int v = Integer.parseInt(Character.toString(c), 16);
            sb.append(HEXDIGIT[v]);
        }
        new Day16().start(sb.toString());
    }

    private void start(String input) {
        List<Long> values = parse(new Reader(input));
        System.out.println("First: " + versionSum);
        System.out.println("Second: " + values.get(0));
    }

    private List<Long> parse(Reader reader) {
        List<Long> list = new ArrayList<>();
        while (true) {
            try {
                list.add(parsePacket(reader));
            } catch (StringIndexOutOfBoundsException ignore) {
                break;
            }
        }
        return list;
    }

    private long parsePacket(Reader reader) {
        long result = 0L;
        int version = reader.take(3);
        versionSum += version;
        int type = reader.take(3);
        if (type == 4) {
            boolean cnt = true;
            while (cnt) {
                cnt = reader.take(1) != 0;
                int value = reader.take(4);
                result = (result << 4) | value;
            }
        } else {
            int lengthType = reader.take(1);
            List<Long> values;
            if (lengthType == 0) {
                int length = reader.take(15);
                values = parse(reader.takeSub(length));
            } else {
                int count = reader.take(11);
                values = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    values.add(parsePacket(reader));
                }
            }
            switch (type) {
                case 0:
                    result = values.stream().mapToLong(x -> x).sum();
                    break;
                case 1:
                    result = values.stream().reduce(1L, (a, b) -> a * b);
                    break;
                case 2:
                    result = values.stream().min(Comparator.naturalOrder()).get();
                    break;
                case 3:
                    result = values.stream().max(Comparator.naturalOrder()).get();
                    break;
                case 5:
                    result = values.get(0) > values.get(1) ? 1 : 0;
                    break;
                case 6:
                    result = values.get(0) < values.get(1) ? 1 : 0;
                    break;
                case 7:
                    result = values.get(0).equals(values.get(1)) ? 1 : 0;
                    break;
            }
        }
        return result;
    }

    static class Reader {
        String input;
        int position;

        public Reader(String input) {
            this.input = input;
        }

        public int take(int bits) {
            String part = input.substring(position, position + bits);
            position += bits;
            return Integer.parseInt(part, 2);
        }

        public Reader takeSub(int bits) {
            String part = input.substring(position, position + bits);
            position += bits;
            return new Reader(part);
        }
    }
}

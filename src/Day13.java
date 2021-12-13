import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day13 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("input13.txt").toPath());
        Set<Position> set = new HashSet<>();
        for (String line : lines) {
            if (line.contains(",")) {
                int index = line.indexOf(",");
                int x = Integer.parseInt(line.substring(0, index));
                int y = Integer.parseInt(line.substring(index + 1));
                set.add(new Position(x, y));
            }
            if (line.contains("x=")) {
                int index = line.indexOf("x=");
                int x = Integer.parseInt(line.substring(index + 2));
                set = foldx(set, x);
                System.out.println(set.size());
            }
            if (line.contains("y=")) {
                int index = line.indexOf("y=");
                int y = Integer.parseInt(line.substring(index + 2));
                set = foldy(set, y);
                System.out.println(set.size());
            }
        }
        print(set);
    }

    private static void print(Set<Position> set) {
        int minx = set.stream().map(Position::getX).min(Comparator.naturalOrder()).get();
        int maxx = set.stream().map(Position::getX).max(Comparator.naturalOrder()).get();
        int miny = set.stream().map(Position::getY).min(Comparator.naturalOrder()).get();
        int maxy = set.stream().map(Position::getY).max(Comparator.naturalOrder()).get();
        for (int y = miny; y <= maxy; y++) {
            for (int x = minx; x <= maxx; x++) {
                if (set.contains(new Position(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    private static Set<Position> foldx(Set<Position> set, int x) {
        Set<Position> result = new HashSet<>();
        for (Position position : set) {
            if (position.x <= x) {
                result.add(position);
            } else {
                result.add(new Position(x - (position.x - x), position.y));
            }
        }
        return result;
    }

    private static Set<Position> foldy(Set<Position> set, int y) {
        Set<Position> result = new HashSet<>();
        for (Position position : set) {
            if (position.y <= y) {
                result.add(position);
            } else {
                result.add(new Position(position.x, y - (position.y - y)));
            }
        }
        return result;
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}

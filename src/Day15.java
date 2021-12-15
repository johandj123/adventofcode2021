import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day15 {
    int[][] grid;

    public static void main(String[] args) throws IOException {
        new Day15().start();
    }

    private void start() throws IOException {
        readInput();
        path("First: ");
        biggerCave();
        path("Second: ");
    }

    private void biggerCave() {
        int width = grid[0].length;
        int height = grid.length;
        int[][] newGrid = new int[height * 5][width * 5];
        for (int y = 0; y < height * 5; y++) {
            for (int x = 0; x < width * 5; x++) {
                int base = grid[y % height][x % width];
                int px = x / width;
                int py = y / height;
                newGrid[y][x] = add(base, px + py);
            }
        }
        grid = newGrid;
    }

    private int add(int a,int b)
    {
        int result = a;
        for (int i = 0; i < b; i++) {
            if (result < 9) {
                result++;
            } else {
                result = 1;
            }
        }
        return result;
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input15.txt").toPath());
        grid = new int[lines.size()][lines.get(0).length()];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x) - '0';
            }
        }
    }

    private void path(String s) {
        Set<Position> visited = new HashSet<>();
        SortedSet<PositionDistace> queue = new TreeSet<>();
        Position root = new Position(0, 0);
        queue.add(new PositionDistace(root, 0));
        while (!queue.isEmpty()) {
            PositionDistace currrent = queue.first();
            queue.remove(currrent);
            if (visited.contains(currrent.position)) {
                continue;
            }
            visited.add(currrent.position);
            if (currrent.position.x == grid[0].length - 1 && currrent.position.y == grid.length - 1) {
                System.out.println(s + currrent.distance);
                break;
            }
            for (Position next : currrent.position.getNeighbours()) {
                if (!visited.contains(next)) {
                    int nextDistance = currrent.distance + grid[next.y][next.x];
                    queue.add(new PositionDistace(next, nextDistance));
                }
            }
        }
    }

    class Position {
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

        public List<Position> getNeighbours() {
            List<Position> result = new ArrayList<>();
            addIfExists(x - 1, y, result);
            addIfExists(x + 1, y, result);
            addIfExists(x, y - 1, result);
            addIfExists(x, y + 1, result);
            return result;
        }

        private void addIfExists(int x, int y, List<Position> result) {
            if (x >= 0 && y >= 0 && x < grid[0].length && y < grid.length) {
                result.add(new Position(x, y));
            }
        }
    }

    class PositionDistace implements Comparable<PositionDistace> {
        final Position position;
        final int distance;

        public PositionDistace(Position position, int distance) {
            this.position = position;
            this.distance = distance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PositionDistace that = (PositionDistace) o;
            return distance == that.distance && position.equals(that.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, distance);
        }

        @Override
        public int compareTo(PositionDistace o) {
            if (distance < o.distance) {
                return -1;
            }
            if (distance > o.distance) {
                return 1;
            }
            if (position.x < o.position.x) {
                return -1;
            }
            if (position.x > o.position.x) {
                return 1;
            }
            if (position.y < o.position.y) {
                return -1;
            }
            if (position.y > o.position.y) {
                return 1;
            }
            return 0;
        }
    }
}

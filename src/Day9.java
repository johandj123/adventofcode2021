import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day9 {
    int[][] grid;
    boolean[][] done;

    public static void main(String[] args) throws IOException {
        new Day9().start();
    }

    public void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void first() {
        int result = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (isLow(x, y)) {
                    result += (grid[y][x] + 1);
                }
            }
        }
        System.out.println("First: " + result);
    }

    private void second() {
        List<Integer> sizes = new ArrayList<>();
        done = new boolean[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (done[y][x]) continue;
                if (isLow(x, y)) {
                    int size = floodFill(x, y);
                    sizes.add(size);
                }
            }
        }
        sizes.sort(Comparator.reverseOrder());
        System.out.println("Second: " + (sizes.get(0) * sizes.get(1) * sizes.get(2)));
    }

    private int floodFill(int x, int y) {
        if (!isInRange(x, y)) return 0;
        if (grid[y][x] >= 9) return 0;
        if (done[y][x]) return 0;
        done[y][x] = true;
        int result = 1;
        int value = grid[y][x];
        if (get(x - 1, y) > value) result += floodFill(x - 1, y);
        if (get(x + 1, y) > value) result += floodFill(x + 1, y);
        if (get(x, y - 1) > value) result += floodFill(x, y - 1);
        if (get(x, y + 1) > value) result += floodFill(x, y + 1);
        return result;
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input9.txt").toPath());
        grid = new int[lines.size()][lines.get(0).length()];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x) - '0';
            }
        }
    }

    private int get(int x, int y) {
        if (isInRange(x, y)) {
            return grid[y][x];
        } else {
            return 99;
        }
    }

    private boolean isInRange(int x, int y) {
        return x >= 0 && y >= 0 && x < grid[0].length && y < grid.length;
    }

    private boolean isLow(int x, int y) {
        int value = grid[y][x];
        return (value < get(x - 1, y) &&
                value < get(x + 1, y) &&
                value < get(x, y - 1) &&
                value < get(x, y + 1));
    }
}

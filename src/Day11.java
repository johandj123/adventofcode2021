import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day11 {
    int[][] grid;

    public static void main(String[] args) throws IOException {
        new Day11().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        readInput();
        second();
    }

    private void first() {
        int result = 0;
        for (int i = 0; i < 100; i++) {
            result += step();
        }
        System.out.println("First: " + result);
    }

    private void second() {
        int i = 1;
        while (true) {
            if (step() == grid.length * grid[0].length) {
                break;
            }
            i++;
        }
        System.out.println("Second: " + i);
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input11.txt").toPath());
        grid = new int[lines.size()][lines.get(0).length()];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x) - '0';
            }
        }
    }

    private int step() {
        boolean[][] flashed = new boolean[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x]++;
            }
        }

        boolean changed;
        do {
            changed = false;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (!flashed[y][x] && grid[y][x] > 9) {
                        flashed[y][x] = true;
                        changed = true;
                        for (int dy = -1; dy <= 1; dy++) {
                            for (int dx = -1; dx <= 1; dx++) {
                                if (dy != 0 || dx != 0) {
                                    int nx = x + dx;
                                    int ny = y + dy;
                                    if (nx >= 0 && ny >= 0 && nx < grid[0].length && ny < grid.length) {
                                        grid[ny][nx]++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } while (changed);
        int counter = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (flashed[y][x]) {
                    counter++;
                    grid[y][x] = 0;
                }
            }
        }
        return counter;
    }
}

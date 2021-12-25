import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day25 {
    public static void main(String[] args) throws IOException {
        char[][] grid = readInput();
        int steps = 0;
        while (true) {
            char[][] nextGrid = south(east(grid));
            steps++;
            if (equal(grid, nextGrid)) {
                System.out.println("First: " + steps);
                break;
            }
            grid = nextGrid;
        }
    }

    private static boolean equal(char[][] a, char[][] b) {
        for (int y = 0; y < a.length; y++) {
            for (int x = 0; x < b.length; x++) {
                if (a[y][x] != b[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static char[][] east(char[][] grid) {
        char[][] result = new char[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = grid[0].length - 1; x >= 0; x--) {
                if (grid[y][x] == '>') {
                    if (grid[y][(x + 1) % grid[0].length] == '\0') {
                        result[y][(x + 1) % grid[0].length] = '>';
                    } else {
                        result[y][x] = '>';
                    }
                } else if (grid[y][x] == 'v') {
                    result[y][x] = 'v';
                }
            }
        }
        return result;
    }

    private static char[][] south(char[][] grid) {
        char[][] result = new char[grid.length][grid[0].length];
        for (int x = 0; x < grid[0].length; x++) {
            for (int y = grid.length - 1; y >= 0; y--) {
                if (grid[y][x] == 'v') {
                    if (grid[(y + 1) % grid.length][x] == '\0') {
                        result[(y + 1) % grid.length][x] = 'v';
                    } else {
                        result[y][x] = 'v';
                    }
                } else if (grid[y][x] == '>') {
                    result[y][x] = '>';
                }
            }
        }
        return result;
    }

    private static char[][] readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input25.txt").toPath());
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                grid[y][x] = (c == '.') ? '\0' : c;
            }
        }
        return grid;
    }
}

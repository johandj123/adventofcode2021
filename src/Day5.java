import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {
    public static void main(String[] args) throws IOException {
        List<Line> lines = Files.readAllLines(new File("input5.txt").toPath()).stream()
                .map(Line::new)
                .collect(Collectors.toList());
        first(lines);
        second(lines);
    }

    private static void first(List<Line> lines) {
        int count = calculuateNumberOfIntersections(lines, false);
        System.out.println("First: " + count);
    }

    private static void second(List<Line> lines) {
        int count = calculuateNumberOfIntersections(lines, true);
        System.out.println("Second: " + count);
    }

    private static int calculuateNumberOfIntersections(List<Line> lines,boolean diagonal) {
        int[][] field = new int[1000][1000];
        for (Line line : lines) {
            if (line.a.y == line.b.y) {
                int y = line.a.y;
                for (int x = Math.min(line.a.x, line.b.x); x <= Math.max(line.a.x, line.b.x); x++) {
                    field[x][y]++;
                }
            } else if (line.a.x == line.b.x) {
                int x = line.a.x;
                for (int y = Math.min(line.a.y, line.b.y); y <= Math.max(line.a.y, line.b.y); y++) {
                    field[x][y]++;
                }
            } else if (diagonal) {
                if (line.a.x < line.b.x) {
                    drawDiagonal(field, line.a, line.b);
                } else {
                    drawDiagonal(field, line.b, line.a);
                }
            }
        }
        int count = 0;
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[x].length; y++) {
                if (field[x][y] > 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void drawDiagonal(int[][] field, Position a, Position b) {
        int y = a.y;
        int dy = (a.y < b.y) ? 1 : -1;
        for (int x = a.x; x <= b.x; x++) {
            field[x][y]++;
            y += dy;
        }
    }

    static class Position
    {
        Position(String part)
        {
            String[] parts = part.split(",");
            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
        }

        int x;
        int y;
    }

    static class Line
    {
        Line(String line)
        {
            String[] parts = line.split(" -> ");
            a = new Position(parts[0]);
            b = new Position(parts[1]);
        }

        Position a;
        Position b;
    }
}

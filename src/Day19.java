import javax.sql.PooledConnection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day19 {
    public static final int[][] TX =
            {
                    {
                            1, 0, 0
                    },

                    {
                            0, 0, -1
                    },

                    {
                            0, 1, 0
                    }
            };
    public static final int[][] TY =
            {
                    {
                            0, 0, -1
                    },

                    {
                            0, 1, 0
                    },

                    {
                            1, 0, 0
                    }
            };

    public static final int[][] TZ =
            {
                    {
                            0, -1, 0
                    },

                    {
                            1, 0, 0
                    },

                    {
                            0, 0, 1
                    }
            };

    public static final Matrix[] BASIC_TRANSFORMS = new Matrix[]{
            new Matrix(TX), new Matrix(TY), new Matrix(TZ)};

    public static void main(String[] args) throws IOException {
        List<Scanner> scanners = readInput();
        List<Matrix> transforms = allTransforms();
        first(scanners, transforms);
        second(scanners);
    }

    private static List<Scanner> readInput() throws IOException {
        String input = Files.readString(new File("input19.txt").toPath()).trim();
        return Arrays.stream(input.split("\n\n"))
                .map(Scanner::new)
                .collect(Collectors.toList());
    }

    private static void first(List<Scanner> scanners, List<Matrix> transforms) {
        Set<Scanner> matchedScanners = new HashSet<>();
        matchedScanners.add(scanners.get(0));
        while (matchedScanners.size() < scanners.size()) {
            System.out.println(matchedScanners.size());
            outer:
            for (Scanner scanner : scanners) {
                if (!matchedScanners.contains(scanner)) {
                    for (Scanner rootScanner : matchedScanners) {
                        if (Scanner.tryMatch(rootScanner, scanner, transforms)) {
                            matchedScanners.add(scanner);
                            break outer;
                        }
                    }
                }
            }
        }
        Set<Position> positions = new HashSet<>();
        for (Scanner scanner : scanners) {
            positions.addAll(scanner.positions);
        }
        System.out.println("First: " + positions.size());
    }

    private static void second(List<Scanner> scanners) {
        int maxDistance = Integer.MIN_VALUE;
        for (Scanner a : scanners) {
            for (Scanner b : scanners) {
                int distance = Position.manhattan(a.position, b.position);
                maxDistance = Math.max(maxDistance, distance);
            }
        }
        System.out.println("Second: " + maxDistance);
    }

    public static List<Matrix> allTransforms() {
        Set<Matrix> result = new HashSet<>();
        result.add(new Matrix());
        Set<Matrix> added;
        do {
            added = new HashSet<>();
            for (Matrix matrix : result) {
                for (Matrix transform : BASIC_TRANSFORMS) {
                    Matrix newMatrix = Matrix.matrixProduct(transform, matrix);
                    if (!result.contains(newMatrix)) {
                        added.add(newMatrix);
                    }
                }
            }
            result.addAll(added);
        } while (!added.isEmpty());

        return new ArrayList<>(result);
    }

    static class Scanner {
        List<Position> positions = new ArrayList<>();
        Position position = new Position();

        public Scanner(String input) {
            String[] lines = input.split("\n");
            for (int i = 1; i < lines.length; i++) {
                String[] parts = lines[i].split(",");
                Position position = new Position(Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
                positions.add(position);
            }
        }

        public static boolean tryMatch(Scanner root, Scanner scanner, List<Matrix> transforms) {
            for (Matrix matrix : transforms) {
                if (tryMatch(root, scanner, matrix)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean tryMatch(Scanner root, Scanner scanner, Matrix matrix) {
            List<Position> transformedPositions = scanner.positions.stream()
                    .map(matrix::transform)
                    .collect(Collectors.toList());
            for (Position rootPosition : root.positions) {
                for (Position otherPosition : transformedPositions) {
                    Position relativePosition = Position.subtract(otherPosition, rootPosition);
                    List<Position> movedPositions = transformedPositions.stream()
                            .map(position -> Position.subtract(position, relativePosition))
                            .collect(Collectors.toList());
                    int i = commonElements(root.positions, movedPositions);
                    if (i >= 12) {
                        scanner.positions = movedPositions;
                        scanner.position = relativePosition;
                        return true;
                    }
                }
            }

            return false;
        }

        private static int commonElements(List<Position> a, List<Position> b) {
            Set<Position> sa = new HashSet<>(a);
            return (int) b.stream()
                    .filter(sa::contains)
                    .count();
        }
    }

    static class Position {
        final int x;
        final int y;
        final int z;

        public Position() {
            this(0, 0, 0);
        }

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static Position subtract(Position a, Position b) {
            return new Position(a.x - b.x, a.y - b.y, a.z - b.z);
        }

        public static int manhattan(Position a, Position b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y && z == position.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d)", x, y, z);
        }
    }

    static class Matrix {
        final int[][] matrix;

        public Matrix() {
            matrix = new int[3][3];
            matrix[0][0] = matrix[1][1] = matrix[2][2] = 1;
        }

        public Matrix(int[][] matrix) {
            this.matrix = matrix;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Matrix matrix = (Matrix) o;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (this.matrix[y][x] != matrix.matrix[y][x]) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    result = 31 * result + matrix[y][x];
                }
            }
            return result;
        }

        public Position transform(Position position) {
            return new Position(
                    matrix[0][0] * position.x + matrix[0][1] * position.y + matrix[0][2] * position.z,
                    matrix[1][0] * position.x + matrix[1][1] * position.y + matrix[1][2] * position.z,
                    matrix[2][0] * position.x + matrix[2][1] * position.y + matrix[2][2] * position.z
            );
        }

        public Matrix invert() {
            int[][] matrix = new int[3][3];
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    matrix[y][x] = this.matrix[x][y];
                }
            }
            return new Matrix(matrix);
        }

        public static Matrix matrixProduct(Matrix a, Matrix b) {
            int[][] matrix = new int[3][3];
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    int v = 0;
                    for (int z = 0; z < 3; z++) {
                        v += a.matrix[y][z] * b.matrix[z][x];
                    }
                    matrix[y][x] = v;
                }
            }
            return new Matrix(matrix);
        }
    }
}

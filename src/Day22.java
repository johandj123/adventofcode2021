import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day22 {
    public static void main(String[] args) throws IOException {
        List<Cuboid> cuboids = Files.readAllLines(new File("input22.txt").toPath())
                .stream()
                .map(Cuboid::new)
                .collect(Collectors.toList());
        first(cuboids);
        second(cuboids);
    }

    private static void first(List<Cuboid> cuboids) {
        boolean[][][] state = new boolean[101][101][101];
        for (Cuboid cuboid : cuboids) {
            if (cuboid.ranges[0].start >= -50 && cuboid.ranges[0].end <= 50) {
                for (int x = cuboid.ranges[0].start; x <= cuboid.ranges[0].end; x++) {
                    for (int y = cuboid.ranges[1].start; y <= cuboid.ranges[1].end; y++) {
                        for (int z = cuboid.ranges[2].start; z <= cuboid.ranges[2].end; z++) {
                            state[x + 50][y + 50][z + 50] = cuboid.state;
                        }
                    }
                }
            }
        }
        int count = 0;
        for (int x = 0; x < 101; x++) {
            for (int y = 0; y < 101; y++) {
                for (int z = 0; z < 101; z++) {
                    if (state[x][y][z]) {
                        count++;
                    }
                }
            }
        }
        System.out.println("First: " + count);
    }

    private static void second(List<Cuboid> cuboids) {
        DimensionBuilder[] builders = new DimensionBuilder[3];
        for (int i = 0; i < 3; i++) {
            builders[i] = new DimensionBuilder();
        }
        for (Cuboid cuboid : cuboids) {
            for (int i = 0; i < 3; i++) {
                builders[i].add(cuboid.ranges[i]);
            }
        }
        Grid grid = new Grid(builders);
        for (Cuboid cuboid : cuboids) {
            grid.process(cuboid);
        }
        System.out.println("Second: " + grid.count());
    }

    static class Grid {
        Dimension[] dimension;
        boolean[][][] state;

        Grid(DimensionBuilder[] builders) {
            dimension = new Dimension[3];
            for (int i = 0; i < 3; i++) {
                dimension[i] = builders[i].build();
            }
            state = new boolean[dimension[0].size()][dimension[1].size()][dimension[2].size()];
        }

        void process(Cuboid cuboid) {
            Range xr = dimension[0].lookup(cuboid.ranges[0]);
            Range yr = dimension[1].lookup(cuboid.ranges[1]);
            Range zr = dimension[2].lookup(cuboid.ranges[2]);
            for (int x = xr.start; x <= xr.end; x++) {
                for (int y = yr.start; y <= yr.end; y++) {
                    for (int z = zr.start; z <= zr.end; z++) {
                        state[x][y][z] = cuboid.state;
                    }
                }
            }
        }

        long count() {
            long result = 0L;
            for (int x = 0; x < dimension[0].size(); x++) {
                for (int y = 0; y < dimension[1].size(); y++) {
                    for (int z = 0; z < dimension[2].size(); z++) {
                        if (state[x][y][z]) {
                            result += dimension[0].cellSize(x) * dimension[1].cellSize(y) * dimension[2].cellSize(z);
                        }
                    }
                }
            }
            return result;
        }
    }

    static class Dimension {
        final int[] array;

        public Dimension(Set<Integer> set) {
            List<Integer> list = new ArrayList<>(set);
            list.sort(null);
            array = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
        }

        int size() {
            return array.length - 1;
        }

        long cellSize(int index) {
            return array[index + 1] - array[index];
        }

        int lookup(int coordinate) {
            for (int i = 0; i < array.length - 1; i++) {
                if (coordinate < array[i + 1]) {
                    return i;
                }
            }
            throw new UnsupportedOperationException();
        }

        Range lookup(Range range) {
            return new Range(lookup(range.start), lookup(range.end));
        }
    }

    static class DimensionBuilder {
        Set<Integer> set = new HashSet<>();

        void add(Range range) {
            set.add(range.start);
            set.add(range.end + 1);
        }

        Dimension build() {
            return new Dimension(set);
        }
    }

    static class Range {
        final int start;
        final int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    static class Cuboid {
        boolean state;
        Range[] ranges = new Range[3];

        public Cuboid(String line) {
            state = line.startsWith("on");
            initDimension(line, 'x', 0);
            initDimension(line, 'y', 1);
            initDimension(line, 'z', 2);
        }

        private void initDimension(String line, char x, int dimension) {
            int index = line.indexOf(x);
            int index2 = line.indexOf(',', index);
            String part = line.substring(index + 2, index2 == -1 ? line.length() : index2);
            String[] parts = part.split("\\.\\.");
            ranges[dimension] = new Range(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }
}

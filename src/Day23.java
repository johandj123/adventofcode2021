import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day23 {
    public static void main(String[] args) throws IOException {
        System.out.println("First: " + calculateMinimumEnergy(readInput("input23.txt")));
        System.out.println("Second: " + calculateMinimumEnergy(readInput("input23-2.txt")));
//        System.out.println("First example: " + calculateMinimumEnergy(readInput("example23.txt")));
//        System.out.println("Second example: " + calculateMinimumEnergy(readInput("example23-2.txt")));
    }

    public static int calculateMinimumEnergy(char[][] grid) {
        Node root = new Node(grid);
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        SortedSet<NodeDistance> queue = new TreeSet<>();
        queue.add(new NodeDistance(0, root));
        distances.put(root, 0);
        int minEnergy = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            NodeDistance current = queue.first();
            queue.remove(current);
            if (current.node.isEnd()) {
                minEnergy = current.distance;
                Node node = current.node;
                while (previous.containsKey(node)) {
                    System.out.println(node);
                    node = previous.get(node);
                }
                break;
            }
            Map<Node, Integer> neighbours = current.node.getNeighbours();
            for (Map.Entry<Node, Integer> entry : neighbours.entrySet()) {
                Node next = entry.getKey();
                int distance = current.distance + entry.getValue();
                Integer oldDistance = distances.get(next);
                if (oldDistance == null || distance < oldDistance) {
                    if (oldDistance != null) {
                        queue.remove(new NodeDistance(oldDistance, next));
                    }
                    queue.add(new NodeDistance(distance, next));
                    distances.put(next, distance);
                    previous.put(next, current.node);
                }
            }
        }
        return minEnergy;
    }

    private static char[][] readInput(String name) throws IOException {
        List<String> lines = Files.readAllLines(new File(name).toPath());
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
            }
            for (int x = line.length(); x < lines.get(0).length(); x++) {
                grid[y][x] = ' ';
            }
        }
        return grid;
    }

    static class Node {
        char[][] grid;
        boolean[][] done;

        public Node(char[][] grid) {
            this.grid = grid;
            this.done = new boolean[grid.length][grid[0].length];
        }

        public Node(Node node) {
            this.grid = new char[node.grid.length][node.grid[0].length];
            this.done = new boolean[node.grid.length][node.grid[0].length];
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    this.grid[y][x] = node.grid[y][x];
                    this.done[y][x] = node.done[y][x];
                }
            }
        }

        public boolean isEnd() {
            List<Character> list = new ArrayList<>();
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[1][x] != '.' && grid[1][x] != '#') {
                    return false;
                }
                Set<Character> set = new HashSet<>();
                for (int y = 2; y < grid.length; y++) {
                    char c = grid[y][x];
                    if (c >= 'A' && c <= 'D') {
                        set.add(c);
                    }
                }
                if (set.size() > 1) {
                    return false;
                }
                if (!set.isEmpty()) {
                    list.add(set.stream().findFirst().get());
                }
            }
            List<Character> listSorted = new ArrayList<>(list);
            listSorted.sort(null);
            return list.equals(listSorted);
        }

        private int cost(char c) {
            int cost = 1;
            while (c > 'A') {
                cost *= 10;
                c--;
            }
            return cost;
        }

        public Map<Node, Integer> getNeighbours() {
            Map<Node, Integer> result = new HashMap<>();
            moveIntoHallway(result);
            moveIntoRoom(result);
            return result;
        }

        private void moveIntoHallway(Map<Node, Integer> result) {
            for (int x = 0; x < grid[0].length; x++) {
                for (int y = 2; y < grid.length; y++) {
                    char c = grid[y][x];
                    if (c >= 'A' && c <= 'D' && !done[y][x]) {
                        moveIntoHallway(result, x, y);
                        break;
                    }
                }
            }
        }

        private void moveIntoHallway(Map<Node, Integer> result, int x1, int y1) {
            char c = grid[y1][x1];
            for (int y = 1; y < y1; y++) {
                if (grid[y][x1] != '.') {
                    return;
                }
            }
            int distanceUp = y1 - 1;
            for (int x = x1 - 1; x >= 0; x--) {
                if (grid[1][x] != '.') {
                    break;
                }
                if (notAboveRoom(x)) {
                    Node node = new Node(this);
                    node.grid[y1][x1] = '.';
                    node.grid[1][x] = c;
                    int distanceHorizontal = Math.abs(x1 - x);
                    result.put(node, cost(c) * (distanceUp + distanceHorizontal));
                }
            }
            for (int x = x1 + 1; x < grid[0].length; x++) {
                if (grid[1][x] != '.') {
                    break;
                }
                if (notAboveRoom(x)) {
                    Node node = new Node(this);
                    node.grid[y1][x1] = '.';
                    node.grid[1][x] = c;
                    int distanceHorizontal = Math.abs(x - x1);
                    result.put(node, cost(c) * (distanceUp + distanceHorizontal));
                }
            }
        }

        private boolean notAboveRoom(int x) {
            return x != 3 && x != 5 && x != 7 && x != 9;
        }

        private void moveIntoRoom(Map<Node, Integer> result) {
            for (int x = 0; x < grid[0].length; x++) {
                char c = grid[1][x];
                if (c >= 'A' && c <= 'D') {
                    moveIntoRoom(result, x);
                }
            }
        }

        private void moveIntoRoom(Map<Node, Integer> result, int x1) {
            char c = grid[1][x1];
            int x2 = 3 + 2 * (c - 'A');
            int x = x1;
            while (x != x2) {
                if (x != x1 && grid[1][x] != '.') {
                    break;
                }
                if (x < x2) {
                    x++;
                } else {
                    x--;
                }
            }
            if (x == x2 && grid[1][x] == '.') {
                int distanceHorizontal = Math.abs(x1 - x);
                int y = 1;
                while (grid[y + 1][x] == '.') {
                    y++;
                }
                if (y > 1) {
                    boolean wrong = false;
                    for (int yy = y + 1; yy < grid.length; yy++) {
                        if (grid[yy][x] == '#') {
                            break;
                        }
                        if (grid[yy][x] >= 'A' && grid[yy][x] <= 'D' && grid[yy][x] != c) {
                            wrong = true;
                            break;
                        }
                    }
                    if (!wrong) {
                        Node node = new Node(this);
                        node.grid[1][x1] = '.';
                        node.grid[y][x] = c;
                        node.done[y][x] = true;
                        int distanceDown = y - 1;
                        result.put(node, cost(c) * (distanceDown + distanceHorizontal));
                    }
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (grid[y][x] != node.grid[y][x] || done[y][x] != node.done[y][x]) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    result = 31 * result + grid[y][x];
                    result = 31 * result + (done[y][x] ? 1231 : 1237);
                }
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (done[y][x]) {
                        sb.append((char) (grid[y][x] + 32));
                    } else {
                        sb.append(grid[y][x]);
                    }
                }
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    static class NodeDistance implements Comparable<NodeDistance> {
        final int distance;
        final Node node;

        public NodeDistance(int distance, Node node) {
            this.distance = distance;
            this.node = node;
        }

        @Override
        public int compareTo(NodeDistance o) {
            if (distance < o.distance) {
                return -1;
            }
            if (distance > o.distance) {
                return 1;
            }
            Node a = node;
            Node b = o.node;
            for (int y = 0; y < a.grid.length; y++) {
                for (int x = 0; x < a.grid[0].length; x++) {
                    if (a.grid[y][x] < b.grid[y][x]) {
                        return -1;
                    }
                    if (a.grid[y][x] > b.grid[y][x]) {
                        return 1;
                    }
                    if (!a.done[y][x] && b.done[y][x]) {
                        return -1;
                    }
                    if (a.done[y][x] && !b.done[y][x]) {
                        return 1;
                    }
                }
            }
            return 0;
        }

        @Override
        public String toString() {
            return node.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeDistance that = (NodeDistance) o;
            return distance == that.distance && node.equals(that.node);
        }

        @Override
        public int hashCode() {
            return Objects.hash(distance, node);
        }
    }
}

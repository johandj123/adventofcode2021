import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day18 {
    public static void main(String[] args) throws IOException {
        List<Node> inputNodes = readInput();
        first(inputNodes);
        second(inputNodes);
    }

    private static List<Node> readInput() throws IOException {
        List<Node> inputNodes = Files.readAllLines(new File("input18.txt").toPath()).stream()
                .map(Day18::parse)
                .collect(Collectors.toList());
        return inputNodes;
    }

    private static void first(List<Node> inputNodes) {
        Node node = inputNodes.get(0);
        for (int i = 1; i < inputNodes.size(); i++) {
            node = add(node, inputNodes.get(i));
        }
        System.out.println("First: " + node.magnitude());
    }

    private static void second(List<Node> inputNodes) {
        long maxMagnitude = Long.MIN_VALUE;
        for (int i = 0; i < inputNodes.size(); i++) {
            for (int j = 0; j < inputNodes.size(); j++) {
                if (i != j) {
                    Node node = add(inputNodes.get(i), inputNodes.get(j));
                    long magnitude = node.magnitude();
                    maxMagnitude = Math.max(maxMagnitude, magnitude);
                }
            }
        }
        System.out.println("Second: " + maxMagnitude);
    }

    public static Node add(Node node1,Node node2)
    {
        Node node = new Node(new Node(node1), new Node(node2));
        reduce(node);
        return node;
    }

    public static void reduce(Node node) {
        boolean changes;
        do {
            changes = performExplosion(node);
            if (!changes) {
                changes = performSplits(node);
            }
        } while (changes);
    }

    public static boolean performExplosion(Node node) {
        List<NodeDepth> nd = new ArrayList<>();
        constructNodeDepth(node, nd, 0);
        for (int i = 0; i < nd.size(); i++) {
            NodeDepth nodeDepth = nd.get(i);
            Node curnode = nodeDepth.node;
            if (nodeDepth.depth >= 4 &&
                    curnode.pair &&
                    !curnode.node1.pair &&
                    !curnode.node2.pair) {
                int j = i - 2;
                while (j >= 0) {
                    Node leftnode = nd.get(j).node;
                    if (!leftnode.pair) {
                        leftnode.number += curnode.node1.number;
                        break;
                    }
                    j--;
                }
                int k = i + 2;
                while (k < nd.size()) {
                    Node rightnnode = nd.get(k).node;
                    if (!rightnnode.pair) {
                        rightnnode.number += curnode.node2.number;
                        break;
                    }
                    k++;
                }
                curnode.pair = false;
                curnode.number = 0;
                return true;
            }
        }
        return false;
    }

    private static void constructNodeDepth(Node node, List<NodeDepth> nd, int depth) {
        if (node.pair) {
            constructNodeDepth(node.node1, nd, depth + 1);
            nd.add(new NodeDepth(node, depth));
            constructNodeDepth(node.node2, nd, depth + 1);
        } else {
            nd.add(new NodeDepth(node, depth));
        }
    }

    public static boolean performSplits(Node node) {
        if (node.pair) {
            if (node.node1.pair) {
                if (performSplits(node.node1)) {
                    return true;
                }
            } else if (node.node1.number > 9) {
                node.node1 = node.node1.split();
                return true;
            }

            if (node.node2.pair) {
                if (performSplits(node.node2)) {
                    return true;
                }
            } else if (node.node2.number > 9) {
                node.node2 = node.node2.split();
                return true;
            }
        }
        if (!node.pair && node.number > 9) {
            System.out.println("!");
        }
        return false;
    }

    public static Node parse(String input) {
        Reader reader = new Reader(input);
        Node result = parse(reader);
        if (!reader.isAtEnd()) {
            throw new IllegalArgumentException("Unexpected character at end " + reader.current());
        }
        return result;
    }

    private static Node parse(Reader reader) {
        if (reader.current() >= '0' && reader.current() <= '9') {
            int value = reader.current() - '0';
            reader.next();
            return new Node(value);
        } else if (reader.current() == '[') {
            reader.next();
            Node node1 = parse(reader);
            if (reader.current() != ',') {
                throw new IllegalArgumentException("Expected ,");
            }
            reader.next();
            Node node2 = parse(reader);
            if (reader.current() != ']') {
                throw new IllegalArgumentException("Expected ]");
            }
            reader.next();
            return new Node(node1, node2);
        } else {
            throw new IllegalArgumentException("Unknown character " + reader.current());
        }
    }

    static class Reader {
        String input;
        int position;

        public Reader(String input) {
            this.input = input;
        }

        public char current() {
            return input.charAt(position);
        }

        public void next() {
            position++;
        }

        public boolean isAtEnd() {
            return position >= input.length();
        }
    }

    static class Node {
        boolean pair;
        int number;
        Node node1;
        Node node2;

        Node(int number) {
            this.pair = false;
            this.number = number;
        }

        Node(Node node1, Node node2) {
            this.pair = true;
            this.node1 = node1;
            this.node2 = node2;
        }

        Node(Node node) {
            this.pair = node.pair;
            this.number = node.number;
            if (pair) {
                this.node1 = new Node(node.node1);
                this.node2 = new Node(node.node2);
            }
        }

        @Override
        public String toString() {
            if (pair) {
                return "[" + node1 + "," + node2 + "]";
            } else {
                return "" + number;
            }
        }

        Node split() {
            return new Node(new Node(number / 2), new Node((number + 1) / 2));
        }

        long magnitude() {
            if (pair) {
                return 3L * node1.magnitude() + 2L * node2.magnitude();
            } else {
                return (long) number;
            }
        }
    }

    static class NodeDepth {
        Node node;
        int depth;

        public NodeDepth(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }

        @Override
        public String toString() {
            return "NodeDepth{" +
                    "node=" + node +
                    ", depth=" + depth +
                    '}';
        }
    }
}

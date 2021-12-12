import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {
    private Map<String, List<String>> go;

    private Set<String> visited;
    private int counter;
    private VisitCheck visitCheck;

    public static void main(String[] args) throws IOException {
        new Day12().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        second();
    }

    private void first() {
        visited = new HashSet<>();
        counter = 0;
        walkFirst("start");
        System.out.println("First: " + counter);
    }

    private void walkFirst(String v1) {
        if ("end".equals(v1)) {
            counter++;
            return;
        }
        visited.add(v1);
        for (String v2 : go.get(v1)) {
            if (!isSmallCave(v2) || !visited.contains(v2)) {
                walkFirst(v2);
            }
        }
        visited.remove(v1);
    }

    private void second() {
        visitCheck = new VisitCheck();
        counter = 0;
        walkSecond("start");
        System.out.println("Second: " + counter);
    }

    private void walkSecond(String v1) {
        if ("end".equals(v1)) {
            counter++;
            return;
        }
        visitCheck.add(v1);
        for (String v2 : go.get(v1)) {
            if (visitCheck.mayVisit(v2)) {
                walkSecond(v2);
            }
        }
        visitCheck.remove(v1);
    }

    private void readInput() throws IOException {
        List<Edge> edges = Files.readAllLines(new File("input12.txt").toPath())
                .stream()
                .map(Edge::new)
                .collect(Collectors.toList());
        go = new HashMap<>();
        for (Edge edge : edges) {
            add(go, edge.v1, edge.v2);
            add(go, edge.v2, edge.v1);
        }
    }

    private void add(Map<String, List<String>> go, String v1, String v2) {
        List<String> list = go.computeIfAbsent(v1, key -> new ArrayList<>());
        list.add(v2);
    }

    private boolean isSmallCave(String v) {
        return Character.isLowerCase(v.charAt(0));
    }

    class Edge {
        String v1;
        String v2;

        Edge(String line) {
            String[] parts = line.split("-");
            v1 = parts[0];
            v2 = parts[1];
        }
    }

    class VisitCheck {
        Set<String> visited = new HashSet<>();
        String visitedTwice = null;

        void add(String v) {
            if (isSmallCave(v)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                } else {
                    visitedTwice = v;
                }
            }
        }

        void remove(String v) {
            if (isSmallCave(v)) {
                if (v.equals(visitedTwice)) {
                    visitedTwice = null;
                } else {
                    visited.remove(v);
                }
            }
        }

        boolean mayVisit(String v) {
            if (!isSmallCave(v)) {
                return true;
            } else if (!visited.contains(v)) {
                return true;
            } else if (visitedTwice == null && !"start".equals(v) && !"end".equals(v)) {
                return true;
            } else {
                return false;
            }
        }
    }
}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 {
    private List<Integer> numbers;
    private List<int[][]> boards;

    public static void main(String[] args) throws IOException {
        new Day4().start();
    }

    private void start() throws IOException {
        readInput();
        first();
        readInput();
        second();
    }

    private void readInput() throws IOException {
        List<String> lines = Files.readAllLines(new File("input4.txt").toPath());
        numbers = Arrays.stream(lines.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        boards = new ArrayList<>();
        for (int i = 2; i < lines.size(); i += 6) {
            int[][] board = new int[5][5];
            for (int j = 0; j < 5; j++) {
                String line = lines.get(i + j).trim();
                String[] parts = line.split(" +");
                for (int k = 0; k < 5; k++) {
                    board[j][k] = Integer.parseInt(parts[k]);
                }
            }
            boards.add(board);
        }
    }

    private void first() {
        for (int value : numbers) {
            mark(value);
            for (int[][] board : boards) {
                if (isWinner(board)) {
                    int score = value * calculateBoardScore(board);
                    System.out.println("First: " + score);
                    return;
                }
            }
        }
    }

    private void second() {
        int score = -1;
        for (int value : numbers) {
            mark(value);
            for (int[][] board : new ArrayList<>(boards)) {
                if (isWinner(board)) {
                    score = value * calculateBoardScore(board);
                    boards.remove(board);
                }
            }
        }
        System.out.println("Second: " + score);
    }

    private void mark(int value)
    {
        for (int[][] board : boards) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (board[j][k] == value) {
                        board[j][k] = -1;
                    }
                }
            }
        }
    }

    private boolean isWinner(int[][] board)
    {
        for (int j = 0; j < 5; j++) {
            if (isWinRow(board, j)) {
                return true;
            }
        }
        for (int k = 0; k < 5; k++) {
            if (isWinColumun(board, k)) {
                return true;
            }
        }
        return false;
    }

    private boolean isWinRow(int[][] board,int j)
    {
        for (int k = 0; k < 5; k++) {
            if (board[j][k] >= 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isWinColumun(int[][] board,int k)
    {
        for (int j = 0; j < 5; j++) {
            if (board[j][k] >= 0) {
                return false;
            }
        }
        return true;
    }

    private int calculateBoardScore(int[][] board)
    {
        int sum = 0;
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 5; k++) {
                if (board[j][k] >= 0) {
                    sum += board[j][k];
                }
            }
        }
        return sum;
    }
}

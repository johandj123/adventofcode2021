import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Day21 {

    static final int[] COMBINATIONS;

    static {
        COMBINATIONS = new int[10];
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                for (int k = 1; k <= 3; k++) {
                    COMBINATIONS[i + j + k]++;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Day21().start();
    }

    private void start() {
        first();
        second();
    }

    private void first() {
        Die die = new Die();
        Player[] players = new Player[]{new Player(4), new Player(5)};
        int currentPlayerIndex = 0;
        while (true) {
            Player currentPlayer = players[currentPlayerIndex];
            currentPlayer.turn(die);
            if (currentPlayer.isWin()) {
                break;
            }
            currentPlayerIndex = 1 - currentPlayerIndex;
        }
        int result = die.rolls * players[1 - currentPlayerIndex].score;
        System.out.println("First: " + result);
    }

    private void second() {
        Map<DiracGameState, Long> states = new HashMap<>();
        states.put(new DiracGameState(4, 5), 1L);
        long[] totalWinStates = new long[2];
        while (!states.isEmpty()) {
            Map<DiracGameState, Long> newStates = new HashMap<>();
            for (Map.Entry<DiracGameState, Long> entry : states.entrySet()) {
                int currentPlayerIndex = entry.getKey().currentPlayer;
                for (int roll = 3; roll <= 9; roll++) {
                    DiracGameState newKey = new DiracGameState(entry.getKey());
                    long universes = entry.getValue() * COMBINATIONS[roll];
                    if (newKey.turn(roll)) {
                        totalWinStates[currentPlayerIndex] += universes;
                    } else {
                        long count = newStates.getOrDefault(newKey, 0L);
                        newStates.put(newKey, count + universes);
                    }
                }
            }
            states = newStates;
        }
        long max = Arrays.stream(totalWinStates).max().getAsLong();
        System.out.println("Second: " + max);
    }

    private class DiracGameState {
        Player[] player;
        int currentPlayer;

        public DiracGameState(int position1, int position2) {
            player = new Player[]{new Player(position1), new Player(position2)};
            currentPlayer = 0;
        }

        public DiracGameState(DiracGameState d) {
            this.player = new Player[]{new Player(d.player[0]), new Player(d.player[1])};
            this.currentPlayer = d.currentPlayer;
        }

        public boolean turn(int roll) {
            Player player = this.player[currentPlayer];
            player.forward(roll);
            currentPlayer = 1 - currentPlayer;
            return player.score >= 21;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DiracGameState that = (DiracGameState) o;
            return currentPlayer == that.currentPlayer && Arrays.equals(player, that.player);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(currentPlayer);
            result = 31 * result + Arrays.hashCode(player);
            return result;
        }
    }

    private class Player {
        int position;
        int score = 0;

        public Player(int position) {
            this.position = position;
        }

        public Player(Player player) {
            this.position = player.position;
            this.score = player.score;
        }

        public void turn(Die die) {
            int roll = 0;
            for (int i = 0; i < 3; i++) {
                roll += die.roll();
            }
            forward(roll);
        }

        public void forward(int places) {
            position += places;
            while (position >= 11) {
                position -= 10;
            }
            score += position;
        }

        public boolean isWin() {
            return score >= 1000;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return position == player.position && score == player.score;
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, score);
        }
    }

    private class Die {
        int current = 1;
        int rolls = 0;

        public int roll() {
            int result = current;
            current++;
            if (current == 101) {
                current = 1;
            }
            rolls++;
            return result;
        }
    }
}

package memory;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  Contains the main method, acts as driver class handling general game loop.
 *  @author Brice Halder
 */
public class Memory {

    /** The maximum number of cards on the board. */
    private static final int BOARD_SIZE = 36;

    /**
     *  Main driver method.
     *  @param args not used
     */
    public static void main(String[] args) {

        System.out.print("Enter number of players: ");

        Scanner in = new Scanner(System.in);
        int numPlayers = in.nextInt();

        List<Player> players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i + 1));
        }

        // Implement difficulties here
        Board board = new Board(BOARD_SIZE);

        // Strings to store user-inputted coordinates of cards
        String coord1, coord2;

        // Loop as long as the board has unmatched card pairings still in play
        while (board.hasUnmatched()) {
            for (Player player : players) {
                // Repeat until user enters valid coordinates
                do {
                    System.out.print("Enter two coordinates: ");
                    coord1 = in.next(Pattern.compile("[A-F][1-6]"));
                    coord2 = in.next(Pattern.compile("[A-F][1-6]"));
                } while (!validCoordinates(coord1, coord2));

                // If the cards are a match, increment the player's score
                // else, increment their # of non-matching pairs chosen
                if (board.isMatch(coord1, coord2)) {
                    player.increaseScore();
                } else {
                    player.increaseNonmatches();
                }
            }
        }

        // Build a max-heap of the players' scores
        PriorityQueue<Player> scores = new PriorityQueue<>(players.size(),
                ((o1, o2) -> o1.getScore() - o2.getScore()));

        // Print each of the players' scores in descending order
        int count = 1;
        System.out.println("SCORES\n------------\n");
        while (!scores.isEmpty()) {
            Player p = scores.poll();
            System.out.println("Player " + p.getPlayerNumber() + "\t" + p.getScore());
            count++;
        }


    }

    /**
     *  Determines whether the coordinates given are valid coordinates
     *  of two unmatched cards
     *  @param coord1 The coordinate of the first card
     *  @param coord2 The coordinate of the second card
     *  @return true if both of the cards are yet to be matched
     */
    private static boolean validCoordinates(String coord1, String coord2) {
        return true;
    }
}

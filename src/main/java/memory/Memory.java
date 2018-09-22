package memory;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  Contains the main method, acts as driver class handling general game loop.
 *  @author Brice Halder
 */
public class Memory {

    /**
     *  Main driver method.
     *  @param args not used
     */
    public static void main(String[] args) {

        int numPlayers = 0;

        do {
            // Declare Scanner outside since we use it in catch block
            Scanner in = new Scanner(System.in);
            try {
                System.out.print("\nEnter number of players: ");
                numPlayers = in.nextInt();
            } catch (InputMismatchException e) {
                in.nextLine();
            }
        } while (numPlayers <= 0);

        // Add players to a list
        List<Player> players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i + 1));
        }

        int rows = 0;
        int cols = 0;
        // User enters row and column size of board
        do {
            Scanner in = new Scanner(System.in);
            try {
                System.out.print("\nEnter rows and columns (e.g. \"2 3\" for a 2x3 board): ");
                rows = in.nextInt();
                cols = in.nextInt();
            } catch (InputMismatchException e) {
                in.nextLine();
            }
        } while (rows <= 0 || cols <= 0);

        Board.setUp(rows, cols);

        // Print board with all cards face down
        Board.print(null, null);

        // Loop as long as the board has unmatched card pairings still in play
        while (Board.hasUnmatched()) {
            for (Player player : players) {
                System.out.println("\nPlayer " + player.getPlayerNumber() + "!");

                // Strings to store user-inputted coordinates of cards
                String coord1 = null;
                String coord2 = null;

                // Repeat until user enters valid coordinates of two unmatched cards
                do {
                    Scanner in = new Scanner(System.in);
                    System.out.print("Enter two coordinates: ");

                    coord1 = in.next().toUpperCase();
                    coord2 = in.next().toUpperCase();
                } while (!Board.validCoordinates(coord1, coord2));

                // Flip the cards over and print the board
                Board.print(coord1, coord2);

                if (Board.isMatch(coord1, coord2)) {
                    player.increaseScore();
                } else {
                    player.increaseNonmatches();
                }

                // Check if game is finished so we don't loop through the rest of the players
                if (!Board.hasUnmatched()) {
                    break;
                }
            }
        }

        // Build a max-heap of the players' scores
        PriorityQueue<Player> scores = new PriorityQueue<>(players);

        System.out.println("SCORES\n------------\n");

        // Print each of the players' scores in descending order of their # of matches
        while (!scores.isEmpty()) {
            Player p = scores.poll();
            System.out.println("Player " + p.getPlayerNumber() + "\t\tMatches:\t" + p.getScore() +
                    "\t\tNon-matches:\t" + p.getNonMatches());
        }
    }
}

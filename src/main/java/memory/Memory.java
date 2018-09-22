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

    /**
     *  Main driver method.
     *  @param args not used
     */
    public static void main(String[] args) {

        System.out.print("Enter number of players: ");

        Scanner in = new Scanner(System.in);
        int numPlayers = in.nextInt();

        List<Player> players = new ArrayList<Player>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i + 1));
        }

        // Implement difficulties here
        Board board = new Board(6, 6);

        // Strings to store user-inputted coordinates of cards
        String coord1, coord2;

        // Loop as long as the board has unmatched card pairings still in play
        while (Board.hasUnmatched()) {
            for (Player player : players) {
                System.out.println("Player " + player.getPlayerNumber() + "!");

                // Repeat until user enters valid coordinates of two unmatched cards
                do {
                    System.out.print("Enter two coordinates: ");
                    coord1 = in.next(Pattern.compile("[A-F][1-6]"));
                    coord2 = in.next(Pattern.compile("[A-F][1-6]"));
                } while (!Board.validCoordinates(coord1, coord2));

                Board.print();

                // If the cards are a match, increment the player's score
                // else, increment their # of non-matching pairs chosen
                if (Board.isMatch(coord1, coord2)) {
                    player.increaseScore();
                } else {
                    player.increaseNonmatches();
                }
            }
        }

        // Build a max-heap of the players' scores
        PriorityQueue<Player> scores = new PriorityQueue<Player>(players);

        // Print each of the players' scores in descending order
        int count = 1;
        System.out.println("SCORES\n------------\n");
        while (!scores.isEmpty()) {
            Player p = scores.poll();
            System.out.println("Player " + p.getPlayerNumber() + "\t" + p.getScore());
            count++;
        }


    }
}

package memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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
        // Clear the console
        System.out.print("\033[H\033[2J");

        // User inputs # of players
        int numPlayers = getNumPlayers();

        // Add players to a list
        List<Player> players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i + 1));
        }

        // User inputs dimensions of board
        Board.Dimensions dim = getDimensions();

        // Initializes board with given dimensions
        Board.setUp(dim);

        // Print board with all cards face down
        Board.print(null, null);

        // Main game loop, loop as long as the board has unmatched card pairings still in play
        gameLoop:
        while (Board.hasUnmatched()) {
            for (Player player : players) {
                System.out.println("Player " + player.getPlayerNumber() + "!");

                // Strings to store user-inputted coordinates of cards
                String coord1;
                String coord2;

                // Repeat until user enters valid coordinates of two unmatched cards
                do {
                    Scanner in = new Scanner(System.in);
                    System.out.print("Enter two coordinates, or 'Q' to quit: ");

                    coord1 = in.next().toUpperCase();

                    if (coord1.equals("Q")) {
                        break gameLoop;
                    }

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

//        PriorityQueue<Player> scores = new PriorityQueue<>(players);
        // can use priority queue if we wanted to maintain state & ordering of players
        // but in this case, the game is finished so we can sort the original list

        // Sort players list according to their score
        Collections.sort(players);

        System.out.print("\nSCORES\n------------\n");

        // Print each of the players' scores in descending order of their # of matches
        for (Player p : players) {
            System.out.println("Player " + p.getPlayerNumber() + "\t\tMatches:\t" + p.getScore()
                    + "\t\tNon-matches:\t" + p.getNonMatches());
        }
    }

    /**
     *  Loops until user inputs valid dimensions of a board.
     *  Dimensions must multiply to a number divisible by 2
     *  in order for there to be an even number of cards.
     *  Maximum dimension for either side is 9 to ensure game is well
     *  within the bounds of rationality (and also for regex to pattern match).
     *  @return the dimensions of the board
     */
    private static Board.Dimensions getDimensions() {
        Board.Dimensions dim = new Board.Dimensions();

        // User inputs row and column size of board
        do {
            // Declare Scanner outside since we use it in catch block
            Scanner in = new Scanner(System.in);
            try {
                System.out.print("\nEnter rows and columns (e.g. \"2 3\" for a 2x3 board): ");
                dim.rows = in.nextInt();
                dim.cols = in.nextInt();

                if (dim.rows * dim.cols % 2 != 0) {
                    System.out.print("\nRows * Columns should be divisible by 2!");
                    throw new InputMismatchException();
                }

                if (dim.rows > 9 || dim.cols > 9) {
                    System.out.println("\nDimensions are too large!");
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                in.nextLine();
                dim.rows = -1; // to make sure while loop evaluates again
            }
        } while (dim.rows <= 0 || dim.cols <= 0);

        return dim;
    }

    /**
     *  Loops until user inputs a valid number of players.
     *  @return the number of players
     */
    private static int getNumPlayers() {
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
        return numPlayers;
    }
}

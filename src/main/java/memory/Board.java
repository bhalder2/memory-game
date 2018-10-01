package memory;

import emoji4j.Emoji;
import emoji4j.EmojiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 *  Maintains the current state of the board using a list of Card objects.
 *  @author Brice Halder
 */
class Board {

    /**
     *  Struct to allow Memory.main method to
     *  pass in multiple params to Board.java
     */
    static class Dimensions {
        int rows;
        int cols;
    }

    /** List of all the cards in the game. */
    private static List<Card> cards;

    /** Set containing the cards that have yet to be matched. */
    private static Set<Card> unmatched;

    /** The rows of the board. */
    private static int rows;

    /** The columns of the board. */
    private static int cols;

    /**
     *  Initializes the board with given dimensions.
     *  @param d the dimensions of the board
     */
    static void setUp(Dimensions d) {
        rows = d.rows;
        cols = d.cols;

        cards = new ArrayList<>(rows * cols);
        unmatched = new HashSet<>(rows * cols);

        // Generate cards
        for (int i = 0; i < (rows * cols) / 2; i++) {
            String symbol;

            // Generate a random symbol. If the set already contains a card with
            // that symbol, generate a new one & repeat until it finds a symbol not
            // in the set
            do {
                symbol = randomEmoji();
            } while (unmatched.contains(new Card(symbol)));

            Card newCard = new Card(symbol);

            // Add two copies of the card to the list
            // Only one copy needs to be in the set of unmatched card pairings
            cards.add(newCard);
            cards.add(newCard);
            unmatched.add(newCard);
        }

        // O(N) Fisher-Yates shuffling of the cards
        Collections.shuffle(cards);
    }

    /**
     *  Determines if board has any unmatched cards.
     *  @return true if board has cards yet to be matched, false otherwise
     */
    static boolean hasUnmatched() {
        return !unmatched.isEmpty();
    }

    /**
     *  Determines whether two given coordinates contain matching cards.
     *  @param coord1 the first coordinate
     *  @param coord2 the second coordinate
     *  @return true if the coordinates contain matching cards, false otherwise
     */
    static boolean isMatch(String coord1, String coord2) {
        if (coord1 == null || coord2 == null) {
            return false;
        }
        int index1 = coordToIndex(coord1);
        int index2 = coordToIndex(coord2);

        return cards.get(index1).equals(cards.get(index2));
    }

    /**
     *  Determines whether the coordinates given are valid coordinates
     *  of two unmatched cards
     *  @param coord1 The coordinate of the first card
     *  @param coord2 The coordinate of the second card
     *  @return true if both of the cards are yet to be matched
     */
    static boolean validCoordinates(String coord1, String coord2) {
        // Get the letter of the last valid column
        String lastCol = Character.toString((char) ('A' + (cols - 1)));

        // The expected pattern of the input
        // First character should be between A and the last column
        // Second character should be between 1 and the last row
        String expected = "[A-" + lastCol + "][1-" + rows + "]";

        // Check if the coordinates entered match the expected input
        if (!coord1.matches(expected) || !coord2.matches(expected)) {
            return false;
        }

        if (coord1.equals(coord2)) {
            return false;
        }

        Card one = cards.get(coordToIndex(coord1));
        Card two = cards.get(coordToIndex(coord2));

        return unmatched.contains(one) && unmatched.contains(two);
    }

    /**
     *  Prints the board to stdout, flipping the two cards given
     *  for 2 seconds, then re-printing with cards face-down (if
     *  they weren't matching).
     *  @param coord1 the coordinate of the first card
     *  @param coord2 the coordinate of the second card
     */
    static void print(String coord1, String coord2) {
        // Colors for output
        String RESET = "\033[49;39m";
        String WHITE = "\033[107;31m";
        String CLEAR_CONSOLE = "\033[H\033[2J";

        System.out.println(CLEAR_CONSOLE);
        System.out.print("\n\t\t");

        // Prints the column headers
        for (int i = 0; i < cols; i++) {
            System.out.print((char) ('A' + i) + "\t");
        }

        // Declarations here to use in the for-loop
        boolean match = Board.isMatch(coord1, coord2); // if the cards are a match
        int index1 = coordToIndex(coord1);
        int index2 = coordToIndex(coord2);

        // Prints all the cards
        for (int i = 0; i < cards.size(); i++) {

            // Print the row #
            if (i % cols == 0) {
                System.out.print("\n" + RESET + "\n");
                System.out.print(RESET + "\t" + ((i / cols) + 1) + "\t");
            }

            Card toPrint = cards.get(i);

            // Prints the cards given (flipping them)
            // If they're a match, remove them from our unmatched set
            if (i == index1 || i == index2) {
                System.out.print(toPrint + "\t");
                if (match) {
                    unmatched.remove(toPrint);
                }
                continue;
            }

            // Print a blank for unmatched cards, else print the card
            if (unmatched.contains(toPrint)) {
                System.out.print(WHITE + " " + RESET + "\t");
            } else {
                System.out.print(toPrint + "\t");
            }
        }
        System.out.print("\n\n");

        // If cards are given, pause for 1.5s, then print the board
        // again, not providing cards this time (so no cards are flipped)
        if (coord1 != null) {
            System.out.println(match ? "Match!" : "No match!");

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Clears the board
            System.out.println(CLEAR_CONSOLE);
            Board.print(null, null);
        }
    }

    /**
     *  Converts coordinates to indices in our card list.
     *  @param coord the given coordinate to convert
     *  @return the index of the card corresponding to the coordinate,
     *  if null coordinate provided returns -1
     */
    private static int coordToIndex(String coord) {
        if (coord == null) {
            return -1;
        }
        String coordUpper = coord.toUpperCase();
        int letter = coordUpper.charAt(0) - 'A';
        int num = coordUpper.charAt(1) - '0';

        return letter + cols * (num - 1);
    }

    /**
     *  Generates a random emoji from 100 different emoji
     *  @return an emoji's HTML Entity decimal code
     */
    private static String randomEmoji() {
        Emoji emoji;
        do {
            // Choose from 100 different emojis
            String randEmojiDecimalCode = "&#" + (127789 + new Random().nextInt(100)) + ";";

            // Returns the emoji corresponding to the given HTML Entity decimal code
            // @source https://github.com/kcthota/emoji4j
            emoji = EmojiUtils.getEmoji(randEmojiDecimalCode);
        } while (emoji == null);
        return emoji.getEmoji();
    }
}

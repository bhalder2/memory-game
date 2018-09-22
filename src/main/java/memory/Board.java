package memory;

import emoji4j.Emoji;
import emoji4j.EmojiManager;
import emoji4j.EmojiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *  Maintains the current state of the board using a hash table of
 *  Card objects.
 *  @author Brice Halder
 */
class Board {

    private static class Card {

        private String symbol;

        Card(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Card card = (Card) o;
            return Objects.equals(symbol, card.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol);
        }
    }

    /** List of all the cards in the game. */
    private static List<Card> cards;

    /** Set containing the cards that have yet to be matched. */
    private static Set<Card> unmatched;

    private static int rows;

    private static int cols;

    // String declarations for coloring cards
    static String RED = "\033[107;31m";
    static String BLACK = "\033[107;30m";
    static String RESET = "\033[49;39m";
    static String RESET_BACK = "\033[0m";  // Text Reset

    static String REDTEXT = "\033[31;49m";

    static String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    static String ANSI_RESET = "\u001B[0m";

    static String ANSI_RED = "\u001B[31m";


    static String blankCard = ANSI_RED;

    static void setUp(int r, int c) {
        rows = r;
        cols = c;

        cards = new ArrayList<>(rows * cols);
        unmatched = new HashSet<>(rows * cols);

        for (int i = 0; i < (rows * cols) / 2; i++) {
            String symbol;

            // Generate a random symbol. If the set already contains a card with
            // that symbol, generate a new one & repeat until it finds a symbol not
            // in the set
            do {
                symbol = randomEmoji();
            } while (unmatched.contains(new Card(symbol)));
            assert (!unmatched.contains(new Card(symbol)));

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

    static boolean hasUnmatched() {
        return !unmatched.isEmpty();
    }

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

        String lastCol = Character.toString((char) ('A' + (cols - 1)));
        String expected = "[A-" + lastCol + "][1-" + rows + "]";
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

    static void print(String coord1, String coord2) {
        System.out.print("\n\t\t");
        for (int i = 0; i < cols; i++) {
            System.out.print((char) ('A' + i) + "\t");
        }

        boolean match = Board.isMatch(coord1, coord2);

        int index1 = coordToIndex(coord1);
        int index2 = coordToIndex(coord2);

        for (int i = 0; i < cards.size(); i++) {
            if (i % cols == 0) {
                System.out.print("\n" + RESET + "\n");
                System.out.print(RESET + "\t" + ((i / cols) + 1) + "\t");
            }

            Card toPrint = cards.get(i);

            if (i == index1 || i == index2) {
                System.out.print(toPrint + "\t");
                if (match) {
                    unmatched.remove(toPrint);
                }
                continue;
            }

            if (unmatched.contains(toPrint)) {
                System.out.print(RED + " " + RESET + "\t");
            } else {
                System.out.print(toPrint + "\t");
            }
        }
        System.out.print("\n\n");

        if (coord1 != null) {
            System.out.println(match ? "Match!" : "No match!");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("\033[H\033[2J");
            Board.print(null, null);
        }
    }

    private static int coordToIndex(String coord) {
        if (coord == null) {
            return -1;
        }
        String coordUpper = coord.toUpperCase();
        int letter = coordUpper.charAt(0) - 'A';
        int num = coordUpper.charAt(1) - '0';

        return letter + cols * (num - 1);
    }

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

package memory;

import emoji4j.Emoji;
import emoji4j.EmojiManager;
import emoji4j.EmojiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *  Maintains the current state of the board using a hash table of
 *  Card objects.
 *  @author Brice Halder
 */
class Board {

    private class Card {

        private String symbol;

        Card(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    /** List of all the cards in the game. */
    private static List<Card> cards;

    /** Set containing the cards that have yet to be matched. */
    private static Set<Card> unmatched;

    private static int rows;

    private static int cols;

    Board(int numRows, int numCols) {
        rows = numRows;
        cols = numCols;

        cards = new ArrayList<Card>(rows * cols);
        unmatched = new HashSet<Card>(rows * cols);

        for (int i = 0; i < (rows * cols) / 2; i++) {
            String symbol;

            // Generate a random symbol, if the set already contains that
            // card, generate a new one until it finds a symbol not
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

    static boolean hasUnmatched() {
        return !unmatched.isEmpty();
    }

    static boolean isMatch(String coord1, String coord2) {
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
        Card one = cards.get(coordToIndex(coord1));
        Card two = cards.get(coordToIndex(coord2));

        return unmatched.contains(one) && unmatched.contains(two);
    }

    static void print() {
        for (Card card : cards) {
            System.out.print(card);
        }
        System.out.println();
    }

    private static String randomEmoji() {
        return EmojiUtils.getEmoji("&#x1f42d;").getEmoji();
    }

    private static int coordToIndex(String coord) {
        int letter = coord.charAt(0) - 'A';
        int num = coord.charAt(1) - '0';

        return letter + cols * (num - 1);

    }
}

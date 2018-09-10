package memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *  Maintains the current state of the board using a hash table of
 *  Card objects.
 *  @author Brice Halder
 */
class Board {

    /** List of all the cards in the game. */
    private List<Card> cards;

    /** Set containing the cards that have yet to be matched. */
    private Set<Card> unmatched;

    Board(int numCards) {
        cards = new ArrayList<>(numCards);

        for (int i = 0; i < numCards / 2; i++) {
            char symbol;

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

    private char randomEmoji() {
        return 0x2202;
    }

    boolean hasUnmatched() {
        return !unmatched.isEmpty();
    }

    boolean isMatch(String coord1, String coord2) {
        int index1 = coordToIndex(coord1);
        int index2 = coordToIndex(coord2);

        return cards.get(index1).equals(cards.get(index2));
    }

    private int coordToIndex(String coord) {
    }
}

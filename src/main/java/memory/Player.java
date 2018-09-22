package memory;

class Player implements Comparable<Player> {

    /**
     *  The number of non-matching pairs turned over,
     *  mostly intended as a metric for solo play.
     */
    private int nonMatches;

    /**
     *  The number of matching pairs turned over,
     *  intended as a metric for multiplayer gameplay.
     */
    private int matches;

    /**
     *  Which player number this player is.
     */
    private int playerNumber;

    Player(int playerNumber) {
        this.nonMatches = 0;
        this.matches = 0;
        this.playerNumber = playerNumber;
    }

    void increaseScore() {
        matches++;
    }

    void increaseNonmatches() {
        nonMatches++;
    }

    int getNonMatches() {
        return nonMatches;
    }

    int getScore() {
        return matches;
    }

    int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public int compareTo(Player o) {
        // Compares by matches first, then non-matches (non-matches sorted in reverse
        // order since lower non-matches is better)
        int matchDiff = o.getScore() - this.matches;
        return (matchDiff != 0) ? matchDiff : (this.nonMatches - o.getNonMatches());
    }
}

package memory;

class Player implements Comparable<Player> {

    /**
     *  The number of non-matching pairs turned over,
     *  intended as a metric for solo play.
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

    public int compareTo(Player o) {
        return o.getScore() - this.matches;
    }
}

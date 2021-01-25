package players;

public interface Player {
    /**
     * Called to request a move from the player.
     */
    public void getMove();

    /**
     * Called when the opponent makes a move. It makes the move on their own board
     * returns the results about the move.
     * @param x The X coordinate of the move
     * @param y The Y coorindate of the move
     * @return [0]: whether ship was hit, [1]: whether ship was sunk, [2]: whether all ships are destroyed
     */
    public boolean[] enemyMove(int x, int y);

    /**
     * After making a move this updates the enemies board with results provided by the opponent.
     * @param x
     * @param y
     * @param isHit
     */
    public void update(int x, int y, boolean isHit);
}

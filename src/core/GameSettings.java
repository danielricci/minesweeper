package core;

import java.awt.Dimension;

/**
 * The game settings that are available within the game
 *
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public enum GameSettings { 
    BEGINNER(8, 8, 10),
    INTERMEDITE(16, 16, 40),
    EXPERT(30, 16, 99);
    
    public final int ROWS;
    public final int COLUMNS;
    public final int MINES;
    
    GameSettings(int rows, int columns, int mines) {
        ROWS = rows;
        COLUMNS = columns;
        MINES = mines;
    }
    
    public Dimension getDimensions() {
        return new Dimension(COLUMNS, ROWS);
    }
}
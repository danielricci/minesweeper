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
    EXPERT(16, 30, 99),
    CUSTOM(0, 0, 0);
    
    public int ROWS;
    public int COLUMNS;
    public int MINES;
    
    GameSettings(int rows, int columns, int mines) {
        ROWS = rows;
        COLUMNS = columns;
        MINES = mines;
    }
    
    public Dimension getDimensions() {
        return new Dimension(COLUMNS, ROWS);
    }
    
    public static GameSettings getCustomGameSetting(int rows, int columns, int mines) {
        GameSettings settings = GameSettings.CUSTOM;
        settings.ROWS = rows;
        settings.COLUMNS = columns;
        settings.MINES = mines;
        
        return settings;
    }
}
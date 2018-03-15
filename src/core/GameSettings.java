package core;

import java.awt.Dimension;

/**
 * The game settings that are available within the game
 *
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public enum GameSettings { 
    
    /**
     * The beginner game setting
     */
    BEGINNER(9, 9, 10),
    
    /**
     * The intermediate game setting 
     */
    INTERMEDITE(16, 16, 40),
    
    /**
     * The expert game setting
     */
    EXPERT(16, 30, 99),
    
    /**
     * The customer game setting
     */
    CUSTOM(0, 0, 0);

    /**
     * The flag indicating if the marks field is enabled for the game
     */
    public static boolean MARKS_ENABLED = false;
    
    /**
     * The counter of the game settings
     */
    private static int _counter;
    
    /**
     * The time of the high score of the game setting
     */
    private int _time = 999;
    
    /**
     * The name of the high score of the game setting
     */
    private String _name = "Anonymous";
    
    /**
     * The identifier associated to this game setting
     */
    public int IDENTIFIER;

    /**
     * The row count associated to this game setting
     */
    public int ROWS;
    
    /**
     * The column count associated to this game setting
     */
    public int COLUMNS;
    
    /**
     * The number of mines associated to this game setting
     */
    public int MINES;
    
    /**
     * 
     * Constructs a new instance of this class type
     *
     * @param rows The number of rows
     * @param columns The number of columns
     * @param mines The number of mines
     * 
     */
    GameSettings(int rows, int columns, int mines) {
        ROWS = rows;
        COLUMNS = columns;
        MINES = mines;
        
        setIdentifier();
    }
    
    /**
     * Sets the identifier of this game setting
     */
    private void setIdentifier() {
        IDENTIFIER = _counter++;
    }

    /**
     * The dimensions associated to this game setting
     * 
     * @return The dimensions of this game setting
     */
    public Dimension getDimensions() {
        return new Dimension(COLUMNS, ROWS);
    }
    
    /**
     * Gets the time of this game setting
     * 
     * @return The time associated to this game setting
     */
    public int getTime() {
        return _time;
    }
    
    /**
     * Gets the name associated to the game setting
     * 
     * @return The name associated to the game setting
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Sets the high score of this game setting
     * 
     * @param name The name associated to the high score
     * @param time The time associated to the high score
     */
    public void setHighScore(String name, int time) {
        _name = name;
        _time = time;
    }
    
    /**
     * Resets the game settings high score
     */
    public void resetHighscore() {
        _name = "Anonymous";
        _time = 999;
    }
    
    /**
     * Gets a customer game based on the rows, columns, and mines provided
     * 
     * @param rows The rows
     * @param columns The columns
     * @param mines The mines
     * 
     * @return A custom game setting
     */
    public static GameSettings getCustomGameSetting(int rows, int columns, int mines) {
        GameSettings settings = GameSettings.CUSTOM;
        settings.ROWS = rows;
        settings.COLUMNS = columns;
        settings.MINES = mines;
        
        return settings;
    }
}
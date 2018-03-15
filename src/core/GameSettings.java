package core;

import java.awt.Dimension;

/**
 * The game settings that are available within the game
 *
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public enum GameSettings { 
    BEGINNER(9, 9, 10),
    INTERMEDITE(16, 16, 40),
    EXPERT(16, 30, 99),
    CUSTOM(0, 0, 0);
    
    private int _time = 999;
    private String _name = "Anonymous";
    
    private static int _counter;
    public int IDENTIFIER;
    
    public static boolean MARKS_ENABLED = false;
    
    public int ROWS;
    public int COLUMNS;
    public int MINES;
    
    GameSettings(int rows, int columns, int mines) {
        ROWS = rows;
        COLUMNS = columns;
        MINES = mines;
        
        setIdentifier();
    }
    
    private void setIdentifier() {
        IDENTIFIER = _counter++;
    }
    
    public Dimension getDimensions() {
        return new Dimension(COLUMNS, ROWS);
    }
    
    public int getTime() {
        return _time;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setHighScore(String name, int time) {
        _name = name;
        _time = time;
    }
    
    public void reset() {
        _name = "Anonymous";
        _time = 999;
    }
    
    public static GameSettings getCustomGameSetting(int rows, int columns, int mines) {
        GameSettings settings = GameSettings.CUSTOM;
        settings.ROWS = rows;
        settings.COLUMNS = columns;
        settings.MINES = mines;
        
        return settings;
    }
}
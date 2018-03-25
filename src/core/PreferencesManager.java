/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package core;

import java.awt.Point;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import engine.utils.logging.Tracelog;

/**
 * The preferences manager that manages the persisted preferences of the application
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class PreferencesManager {

    /**
     * The preferences node of this class
     */
    private Preferences preferences = Preferences.userNodeForPackage(this.getClass());

    /**
     * The x-axis position of the window
     */
    private int _posX;
    
    /**
     * The y-axis position of the application
     */
    private int _posY;
    
    /**
     * The difficulty setting associated to the application
     */
    private int _difficulty;
    
    /**
     * The mines count associated to the application
     */
    private int _mines;
    
    /**
     * The rows count associated to the application
     */
    private int _rows;
    
    /**
     * The column count associated to the application
     */
    private int _columns;
    
    /**
     * The flag indicating if the marks flag is enabled
     */
    private boolean _marksEnabled;
    
    /**
     * The beginner game setting
     */
    private GameSettings _beginnerSetting = GameSettings.BEGINNER;
    
    /**
     * The intermediate game setting
     */
    private GameSettings _intermediateSetting = GameSettings.INTERMEDITE;
    
    /**
     * The expert game setting
     */
    private GameSettings _expertSetting = GameSettings.EXPERT;
    
    /**
     * The preferences manager singleton instance
     */
    private static PreferencesManager _instance;
    
    /**
     * Constructs a new instance of this class type
     */
    private PreferencesManager() {
        load();
    }
    
    /**
     * The singleton instance associated to this class
     * 
     * @return The singleton instance assoiated to this class
     */
    public static PreferencesManager instance() {
        if(_instance == null) {
            _instance = new PreferencesManager();
        }
        
        return _instance;
    }
    
    /**
     * Stores the window position 
     * 
     * @param point The window position
     */
    public void setWindowPosition(Point point) {
        _posX = point.x;
        _posY = point.y;
    }
    
    /**
     * Gets the window position that was stored
     * 
     * @return The window position
     */
    public Point getWindowPosition() {
        return new Point(_posX, _posY);
    }
    
    /**
     * Stores the game difficulty
     * 
     * @param difficulty The game difficulty
     */
    public void setGameDifficulty(int difficulty) {
        _difficulty = difficulty;
    }
    
    /**
     * Gets the game difficulty
     * 
     * @return The game difficulty
     */
    public int getGameDifficulty() {
        return _difficulty;
    }

    /**
     * Stores the game mines counter
     * 
     * @param mines The mines counter
     */
    public void setGameMines(int mines) {
        _mines = mines;
    }
    
    /**
     * Gets the game mines count
     * 
     * @return The game mines count
     */
    public int getGameMines() {
        return _mines;
    }
    
    /**
     * Stores the game rows count
     * 
     * @param rows The rows count
     */
    public void setGameRows(int rows) {
        _rows = rows;
    }
    
    /**
     * Gets the game rows count
     * 
     * @return The game rows count
     */
    public int getGameRows() {
        return _rows;
    }

    /**
     * Stores the column count
     * 
     * @param columns The column count
     */
    public void setGameColumns(int columns) {
        _columns = columns;
    }
    
    /**
     * Gets the game columns count
     * 
     * @return The game columns count
     */
    public int getGameColumns() {
        return _columns;
    }
    
    /**
     * Gets the game settings associcated to this manager
     * 
     * @param setting The game setting to get
     * 
     * @return An updated representation of the specified game setting provided
     */
    public GameSettings getGameSettings(GameSettings setting) {
        switch(setting) {
        case BEGINNER:
            return _beginnerSetting;
        case INTERMEDITE:
            return _intermediateSetting;
        case EXPERT:
            return _expertSetting;
        default:
            return null;
        }
    }
    
    /**
     * Stores the marks enabled flag
     * 
     * @param marksEnabled TRUE if the marks is enabled, FALSE otherwise
     */
    public void setMarksEnabled(boolean marksEnabled) {
        _marksEnabled = marksEnabled;
    }
    
    /**
     * Gets the marks enabled flag
     * 
     * @return TRUE if marks is enabled, FALSE otherwise
     */
    public boolean getMarksEnabled() {
        return _marksEnabled;
    }
    
    /**
     * Resets the preferences manager
     */
    public void reset() {
        try {
            Preferences.userNodeForPackage(this.getClass()).clear();
        } catch (BackingStoreException exception) {
            Tracelog.log(Level.SEVERE, true, exception);
        }
    }
    
    /**
     * Saves all the preferences stored within this preferences manager
     */
    public void save() {
        preferences.putInt("_posX", _posX);
        preferences.putInt("_posY", _posY);
        preferences.putInt("_difficulty", _difficulty);
        preferences.putInt("_rows", _rows);
        preferences.putInt("_columns", _columns);
        preferences.putInt("_mines", _mines);
        preferences.putBoolean("_marksEnabled", _marksEnabled);
        preferences.put("_beginnerSettingName", _beginnerSetting.getName());
        preferences.putInt("_beginnerSettingTime", _beginnerSetting.getTime());
        preferences.put("_intermediateSettingName", _intermediateSetting.getName());
        preferences.putInt("_intermediateSettingTime", _intermediateSetting.getTime());
        preferences.put("_expertSettingName", _expertSetting.getName());
        preferences.putInt("_expertSettingTime", _expertSetting.getTime());
        try {
            preferences.flush();
        } catch (BackingStoreException exception) {
            Tracelog.log(Level.SEVERE, true, exception);
        }
    }
    
    /**
     * Loads all the preferences into the preferences manager
     */
    private void load() {
        _posX = preferences.getInt("_posX", 0);
        _posY = preferences.getInt("_posY", 0);
        _difficulty = preferences.getInt("_difficulty", 0);
        _rows = preferences.getInt("_rows", 0);
        _columns = preferences.getInt("_columns", 0);
        _mines = preferences.getInt("_mines", 0);
        
        _marksEnabled = preferences.getBoolean("_marksEnabled", false);
        
        _beginnerSetting.setHighScore(
            preferences.get("_beginnerSettingName", "Anonymous"),
            preferences.getInt("_beginnerSettingTime", 999)
        );
        
        _intermediateSetting.setHighScore(
            preferences.get("_intermediateSettingName", "Anonymous"),
            preferences.getInt("_intermediateSettingTime", 999)
        );
        
        _expertSetting.setHighScore(
            preferences.get("_expertSettingName", "Anonymous"),
            preferences.getInt("_expertSettingTime", 999)
        );
    }    
}
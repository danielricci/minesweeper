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

public class PreferencesManager {

    private int _posX;
    private int _posY;
    private int _difficulty;
    private int _mines;
    private int _rows;
    private int _columns;
    
    private boolean _marksEnabled;
    
    private GameSettings _beginnerSetting = GameSettings.BEGINNER;
    private GameSettings _intermediateSetting = GameSettings.INTERMEDITE;
    private GameSettings _expertSetting = GameSettings.EXPERT;
    
    private static PreferencesManager _instance;
    
    private PreferencesManager() {
        load();
    }
    
    public static PreferencesManager instance() {
        if(_instance == null) {
            _instance = new PreferencesManager();
        }
        
        return _instance;
    }
    
    public void setWindowPositionX(Point point) {
        _posX = point.x;
        _posY = point.y;
    }
    
    public Point getWindowPositionX() {
        return new Point(_posX, _posY);
    }
    
    public void setGameDifficulty(int difficulty) {
        _difficulty = difficulty;
    }
    
    public int getGameDifficulty() {
        return _difficulty;
    }

    public void setGameMines(int mines) {
        _mines = mines;
    }
    
    public int getGameMines() {
        return _mines;
    }
    
    public void setGameRows(int rows) {
        _rows = rows;
    }
    
    public int getGameRows() {
        return _rows;
    }

    public void setGameColumns(int columns) {
        _columns = columns;
    }
    
    public int getGameColumns() {
        return _columns;
    }
    
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
    
    public void setMarksEnabled(boolean marksEnabled) {
        _marksEnabled = marksEnabled;
    }
    
    public boolean getMarksEnabled() {
        return _marksEnabled;
    }
    
    public void reset() {
        try {
            Preferences.userNodeForPackage(this.getClass()).clear();
        } catch (BackingStoreException exception) {
            Tracelog.log(Level.SEVERE, true, exception);
        }
    }
    
    public void save() {
        Preferences preferences = Preferences.userNodeForPackage(this.getClass());
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
    
    private void load() {
        Preferences preferences = Preferences.userNodeForPackage(this.getClass());
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
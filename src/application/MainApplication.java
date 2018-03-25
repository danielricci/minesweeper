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

package application;

import java.awt.EventQueue;
import java.awt.Point;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JCheckBoxMenuItem;

import controllers.BoardController;
import core.PreferencesManager;
import engine.core.factories.AbstractSignalFactory;
import engine.core.navigation.MenuBuilder;
import engine.core.system.AbstractApplication;
import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;
import engine.utils.globalisation.Localization;
import engine.utils.logging.Tracelog;
import menu.AboutMenuItem;
import menu.BeginnerModeMenuItem;
import menu.CustomModeMenuItem;
import menu.DebugEmptyTilesMenuItem;
import menu.DebugGameMenuItem;
import menu.DebugNeighboursMenuItem;
import menu.DebuggerWindowMenuItem;
import menu.ExitMenuItem;
import menu.ExpertModeMenuItem;
import menu.HighScoresMenuItem;
import menu.IntermediateModeMenuItem;
import menu.MarksMenuItem;
import menu.NewGameMenuItem;
import resources.LocalizedStrings;

/**
 * This is the main application, the main method resides within this class
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class MainApplication extends AbstractApplication {

    /**
     * Constructs a new instance of this class type
     */
    public MainApplication() {
        setResizable(false);
    }

    /**
     * The main method entry-point for the application
     * 
     * @param args The list of args for the application
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                // Get the debug mode state based on the arguments passed into the application
                boolean debugMode = false;
                for(String arg : args) {
                    if(arg.trim().equalsIgnoreCase("-debug")) {
                        debugMode = true;
                        break;
                    }
                }
                MainApplication.initialize(MainApplication.class, debugMode);
                MainApplication.instance().setVisible(true);
            }
        });
    }

    /**
     * Populates the game menu
     */
    private void populateGameMenu() {
        MenuBuilder.start(getJMenuBar())
        .addMenu(Localization.instance().getLocalizedString(LocalizedStrings.Game))
        .addMenuItem(NewGameMenuItem.class)
        .addSeparator() 
        .addMenuItem(BeginnerModeMenuItem.class)
        .addMenuItem(IntermediateModeMenuItem.class)
        .addMenuItem(ExpertModeMenuItem.class)
        .addMenuItem(CustomModeMenuItem.class)
        .addSeparator()
        .addMenuItem(MarksMenuItem.class)
        .addSeparator()
        .addMenuItem(HighScoresMenuItem.class)
        .addSeparator()
        .addMenuItem(ExitMenuItem.class);
    }

    /**
     * Populates the debug menu
     */
    private void populateDebugMenu() {
        MenuBuilder.start(getJMenuBar())
        .addMenu(Localization.instance().getLocalizedString(LocalizedStrings.Debug))
        .addMenuItem(DebugGameMenuItem.class)
        .addSeparator()
        .addMenuItem(DebugNeighboursMenuItem.class)
        .addMenuItem(DebugEmptyTilesMenuItem.class)
        .addSeparator()
        .addMenuItem(DebuggerWindowMenuItem.class);
    }

    /**
     * Populates the help menu
     */
    private void populateHelpMenu() {
        MenuBuilder.start(getJMenuBar())
        .addMenu(Localization.instance().getLocalizedString(LocalizedStrings.Help))
        .addMenuItem(AboutMenuItem.class);
    }
    
    @Override public boolean clear() {

        // Clear the factory
        AbstractSignalFactory.reset();
        
        this.getContentPane().removeAll();

        // validate this container and all of the sub-components
        validate();

        // Repaint everything to apply the changes made
        repaint();

        return super.clear();
    }

    @Override protected void onInitializeWindow() {
        super.onInitializeWindow();

        setTitle(Localization.instance().getLocalizedString(LocalizedStrings.Title));
        setIconImage(Localization.instance().getLocalizedData(LocalizedStrings.GameIcon));
        
        // Populate the game menu
        populateGameMenu();

        // If this is a debug session, populate the debug menu
        if(isDebug()) {
            populateDebugMenu();
        }

        // Populate the help menu
        populateHelpMenu();

        // If the game is in debug mode then call the debug mode new game, else call the normal new game
        if(isDebug()) {
            MenuBuilder.search(MainApplication.instance().getJMenuBar(), DebugGameMenuItem.class).onExecute(null);
        }
        else {
            switch(PreferencesManager.instance().getGameDifficulty())
            {
            case 0:
                MenuBuilder.search(MainApplication.instance().getJMenuBar(), BeginnerModeMenuItem.class).getComponent(JCheckBoxMenuItem.class).doClick();
                break;
            case 1:
                MenuBuilder.search(MainApplication.instance().getJMenuBar(), IntermediateModeMenuItem.class).getComponent(JCheckBoxMenuItem.class).doClick();
                break;
            case 2:
                MenuBuilder.search(MainApplication.instance().getJMenuBar(), ExpertModeMenuItem.class).getComponent(JCheckBoxMenuItem.class).doClick();
                break;
            case 3:
                // Note: When we load custom mode up, we go to the default setting
                MenuBuilder.search(MainApplication.instance().getJMenuBar(), BeginnerModeMenuItem.class).getComponent(JCheckBoxMenuItem.class).doClick();
                break;
            default:
                Tracelog.log(Level.SEVERE, true, "Unexpected difficulty given, defaulting back to beginner");
                MenuBuilder.search(MainApplication.instance().getJMenuBar(), NewGameMenuItem.class).getComponent(JCheckBoxMenuItem.class).doClick();
            }
        }
        
        // Get the location that was last saved and position the window there
        Point location = PreferencesManager.instance().getWindowPosition(); 
        if(location.x == 0 && location.y == 0){
            setLocationRelativeTo(null);
        }
        else {
            setLocation(PreferencesManager.instance().getWindowPosition());    
        }
        
        if(PreferencesManager.instance().getMarksEnabled()) {
            MenuBuilder.search(getJMenuBar(), MarksMenuItem.class).getComponent(JCheckBoxMenuItem.class).doClick();
        }
    }

    @Override protected void onBeforeEngineDataInitialized() {
        EngineProperties.instance().setProperty(Property.DATA_PATH_XML, "/generated/tilemap.xml");
        EngineProperties.instance().setProperty(Property.DATA_PATH_SHEET, "/generated/tilemap.png");
        EngineProperties.instance().setProperty(Property.LOCALIZATION_PATH_CVS, "resources/LocalizedStrings.csv");
        EngineProperties.instance().setProperty(Property.ENGINE_OUTPUT, "true");
        EngineProperties.instance().setProperty(Property.LOG_DIRECTORY,  System.getProperty("user.home") + File.separator + "desktop" + File.separator);
    }
    
    @Override public void dispose() {
        PreferencesManager.instance().setGameColumns(BoardController.GAME_SETTINGS.COLUMNS);
        PreferencesManager.instance().setGameRows(BoardController.GAME_SETTINGS.ROWS);
        PreferencesManager.instance().setGameMines(BoardController.GAME_SETTINGS.MINES);
        PreferencesManager.instance().setGameDifficulty(BoardController.GAME_SETTINGS.IDENTIFIER);
        PreferencesManager.instance().setWindowPosition(getLocation());
        PreferencesManager.instance().save();
        
        super.dispose();
    }
}
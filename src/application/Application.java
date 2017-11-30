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

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import engine.core.navigation.MenuBuilder;
import engine.core.system.AbstractApplication;
import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;
import engine.utils.globalisation.Localization;
import menu.AboutMenuItem;
import menu.BeginnerModeMenuItem;
import menu.BestTimesMenuItem;
import menu.CustomModeMenuItem;
import menu.DebugGameMenuItem;
import menu.DebuggerWindowMenuItem;
import menu.ExitMenuItem;
import menu.ExpertModeMenuItem;
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
public final class Application extends AbstractApplication {
	
	/**
	 * Constructs a new instance of this class type
	 */
	public Application() {

		// Set the application dimensions
		Dimension applicationDimensions = new Dimension(600, 600);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Set the size of application
        setSize(applicationDimensions);

		// Set the location of the window to be in middle of the screen
		setLocation(
			screenSize.width / 2 - applicationDimensions.width / 2,
			screenSize.height / 2 - applicationDimensions.height / 2
		);
		
	      // The user cannot resize the game
        setResizable(false);
	}
	
	/**
	 * The main method entry-point for the application
	 * 
	 * @param args The outside argument / command line argument
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
			
			Application.initialize(Application.class, debugMode);
			Application.instance().setVisible(true);
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
                .addMenuItem(BestTimesMenuItem.class)
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
	
	
	@Override protected void onApplicationShown() {
		populateGameMenu();
		if(isDebug()) {
			populateDebugMenu();
		}
		populateHelpMenu();
	}
	
	@Override protected void onBeforeEngineDataInitialized() {
		EngineProperties.instance().setProperty(Property.LOCALIZATION_PATH_CVS, "resources/LocalizedStrings.csv");
		EngineProperties.instance().setProperty(Property.ENGINE_OUTPUT, "true");
		//EngineProperties.instance().setProperty(Property.LOG_DIRECTORY,  System.getProperty("user.home") + File.separator + "desktop" + File.separator);
		//EngineProperties.instance().setProperty(Property.DATA_PATH_XML, "/generated/tilemap.xml");
		//EngineProperties.instance().setProperty(Property.DATA_PATH_SHEET, "/generated/tilemap.png");
	}
}
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import engine.core.system.AbstractApplication;

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
		
		// Set the default state of the application to be maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Pressing on the close button won't do it's default action
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			
			/**
			 * Catches a closing of this JFrame so we can handle it properly
			 * 
			 * @param windowEvent The event that this window triggered
			 */
			@Override public void windowClosing(WindowEvent windowEvent) {
				if(!Application.instance().clear()) {
					return;
				}
				
				// Dispose the window
				Application.shutdown();
			};		
		});

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
        try {      	
        	EventQueue.invokeLater(new Runnable() {
        		@Override public void run() {
        			try {
        				Application.initialize(Application.class, false);
        				Application.instance().setVisible(true);
        			} catch (Exception exception) {
        				exception.printStackTrace();
        			}
            	}
        	});
		} 
        catch (Exception exception) {
        		exception.printStackTrace();
        }
    }
	
	/**
	 * Populates the file menu
	 */
	private void PopulateFileMenu() {
		// Get an option builder to act as the root builder
//		MenuBuilder.start(getJMenuBar())
//			.addMenu("Test")
//				.addMenuItem(FirstEntry.class)
//				.addMenuItem(SecondEntry.class)
//				.addMenuItem(ThirdEntry.class)
//				.addMenuItem(FourthEntry.class);
	}
	
	@Override protected void onBeforeEngineDataInitialized() {
		//EngineProperties.instance().setProperty(Property.DATA_PATH_XML, "/generated/tilemap.xml");
		//EngineProperties.instance().setProperty(Property.DATA_PATH_SHEET, "/generated/tilemap.png");
		//EngineProperties.instance().setProperty(Property.ENGINE_OUTPUT, "true");
		//EngineProperties.instance().setProperty(Property.LOG_DIRECTORY,  System.getProperty("user.home") + File.separator + "desktop" + File.separator);
	}
	
	@Override protected void onApplicationShown() {
		PopulateFileMenu();
	}
}
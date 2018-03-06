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

package menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import application.MainApplication;
import controllers.GameStateController;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.navigation.AbstractMenuItem;
import engine.utils.globalisation.Localization;
import game.core.factories.ControllerFactory;
import game.core.factories.ViewFactory;
import resources.LocalizedStrings;
import views.MainView;

/**
 * Menu item for starting a new game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class NewGameMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     *
     * @param parent The parent associated to this menu item
     */
    public NewGameMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(LocalizedStrings.New)), parent);
        super.get(JMenuItem.class).setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        
        // Reset the game contents before continuing
        GameStateController gameStateController = AbstractFactory.getFactory(ControllerFactory.class).get(GameStateController.class);
        if(gameStateController != null) {
            gameStateController.resetGame(false);
        }
        
        // Flush the application before continuing
        if(!MainApplication.instance().clear()) {
            return;
        }
        
        // Create the board view
        MainView view = AbstractSignalFactory.getFactory(ViewFactory.class).add(new MainView(), true);

        // Add the view to the application
        MainApplication.instance().add(view);

        // Render the specified view
        view.render();
 
        // Fit the application with its new dimensions
        MainApplication.instance().pack();

        // Center the application in the middle of the screen. This must be done after a call is done to pack()
        // or it will not be centered properly
        MainApplication.instance().setLocationRelativeTo(null);
    }
}
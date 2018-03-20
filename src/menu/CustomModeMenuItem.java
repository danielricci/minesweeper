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

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import application.MainApplication;
import controllers.BoardController;
import core.GameSettings;
import core.PreferencesManager;
import engine.core.factories.AbstractSignalFactory;
import engine.core.navigation.AbstractMenuItem;
import engine.core.navigation.MenuBuilder;
import engine.utils.globalisation.Localization;
import game.core.factories.ViewFactory;
import resources.LocalizedStrings;
import views.CustomGameDialogView;

/**
 * Menu item for custom game mode
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class CustomModeMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     *
     * @param parent The parent associated to this menu item
     */
    public CustomModeMenuItem(JComponent parent) {
        super(new JCheckBoxMenuItem(Localization.instance().getLocalizedString(LocalizedStrings.CustomMode)), parent);
    }

    @Override protected String getGroupName() {
        return "GameMode";
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        if(actionEvent != null) {
            // Create and show the dialog related to the custom game
            CustomGameDialogView dialog = AbstractSignalFactory.getFactory(ViewFactory.class).add(new CustomGameDialogView(), false);
            dialog.render();
            
            // If the user did not select the OK button then remove the contents of the dialog
            if(dialog.getDialogResult() != JOptionPane.OK_OPTION) {
                dialog.remove();
                return;
            }
            
            // Get a reference to the board conntroller and set the game settings to the values
            // entered within the dialog previously
            BoardController.GAME_SETTINGS = GameSettings.getCustomGameSetting(dialog.getRowsCount(), dialog.getColumnsCount(), dialog.getMinesCount());
        }
        else {
            BoardController.GAME_SETTINGS = GameSettings.getCustomGameSetting(
                PreferencesManager.instance().getGameRows(),
                PreferencesManager.instance().getGameColumns(),
                PreferencesManager.instance().getGameMines()
            );
        }
        
        MenuBuilder.search(MainApplication.instance().getJMenuBar(), NewGameMenuItem.class).onExecute(actionEvent);
    }
}
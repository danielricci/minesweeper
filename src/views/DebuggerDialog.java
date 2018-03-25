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

package views;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;

import application.MainApplication;
import controllers.BoardController;
import controllers.DebuggerController;
import core.PreferencesManager;
import engine.communication.internal.signal.arguments.BooleanEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.DialogView;
import engine.utils.globalisation.Localization;
import game.core.factories.ControllerFactory;
import game.core.factories.ViewFactory;
import resources.LocalizedStrings;

/**
 * This view represents the configuration window for debugging different game scenarios
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 */
public class DebuggerDialog extends DialogView {

    /**
     * This checkbox when selected will allow you to place mines on the board
     */
    private final JCheckBoxMenuItem _minesCheckBox = new JCheckBoxMenuItem(Localization.instance().getLocalizedString(LocalizedStrings.Mines));

    /**
     * This checkbox when selected will hide all the buttons from the board
     */
    private final JCheckBoxMenuItem _buttonsHideCheckBox = new JCheckBoxMenuItem(Localization.instance().getLocalizedString(LocalizedStrings.HideButtons));
    
    /**
     * This button when clicked will clear the contents of the board
     */
    private final JButton _clearButton = new JButton(Localization.instance().getLocalizedString(LocalizedStrings.Clear));
    
    /**
     * This button when clicked will clear the preferences that have been saved within the application
     */
    private final JButton _clearPreferencesButton = new JButton(Localization.instance().getLocalizedString(LocalizedStrings.ClearPreferences));

    /**
     * Constructs a new instance of this class type
     */
    public DebuggerDialog() {
        super(MainApplication.instance(), Localization.instance().getLocalizedString(LocalizedStrings.DebugWindow), 300, 300);

        // Set the controller for this dialog view
        getViewProperties().setEntity(AbstractSignalFactory.getFactory(ControllerFactory.class).add(new DebuggerController(), true));

        // Set the view properties for this dialog view
        setResizable(false);
        setAlwaysOnTop(true);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));        
    }

    @Override public void initializeComponents() {
        // MINES Panel
        JPanel minesPanel = new JPanel();
        minesPanel.add(_minesCheckBox);
        minesPanel.setMaximumSize(minesPanel.getPreferredSize());
        add(minesPanel);

        // BUTTONS Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(_buttonsHideCheckBox);
        buttonsPanel.setMaximumSize(buttonsPanel.getPreferredSize());
        add(buttonsPanel);

        // ACTIONS PANEL
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.add(_clearButton);
        actionPanel.add(_clearPreferencesButton);        
        actionPanel.setMaximumSize(actionPanel.getPreferredSize());
        add(actionPanel);
    }

    @Override public void initializeComponentBindings() {
        
        // Get a reference to the controllers being used
        BoardController boardController = AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class);
        DebuggerController debuggerController = getViewProperties().getEntity(DebuggerController.class);
        
        
        // Mouse listener for when the mines checkbox is clicked
        _minesCheckBox.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent event) {
                debuggerController.setMinesEnabled(_minesCheckBox.isSelected());
            }
        });
        
        // Mouse listener for when the hide checkbox is selected
        _buttonsHideCheckBox.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent event) {
                AbstractFactory.getFactory(ViewFactory.class).multicastSignalListeners(
                    TileView.class, new BooleanEventArgs(DebuggerDialog.this, TileView.EVENT_SHOW_TILE_BUTTONS, _buttonsHideCheckBox.isSelected())
                );
            }
        });

        // Mouse listener for when the clear button is clicked
        _clearButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent event) {
                boardController.clearEntities();
            }
        });
        
        // Mouse listener for when the preferences button is clicked
        _clearPreferencesButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent event) {
                PreferencesManager.instance().reset();
                System.exit(0);
            }
        });
    }

    @Override public void render() {

        // Pack the contents of the layout
        pack();

        // Positions this dialog at the middle-right of the application
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation((int)(screenSize.getWidth() - getWidth()), (int)((screenSize.getHeight() - getHeight()) / 2));

        // Display the dialog
        setVisible(true);

        // Request focus on the window
        requestFocus();
    }
}
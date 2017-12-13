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

package views.dialogs;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;

import application.MainApplication;
import controllers.DebuggerController;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.DialogView;
import engine.utils.globalisation.Localization;
import game.core.ControllerFactory;
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
     * This button when clicked will generate the contents of the board
     */
    private final JButton _generateButton = new JButton(Localization.instance().getLocalizedString(LocalizedStrings.Generate));

    /**
     * This button when clicked will clear the contents of the board
     */
    private final JButton _clearButton = new JButton(Localization.instance().getLocalizedString(LocalizedStrings.Clear));

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

        // Set the enabled states of the dialogs components 
        _minesCheckBox.setEnabled(true);
        _generateButton.setEnabled(true);

        // MINES Panel
        JPanel minesPanel = new JPanel();
        minesPanel.add(_minesCheckBox);
        minesPanel.setMaximumSize(minesPanel.getPreferredSize());
        add(minesPanel);

        // ACTIONS PANEL
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.add(_generateButton);
        actionPanel.add(_clearButton);
        actionPanel.setMaximumSize(actionPanel.getPreferredSize());
        add(actionPanel);
    }

    @Override public void initializeComponentBindings() {

        // Mouse listener for when the mines button is clicked
        _minesCheckBox.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent e) {
                DebuggerController controller = DebuggerDialog.this.getViewProperties().getEntity(DebuggerController.class);
                controller.setMinesEnabled(_minesCheckBox.isSelected());
            }
        });

        // Mouse listener for when the generate button is clicked
        _generateButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent event) {
                _generateButton.setEnabled(false);
                _minesCheckBox.setEnabled(false);
            }
        });

        // Mouse listener for when the clear button is clicked
        _clearButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent event) {
                _generateButton.setEnabled(true);
                _minesCheckBox.setEnabled(true);
                
                DebuggerController controller = DebuggerDialog.this.getViewProperties().getEntity(DebuggerController.class);
                controller.clearContents();
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
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

import javax.swing.BoxLayout;

import application.MainApplication;
import engine.core.mvc.view.DialogView;
import engine.utils.globalisation.Localization;
import resources.LocalizedStrings;

/**
 * This view represents the configuration window for debugging different game scenarios
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 */
public class DebuggerDialog extends DialogView {

    /**
     * Constructs a new instance of this class type
     */
    public DebuggerDialog() {
        super(MainApplication.instance(), Localization.instance().getLocalizedString(LocalizedStrings.DebugWindow), 300, 300);

        
        
        setResizable(false);
        setAlwaysOnTop(true);

        // Specify the controller of this dialog
        //getViewProperties().setEntity(AbstractSignalFactory.getFactory(ControllerFactory.class).get(new DebuggerController(this), t));

        // Set the layout manager of this dialog to be a grid-like layout
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    }

    @Override public void initializeComponents() {

    }

    @Override public void initializeComponentBindings() {

    }

    @Override public void render() {
        
        pack();
        
        // Positions this dialog at the middle-right of the application
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation((int)(screenSize.getWidth() - getWidth()), (int)((screenSize.getHeight() - getHeight()) / 2));

        setVisible(true);
        
        // Request focus on the window
        requestFocus();
    }
}
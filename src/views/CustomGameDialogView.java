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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.PlainDocument;

import application.MainApplication;
import core.GameSettings;
import engine.core.mvc.view.DialogView;
import engine.core.mvc.view.layout.SpringLayoutHelper;
import engine.utils.filters.DocumentIntegerFilter;
import engine.utils.globalisation.Localization;

/**
 * This dialog view is associated with creating a new game with custom set entries (width, heigh, bombs, etc)
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class CustomGameDialogView extends DialogView {

    private JButton _okayButton = new JButton("OK");
    
    private JButton _cancelButton = new JButton("Cancel");
    
    private JTextField _heightTextField = new JTextField(3);
    
    private JTextField _widthTextField = new JTextField(3);
    
    private JTextField _minesTextField = new JTextField(3);
    
    /**
     * Constructs a new instance of this class type
     */
    public CustomGameDialogView() {
        super(MainApplication.instance(), "Custom Game");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
        // Set the modal state
        setModal(true);

        // Make sure it is always on top
        setAlwaysOnTop(true);

        // Do not allow it to be resized
        setResizable(false);
    }


    @Override public void initializeComponents() {
        
        JPanel customPanel = new JPanel(new SpringLayout());
        JPanel actionPanel = new JPanel();
        
        // HEIGHT
        customPanel.add(new JLabel("Height:"));
        _heightTextField.setMaximumSize(_heightTextField.getPreferredSize());
        ((PlainDocument)_heightTextField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        customPanel.add(_heightTextField);
        
        // WIDTH
        customPanel.add(new JLabel("Width:"));
        _widthTextField.setMaximumSize(_widthTextField.getPreferredSize());
        ((PlainDocument)_widthTextField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        customPanel.add(_widthTextField);
        
        // MINES
        customPanel.add(new JLabel("Mines:"));
        _minesTextField.setMaximumSize(_minesTextField.getPreferredSize());
        ((PlainDocument)_minesTextField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        customPanel.add(_minesTextField);
        
        // OK
        actionPanel.add(_okayButton);
        
        // CANCEL
        actionPanel.add(_cancelButton);
        
        // Make the grid compact 
        SpringLayoutHelper.makeCompactGrid(customPanel, 3, 2, 6, 6, 10, 0);
        
        // Add the panel to this dialog
        add(customPanel);
        add(actionPanel);
    }
    
    @Override public void initializeComponentBindings() {
        _okayButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent mouseEvent) {
                if(validateDialog()) {
                    setDialogResult(JOptionPane.OK_OPTION);
                    setVisible(false);
                }
                else {
                    JOptionPane.showMessageDialog(
                        CustomGameDialogView.this,
                        Localization.instance().getLocalizedString("Invalid configuration"),
                        Localization.instance().getLocalizedString("Error"),
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        _cancelButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent mouseEvent) {
                setDialogResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });
    }

    @Override public void render() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override protected boolean validateDialog() {
        try {
            int height = getRowsCount();
            int width = getColumnsCount();
            int mines = getMinesCount();
            
            return height < 99 && width < 99 && mines < width * height; 
        }
        catch (Exception exception){
            // NOTE: This is not logged, the range of errors that the user can type in 
            // are not worth checking
            return false;
        }
    }

    public int getRowsCount() {
        return Math.max(GameSettings.BEGINNER.ROWS, Integer.parseInt(_heightTextField.getText()));
    }
    
    public int getColumnsCount() {
        return Math.max(GameSettings.BEGINNER.COLUMNS, Integer.parseInt(_widthTextField.getText()));
    }
    
    public int getMinesCount() {
        return Math.max(GameSettings.BEGINNER.MINES, Integer.parseInt(_minesTextField.getText()));
    }
}
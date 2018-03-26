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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import controllers.BoardController;
import core.GameSettings;
import engine.core.mvc.view.DialogView;
import engine.core.mvc.view.layout.SpringLayoutHelper;
import engine.utils.filters.DocumentIntegerFilter;
import engine.utils.globalisation.Localization;
import resources.LocalizedStrings;

/**
 * This dialog view is associated with creating a new game with custom set entries (width, heigh, bombs, etc)
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class CustomGameDialogView extends DialogView {

    /**
     * The okay button
     */
    private JButton _okayButton = new JButton(Localization.instance().getLocalizedString(LocalizedStrings.OK));
    
    /**
     * The cancel button
     */
    private JButton _cancelButton = new JButton(Localization.instance().getLocalizedString(LocalizedStrings.Cancel));
    
    /**
     * The height text field
     */
    private JTextField _heightTextField = new JTextField(4);
    
    /**
     * The width text field
     */
    private JTextField _widthTextField = new JTextField(4);
    
    /**
     * The mines text field
     */
    private JTextField _minesTextField = new JTextField(4);
    
    /**
     * Constructs a new instance of this class type
     */
    public CustomGameDialogView() {
        super(MainApplication.instance(), Localization.instance().getLocalizedString(LocalizedStrings.CustomMode));

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
        // Set the modal state
        setModal(true);

        // Make sure it is always on top
        setAlwaysOnTop(true);

        // Do not allow it to be resized
        setResizable(false);
    }

    /**
     * Gets the rows count of this dialog
     * 
     * @return The rows count of this dialog
     */
    public int getRowsCount() {
        return Math.max(GameSettings.BEGINNER.ROWS, Integer.parseInt(_heightTextField.getText()));
    }
    
    /**
     * Gets the columns count of this dialog
     * 
     * @return The columns count of this dialog
     */
    public int getColumnsCount() {
        return Math.max(GameSettings.BEGINNER.COLUMNS, Integer.parseInt(_widthTextField.getText()));
    }
    
    /**
     * Gets the mines count of this dialog
     * 
     * @return The mines count of this dialog
     */
    public int getMinesCount() {
        return Math.max(1, Integer.parseInt(_minesTextField.getText()));
    }

    @Override public void initializeComponents() {
        
        JPanel customPanel = new JPanel(new SpringLayout());
        JPanel actionPanel = new JPanel();
        
        // HEIGHT
        customPanel.add(new JLabel("Height:"));
        _heightTextField.setMaximumSize(_heightTextField.getPreferredSize());
        ((PlainDocument)_heightTextField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        customPanel.add(_heightTextField);
        _heightTextField.setText(BoardController.GAME_SETTINGS.ROWS + "");
        
        // WIDTH
        customPanel.add(new JLabel("Width:"));
        _widthTextField.setMaximumSize(_widthTextField.getPreferredSize());
        ((PlainDocument)_widthTextField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        customPanel.add(_widthTextField);
        _widthTextField.setText(BoardController.GAME_SETTINGS.COLUMNS + "");
        
        // MINES
        customPanel.add(new JLabel("Mines:"));
        _minesTextField.setMaximumSize(_minesTextField.getPreferredSize());
        ((PlainDocument)_minesTextField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        customPanel.add(_minesTextField);
        _minesTextField.setText(BoardController.GAME_SETTINGS.MINES + "");
        
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
        
        _heightTextField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent event) {
                _heightTextField.selectAll();
            }
        });
        _widthTextField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent event) {
                _widthTextField.selectAll();
            }
        });
        _minesTextField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent event) {
                _minesTextField.selectAll();
            }
        });
        _okayButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent mouseEvent) {
                setDialogResult(JOptionPane.OK_OPTION);
                setVisible(false);
            }
        });
        _okayButton.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                    setDialogResult(JOptionPane.OK_OPTION);
                    setVisible(false);
                }
            }
        });
        _cancelButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent mouseEvent) {
                setDialogResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });
        _cancelButton.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                    setDialogResult(JOptionPane.CANCEL_OPTION);
                    setVisible(false);
                }
            }
        });
    }

    @Override public void render() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override protected boolean validateDialog() {
        return true;
    }
}
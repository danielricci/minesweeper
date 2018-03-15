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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import application.MainApplication;
import controllers.HighScoreController;
import engine.communication.internal.signal.arguments.AbstractEventArgs;
import engine.communication.internal.signal.arguments.ModelEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.view.DialogView;
import engine.core.mvc.view.layout.SpringLayoutHelper;
import engine.utils.globalisation.Localization;
import engine.utils.logging.Tracelog;
import game.core.factories.ControllerFactory;
import models.HighScoreModel;
import resources.LocalizedStrings;

/**
 * This view represents the high scores earned throughout the games lifespan
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 */
public class HighScoresDialog extends DialogView {

    /**
     * The reset scores button
     */
    private JButton _resetScoresButton = new JButton("Reset Scores");
    
    /**
     * The okay button
     */
    private JButton _okButton = new JButton("OK");
    
    /**
     * The beginner label
     */
    private JLabel _beginnerLabel = new JLabel("Beginner");
    
    /**
     * The beginners seconds label
     */
    private JLabel _beginnerSecondsLabel = new JLabel();
    
    /**
     * The beginners name label
     */
    private JLabel _beginnerNameLabel = new JLabel();
    
    /**
     * The intermediate label
     */
    private JLabel _intermediateLabel = new JLabel("Intermediate");
    
    /**
     * The intermediate seconds label
     */
    private JLabel _intermediateSecondsLabel = new JLabel();
    
    /**
     * The intermediate name label
     */
    private JLabel _intermediateNameLabel = new JLabel();
    
    /**
     * The expert label
     */
    private JLabel _expertLabel = new JLabel("Expert");
    
    /**
     * The expert seconds label
     */
    private JLabel _expertSecondsLabel = new JLabel();
    
    /**
     * The expert name label
     */
    private JLabel _expertNameLabel = new JLabel();
    
    /**
     * Constructs a new instance of this class type
     */
    public HighScoresDialog() {
        super(MainApplication.instance(), Localization.instance().getLocalizedString(LocalizedStrings.HighScores));

        HighScoreController controller = AbstractFactory.getFactory(ControllerFactory.class).add(new HighScoreController(), true);
        controller.addListener(this);
        getViewProperties().setEntity(controller);
                
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        setResizable(false);
        setAlwaysOnTop(true);
        setModal(true);
    }

    /**
     * Updates the view with the specified high score
     * 
     * @param score The high score
     */
    private void updateHighScore(HighScoreModel score) {
        switch(score.SETTING) {
        case BEGINNER:
            _beginnerSecondsLabel.setText(score.SETTING.getTime() + " seconds");
            _beginnerNameLabel.setText(score.SETTING.getName());
            break;
        case INTERMEDITE:
            _intermediateSecondsLabel.setText(score.SETTING.getTime() + " seconds");
            _intermediateNameLabel.setText(score.SETTING.getName());
            break;
        case EXPERT:
            _expertSecondsLabel.setText(score.SETTING.getTime() + " seconds");
            _expertNameLabel.setText(score.SETTING.getName());
            break;
        case CUSTOM:
        default:
            Tracelog.log(Level.WARNING, true, "Invalid score attemting to be added");
            break;
        }
    }
    
    @Override public void initializeComponents() {

        JPanel innerPanel = new JPanel(new SpringLayout());
        JPanel actionPanel = new JPanel(new SpringLayout());
        
        innerPanel.add(_beginnerLabel);
        innerPanel.add(_beginnerSecondsLabel);
        innerPanel.add(_beginnerNameLabel);
        
        innerPanel.add(_intermediateLabel);
        innerPanel.add(_intermediateSecondsLabel);
        innerPanel.add(_intermediateNameLabel);
        
        innerPanel.add(_expertLabel);
        innerPanel.add(_expertSecondsLabel);
        innerPanel.add(_expertNameLabel);
        
        actionPanel.add(_resetScoresButton);
        actionPanel.add(_okButton);
        
        // Make the grid compact 
        SpringLayoutHelper.makeCompactGrid(innerPanel, 3, 3, 20, 20, 30, 5);
        SpringLayoutHelper.makeCompactGrid(actionPanel, 1, 2, 20, 20, 40, 5);
        
        add(innerPanel);
        add(actionPanel);
    }

    @Override public void initializeComponentBindings() {
        _okButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent actionEvent) {
                setDialogResult(JOptionPane.OK_OPTION);
                setVisible(false);
            }
        });
        _resetScoresButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent actionEvent) {
                AbstractFactory.getFactory(ControllerFactory.class).get(HighScoreController.class).reset();
            }
        });
    }

    @Override public void render() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    @Override public void update(AbstractEventArgs signalEvent) {
        super.update(signalEvent);
        
        if(signalEvent instanceof ModelEventArgs) {
            
            ModelEventArgs args = (ModelEventArgs) signalEvent;
            HighScoreModel model = (HighScoreModel) args.getSource();
            updateHighScore(model);
            repaint();
        }
    }
}
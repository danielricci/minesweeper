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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import controllers.GameStateController;
import engine.communication.internal.signal.arguments.AbstractEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.view.PanelView;
import game.core.factories.ControllerFactory;
import models.GameStateModel;

public class GameStateView extends PanelView {
    
    private JButton _gameStateButton = new JButton();
    
    /**
     * Constructs a new instance of this class type
     */
    public GameStateView() {
        // Create the controller and listen to the contents of its model
        GameStateController controller = AbstractFactory.getFactory(ControllerFactory.class).add(new GameStateController(), true);
        getViewProperties().setEntity(controller);
        controller.addListener(this);
        setLayout(new BorderLayout());
    }
    
    @Override public Dimension getPreferredSize() {
        return new Dimension(24, 24);
    }
    
    @Override public void initializeComponents() {
        // Do not paint the borders of the button, it should simply be an image of sorts
        _gameStateButton.setBorderPainted(false);
        
        // Hide everything about the button, except for the icon that will be draw later
        _gameStateButton.setContentAreaFilled(false);
        
        // Add the tile to the middle of the panel
        add(_gameStateButton, BorderLayout.CENTER);
    }

    @Override public void initializeComponentBindings() {
        _gameStateButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent mouseEvent) {
                getViewProperties().getEntity(GameStateController.class).resetGame();
            }
        });
    }
       
    @Override public void update(AbstractEventArgs event) {
        super.update(event);
    
        if(event.getSource() instanceof GameStateModel) {
            // Get the model associated to the event received
            GameStateModel gameStateModel = (GameStateModel) event.getSource();
            _gameStateButton.setIcon(new ImageIcon(gameStateModel.getEntity().getRenderableContent()));
            
            repaint();
        }
    }
}

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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import engine.core.factories.AbstractFactory;
import engine.core.mvc.view.PanelView;
import game.core.factories.ViewFactory;

/**
 * The status view shows the game information such as timers, counters, and game state facial gestures
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class StatusBarView extends PanelView {

    private JPanel _bombsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    private JPanel _gameStatePanel = new JPanel();
    
    private JPanel _gameTimerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    /**
     * Constructs a new instance of this class type
     */
    public StatusBarView() {
        GridLayout layout = new GridLayout(1, 3);
        setLayout(layout);
        setBorder(BorderFactory.createLoweredSoftBevelBorder());
    }
    
    @Override public void initializeComponents() {

        _gameStatePanel.setLayout(new BoxLayout(_gameStatePanel, BoxLayout.X_AXIS));
        
        _bombsPanel.add(AbstractFactory.getFactory(ViewFactory.class).add(new BombsCounterView(), false));
        _gameStatePanel.add(AbstractFactory.getFactory(ViewFactory.class).add(new GameStateView(), false));
        _gameTimerPanel.add(AbstractFactory.getFactory(ViewFactory.class).add(new GameTimerView(), false));
        
        add(_bombsPanel);
        add(_gameStatePanel);
        add(_gameTimerPanel);
    }

    @Override public void initializeComponentBindings() {
    }
    
    @Override public void render() {
        super.render();
        
        render(_bombsPanel);
        render(_gameStatePanel);
        render(_gameTimerPanel);
    }
}

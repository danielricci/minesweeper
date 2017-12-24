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

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import engine.core.factories.AbstractFactory;
import engine.core.mvc.view.PanelView;
import game.core.ViewFactory;

/**
 * The status view shows the game information such as timers, counters, and game state facial gestures
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class StatusBarView extends PanelView {

    private JPanel _flagsPanel = new JPanel();
    
    private JPanel _gameStatePanel = new JPanel();

    /**
     * Constructs a new instance of this class type
     */
    public StatusBarView() {
        setLayout(new GridLayout(1, 3));
    }

    @Override public void initializeComponents() {
        
        _flagsPanel.setBackground(Color.red);
        _gameStatePanel.setBackground(Color.green);
        add(_flagsPanel);
        add(_gameStatePanel);
        add(AbstractFactory.getFactory(ViewFactory.class).add(new GameTimerView(), false));
    }

    @Override public void initializeComponentBindings() {
        
    }
}

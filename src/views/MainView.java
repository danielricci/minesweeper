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
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import engine.api.IView;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.PanelView;
import game.core.factories.ViewFactory;

/**
 * The main window of the application
 *
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public class MainView extends PanelView {

    private static final Color BACKGROUND_COLOR = new Color(192, 192, 192);

    
    public MainView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(BACKGROUND_COLOR);
    }

    @Override public void initializeComponents() {
        add(AbstractSignalFactory.getFactory(ViewFactory.class).add(new StatusBarView(), true));
        add(AbstractSignalFactory.getFactory(ViewFactory.class).add(new DebuggerBoardView(), true));
    }

    @Override public void initializeComponentBindings() {
    }

    @Override public void render() {
        super.render();
        for(Component component : getComponents()) {
            if(component instanceof IView) {
                ((IView) component).render();
            }
        }
    }
}
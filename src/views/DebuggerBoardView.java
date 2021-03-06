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

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import controllers.BoardController;
import controllers.DebuggerController;
import engine.communication.internal.signal.ISignalListener;
import engine.core.factories.AbstractFactory;
import game.core.factories.ControllerFactory;

/**
 * This view represents the debuggable version of the application where tests can be performed
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class DebuggerBoardView extends BoardView {

    @Override public void initializeComponentBindings() {
        super.initializeComponentBindings();

        // Go through all the tile views that have been created and add a mouse
        // listener such that when clicked it will perform the specified action
        for(Component component : getComponents()) {
            if(component instanceof TileView) {
                component.addMouseListener(new MouseAdapter() {
                    @Override public void mouseReleased(MouseEvent event) {
                        DebuggerController debuggerController = AbstractFactory.getFactory(ControllerFactory.class).get(DebuggerController.class);
                        if(debuggerController != null) {
                            if(debuggerController.getIsMinesEnabled()) {
                                BoardController controller = DebuggerBoardView.this.getViewProperties().getEntity(BoardController.class);
                                controller.setMine((ISignalListener) event.getSource());
                            }
                        }
                    }
                });
            }
        }
    }
}
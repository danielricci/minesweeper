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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.Border;

import controllers.BoardController;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.PanelView;
import game.core.ControllerFactory;
import game.core.ModelFactory;
import model.TileModel;

/**
 * This view represents the visual contents of a single tile in this game
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public class TileView extends PanelView {

    /**
     * Normal border style of this view
     */
    private final Border BORDER_STYLE = BorderFactory.createMatteBorder(0, 1, 1, 0, new Color(146, 146, 146));
    
    /**
     * The background color of this tile
     */
    private final Color BACKGROUND_STYLE = new Color(204, 204, 204);

    /**
     * Constructs a new instance of this class type
     */
    public TileView() {
        // Set the controller to the be the common board controller 
        BoardController controller = AbstractSignalFactory.getFactory(ControllerFactory.class).get(BoardController.class);
        getViewProperties().setEntity(controller);
        
        // Create a new tile model and listen in on it
        controller.addTileModel(AbstractFactory.getFactory(ModelFactory.class).add(new TileModel(this), false));
        
        // Set the rest of the layout data for this page
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BORDER_STYLE);
        setBackground(BACKGROUND_STYLE);
    }
    
    @Override public void initializeComponents() {
    }

    @Override public void initializeComponentBindings() {
    }

    @Override public void registerSignalListeners() {
    }
}
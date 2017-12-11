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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.Border;

import controllers.BoardController;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.arguments.AbstractEventArgs;
import engine.communication.internal.signal.arguments.BooleanEventArgs;
import engine.communication.internal.signal.arguments.ModelEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.PanelView;
import game.core.ControllerFactory;
import game.core.ModelFactory;
import models.TileModel;

/**
 * This view represents the visual contents of a single tile in this game
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public class TileView extends PanelView {

    /**
     * The event associated to displaying the neighbours of this tile
     */
    public static final String EVENT_NEIGHBORS = "EVENT_NEIGHBORS";
    
    /**
     * Event associated to show neighbors
     */
    public static final String EVENT_SHOW_NEIGHBORS = "EVENT_SHOW_NEIGHBORS";
    
    /**
     * Event associated to hiding neighbors
     */
    public static final String EVENT_HIDE_NEIGHBORS = "EVENT_HIDE_NEIGHBORS";

    /**
     * This flag when set to true with perform a neighbor highlight on this tile when you mouse over it
     */
    private boolean _highlightNeighbors;

    /**
     * Normal border style of this view
     */
    private final Border DEFAULT_BORDER = BorderFactory.createMatteBorder(0, 1, 1, 0, new Color(146, 146, 146));

    /**
     * The background color of this tile
     */
    private final Color DEFAULT_BACKGROUND_COLOR = new Color(204, 204, 204);
    
    /**
     * The color used when a tile is highlighted
     */
    private static final Color HIGHLIGHTED_COLOR = Color.BLUE;

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
        setBorder(DEFAULT_BORDER);
        setBackground(DEFAULT_BACKGROUND_COLOR);
    }

    @Override public void initializeComponents() {
    }

    @Override public void initializeComponentBindings() {
        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent event) {
                if(_highlightNeighbors) {
                    getViewProperties().getEntity(BoardController.class).showTileNeighborsDebug(TileView.this, true);
                }
            }

            @Override public void mouseExited(MouseEvent event) {               
                if(_highlightNeighbors) {
                    getViewProperties().getEntity(BoardController.class).showTileNeighborsDebug(TileView.this, false);
                }
            }
        });     
    }

    @Override public void registerSignalListeners() {
        addSignalListener(EVENT_NEIGHBORS, new ISignalReceiver<BooleanEventArgs>() {
            @Override public void signalReceived(BooleanEventArgs event) {
                _highlightNeighbors = event.getResult();
                
                // If the highlight is being removed, force the model to remove its highlight. This will prevent a bug
                // where if you are highlighting tiles and you navigate with your keyboard to remove highlight, then
                // what was currently highlighted would not be properly removed
                if(!_highlightNeighbors) {
                    getViewProperties().getEntity(BoardController.class).showTileNeighborsDebug(TileView.this, false);
                }
            }
        });
    }
    
    @Override public void update(AbstractEventArgs event) {
        super.update(event);
        
        if(event instanceof ModelEventArgs)
        {
            ModelEventArgs args = (ModelEventArgs) event;
            TileModel tileModel = (TileModel) args.getSource();
            if(tileModel.getIsHighlighted()) {     
                this.setBackground(HIGHLIGHTED_COLOR);
            }
            else {
                this.setBackground(DEFAULT_BACKGROUND_COLOR);               
            }
            
            addRenderableContent(tileModel.getEntity());
        }
        
        repaint();
    }
}
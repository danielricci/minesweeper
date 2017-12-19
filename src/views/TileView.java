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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
     * The event associated to displaying the empty tiles with respect to this tile
     */
    public static final String EVENT_EMPTY_TILES = "EVENT_EMPTY_TILES";
    
    /**
     * The button associated to this tile view that the user will click to expose the contents of this tile
     */
    private final JButton _tileButton = new JButton();
    
    /**
     * This flag when set will perform a neighbor highlight on this tile when you mouse over it
     */
    private boolean _highlightNeighbors;
    
    /**
     * This flag when set will highlight all empty adjacent tiles to this tile continuously 
     */
    private boolean _highlightEmptySpaces;
    
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
        setLayout(new BorderLayout());
    }

    @Override public void initializeComponents() {
        add(_tileButton, BorderLayout.CENTER);
    }

    @Override public void initializeComponentBindings() {
        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent event) {
                if(_highlightNeighbors) {
                    getViewProperties().getEntity(BoardController.class).showTileNeighborsDebug(TileView.this, true);
                }
                else if(_highlightEmptySpaces) {
                    getViewProperties().getEntity(BoardController.class).showEmptyTileNeighborsDebug(TileView.this, true);
                }
            }

            @Override public void mouseExited(MouseEvent event) {               
                if(_highlightNeighbors) {
                    getViewProperties().getEntity(BoardController.class).showTileNeighborsDebug(TileView.this, false);
                }
                else if(_highlightEmptySpaces) {
                    getViewProperties().getEntity(BoardController.class).showEmptyTileNeighborsDebug(TileView.this, false);
                }
            }
        });
        
        _tileButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent e) {
                _tileButton.setVisible(false);
                setBorder(DEFAULT_BORDER);
                setBackground(DEFAULT_BACKGROUND_COLOR);
            }
        });
    }

    @Override public void registerSignalListeners() {
        addSignalListener(EVENT_NEIGHBORS, new ISignalReceiver<BooleanEventArgs>() {
            @Override public void signalReceived(BooleanEventArgs event) {
                _highlightNeighbors = event.getResult();
                
                if(!_highlightNeighbors) {
                    getViewProperties().getEntity(BoardController.class).showTileNeighborsDebug(TileView.this, false);
                }                
            }
        });        
        addSignalListener(EVENT_EMPTY_TILES, new ISignalReceiver<BooleanEventArgs>() {
            @Override public void signalReceived(BooleanEventArgs event) {
                _highlightEmptySpaces = event.getResult();
                
                if(!_highlightEmptySpaces) {
                    getViewProperties().getEntity(BoardController.class).showEmptyTileNeighborsDebug(TileView.this, false);
                }
            }
        });

    }
    
    @Override public void update(AbstractEventArgs event) {
        super.update(event);
        
        if(event instanceof ModelEventArgs) {
            
            // Get the tile model
            ModelEventArgs args = (ModelEventArgs) event;
            TileModel tileModel = (TileModel) args.getSource();

            // Set the highlighted state
            if(tileModel.getIsHighlighted()) {     
                this.setBackground(HIGHLIGHTED_COLOR);
            }
            else {
                this.setBackground(DEFAULT_BACKGROUND_COLOR);
                
                // Add the renderable entity to be shown. This should not be shown if
                // the tile is being highlighted
                addRenderableContent(tileModel.getEntity());
            }
            
            System.out.println("Background is " + getBackground().toString());
            repaint();
        }        
    }
}
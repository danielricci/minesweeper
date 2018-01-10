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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import controllers.BoardController;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.arguments.AbstractEventArgs;
import engine.communication.internal.signal.arguments.BooleanEventArgs;
import engine.communication.internal.signal.arguments.ModelEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.PanelView;
import game.core.factories.ControllerFactory;
import game.core.factories.ModelFactory;
import models.TileModel;

/**
 * This view represents the visual contents of a single tile in this game
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public class TileView extends PanelView {

    /**
     * The event associated to displaying the empty tiles with respect to this tile
     */
    public static final String EVENT_DEBUG_FLOODFILL = "EVENT_DEBUG_FLOODFILL";

    /**
     * The event associated to displaying the button for this tile
     */
    public static final String EVENT_SHOW_TILE_BUTTONS = "EVENT_SHOW_TILE_BUTTONS";
    
    /**
     * The event associated to displaying the neighbours of this tile
     */
    public static final String EVENT_SHOW_NEIGHBORS = "EVENT_SHOW_NEIGHBORS";
    
    /**
     * The background color of this tile
     */
    private final Color DEFAULT_BACKGROUND_COLOR = new Color(204, 204, 204);

    /**
     * Normal border style of this view
     */
    private final Border DEFAULT_BORDER = BorderFactory.createMatteBorder(1, 1, 0, 0, new Color(146, 146, 146));

    /**
     * The color used when a tile is highlighted
     */
    private final Color HIGHLIGHTED_COLOR = Color.BLUE;

    /**
     * This flag when set will highlight all empty adjacent tiles to this tile continuously 
     */
    private boolean _highlightEmptySpaces;

    /**
     * This flag when set will perform a neighbor highlight on this tile when you mouse over it
     */
    private boolean _highlightNeighbors;

    /**
     * The button associated to this tile view that the user will click to expose the contents of this tile
     */
    private final JButton _tileButton = new JButton();
    
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
        
        // Do not paint the borders of the button, it should simply be an image of sorts
        _tileButton.setBorderPainted(false);
        
        // Add the tile to the middle of the panel
        add(_tileButton, BorderLayout.CENTER);
    }

    @Override public void initializeComponentBindings() {
        addMouseListener(new MouseAdapter() {
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
        
        // Bind the mouse click events to the button component associated to this view
        _tileButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent mouseEvent) {

                // Only perform operations on the button itself if the button is actually visible
                if(_tileButton.isVisible()) {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    
                        // Set the border and the background color for this tile
                        setBorder(DEFAULT_BORDER);
                        setBackground(DEFAULT_BACKGROUND_COLOR);
                    
                        // Remove the visibility of the button
                        _tileButton.setVisible(false);
                    
                        // Call the controller to notify that the button has been hidden
                        getViewProperties().getEntity(BoardController.class).buttonSelectedEvent(TileView.this);
                    }
                    else if(SwingUtilities.isRightMouseButton(mouseEvent)) {
                        _tileButton.setVisible(false);
                    
                        // Call the controller to notify that the button has been hidden
                        getViewProperties().getEntity(BoardController.class).buttonSelectedEvent(TileView.this);
                    }
                }
            }
        });
    }

    @Override public void registerSignalListeners() {
        addSignalListener(EVENT_SHOW_NEIGHBORS, new ISignalReceiver<BooleanEventArgs>() {
            @Override public void signalReceived(BooleanEventArgs event) {
                _highlightNeighbors = event.getResult();
                
                if(!_highlightNeighbors) {
                    getViewProperties().getEntity(BoardController.class).showTileNeighborsDebug(TileView.this, false);
                }                
            }
        });        
        addSignalListener(EVENT_DEBUG_FLOODFILL, new ISignalReceiver<BooleanEventArgs>() {
            @Override public void signalReceived(BooleanEventArgs event) {
                _highlightEmptySpaces = event.getResult();
                
                if(!_highlightEmptySpaces) {
                    getViewProperties().getEntity(BoardController.class).showEmptyTileNeighborsDebug(TileView.this, false);
                }
            }
        });
        addSignalListener(EVENT_SHOW_TILE_BUTTONS, new ISignalReceiver<BooleanEventArgs>() {
            @Override public void signalReceived(BooleanEventArgs event) {
                
                boolean visible = !event.getResult(); 
                _tileButton.setVisible(visible);

                setBorder(DEFAULT_BORDER);
                setBackground(DEFAULT_BACKGROUND_COLOR);
                
                // Call the controller to notify that the button has been hidden
                getViewProperties().getEntity(BoardController.class).buttonSelectedEvent(TileView.this);
            }
        });        
    }
    
    @Override public void update(AbstractEventArgs event) {
        super.update(event);
        
        if(event instanceof ModelEventArgs) {
            // Get the tile model associated to the event passed in
            TileModel tileModel = (TileModel) event.getSource();
                        
            if(tileModel.getIsHighlighted()) {
                this.setBackground(HIGHLIGHTED_COLOR);
            }
            else {
                
                // Set the background to its default color state
                this.setBackground(DEFAULT_BACKGROUND_COLOR);

                if(_tileButton.isVisible()) {
                    _tileButton.setIcon(new ImageIcon(tileModel.getButtonStateEntity().getRenderableContent()));    
                }
                else {
                    addRenderableContent(tileModel.getTileStateEntity());    
                }
            }

            // Finalize the draw update procedure
            repaint();
        }
    }
}
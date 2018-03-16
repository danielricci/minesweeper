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

package models;

import java.util.ArrayList;

import engine.communication.internal.signal.ISignalListener;
import engine.core.graphics.IRenderable;
import engine.core.mvc.model.BaseModel;
import entities.ButtonStateEntity;
import entities.TileStateEntity;

/**
 * The model representation of a tile 
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class TileModel extends BaseModel {

    /**
     * Signal indicating that this model's highlight state has changed
     */
    public static final String EVENT_HIGHLIGHT_CHANGED = "EVENT_HIGHLIGHT_CHANGED";
    
    /**
     * Property indicating this tile is highlighted
     */
    private boolean _isHighlighted;
    
    /**
     * The button state entity of this tile model
     */
    private final ButtonStateEntity _buttonStateEntity = new ButtonStateEntity();
    
    /**
     * The tile state entity of this tile model
     */
    private final TileStateEntity _tileStateEntity = new TileStateEntity();

    /** 
     * Constructs a new instance of this class type
     *
     * @param listeners The list of receivers
     */
    public TileModel(ISignalListener... listeners) {
        super(listeners);
        doneUpdating();
    }
        
    /**
     * Gets button state entity associated to this tile model
     * 
     * @return The button state entity associated to this tile model
     */
    public ButtonStateEntity getButtonStateEntity() {
        return _buttonStateEntity;
    }

    /**
     * Gets the tile state entity associated to this tile model
     * 
     * @return The tile state entity associated to this tile model
     */
    public TileStateEntity getTileStateEntity() {
        return _tileStateEntity;
    }
    
    public ArrayList<IRenderable> getRenderableContent() {
        return new ArrayList<IRenderable>() {{
            add(_buttonStateEntity);
            add(_tileStateEntity);
        }};
    }
    
    /**
     * Sets the highlight state of this model
     * 
     * @param highlighted If the tile model should be highlighted
     */
    public void setHighlighted(boolean highlighted) {
        _isHighlighted = highlighted;
        setOperation(EVENT_HIGHLIGHT_CHANGED);
        doneUpdating();
    }

    /**
     * Gets if the tile model is in a highlighted state
     * 
     * @return If the tile model is highlighted
     */
    public boolean getIsHighlighted() {
        return _isHighlighted;
    }
}
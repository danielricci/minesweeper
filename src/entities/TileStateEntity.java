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

package entities;

import java.awt.Image;

import generated.DataLookup.TILE_STATE;

/**
 * Represents a tile state entity that holds all the contents of a singular data entity in the game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class TileStateEntity extends AbstractGameEntity {
        
    /**
     * Hold a reference to any potential numeral entities within this tile
     */
    private final MineNumeralEntity _tileNumeralEntity = new MineNumeralEntity();

    /**
     * The currently active state of this entity
     */
    private TILE_STATE _tileState = null;
    
    /**
     * Gets the mine numeral entity associated to this entity
     * 
     * @return The mine numeral entity associated to this entity
     */
    public MineNumeralEntity getMineNumeralEntity() {
        return _tileNumeralEntity;
    }

    /**
     * Gets if this entity is in a 'bomb revealed' state
     * 
     * @return TRUE if this entity is in a 'bomb revealed' state, FALSE otherwise
     */
    public boolean hasMine() {
        return _tileState == TILE_STATE.BOMB_REVEALED;
    }

    /**
     * Sets the current state of this entity
     * 
     * @param tileState The state to set this entity to
     */
    public void setTileState(TILE_STATE tileState) {
        
        // Call the super functionality
        super.setActiveData(tileState);
        
        // Store a reference to the current state for further use
        _tileState = tileState;
    }

    @Override public Image getRenderableContent() {
      
        if(hasActiveData()) {
            return super.getRenderableContent();
        }
        if(_tileNumeralEntity.hasActiveData()) {
            return _tileNumeralEntity.getRenderableContent();
        }
        
        return null;
    }

    /**
     * Gets if this entity is empty
     * 
     * @return TRUE if this entity and all of its associated entities are empty, FALSE otherwise
     */
    public boolean isEmpty() {
        return !(hasActiveData() || _tileNumeralEntity.hasActiveData());
    }
    
    @Override public void reset() {
        _tileNumeralEntity.reset();
        setTileState(null);
    }
}
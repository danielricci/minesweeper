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

public final class TileStateEntity extends AbstractGameEntity {
    
    private final MineNumeralEntity _tileNumeralEntity = new MineNumeralEntity();
    
    private ButtonStateEntity _buttonStateEntity = new ButtonStateEntity();
    
    private TILE_STATE _tileState = null;
    
    @Override public Image getRenderableContent() {
        if(hasActiveData()) {
            return super.getRenderableContent();
        }
        
        if(_buttonStateEntity != null && _buttonStateEntity.hasActiveData()) {
            return _buttonStateEntity.getRenderableContent();
        }
        
        
        return _buttonStateEntity.getRenderableContent();
    }

    @Override public void reset() {
        super.reset();
      
        _buttonStateEntity.reset();
        _tileNumeralEntity.reset();
    }
    
    public void setTileState(TILE_STATE tileState) {
        super.setActiveData(tileState);
        _tileState = tileState;
    }

    public MineNumeralEntity getMineNumeralEntity() {
        return _tileNumeralEntity;
    }
    
    public void clearButtonState() {
        _buttonStateEntity = null;
    }
    
    public ButtonStateEntity getButtonStateEntity() {
        return _buttonStateEntity;
    }
    
    public boolean hasMine() {
        return _tileState == TILE_STATE.BOMB_REVEALED;
    }
}
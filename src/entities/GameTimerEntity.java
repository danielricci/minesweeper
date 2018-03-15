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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;

import application.MainApplication;
import engine.core.graphics.RawData;
import engine.utils.logging.Tracelog;
import generated.DataLookup;

/**
 * The game timer entity
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class GameTimerEntity extends AbstractGameEntity {
    
    /**
     * The bomb timer constant data value
     */
    private static final String BOMB_CONSTANT = "TIMER_";
 
    /**
     * The numeral representation of the data of this entity
     */
    private int _numeral;
    
    /**
     * Constructs a new instance of this class type
     */
    public GameTimerEntity() {
        setNumeralImpl();
    }

    /**
     * Constructs a new instance of this class type
     *
     * @param numeral The explicit numeral to set
     */
    public GameTimerEntity(int numeral) {
        _numeral = numeral;
        setNumeralImpl();
    }   

    /**
     * Gets the numeral value of this entity
     * 
     * @return The numeral value of this entity
     */
    public int getNumeral() {
        return _numeral;
    }
    
    /**
     * Sets the numeral to an empty value, effectively clearning it
     */
    public void setNumeralEmpty() {
        _numeral = Integer.MIN_VALUE;
        setNumeralImpl();
    }
    
    /**
     * Sets the numeral to an explicit value
     * 
     * @param numeral The numeral to set
     */
    public void setNumeral(int numeral) {
        
        if(numeral == _numeral) {
            return;
        }
    
        _numeral = numeral;
        
        if(numeral > 9) {
            numeral = 0;
            Tracelog.log(Level.WARNING, true, "Game timer entity exceeded expected range, defaulting to an acceptable value");
        }
            
        setNumeralImpl();            
    }
    
    /**
     * Flattens the specified entities into a singular piece of data
     * 
     * @param entities The entities to flatten
     * 
     * @return The raw data representation of the entities specified
     */
    public static RawData flattenData(List<GameTimerEntity> entities) {
        
        int width = entities.parallelStream().map(z -> z.getRenderableContent()).mapToInt(z -> z.getWidth(null)).sum();
        int height = entities.get(0).getRenderableContent().getHeight(null);
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = img.getGraphics();
        
        int xOffset = 0;
        for(GameTimerEntity entity : entities) {
            graphics.drawImage(entity.getRenderableContent(), xOffset, 0, null);
            xOffset = xOffset + entity.getRenderableContent().getWidth(null);
        }
        
        return new RawData(img);
    }

    /**
     * The set numeral implementation
     */
    private void setNumeralImpl() {
        if(MainApplication.instance().isDebug()) {
            setActiveData(_numeral < 0 ? DataLookup.NUMERAL_DEBUG.TIMER_EMPTY : DataLookup.NUMERAL_DEBUG.valueOf(BOMB_CONSTANT + _numeral));
        }
        else {
            setActiveData(_numeral < 0 ? DataLookup.NUMERAL.TIMER_EMPTY : DataLookup.NUMERAL.valueOf(BOMB_CONSTANT + _numeral));
        }
    }
}
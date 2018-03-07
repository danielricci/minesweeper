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

import generated.DataLookup.MINE_NUMBER;

/**
 * This class represents the data associated to a numeral entity
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class MineNumeralEntity extends AbstractGameEntity {
    
    /**
     * The numeral of this entity
     */
    private int _numeral;
    
    /**
     * The constant field associated to the mine numeral
     */
    private static final String BOMB = "BOMB_";
    
    /**
     * Sets the numeral of the mine
     * 
     * @param numeral The numeral of the mine
     */
    public void setNumeral(int numeral) {
        _numeral = numeral;
        if(numeral <= 0) {
            setActiveData(null);
        }
        else {
            setActiveData(MINE_NUMBER.valueOf(BOMB + numeral));    
        }
    }
    
    public int getNumeral() {
        return _numeral;
    }
    
    @Override public void reset() {
        setNumeral(0);
    }
}
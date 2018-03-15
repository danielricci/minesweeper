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
import java.util.List;

import engine.core.graphics.IRenderable;
import engine.core.mvc.model.BaseModel;
import entities.GameTimerEntity;

/**
 * The model associated to the bombs counter
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class BombsCounterModel extends BaseModel {

    /**
     * The maximum value associated to this gamer model.
     */
    public final static int MAX_VALUE = 999; 
    
    /**
     * The initial value associated to the bombs counter
     */
    private int _initialTimerValue;
    
    /**
     * The game timer current value associated to this model
     */
    private int _timerValue;
    
    /**
     * The entity list representation of the numerical timer
     */
    public List<GameTimerEntity> _timerEntities = new ArrayList();
    
    /**
     * Constructs a new instance of this class type
     */
    public BombsCounterModel() {
        // By default, the precision is set to 3
        for(int i = 0; i < 3; ++i) {
            _timerEntities.add(new GameTimerEntity());
        }            
    }

    /**
     * Sets the initial timer value for this model
     * 
     * @param initialTimerValue The initial timer value
     */
    public void setInitialTimer(int initialTimerValue) {
        _initialTimerValue = initialTimerValue;
        setTimer(initialTimerValue);
    }
    
    /**
     * Sets the timer to the specified value
     * 
     * @param timerValue The value to set the timer to
     */
    public void setTimer(int timerValue) {
        String numeralString = String.format("%03d", timerValue);
        
        for(int i = (timerValue < 0 ? 1 : 0), size = numeralString.length(); i < size; ++i) {
            _timerEntities.get(i).setNumeral(
                Character.getNumericValue(numeralString.charAt(i))
            );
        }

        if(timerValue < 0) {
            _timerEntities.get(0).setNumeralEmpty();
        }
        
        _timerValue = timerValue;
            
        doneUpdating();
    }
    
    /**
     * Resets the timer of this model
     */
    public void resetTimer() {
        setTimer(_initialTimerValue);
    }
    
    /**
     * Gets the timer of this model
     * 
     * @return The timer of this model
     */
    public int getTimerValue() {
        return _timerValue;
    }
    
    /**
     * Gets the renderable entity of this model
     * 
     * @return The renderable entity of this model
     */
    public IRenderable getEntity() {
        return GameTimerEntity.flattenData(_timerEntities);
    }
}
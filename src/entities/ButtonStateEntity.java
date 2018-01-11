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

import generated.DataLookup.BUTTON_STATE;

/**
 * This class is associated to the button state of a singular entity
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class ButtonStateEntity extends AbstractGameEntity {
    
    /**
     * The current button state of this entity
     */
    private BUTTON_STATE _currentButtonState = BUTTON_STATE.BUTTON_BLANK;

    /**
     * A flag indicating if this entity's state is enabled
     * 
     * Note: By default the state should be set to enabled
     */
    private boolean _isEnabled = true;
    
    /**
     * Constructs a new instance of this class type
     */
    public ButtonStateEntity() {
        setActiveData(_currentButtonState);
    }
    
    /**
     * Gets if this entity is considered 'empty' by comparing it against a blank button state
     * 
     * @return True if this entity is empty, false otherwise
     */
    public boolean isEmpty() {
        return _currentButtonState == BUTTON_STATE.BUTTON_BLANK;
    }
    
    /**
     * Gets if this entity is enabled
     * 
     * @return True if this entity is enabled, false otherwise
     */
    public boolean isEnabled() {
        return _isEnabled;
    }
    
    /**
     * Sets the enabled state of this entity
     * 
     * @param isEnabled The enabled state to set this entity to
     */
    public void setIsEnabled(boolean isEnabled) {
        _isEnabled = isEnabled;
    }
    
    /**
     * Changes the state of this button to the next available state
     */
    public void changeState() {
        switch(_currentButtonState) {
        case BUTTON_BLANK:
            _currentButtonState = BUTTON_STATE.BUTTON_FLAG;
            break;
        case BUTTON_FLAG:
            _currentButtonState = BUTTON_STATE.BUTTON_BLANK;
            break;
        default:
            return;
        }
        
        setActiveData(_currentButtonState);
    }
}
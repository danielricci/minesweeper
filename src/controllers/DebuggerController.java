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

package controllers;

import engine.core.mvc.controller.BaseController;

/**
 * This controller is responsible for the functionality related to debugging operations within the application for testing purposes
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class DebuggerController extends BaseController {

    /**
     * The flag associated to the state of mines being inserted
     */
    private boolean _isMinesEnabled;
    
    /**
     * Sets the state of the mines insertion
     * 
     * @param isMinesEnabled If the mine insertion is enabled
     */
    public void setMinesEnabled(boolean isMinesEnabled) {
        _isMinesEnabled = isMinesEnabled;
    }
    
    /**
     * Gets the state of the mines insertion
     * 
     * @return TRUE if mine insertion is enabled, FALSE otherwise
     */
    public boolean getIsMinesEnabled() {
        return _isMinesEnabled;
    }

}
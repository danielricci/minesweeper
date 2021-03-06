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

import engine.communication.internal.signal.ISignalListener;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.controller.BaseController;
import game.core.factories.ModelFactory;
import models.BombsCounterModel;

/**
 * The controller associated to the bombs counter
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class BombsCounterController extends BaseController {

    /**
     * The bombs counter model
     */
    private final BombsCounterModel _bombsCounterModel;
 
    /**
     * Constructs a new instance of this class type
     */
    public BombsCounterController() {
        // Create the model that will represent the timer data
        _bombsCounterModel = AbstractFactory.getFactory(ModelFactory.class).add(new BombsCounterModel(),  false);
    }
    
    /**
     * Adds a listener to the underlying model of this controller
     * 
     * @param listener The listener from where the event took place
     */
    public void addListener(ISignalListener listener) {
        _bombsCounterModel.addListener(listener);
    }

    /**
     * Increments the bomb counter
     */
    public void incrementCounter() {
        _bombsCounterModel.setTimer(_bombsCounterModel.getTimerValue() + 1);
    }
    
    /**
     * Decrements the bomb counter
     */
    public void decrementCounter() {
        _bombsCounterModel.setTimer(_bombsCounterModel.getTimerValue() - 1);
    }
    
    /**
     * Resets the bomb counter
     */
    public void resetCounter() {
        _bombsCounterModel.resetTimer();
    }
    
    /**
     * Sets explicitly the bomb counter
     * 
     * @param count The count
     */
    public void setCounter(int count) {
        _bombsCounterModel.setInitialTimer(count);
    }
}
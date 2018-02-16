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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import engine.communication.internal.signal.ISignalListener;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.logging.Tracelog;
import game.core.factories.ModelFactory;
import models.GameTimerModel;

/**
 * 
 * This controller is in charge of providing timer related functionality for the game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class GameTimerController extends BaseController {

    /**
     * The timer model that represents the current game time
     */
    private final GameTimerModel _gameTimerModel;
 
    /**
     * The timer associated to updating values it the timer model
     */
    private Timer _timer;
    
    /**
     * Constructs a new instance of this class type
     */
    public GameTimerController() {
        
        // Create the model that will represent the timer data
        _gameTimerModel = AbstractFactory.getFactory(ModelFactory.class).add(new GameTimerModel(),  false);
    }
    
    
    /**
     * Adds a listener to the underlying model of this controller
     * 
     * @param listener The listener to associated to the underlying model of this controller
     */
    public void addListener(ISignalListener listener) {
        _gameTimerModel.addListener(listener);
    }
    
    /**
     * Resets the game timer
     */
    public void resetGameTimer() {
        stopGameTimer();
        _gameTimerModel.resetTimer();
    }
    
    /**
     * Starts the game timer
     */
    public void startGameTimer() {
        
        // Create a timer task for running the timer. This task will
        // increment the value in the model by one every second
        TimerTask timerTask = new TimerTask() {
            @Override public void run() {
                // Calculate the next second value and if it
                // is within the allocated threshold then apply
                // the change
                int tempValue = _gameTimerModel.getTimerValue() + 1;
                if(tempValue <= GameTimerModel.MAX_VALUE) {
                    _gameTimerModel.setTimer(tempValue);       
                }
                else {
                    Tracelog.log(Level.WARNING, true, "Maximum time exceeded, stopping timer");
                    cancel();
                }
            }
        };
        
        _timer = new Timer(true);
        _timer.schedule(timerTask,  0, 1000);
    }
    
    /**
     * Stops the game timer
     */
    public void stopGameTimer() {
        if(_timer != null) {
            _timer.cancel();
        }
    }
    
    @Override public boolean clear() {
        boolean result = super.clear();
        stopGameTimer();
        return result;
    }
}
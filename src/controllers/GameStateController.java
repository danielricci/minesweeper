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
import game.core.factories.ControllerFactory;
import game.core.factories.ModelFactory;
import generated.DataLookup.GAME_STATE;
import models.GameStateModel;

public final class GameStateController extends BaseController {

    private final GameStateModel _gameStateModel;
 
    /**
     * Constructs a new instance of this class type
     */
    public GameStateController() {
        // Create the model that will represent the timer data
        _gameStateModel = AbstractFactory.getFactory(ModelFactory.class).add(new GameStateModel(),  false);
    }
    
    /**
     * Adds a listener to the underlying model of this controller
     * 
     * @param listener The listener to associated to the underlying model of this controller
     */
    public void addListener(ISignalListener listener) {
        _gameStateModel.addListener(listener);
    }
    
    public void setGameWon() {
        _gameStateModel.getGameState().setGameState(GAME_STATE.GAME_WON);
    }
    
    public void setGameLost() {
        _gameStateModel.getGameState().setGameState(GAME_STATE.GAME_LOST);
    }
    
    public void setGameRunning() {
        _gameStateModel.getGameState().setGameState(GAME_STATE.GAME_RUNNING);
    }
    
    public void setMakingMove() {
        _gameStateModel.getGameState().setGameState(GAME_STATE.GAME_MOVING);
    }

    /**
     * Generates new entries onto the board
     */
    public void generateBoardEntries() {
        
        // Reset the flags counter 
        AbstractFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).resetCounter();
        
        // Reset the game timer
        AbstractFactory.getFactory(ControllerFactory.class).get(GameTimerController.class).resetGameTimer();
        
        // Generate the board entities
        AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class).generateBoardEntries();
    }
}
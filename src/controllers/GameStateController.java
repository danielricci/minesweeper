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

/**
 * The controller associated to the game state of the application
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class GameStateController extends BaseController {

    /**
     * The game state model
     */
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
    
    /**
     * Sets the game as won
     */
    public void setGameWon() {
        _gameStateModel.setGameState(GAME_STATE.GAME_WON);
    }
    
    /**
     * Sets the game as lost
     */
    public void setGameLost() {
        _gameStateModel.setGameState(GAME_STATE.GAME_LOST);
    }
    
    /**
     * Sets the game as running
     */
    public void setGameRunning() {
        _gameStateModel.setGameState(GAME_STATE.GAME_RUNNING);
    }
    
    /**
     * Sets the game as making a move
     */
    public void setMakingMove() {
        _gameStateModel.setGameState(GAME_STATE.GAME_MOVING);
    }
    
    /**
     * Sets the game as game over
     * @return
     */
    public boolean isGameOver() {
        return _gameStateModel.isGameOver();
    }
    
    /**
     * Generates new entries onto the board
     * 
     * @param generateTiles TRUE if the tiles should be generated, FALSE otherwise
     */
    public void resetGame(boolean generateTiles) {
        
        // Set the game state as running
        setGameRunning();
        
        // Reset the flags counter 
        AbstractFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).resetCounter();
        
        // Reset the game timer
        AbstractFactory.getFactory(ControllerFactory.class).get(GameTimerController.class).resetGameTimer();
        
        if(generateTiles) {
            // Generate the board entities
            AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class).generateBoardEntries();
        }
    }
}
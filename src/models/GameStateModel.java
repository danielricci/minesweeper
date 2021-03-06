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

import engine.core.graphics.IRenderable;
import engine.core.mvc.model.BaseModel;
import entities.GameStateEntity;
import generated.DataLookup.GAME_STATE;

/**
 * The model associated to the game state
 *  
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class GameStateModel extends BaseModel {

    /**
     * The game state entity of this model
     */
    private GameStateEntity _gameStateEntity = new GameStateEntity();
 
    /**
     * Sets the game state
     *  
     * @param gameState The game state
     */
    public void setGameState(GAME_STATE gameState) {
        _gameStateEntity.setGameState(gameState);
        doneUpdating();
    }
    
    /**
     * Indicates if this model is in a game over state
     * 
     * @return TRUE if this model is in game over, FALSE otherwise
     */
    public boolean isGameOver() {
        return _gameStateEntity.getGameState() == GAME_STATE.GAME_LOST || _gameStateEntity.getGameState() == GAME_STATE.GAME_WON; 
    }

    /**
     * Gets the renderable entity associated to this model
     * 
     * @return The renderable entity associatd to this model
     */
    public IRenderable getEntity() {
        return _gameStateEntity;
    }
}
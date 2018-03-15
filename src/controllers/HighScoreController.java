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

import java.util.ArrayList;
import java.util.List;

import core.GameSettings;
import core.PreferencesManager;
import engine.communication.internal.signal.ISignalListener;
import engine.core.mvc.controller.BaseController;
import models.HighScoreModel;

/**
 * The controller associated to the high scores of the application
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class HighScoreController extends BaseController {

    /**
     * The list of high score models
     */
    private List<HighScoreModel> _highScores = new ArrayList();
    
    /**
     * Constructs a new instance of this class type
     */
    public HighScoreController() {
        _highScores.add(new HighScoreModel(PreferencesManager.instance().getGameSettings(GameSettings.BEGINNER)));
        _highScores.add(new HighScoreModel(PreferencesManager.instance().getGameSettings(GameSettings.INTERMEDITE)));
        _highScores.add(new HighScoreModel(PreferencesManager.instance().getGameSettings(GameSettings.EXPERT)));
    }
   
    /**
     * Adds the specified listener to the models within this controller
     * 
     * @param listener The listener associated to the event taking place
     */
    public void addListener(ISignalListener listener) {
        _highScores.stream().forEach(z -> z.addListener(listener));
        _highScores.stream().forEach(z -> z.refresh());
    }

    /**
     * Resets the high scores associated to the application
     */
    public void reset() {
        PreferencesManager.instance().reset();
        for(HighScoreModel model : _highScores) {
            model.SETTING.resetHighscore();
            model.doneUpdating();
        }
    }
}
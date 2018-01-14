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

package views;

import controllers.GameStateController;
import engine.communication.internal.signal.arguments.AbstractEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.view.PanelView;
import game.core.factories.ControllerFactory;
import models.GameStateModel;

public class GameStateView extends PanelView {
    
    /**
     * Constructs a new instance of this class type
     */
    public GameStateView() {
        // Create the controller and listen to the contents of its model
        GameStateController controller = AbstractFactory.getFactory(ControllerFactory.class).add(new GameStateController(), true);
        getViewProperties().setEntity(controller);
        controller.addListener(this);
    }
    
    @Override public void initializeComponents() {
    }

    @Override public void initializeComponentBindings() {        
    }
       
    @Override public void update(AbstractEventArgs event) {
        super.update(event);
    
        if(event.getSource() instanceof GameStateModel) {
            
            // Get the model associated to the event received
            GameStateModel bombsCounterModel = (GameStateModel) event.getSource();
            addRenderableContent(bombsCounterModel.getEntity());
            
            // Repaint this view
            invalidate();
            repaint();
        }
    }
}

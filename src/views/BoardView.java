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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import controllers.BoardController;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.PanelView;
import game.core.ControllerFactory;

public class BoardView extends PanelView {
		
	/**
	 * Constructs a new instance of this type
	 */
	public BoardView() {

		// Set the controller associated to this view
		getViewProperties().setEntity(
			AbstractSignalFactory.getFactory(ControllerFactory.class).add(new BoardController(), true)
		);	
		
		setLayout(new GridBagLayout());
	}
	
	@Override public void initializeComponents() {
		
		// Set the constraints of views 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		// Get the board dimensions
//		Dimension boardDimensions = getViewProperties().getEntity(BoardController.class).getBoardDimensions();
//		
//		for(int row = 0, dimensionsX = boardDimensions.width; row < dimensionsX; ++row) {
//			for(int col =  0, dimensionsY = boardDimensions.height; col < dimensionsY; ++col) {		
//			}			
//		}
//		
//		// Add the game panel to this view
//		add(_gamePanel);
	}

	@Override public void initializeComponentBindings() {
	}		
}
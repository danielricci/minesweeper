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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;

import controllers.BoardController;
import engine.core.factories.AbstractSignalFactory;
import engine.core.mvc.view.PanelView;
import game.core.factories.ControllerFactory;
import game.core.factories.ViewFactory;

/**
 * The board view displays the board and all of the board contents
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class BoardView extends PanelView {

    /**
     * Constructs a new instance of this class type
     */
    public BoardView() {

        // Set the controller associated to this view
        getViewProperties().setEntity(
            AbstractSignalFactory.getFactory(ControllerFactory.class).add(new BoardController(), true)
        );

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLoweredSoftBevelBorder());
    }

    @Override public void initializeComponents() {

        // Set the constraints of views 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Get the board dimensions
        // TODO - this should be whatever the current game difficulty is set to
        Dimension boardDimensions = new Dimension(9, 9);

        // Create the board based on the specified dimensions
        for(int row = 0, dimensionsX = boardDimensions.width; row < dimensionsX; ++row) {
            for(int col =  0, dimensionsY = boardDimensions.height; col < dimensionsY; ++col) {
                // Create a tile and add it to our board
                TileView view = AbstractSignalFactory.getFactory(ViewFactory.class).add(new TileView(), false);
                view.setPreferredSize(new Dimension(16, 16));

                gbc.gridx = col;
                gbc.gridy = row;
                add(view, gbc);

                view.render();
            }
        }

        // Link all the tile views together
        getViewProperties().getEntity(BoardController.class).generateLogicalTileLinks();
    }

    @Override public void initializeComponentBindings() {
    }
}
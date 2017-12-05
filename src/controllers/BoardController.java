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

import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import core.EntityMovement;
import engine.core.mvc.controller.BaseController;
import engine.utils.logging.Tracelog;
import model.TileModel;

/**
 * This controller is responsible for the functionality related to the logical components
 * of the board
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class BoardController extends BaseController {

    /**
     * The dimensions of the game
     */
    // TODO - remove hardcoded assignment
    private Dimension _boardDimensions = new Dimension(16,16);

    /**
     * The list of neighbors logically associated to a specified controller
     */
    private final Map<TileModel, Map<EntityMovement, TileModel>> _tileModels = new LinkedHashMap();

    /**
     * Constructs a new instance of this class type
     */
    public BoardController() {
    }

    /**
     * Adds the specified tile model to the list of available tiles
     * 
     * @param tileModel The tile model to add
     */
    public void addTileModel(TileModel tileModel) {
        if(_tileModels.putIfAbsent(tileModel, null) != null) {
            Tracelog.log(Level.SEVERE, true, "Attempting to add a tile model to the tile models list when it has already been added");
        }
    }

    /**
     * Logically attaches the list of tiles together by sub-dividing the list of tiles.
     */
    public void generateLogicalTileLinks() {

        // Get the array representation of our tile models.
        // Note: This is done because it is easier to get a subset of an array
        //       and because the neighbor data structure tracks the insertion 
        //       order at runtime which is what is important here.
        TileModel[] tiles = _tileModels.keySet().toArray(new TileModel[0]);

        // For every row that exists within our setup model
        for(int i = 0, rows = _boardDimensions.height, columns = _boardDimensions.width; i < rows; ++i) {

            // Link the tile rows together
            linkTiles(
                    // Previous row
                    i - 1 >= 0 ? Arrays.copyOfRange(tiles, (i - 1) * columns, ((i - 1) * columns) + columns) : null,
                            // Current Row
                            Arrays.copyOfRange(tiles, i * columns, (i * columns) + columns),
                            // Next Row
                            i + 1 >= 0 ? Arrays.copyOfRange(tiles, (i + 1) * columns, ((i + 1) * columns) + columns) : null
                    );
        }
    }

    /**
     * Links together the passed in rows to form a central node with a 1-level deep neighbouring node set
     *  
     * @param topRow The top row
     * @param neutralRow the neutral row
     * @param bottomRow The bottom row
     */
    private void linkTiles(TileModel[] topRow, TileModel[] neutralRow, TileModel[] bottomRow) {
        for(int i = 0, columns = _boardDimensions.width; i < columns; ++i) {

            // Represents the structural view of a particular tile
            Map<EntityMovement, TileModel> neighbors = new HashMap<EntityMovement, TileModel>();

            // Populate the neighbors structure with the movement elements
            // Note: Diagonals can be fetched using these primitives
            neighbors.put(EntityMovement.UP, topRow == null ? null : topRow[i]);
            neighbors.put(EntityMovement.LEFT, i - 1 < 0 ? null : neutralRow[i - 1]);
            neighbors.put(EntityMovement.RIGHT, i + 1 >= columns ? null : neutralRow[i + 1]);
            neighbors.put(EntityMovement.DOWN, bottomRow == null ? null : bottomRow[i]);

            // Assign the mappings where we reference the neutral-neutral tile as the key
            _tileModels.put(neutralRow[i], neighbors);
        }
    }
}
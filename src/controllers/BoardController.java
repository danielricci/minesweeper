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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import core.EntityMovement;
import core.GameSettings;
import engine.communication.internal.signal.ISignalListener;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.logging.Tracelog;
import game.core.factories.ControllerFactory;
import generated.DataLookup.TILE_STATE;
import models.TileModel;

/**
 * This controller is responsible for the functionality related to the logical components of the board
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class BoardController extends BaseController {

    /**
     * The default game settings
     */
    private static GameSettings GAME_SETTINGS = GameSettings.INTERMEDITE;
    
    /**
     * The list of neighbors logically associated to a specified controller
     */
    private final Map<TileModel, Map<EntityMovement, TileModel>> _tileModels = new LinkedHashMap();

    /**
     * Adds the specified tile model to the list of available tiles
     * 
     * @param tileModel The tile model to add
     */
    public void addTileModel(TileModel tileModel) {
        if(_tileModels.putIfAbsent(tileModel, null) != null) {
            Tracelog.log(Level.SEVERE, true, "Attempting to add a tile model to the tile models list when it has already been added");
        }
        
        // Set the counter of the bombs based on the currently set game settings
        ControllerFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).setCounter(GAME_SETTINGS.MINES);
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
        for(int i = 0, rows = GAME_SETTINGS.getDimensions().height, columns = GAME_SETTINGS.getDimensions().width; i < rows; ++i) {

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
        for(int i = 0, columns = GAME_SETTINGS.getDimensions().width; i < columns; ++i) {

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

    /**
     * Sets the tile neighbors to a highlighted state, this is used for debugging purposes
     * 
     * @param highlighted If the neighbors should be in a highlighted state or not
     */
    public void showTileNeighborsDebug(ISignalListener listener, boolean highlighted) {

        // Get the context of the listener
        TileModel tileModel = _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();

        // Go through the list of tile model neighbors
        for(TileModel tile : getAllNeighbors(tileModel)) {
            tile.setHighlighted(highlighted);
        }
    }

    /**
     * Highlight the list of tiles associated to the specified listener
     * 
     * @param listener The listener
     * @param highlighted The highlighted state
     */
    public void showEmptyTileNeighborsDebug(ISignalListener listener, boolean highlighted) {
        // Get the context of the listener
        TileModel tileModel = _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();

        // If there is a tile model found and it does not have an entity associated to its tile
        // then get all the tiles adjacent to this one and set the highlighted flag accordingly
        if(tileModel != null && tileModel.getTileStateEntity().isEmpty()) {
            getAdjacentTilesFloodFill(tileModel).stream().forEach(z -> z.setHighlighted(highlighted));
        }
    }

    /**
     * Gets adjacent tiles in a flood fill fashion w.r.t the specified initial tile model
     * 
     * @param initialTileModel The initial tile model to perform the flood fill on
     * 
     * @return The list of tile models included in the flood fill
     */
    private List<TileModel> getAdjacentTilesFloodFill(TileModel initialTileModel) {
        List<TileModel> tiles = new ArrayList();
        tiles.add(initialTileModel);

        for(int i = 0;  i < tiles.size(); ++i) {
            tiles.addAll(getAllNeighbors(tiles.get(i)).stream().filter(z -> z.getTileStateEntity().isEmpty() && !tiles.contains(z)).collect(Collectors.toList()));
        }

        return tiles;
    }

    /**
     * Gets all the neighbors associated to the particular model
     * 
     * @param tileModel The tile model to use as a search for neighbors around it
     * 
     * @return The list of tile models that neighbor the passed in tile model
     */
    private List<TileModel> getAllNeighbors(TileModel tileModel) {

        // Get the list of neighbors associated to our tile model
        Map<EntityMovement, TileModel> tileModelNeighbors = _tileModels.get(tileModel);

        // This collection holds the list of all the neighbors
        List<TileModel> allNeighbors = new ArrayList();

        // Go through every entry set in our structure
        for(Map.Entry<EntityMovement, TileModel> entry : tileModelNeighbors.entrySet()) {

            // Get the tile model
            TileModel tile = entry.getValue();
            if(tile == null) {
                continue;
            }

            // Add our tile to the list
            allNeighbors.add(tile);

            // To get the diagonals, make sure that if we are on the top or bottom tile
            // that we fetch their respective tiles, and provided that they are valid
            // add them to our list.
            switch(entry.getKey()) {
            case UP:
            case DOWN:
                TileModel left = _tileModels.get(tile).get(EntityMovement.LEFT);
                if(left != null) {
                    allNeighbors.add(left);
                }

                TileModel right = _tileModels.get(tile).get(EntityMovement.RIGHT);
                if(right != null) {
                    allNeighbors.add(right);
                }
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            default:
                break;
            }
        }

        // return the list of neighbors
        return allNeighbors;
    }


    /**
     * Sets a debug mine onto the specified listeners model
     * 
     * @param listener The listener to set the mine
     */
    public void setDebugMine(ISignalListener listener) {

        // Get the tile model of the listener specified
        TileModel tileModel = _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();
        if(tileModel.getTileStateEntity().hasMine()) {
            tileModel.getTileStateEntity().setTileState(null);
        }
        else {
            tileModel.getTileStateEntity().setTileState(TILE_STATE.BOMB_REVEALED);            
        }
        
        tileModel.doneUpdating();

        // Update the surrounding neighbors to reflect the mine change  
        getAllNeighbors(tileModel).stream().forEach(z -> generateTileNumeral(z));
    }
    
    /**
     * Generates the numeral of a specified tile model
     * 
     * @param tileModel The tile model to generate the numeral on
     */
    private void generateTileNumeral(TileModel tileModel) {
        long count = getAllNeighbors(tileModel).stream().filter(z -> z.getTileStateEntity().hasMine()).count();
        if(count > 0) {
            tileModel.getTileStateEntity().getMineNumeralEntity().setNumeral(count);
        }
        else {
            tileModel.getTileStateEntity().getMineNumeralEntity().setNumeral(0);
        }
        tileModel.doneUpdating();
    }

    /**
     * Handles the event when a button on the specified listener has been selected
     * 
     * @param listener The listener from where the event took place
     */
    public void buttonSelectedEvent(ISignalListener listener) {
        // Get the tile model of the listener specified
        TileModel tileModel = _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();
        
        // If the tile has a valid button state and it is empty (no flag, etc) then remove the button state
        // and update the tile with the surrounding neighbor tiles
        if(tileModel.getButtonStateEntity().isEnabled() && tileModel.getButtonStateEntity().isEmpty()) {
            tileModel.getButtonStateEntity().setIsEnabled(false);
            tileModel.doneUpdating();

            // Update the surrounding neighbors to reflect the mine change  
            getAllNeighbors(tileModel).stream().forEach(z -> generateTileNumeral(z));
        }
    }
    
    /**
     * Handles the event when a button on the specifier listener has it's state changed
     * 
     * @param listener The listener from where the event took place
     */
    public void buttonStateChangeEvent(ISignalListener listener) {
        // Get the tile model of the listener specified
        TileModel tileModel = _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();
        tileModel.getButtonStateEntity().changeState();
        tileModel.doneUpdating();
        
        if(tileModel.getButtonStateEntity().isFlagged()) {
            AbstractFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).decrementCounter();
        }
        else {
            AbstractFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).incrementCounter();
        }
    }
    
    /**
     * Sets the game settings state
     * 
     * @param gameSettings The game settings to set
     */
    public void setGameSettings(GameSettings gameSettings) {
        GAME_SETTINGS = gameSettings;
    }

    /**
     * Gets the dimensions of the game settings
     * 
     * @return The dimensions associated to the game settings
     */
    public Dimension getDimensions() {
        return GAME_SETTINGS.getDimensions();
    }
    
    public void startGame() {
    
    }
    
    /**
     * Clears all the tiles of their entities
     */
    public void clearEntities() {
        for(TileModel model : _tileModels.keySet()) {
            model.getTileStateEntity().reset();
            //model.getButtonStateEntity().reset();
            model.doneUpdating();
        }
    }
}
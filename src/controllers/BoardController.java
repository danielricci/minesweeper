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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import core.EntityPosition;
import core.GameSettings;
import engine.communication.internal.signal.ISignalListener;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.controller.BaseController;
import engine.core.system.AbstractApplication;
import engine.utils.logging.Tracelog;
import game.core.factories.ControllerFactory;
import generated.DataLookup.BUTTON_STATE;
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
    public static GameSettings GAME_SETTINGS = GameSettings.BEGINNER;
    
    /**
     * The list of neighbors logically associated to a specified controller
     */
    private final Map<TileModel, Map<EntityPosition, TileModel>> _tileModels = new LinkedHashMap();

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

        // Set the counter of the bombs based on the currently set game settings
        ControllerFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).setCounter(GAME_SETTINGS.MINES);
        
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
        
        // Generate the board entries
        generateBoardEntries();
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
            Map<EntityPosition, TileModel> neighbors = new HashMap<EntityPosition, TileModel>();

            // Populate the neighbors structure with the movement elements
            // Note: Diagonals can be fetched using these primitives
            neighbors.put(EntityPosition.UP, topRow == null ? null : topRow[i]);
            neighbors.put(EntityPosition.LEFT, i - 1 < 0 ? null : neutralRow[i - 1]);
            neighbors.put(EntityPosition.RIGHT, i + 1 >= columns ? null : neutralRow[i + 1]);
            neighbors.put(EntityPosition.DOWN, bottomRow == null ? null : bottomRow[i]);

            // Assign the mappings where we reference the neutral-neutral tile as the key
            _tileModels.put(neutralRow[i], neighbors);
        }
    }

    /**
     * Sets the tile neighbors to a highlighted state, this is used for debugging purposes
     * 
     * @param listener The listener
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
        
        List<TileModel> completedTiles = new ArrayList();
        List<TileModel> emptyTiles = new ArrayList();
        emptyTiles.add(initialTileModel);

        for(int i = 0;  i < emptyTiles.size(); ++i) {
            TileModel candidateTile = emptyTiles.get(i);
            if(candidateTile.getTileStateEntity().isEmpty()) {
                List<TileModel> neighbours = getAllNeighbors(candidateTile).stream().filter(z -> !emptyTiles.contains(z) && !completedTiles.contains(z)).collect(Collectors.toList());
                for(TileModel neighbour : neighbours) {
                    if(neighbour.getTileStateEntity().isEmpty()) {
                        emptyTiles.add(neighbour);
                    }
                    else if(neighbour.getTileStateEntity().getMineNumeralEntity().hasActiveData()) {
                        completedTiles.add(neighbour);
                    }
                }
            }
            
        }
        completedTiles.addAll(emptyTiles);
        return completedTiles;
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
        Map<EntityPosition, TileModel> tileModelNeighbors = _tileModels.get(tileModel);

        // This collection holds the list of all the neighbors
        List<TileModel> allNeighbors = new ArrayList();

        // Go through every entry set in our structure
        for(Map.Entry<EntityPosition, TileModel> entry : tileModelNeighbors.entrySet()) {

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
                TileModel left = _tileModels.get(tile).get(EntityPosition.LEFT);
                if(left != null) {
                    allNeighbors.add(left);
                }

                TileModel right = _tileModels.get(tile).get(EntityPosition.RIGHT);
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
     * Sets a mine onto the specified listeners model
     * 
     * @param listener The listener from where the event took place
     */
    public void setMine(ISignalListener listener) {

        TileModel tileModel = null;
        
        if(listener instanceof TileModel) {
            tileModel = (TileModel) listener;
        }
        else {
            tileModel = _tileModels.keySet().parallelStream().filter(z -> z.isModelListening(listener)).findFirst().get();    
        }
        
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
        int count = (int)getAllNeighbors(tileModel).stream().filter(z -> z.getTileStateEntity().hasMine()).count();
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
     * @param performingMove TRUE if the move is being performed, false otherwise
     */
    public void buttonSelectedEvent(ISignalListener listener, boolean performingMove) {

        // If the game is over then do not proceed any further
        GameStateController gameStateController = AbstractFactory.getFactory(ControllerFactory.class).get(GameStateController.class);
        if(gameStateController.isGameOver()) {
            return;
        }
        
        // Get the tile model of the listener specified
        TileModel tileModel = listener instanceof TileModel 
                ? (TileModel)listener
                : _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();
                
        performMove(tileModel, performingMove);
    }
    
    /**
     * Performs a move of the specified tile
     * 
     * @param tileModel The tile associated to the move being performed
     * @param performingMove TRUE if the move is being finalized, FALSE if the move is in between
     */
    private void performMove(TileModel tileModel, boolean performingMove) {
        
        GameStateController gameStateController = AbstractFactory.getFactory(ControllerFactory.class).get(GameStateController.class);
        
        // If the move is actually being performed, then and only then should we start the actual game timer and make
        // sure that the tile does not have a mine for the first move.
        if(performingMove) {
            // Check to see if there are any buttons that have already been revealed, if not then this is considered
            // to be the first move.  The first move is always a valid move, so make sure that if there is a mine, that
            // it is placed at a different location
            if(_tileModels.keySet().parallelStream().filter(z -> !z.getButtonStateEntity().isEnabled()).count() == 0) {
                if(tileModel.getButtonStateEntity().isMark() || tileModel.getButtonStateEntity().isEmpty()) {
                    // Set the timer of the game
                    AbstractFactory.getFactory(ControllerFactory.class).get(GameTimerController.class).startGameTimer();
                    
                    // If there is a mine on the selected tile then have it removed
                    if(tileModel.getTileStateEntity().hasMine()) {
                        // Toggle off the mine
                        setMine(tileModel);
                        
                        // Pick a new tile to have set as the mine
                        TileModel newTile = _tileModels.keySet().parallelStream().filter(z -> z != tileModel && z.getButtonStateEntity().isEnabled() && !z.getTileStateEntity().hasMine()).findFirst().get();
                        setMine(newTile);
                    }
                }
            }
        }
        
        // If the tile has a valid button state and it is empty (no flag, etc) then remove the button state
        // and update the tile with the surrounding neighbor tiles
        if(tileModel.getButtonStateEntity().isEnabled() && (tileModel.getButtonStateEntity().isEmpty() || tileModel.getButtonStateEntity().isMark())) {
            
            if(performingMove) {
                
                // Remove the button
                tileModel.getButtonStateEntity().setIsButtonEnabled(false);
                
                // Indicate that the model update has been finished
                tileModel.doneUpdating();

                // Set the game state as running
                if(!gameStateController.isGameOver()) {
                    gameStateController.setGameRunning();
                }
                
                // If the tile selected is considered empty then get the floodfill
                // of adjacent cells and have them discovered
                if(tileModel.getTileStateEntity().isEmpty()) {
                         
                    // Go through the list of adjacent tiles in a flood-fill fashion and 
                    // provided that the tiles are empty, uncover the tile
                    for(TileModel adjacentTile : getAdjacentTilesFloodFill(tileModel)) {
                        if(adjacentTile.getButtonStateEntity().isEmpty()) {
                            adjacentTile.getButtonStateEntity().setIsButtonEnabled(false);
                            adjacentTile.doneUpdating();
                        }
                    }
                }
                
                // If the tile selected has a mine, then display it 
                if(tileModel.getTileStateEntity().hasMine()) {
                    
                    // Set the tile selected as the mine clicked
                    tileModel.getTileStateEntity().setTileState(TILE_STATE.BOMB_CLICKED);
                    
                    // Get all the mines that are on the board and reveal them
                    List<TileModel> tilesWithMine = _tileModels.keySet().parallelStream().filter(z -> z.getTileStateEntity().hasMine() && !z.equals(tileModel)).collect(Collectors.toList());
                    for(TileModel tileWithMine : tilesWithMine) {
                        
                        // If the tile is set as flagged then leave it as flagged. We do not reveal bombs that
                        // have already been flagged
                        if(tileWithMine.getButtonStateEntity().isFlagged()) {
                            continue;
                        }
                        
                        tileWithMine.getButtonStateEntity().setIsButtonEnabled(false);
                        tileWithMine.doneUpdating();
                    }
                    
                    // Get all the tiles that have flags that do not have mines, they should be marked as 'misflagged'
                    List<TileModel> misflaggedTiles = _tileModels.keySet().parallelStream().filter(z -> !z.getTileStateEntity().hasMine() && z.getButtonStateEntity().isFlagged()).collect(Collectors.toList());
                    for(TileModel misFlaggedTile : misflaggedTiles) {
                        misFlaggedTile.getButtonStateEntity().setIsButtonEnabled(false);
                        misFlaggedTile.getTileStateEntity().setTileState(TILE_STATE.BOMB_MISFLAGGED);
                        misFlaggedTile.doneUpdating();
                    }
                    
                    // Set the game state as lost and stop the timer
                    gameStateController.setGameLost();
                    AbstractFactory.getFactory(ControllerFactory.class).get(GameTimerController.class).stopGameTimer();
                }
                else if(_tileModels.keySet().parallelStream().filter(z -> z.getButtonStateEntity().isEnabled()).count() == GAME_SETTINGS.MINES) {
                    AbstractFactory.getFactory(ControllerFactory.class).get(GameTimerController.class).stopGameTimer();
                    
                    List<TileModel> mineTiles = _tileModels.keySet().parallelStream().filter(z -> z.getTileStateEntity().hasMine()).collect(Collectors.toList());
                    for(TileModel mineTile : mineTiles) {
                        mineTile.getButtonStateEntity().changeState(BUTTON_STATE.BUTTON_FLAG);
                        mineTile.doneUpdating();
                    }
                    
                    // Set the game as won
                    gameStateController.setGameWon();
                    
                    // Get the timer result of the current game
                    int timerResult = AbstractFactory.getFactory(ControllerFactory.class).get(GameTimerController.class).getGameTimer();
                    if(GAME_SETTINGS != GameSettings.CUSTOM && GAME_SETTINGS.getTime() > timerResult) {
                    
                        // Prompt the user to enter their name
                        String name = JOptionPane.showInputDialog(
                            AbstractApplication.instance(), 
                            "Congratulations, you have achieved a new highscore. Please enter your name", 
                            "Highscore",
                            JOptionPane.WARNING_MESSAGE
                        );
                        
                        // Validate the name that was entered
                        if(name == null || name.trim().isEmpty()) {
                            name = "Anonymous";
                        }
                        else {
                            name = name.trim();
                        }
                        
                        // Set the highscore with the name provided and the new timer
                        GAME_SETTINGS.setHighScore(name, timerResult);
                    }
                }
            }
            else {
                // Set the game state as running, this will show the :O face
                gameStateController.setMakingMove();                
            }
        }
    }
    
    /**
     * Handles the event when a button on the specifier listener has it's state changed
     * 
     * @param listener The listener from where the event took place
     */
    public void buttonStateChangeEvent(ISignalListener listener) {
        
        GameStateController gameStateController = AbstractFactory.getFactory(ControllerFactory.class).get(GameStateController.class);
        if(gameStateController.isGameOver()) {
            return;
        }
        
        AbstractFactory.getFactory(ControllerFactory.class).get(GameTimerController.class).startGameTimer();
        
        // Get the tile model of the listener specified
        TileModel tileModel = _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();
        tileModel.getButtonStateEntity().changeState();
        tileModel.doneUpdating();
        
        gameStateController.setGameRunning();
        
        if(tileModel.getButtonStateEntity().isFlagged()) {
            AbstractFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).decrementCounter();
        }
        else if(tileModel.getButtonStateEntity().isEmpty()) {
            AbstractFactory.getFactory(ControllerFactory.class).get(BombsCounterController.class).incrementCounter();
        }
    }
    
    public void performChord(ISignalListener listener) {
        GameStateController gameStateController = AbstractFactory.getFactory(ControllerFactory.class).get(GameStateController.class);
        if(gameStateController.isGameOver()) {
            return;
        }
        
        // Get the tile model of the listener specified
        TileModel tileModel = _tileModels.keySet().stream().filter(z -> z.isModelListening(listener)).findFirst().get();
        
        // Proceed if the button has been uncovered
        if(!tileModel.getButtonStateEntity().isEnabled()) {
            
            // Get the numeral, and if it is greater than 0 and it has matching flags then proceed
            int numeral = tileModel.getTileStateEntity().getMineNumeralEntity().getNumeral();
            List<TileModel> chordNeighbors = getAllNeighbors(tileModel);
            if(numeral > 0 && chordNeighbors.stream().filter(z -> z.getButtonStateEntity().isFlagged()).count() == numeral) {
                chordNeighbors.forEach(z -> performMove(z, true));
            }
        }
    }
    
    /**
     * Generates the board of random entities
     */
    public void generateBoardEntries() {
        
        // Clear the entities currently on the board, but do not refresh the view, wait until
        // this operation is completed
        _tileModels.keySet().parallelStream().forEach(z -> z.setSuppressUpdates(true));
        clearEntities();
        
        // Get the current list of tiles and randomize them in a new list
        List<TileModel> tiles = new ArrayList(_tileModels.keySet());
        Collections.shuffle(tiles, new Random(System.nanoTime()));
        
        // Go through the newly randomize local list and assign mine
        for(int i = 0, size = GAME_SETTINGS.MINES; i < size; ++i) {
            setMine(tiles.get(i));
        }
        
        // Re-apply the update functionality and perform the update
        _tileModels.keySet().parallelStream().forEach(z -> z.setSuppressUpdates(false));
        _tileModels.keySet().parallelStream().forEach(z -> z.doneUpdating());
    }
    
    /**
     * Clears all the tiles of their entities
     */
    public void clearEntities() {
        _tileModels.keySet().parallelStream().forEach(z -> z.reset());
    }
}
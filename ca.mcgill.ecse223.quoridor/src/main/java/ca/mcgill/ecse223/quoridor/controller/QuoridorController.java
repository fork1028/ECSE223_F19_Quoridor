
package ca.mcgill.ecse223.quoridor.controller;

import java.io.File;
import java.sql.Time;
import java.util.List;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;

public class QuoridorController {

	/**
	 * This method starts the game
	 * 
	 * @param blackPlayer the user assigned to the black pawn
	 * @param whitePlayer the user assigned to the black pawn
	 * @param time        total thinking time, in seconds
	 * @throws InvalidInputException
	 * @author Matteo Barbieri 260805184
	 */
	public static Game startGame(User blackPlayer, User whitePlayer, Time time) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Unable to start Game");

	}

	/**
	 * This method Provides or Selects a User to be used in a game
	 * 
	 * @param username desired to create new User
	 * @throws InvalidInputException
	 * @author Matteo Barbieri 260805184
	 */
	public static boolean provideSelectUser(String username) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Unable to set Username");
	}

	/**
	 * This method sets the total thinking time for both players while a new game is
	 * initalizing.
	 * 
	 * @param min number of minutes
	 * @param sec number of seconds
	 * @throws InvalidInputException
	 * @author Helen Lin, 260715521
	 */
	public static void setTotalThinkingTime(int min, int sec) throws InvalidInputException {

		//started implementation
		//throw new UnsupportedOperationException("Default implementation of setTotalThinkingTime");
		
		//validator to check min and sec are appropriate for total thinking
		if (min > 60 || min <0 || sec >60 || sec<0) {
			throw new InvalidInputException("Invalid request for total thinking time.");
		}

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Time time = getIntToTime(min, sec);
		
		try { 
			Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer(); 
			Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();

			if (!blackPlayer.setRemainingTime(time)) {
				throw new InvalidInputException("Unable to set thinking time for BLACK player.");
			}
			if (!whitePlayer.setRemainingTime(time)) 
				throw new InvalidInputException("Unable to set thinking time for  WHITE player.");
		} catch (RuntimeException e) {
			 throw new InvalidInputException(e.getMessage());
		}
			
	}

	/**
	 * This method initializes a new board.
	 * 
	 * @throws InvalidInputException
	 * @author Helen Lin, 260715521
	 */
	public static void initializeBoard() throws InvalidInputException {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		//started implementation
		
		//It shall be white player to move
		try { 
			GamePosition position = new GamePosition(0, null, null, null, null);
			Player white = quoridor.getCurrentGame().getWhitePlayer();
			
			//note from TA code: there are total 36 tiles in the first four rows and 
			//indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting pos
			
			//white initial position
			Tile whiteStart = quoridor.getBoard().getTile(36);
			PlayerPosition whitePos = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), whiteStart);
			
			
			//black initial position
			Tile blackStart = quoridor.getBoard().getTile(44);
			PlayerPosition blackPos = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), blackStart);
			
			
			GamePosition gamePosition = new GamePosition(0, whitePos, blackPos, white, quoridor.getCurrentGame());
		
		} catch (RuntimeException e) {
			throw new UnsupportedOperationException("Default implementation of initializeBoard");
		}
		
		//10 walls in stock for each player
		try { 
			for (int j = 0; j < 10; j++) {
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(Wall.getWithId(j));
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsInStock(Wall.getWithId(j+10));
			}
		} catch (RuntimeException e) {
			 throw new InvalidInputException("Unable to add initial stock for players.");
		}
	
		//TODO: White clock counting down
		//GUI TODO: clock countdown gui
		//GUI TODO: show that this is white turn
		
	}



	/**
	 * This method allows a user to grab a wall.
	 * 
	 * @param player that is going to grab the wall
	 * @param move   that is going to grab the wall
	 * @param wall   that is on the stack
	 * @throws InvalidInputException
	 * @author Rajaa Boukhelif, 260870030
	 */
	public static void grabWall(Player player, WallMove move, Wall wall) throws UnsupportedOperationException {

		 List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWalls();
		 List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getWalls();
		 Direction startDir = Direction.Horizontal;
	
		 
		 if (whiteWalls.isEmpty()) {
				throw new UnsupportedOperationException("There are no more white walls");
		 }
		 if (!whiteWalls.isEmpty()) {
			 
			 whiteWalls.remove(wall); 
		 }
		 if (blackWalls.isEmpty()) {
				throw new UnsupportedOperationException("There are no more black walls");
		 }
		 if (!blackWalls.isEmpty()) {
			blackWalls.remove(wall); 
		 }
	
	}

	/**
	 * This method rotates a wall.
	 * 
	 * @param player    that is going to rotate the wall
	 * @param move      that is going to rotate the wall
	 * @param wall      that is on the stack
	 * @param direction that is the orientation of the wall
	 * @throws InvalidInputException
	 * @author Rajaa Boukhelif, 260870030
	 */
	public static void rotateWall(Wall wall, WallMove move, Direction dir) throws UnsupportedOperationException {

	if(dir == Direction.Horizontal) {
		 Direction newDir = Direction.Vertical;
		 dir = newDir;
	}
	if(dir == Direction.Vertical) {
		 Direction newDir = Direction.Horizontal;
		 dir = newDir;
	}
	else throw new UnsupportedOperationException("The wall cannot be rotated");
	}
	
	/**
	 * This method enables player to move around a wall on the board.
	 * 
	 * @param Player   that is going to move the wall
	 * @param Wall     that the player intends to move
	 * @param WallMove that player clicked on the arrow keys.
	 * @throws InvalidInputException
	 * @author Xinyue Chen, 260830761
	 */
	public static void moveWall(Player player, Wall wall, WallMove move) throws UnsupportedOperationException {

		throw new java.lang.UnsupportedOperationException("You can't move the wall further.");

	}

	/**
	 * This method enables player to drop a wall if there is no wall under it.
	 * 
	 * @param Player   that is going to drop the wall
	 * @param Wall     that the player intends to drop
	 * @param WallMove that the wall is going to be placed
	 * @throws InvalidInputException
	 * @author Xinyue Chen, 260830761
	 */
	public static void dropWall(Player player, Wall wall) throws UnsupportedOperationException {

		throw new java.lang.UnsupportedOperationException("You can't drop the wall here, there is already a wall.");

	}

	/**
	 * This method saves the current game as a text file.
	 * 
	 * @param newFileName String representing the name to be given to the text file
	 *                    being saved.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */
	public static void saveCurrentGame(String newFileName) throws UnsupportedOperationException {
		// call helper function fileAlreadyExists first!!!
		throw new UnsupportedOperationException("Controller feature not fully implemented yet!");
	}

	/**
	 * This is a helper method that checks if a file already exists.
	 * 
	 * @param newFileName String representing the name of the file that is being
	 *                    searched for.
	 * @throws UnsupportedOperationException
	 * @return Boolean returns whether or not there already exists a file with that
	 *         name in the filesystem.
	 * @author Shayne Leitman, 260688512
	 */
	public static Boolean fileAlreadyExists(String fileName) {
		File tempFile = new File(fileName);
		if (tempFile.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This is a helper method that gives the difference in milliseconds between when a file was last modified, and the current time
	 * 
	 * @param newFileName String representing the name of the file
	 * @throws UnsupportedOperationException
	 * @return Long returns a long value of milliseconds
	 * @author Shayne Leitman, 260688512
	 */
	public static Long lastModifiedToCurrentTime(String fileName) throws UnsupportedOperationException {
		File tempFile = new File(fileName);
		if (tempFile.exists()) {
			Long curTime = System.currentTimeMillis();
			Long lastModifiedTime = tempFile.lastModified();
			Long difference = curTime - lastModifiedTime;
			return difference;
		} else {
			throw new UnsupportedOperationException("No file with this name exists!");
		}
	}

	/**
	 * This method loads a game from a text file, checking to see that it is a valid
	 * position
	 * 
	 * @param fileName String representing the name of the file taht you wish to
	 *                 import.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */
	// Idea is to first create a new game (with the users, and time), then go in and
	// set time for each player.
	// Next, go move by move through the game, checking at each step if a move is
	// alright.
	public static void loadSavedGame(String fileName) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Controller feature not fully implemented yet!");
	}

	/**
	 * This method validates a potential pawn move position.
	 * 
	 * @param row row of the move position
	 * @param col col of the move position
	 * @throws UnsupportedOperationException
	 * @author Sami Junior Kahil, 260834568
	 */
	public static boolean validatePosition(int row, int col, Direction direction) throws UnsupportedOperationException {
		if (direction == null) {
			return validatePawnPosition(row, col);
		} else {
			return validateWallPosition(row, col, direction);
		}
	}

	public static boolean validatePawnPosition(int row, int col) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(" * Invalid position for pawn...");
	}

	/**
	 * This method validates wall position.
	 * 
	 * @param row       row of the center of the wall
	 * @param col       col of the center of the wall
	 * @param direction direction of the wall
	 * @throws UnsupportedOperationException
	 * @author Sami Junior Kahil, 260834568
	 */
	public static boolean validateWallPosition(int row, int col, Direction direction)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException(" * Invalid position for wall...");
	}

	public static boolean validateWallPosition(Wall wall) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(" * Invalid position for wall...");
	}

	// 12. Switch player (aka. Update board) -- Sami
	/**
	 * This method switches the current player.
	 * 
	 * @throws UnsupportedOperationException
	 * @author Sami Junior Kahil, 260834568
	 */
	public static boolean switchCurrentPlayer() throws UnsupportedOperationException {
		throw new UnsupportedOperationException(" * Can't switch player...");
	}

	// helper methods

	/**
	 * Helper method to convert time provided by user to Time.
	 * @param min number of min
	 * @param sec number of seconds
	 * @return Time time
	 * @author Helen Lin 260715521
	 */
	public static Time getIntToTime(int min, int sec) {
		int ms = (min*60 + sec)*1000;
		Time time = new Time(ms);
		return time;
	}

	/**
	 * Helper method to convert time provided in Time to seconds.
	 * 
	 * @param Time time
	 * @return sec Number of seconds
	 * @author Helen Lin, 260715521
	 */
	public static int getTimeToSeconds(Time time) {
		int ms = (int) time.getTime();
		return ms / 1000;
	}

}

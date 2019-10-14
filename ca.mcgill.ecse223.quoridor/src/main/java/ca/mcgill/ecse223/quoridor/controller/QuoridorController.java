
package ca.mcgill.ecse223.quoridor.controller;

import java.io.File;
import java.sql.Time;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
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

		throw new UnsupportedOperationException("Default implementation of setTotalThinkingTime");

		/*
		 * //started implementation for next project iteration Quoridor quoridor =
		 * QuoridorApplication.getQuoridor(); Time time = getIntToTime(min, sec);
		 * 
		 * //todo: validator to check min and sec are appropriate for total thinking
		 * time
		 * 
		 * try { Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer(); Player
		 * whitePlayer = quoridor.getCurrentGame().getWhitePlayer(); if
		 * (!blackPlayer.setRemainingTime(time)) throw new
		 * InvalidInputException("Unable to set thinking time for BLACK player."); if
		 * (!whitePlayer.setRemainingTime(time)) throw new
		 * InvalidInputException("Unable to set thinking time for BLACK player.");
		 * 
		 * } catch (RuntimeException e) { throw new
		 * InvalidInputException(e.getMessage()); }
		 */

	}

	/**
	 * This method initializes a new board.
	 * 
	 * @throws InvalidInputException
	 * @author Helen Lin, 260715521
	 */
	public static void initializeBoard() throws InvalidInputException {

		throw new UnsupportedOperationException("Default implementation of setTotalThinkingTime");

		/*
		 * //started implementation for next iteration
		 * 
		 * Quoridor quoridor = QuoridorApplication.getQuoridor();
		 * 
		 * try { if (!quoridor.hasBoard()) { Board newBoard = new Board(quoridor);
		 * //creates new board and adds it to current quoridor
		 * 
		 * // Creating tiles by rows, i.e., the column index changes with every tile
		 * creation for (int i = 1; i <= 9; i++) { // rows for (int j = 1; j <= 9; j++)
		 * { // columns newBoard.addTile(i, j); } } } else //quoridor board is already
		 * initialized throw new
		 * InvalidInputException("Quordior already has an initialized board."); } catch
		 * (RuntimeException e) { throw new InvalidInputException(e.getMessage()); }
		 */

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
	public static void rotateWall(Wall wall, WallMove move, Direction direction) throws UnsupportedOperationException {

		throw new UnsupportedOperationException("The wall cannot be rotated");
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

		throw new UnsupportedOperationException("There are no more walls");
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
	 * Helper method to convert time provided in seconds to Time.
	 * 
	 * @param sec number of seconds
	 * @return Time time
	 * @author Helen Lin 260715521
	 */
	public static Time getSecondsToTime(int sec) {
		int ms = sec * 1000;
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

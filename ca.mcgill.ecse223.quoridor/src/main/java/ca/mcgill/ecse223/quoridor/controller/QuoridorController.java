

package ca.mcgill.ecse223.quoridor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import javax.swing.Timer;
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
	public static void startGame(User blackPlayer, User whitePlayer, int min, int sec) throws UnsupportedOperationException {
		//create game
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		Game currGame = new Game(GameStatus.Initializing, MoveMode.PlayerMove, null, null, quoridor);
		QuoridorApplication.getQuoridor().setCurrentGame(currGame);
		//create and set players to a colour
		QuoridroController.createUser(whitePlayer);
		QuoridorController.setUserToPlayer(username, false);
	 
		QuoridroController.createUser(blackPlayer);
		QuoridorController.setUserToPlayer(blackPlayer, true);
		
		//set total thinking time
		QuoridorController.SetTotalThinkingTime(min, sec);
		setTimers();
		
		
		//set positions of players and walls to set a game position
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);
		
		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(),
				player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(),
				player2StartPos);
		
		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);
		
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}
		
		currgame.setCurrentPosition(gamePosition);
		
		QuoridorController.initializeBoard();
		QuoridorApplication.getQuoridor.getCurrentGame().setGameStatus(GameStatus.Running);
	}

	/**
	 * This method Provides or Selects a User to be used in a game
	 * 
	 * @param username desired to create new User
	 * @throws InvalidInputException
	 * @author Matteo Barbieri 260805184
	 */
	public static void provideSelectUser(String username) throws UnsupportedOperationException {
		if (getUserByUsername(username) != null) {
			//create user and ouput warning
			createUser(username)
			else
				//creates user without warning
				createUser(username)
		}
		
	}

	/**
	 * This method creates a new User in Quoridor by username, or returns an
	 * existing user
	 * 
	 * @param username desired to create new User
	 * @throws InvalidInputException
	 * @author Helen
	 */
	public static User createUser(String username) throws IllegalArgumentException {
		try {
			// check if pre-existing
			User user = getUserByUsername(username);
			if (user != null) {
				return user; // return pre-existing user
			} else // or create new user
				return QuoridorApplication.getQuoridor().addUser(username);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Unable to create a user");
		}

	}

	/**
	 * This method sets an pre-created user to the current game's black or white
	 * player by their username
	 * 
	 * @param username       String value of username to set
	 * @param forBlackPlayer True to set black player of current game, false for
	 *                       white
	 * @return True if success, false if failed
	 * @author Helen
	 * @throws IllegalArgumentException
	 */
	public static boolean setUserToPlayer(String username, boolean forBlackPlayer) throws IllegalArgumentException {
		try {
			User user = getUserByUsername(username);
			Player player = forBlackPlayer ? new Player(null, user, 1, Direction.Horizontal) // black
					: new Player(null, user, 9, Direction.Horizontal); // white
			return forBlackPlayer ? QuoridorApplication.getQuoridor().getCurrentGame().setBlackPlayer(player)
					: QuoridorApplication.getQuoridor().getCurrentGame().setWhitePlayer(player);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Unable to create a user");
		}

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

		// validator to check min and sec are appropriate for total thinking
		if (min > 60 || min < 0 || sec > 60 || sec < 0) {
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
	 * This method starts clock and countdown when user clicks start clock in view
	 * for the next player to move
	 * 
	 * @author Helen
	 * @throws InvalidInputException
	 */
	public static void startClock() throws InvalidInputException {
		try {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getRemainingTime()
					.notify();

		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to start clock for next player");
		}

		// TODO: UI part in view

	}

	/**
	 * This method initializes a new board.
	 * 
	 * @throws InvalidInputException
	 * @author Helen Lin, 260715521
	 */
	public static void initializeBoard() throws InvalidInputException {
		Quoridor quoridor = QuoridorApplication.getQuoridor();

		// create a new board with all tiles
		try {
			if (!quoridor.hasBoard()) {
				Board board = new Board(quoridor);
				if (!board.hasTiles()) {
					for (int i = 1; i <= 9; i++) { // rows
						for (int j = 1; j <= 9; j++) { // columns
							board.addTile(i, j);
						}
					}
				}
			}
		} catch (RuntimeException e) {
			throw new UnsupportedOperationException("Unable to create board with 81 tiles");
		}

		// set white and plack initial position , and set white player to move
		try {
			Player white = quoridor.getCurrentGame().getWhitePlayer();

			// note from TA code: there are total 36 tiles in the first four rows and
			// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
			// pos
		
			// white initial position
			Tile whiteStart = quoridor.getBoard().getTile(36);
			PlayerPosition whitePos = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), whiteStart);

			// black initial position
			Tile blackStart = quoridor.getBoard().getTile(44);
			PlayerPosition blackPos = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), blackStart);

			GamePosition gamePosition = new GamePosition(0, whitePos, blackPos, white, quoridor.getCurrentGame());
			quoridor.getCurrentGame().setCurrentPosition(gamePosition);

		} catch (RuntimeException e) {
			throw new UnsupportedOperationException("Unable to add white player as next player to move");
		}

		// 10 walls in stock for each player
		try {

		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to create walls for each player.");
		}

		// 10 walls in stock for each player
		try {
			if (!quoridor.getCurrentGame().getCurrentPosition().hasWhiteWallsInStock()
					|| !quoridor.getCurrentGame().getCurrentPosition().hasBlackWallsInStock()) {

				// create walls (lower ID belong to white player) if needed
				if (!quoridor.getCurrentGame().getWhitePlayer().hasWalls()
						|| !quoridor.getCurrentGame().getBlackPlayer().hasWalls()) {
					Player player = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
					for (int i = 0; i < 2; i++) {
						if (i == 1) {
							player = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
						}
						for (int j = 0; j < 10; j++) {
							new Wall(i * 10 + j, player);
						}
					}
				}

				// add walls to stock for each player if needed
				for (int j = 0; j < 10; j++) {
					quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(Wall.getWithId(j));
					quoridor.getCurrentGame().getCurrentPosition().addBlackWallsInStock(Wall.getWithId(j + 10));
				}
			}

		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to add initial stock for players.");
		}

		// TODO: White clock counting down
		// GUI TODO: clock countdown gui
		// GUI TODO: show that this is white turn
		// start clock for white player
		try {
			startClock();
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

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
	public static void grabWall(Player player) throws UnsupportedOperationException {

		/*
		 * ActionListener taskPerformer = new ActionListener() { public void
		 * actionPerformed(ActionEvent evt) { if(grabButton.getModel().isPressed())
		 * 
		 * }};
		 */
		List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getWhiteWallsInStock();
		List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getBlackWallsInStock();
		Direction startDir = Direction.Horizontal;

		Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		int wallIdxForPlayer = 1;

		if (player == whitePlayer) {
			//Tile whiteStart = QuoridorApplication.getQuoridor().getBoard().getTile(36);
			Tile whiteStart=new Tile(5,5,QuoridorApplication.getQuoridor().getBoard());
			Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
					.getWhiteWallsInStock(wallIdxForPlayer);
			WallMove nextmove = new WallMove(0, 0, player, whiteStart, null, startDir, wall);
			QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(nextmove);
			if (!whiteWalls.isEmpty()) {
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
			}									
			 else 
				 throw new UnsupportedOperationException(" There are no more white walls");

		}

		if (player == blackPlayer) {
			//Tile blackStart = QuoridorApplication.getQuoridor().getBoard().getTile(44);
			Tile blackStart=new Tile(5,5,QuoridorApplication.getQuoridor().getBoard());
			Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
					.getBlackWallsInStock(wallIdxForPlayer);
			WallMove nextmove = new WallMove(0, 0, player, blackStart, null, startDir, wall);
			QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(nextmove);

			if (!blackWalls.isEmpty()) {
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
			}
			 else 
				 throw new UnsupportedOperationException(" There are no more black		 walls");

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
	public static void rotateWall(Wall wall, WallMove move, String dir) throws UnsupportedOperationException {

		/*
		 * ActionListener taskPerformer = new ActionListener() { public void
		 * actionPerformed(ActionEvent evt) { if(rotateButton.getModel().isPressed())
		 * 
		 * }};
		 */

		// g2d.translate(wall.x+(wall.width/2), wall.y+(wall.height/2));
		// g2d.rotate(Math.toRadians(90));
		  if (dir.equals("vertical")) {
				Direction newDir1 = Direction.Horizontal;
				QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(newDir1);
			} 
			

		
		  if (dir.equals("horizontal")) {
			Direction newDir = Direction.Vertical;
			QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(newDir);
		  }
			// Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock(wallIdxForPlayer);
			// WallMove nextmove = new WallMove(0, 0, player, whiteStart, null, startDir, wall);
			// QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(nextmove);
			
		
	}

	/**
	 * This method enables player to move a wall on the board.
	 * 
	 * @param moveDirection that indicates the direction the user wants to move the
	 *                      wall
	 * @throws UnsupportedOperationException
	 * @author Xinyue Chen 260830761
	 * @throws InvalidInputException
	 */
	public static void moveWall(String moveDirection) throws UnsupportedOperationException, InvalidInputException {

		Board board = QuoridorApplication.getQuoridor().getBoard();
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
//		Wall wall=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		if (candidate == null) {
			throw new InvalidInputException("WallMove doesn't exist");
		}
		if (board == null) {
			throw new InvalidInputException("Board doesn't exist");
		}
		int row = candidate.getTargetTile().getRow();
		int col = candidate.getTargetTile().getColumn();
		Tile newTile = null;
		if (row == 1 || row == 9) {
			throw new java.lang.UnsupportedOperationException("You can't move the wall further.");
		}
		if (col == 1 || col == 9) {
			throw new java.lang.UnsupportedOperationException("You can't move the wall further.");
		}
		if (moveDirection.equals("right")) {
			// newTile.getBoard().getTile();
			newTile = new Tile(row, col + 1, board);
			//newTile=board.getTile(index);
		}
		if (moveDirection.equals("left")) {
			newTile = new Tile(row, col - 1, board);
		}
		if (moveDirection.equals("up")) {
			newTile = new Tile(row - 1, col, board);
		}
		if (moveDirection.equals("down")) {
			newTile = new Tile(row + 1, col, board);
		}
		if (newTile != null) {
			candidate.setTargetTile(newTile);
		}
		else throw new InvalidInputException("New target tile doesn't exist");
		
	}

	/**
	 * This method enables player to drop a wall.
	 * 
	 * @param Player playerToMove that is going to drop the wall
	 * @param Wall   wall that the player intends to drop
	 * @throws UnsupportedOperationException
	 * @author Xinyue Chen, 260830761
	 * @throws InvalidInputException
	 */
	public static void dropWall(Player playerToMove, Wall wall)
			throws UnsupportedOperationException, InvalidInputException {
		
		Quoridor quoridor=QuoridorApplication.getQuoridor();
		Game game=quoridor.getCurrentGame();

		Player whitePlayer = game.getWhitePlayer();
		Player blackPlayer = game.getBlackPlayer();
		
		playerToMove = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		
		if (whitePlayer == null) {
			throw new InvalidInputException("White player doesn't exist");
		}
		if (blackPlayer == null) {
			throw new InvalidInputException("Black player doesn't exist");
		}
		
		wall = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
		
		if(wall==null) {
			throw new InvalidInputException("Wall doesn't exist");
		}
		
		if (playerToMove == whitePlayer) {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
		}
		if (playerToMove == blackPlayer) {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
		}

	}

	/**
	 * This method enables the user to overwrite an existing file
	 * 
	 * @param newFileName String representing the name to be given to the text file
	 *                    being saved.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */
	public static Boolean overWriteSavePosition(String newFileName, Boolean overWrite)
			throws UnsupportedOperationException {

		if (overWrite) {
			return saveCurrentPosition(newFileName);
		}

		return false;
	}

	/**
	 * This method attempts to save the current game as a text file.
	 * 
	 * @param newFileName String representing the name to be given to the text file
	 *                    being saved.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */
	public static Boolean attemptToSavePosition(String newFileName) throws UnsupportedOperationException {
		Boolean fileExists = fileAlreadyExists(newFileName);

		if (fileExists) {
			return false;
		}

		return saveCurrentPosition(newFileName);
	}

	/**
	 * This method saves the current game as a text file.
	 * 
	 * @param newFileName String representing the name to be given to the text file
	 *                    being saved.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */
	public static Boolean saveCurrentPosition(String newFileName) throws UnsupportedOperationException {

		try {

			FileWriter write = new FileWriter(newFileName, false);
			PrintWriter printW = new PrintWriter(write);

			Quoridor quoridor = QuoridorApplication.getQuoridor();
			Game curGame = quoridor.getCurrentGame();
			GamePosition curGamePos = curGame.getCurrentPosition();
			// new Bool to keep track of which player's turn it is, for text file.
			Boolean whitePlayersTurn = false;
			if (curGamePos.getPlayerToMove() == curGame.getWhitePlayer()) {
				whitePlayersTurn = true;
			}

			PlayerPosition whitePlayerPos = curGamePos.getWhitePosition();
			PlayerPosition blackPlayerPos = curGamePos.getBlackPosition();
			Tile whiteTile = whitePlayerPos.getTile();
			Tile blackTile = blackPlayerPos.getTile();

			int whiteCol = whiteTile.getColumn();
			int blackCol = blackTile.getColumn();
			char whiteChar = (char) (whiteCol + 96);
			char blackChar = (char) (blackCol + 96);
			String whiteTileStr = Character.toString(whiteChar) + whiteTile.getRow();
			String blackTileStr = Character.toString(blackChar) + blackTile.getRow();

			String whiteString = "W: " + whiteTileStr;
			String blackString = "B: " + blackTileStr;

			List<Wall> whiteWalls = curGamePos.getWhiteWallsOnBoard();
			List<Wall> blackWalls = curGamePos.getBlackWallsOnBoard();

			WallMove curWallMove = null;
			String moveString = "";
			Tile tempTile = null;
			int tempCol = 1;
			int tempRow = 1;
			char tempChar = 'a';

			// Loop through white walls first
			for (Wall curWall : whiteWalls) {
				whiteString = whiteString + ", ";
				moveString = "";
				curWallMove = curWall.getMove();
				tempTile = curWallMove.getTargetTile();
				tempCol = tempTile.getColumn();
				tempRow = tempTile.getRow();
				tempChar = (char) (tempCol + 96);
				moveString = Character.toString(tempChar) + tempRow;
				if (curWallMove.getWallDirection() == Direction.Horizontal) {
					moveString = moveString + "h";
				} else {
					moveString = moveString + "v";
				}
				whiteString = whiteString + moveString;
			}

			// Loop through black walls second
			for (Wall curWall : blackWalls) {
				blackString = blackString + ", ";
				moveString = "";
				curWallMove = curWall.getMove();
				tempTile = curWallMove.getTargetTile();
				tempCol = tempTile.getColumn();
				tempRow = tempTile.getRow();
				tempChar = (char) (tempCol + 96);
				moveString = Character.toString(tempChar) + tempRow;
				if (curWallMove.getWallDirection() == Direction.Horizontal) {
					moveString = moveString + "h";
				} else {
					moveString = moveString + "v";
				}
				blackString = blackString + moveString;
			}

			// At this point, both lines for the text file should exist.

			if (whitePlayersTurn) {
				printW.println(whiteString);
				printW.print(blackString);
			} else {
				printW.println(blackString);
				printW.print(whiteString);
			}

			write.close();
			printW.close();
			return true;
		} catch (IOException e) {
			return false;// CHANGE LATER!
		}
	}

	/*
	 * This code will be re-used for Save GAME! Quoridor quoridor =
	 * QuoridorApplication.getQuoridor(); Game curGame = quoridor.getCurrentGame();
	 * GamePosition curGamePos = curGame.getCurrentPosition(); //new Bool to keep
	 * track of which player's turn it is, for text file. Boolean whitePlayersTurn =
	 * false; Player whitePlayer = curGame.getWhitePlayer(); if
	 * (curGamePos.getPlayerToMove() == curGame.getWhitePlayer()) { whitePlayersTurn
	 * = true; } Tile tempTile = null; Direction tempDir = null; String temp = "";
	 * //String wallTemp = ""; int tempCol = 0; char tempChar; String whiteString =
	 * "W: "; String blackString = "B: "; //The following assumes the moves are
	 * added in order! List<Move> allMoves = curGame.getMoves(); for (Move curMove :
	 * allMoves) {
	 * 
	 * tempTile = curMove.getTargetTile(); tempCol = tempTile.getColumn(); tempChar
	 * = (char)(tempCol + 96); temp = ", " + Character.toString(tempChar) +
	 * tempTile.getRow();
	 * 
	 * if (curMove instanceof WallMove) { tempDir =
	 * ((WallMove)curMove).getWallDirection(); if (tempDir == Direction.Horizontal)
	 * { temp = temp + "h"; } else { temp = temp + "v"; } }
	 * 
	 * if (curMove.getPlayer() == whitePlayer) { whiteString = whiteString + ", " +
	 * temp; } else { blackString = blackString + ", " + temp; } temp = ""; }
	 * 
	 * 
	 */

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
	 * This is a helper method that gives the difference in milliseconds between
	 * when a file was last modified, and the current time
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
	 * @param fileName String representing the name of the file that you wish to
	 *                 import.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */

	public static boolean loadSavedPosition(String fileName) throws UnsupportedOperationException {

		BufferedReader reader;
		try {

			reader = new BufferedReader(new FileReader(fileName));
			String fileLine = reader.readLine();
			Boolean whitePlayersTurn = false;
			String whitePlayerStr = "";
			String blackPlayerStr = "";
			// First, check if first line is white or black
			// Sort their line into the correct string variable
			if (fileLine.substring(0, 1).equals("W")) {
				whitePlayersTurn = true;
				whitePlayerStr = fileLine;
				fileLine = reader.readLine();
				blackPlayerStr = fileLine;
			} else {
				blackPlayerStr = fileLine;
				fileLine = reader.readLine();
				whitePlayerStr = fileLine;
			}
			reader.close();
			Quoridor tempQ = new Quoridor();
			// Now, cut the front part, and find the tile each player is on.
			whitePlayerStr = whitePlayerStr.substring(3);
			blackPlayerStr = blackPlayerStr.substring(3);

			String whitePlayerTile = whitePlayerStr.substring(0, 2);
			String blackPlayerTile = blackPlayerStr.substring(0, 2);
			int whitePawnCol = ((int) whitePlayerTile.charAt(0)) - 96;
			int whitePawnRow = Integer.parseInt(whitePlayerTile.substring(1, 2));
			int blackPawnCol = ((int) blackPlayerTile.charAt(0)) - 96;
			int blackPawnRow = Integer.parseInt(blackPlayerTile.substring(1, 2));
			
			
			Boolean statusOfPosition = true;
			if (!validatePawnPosition(whitePawnRow, whitePawnCol, tempQ)) {
				statusOfPosition = false;
				throw new UnsupportedOperationException(" * Invalid position being loaded...");
			}
			if (!validatePawnPosition(blackPawnRow, blackPawnCol, tempQ)) {
				statusOfPosition = false;
				throw new UnsupportedOperationException(" * Invalid position being loaded...");
			}
			if (blackPawnCol == whitePawnCol && blackPawnRow == whitePawnRow) {
				statusOfPosition = false;
				throw new UnsupportedOperationException(" * Invalid position being loaded...");
			}
			

			// We need to create a new "fake" game, add walls one by one, and validate
			// position each time.
			// If the validation fails, then nothing really happens.
			// If it IS valid, then replace the current game with the game we just created.
			// Create users
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			User user1 = quoridor.addUser("tmpUser1");
			User user2 = quoridor.addUser("tmpUser2");
			Player player1 = new Player(new Time(180), user1, 9, Direction.Horizontal);
			Player player2 = new Player(new Time(180), user2, 1, Direction.Horizontal);
			List<Player> players = new ArrayList<Player>();
			players.add(player1);
			players.add(player2);

			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 10; j++) {
					new Wall(i * 10 + j, players.get(i));
				}
			}
			
			Board board = new Board(tempQ);
			// Creating tiles by rows, i.e., the column index changes with every tile
			// creation
			for (int i = 1; i <= 9; i++) { // rows
				for (int j = 1; j <= 9; j++) { // columns
					board.addTile(i, j);
				}
			}

			Tile player1StartPos = tempQ.getBoard().getTile(36);
			Tile player2StartPos = tempQ.getBoard().getTile(44);

			// We want to set the players at the actual tiles they are on:
			List<Tile> tempTileList = board.getTiles();
			for (Tile curTile : tempTileList) {
				if (curTile.getColumn() == whitePawnCol && curTile.getRow() == whitePawnRow) {
					player1StartPos = curTile;
				}
				if (curTile.getColumn() == blackPawnCol && curTile.getRow() == blackPawnRow) {
					player2StartPos = curTile;
				}
			}

			Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, tempQ);
			game.setWhitePlayer(player1);
			game.setBlackPlayer(player2);

			PlayerPosition player1Position = new PlayerPosition(player1, player1StartPos);
			PlayerPosition player2Position = new PlayerPosition(player2, player2StartPos);

			Player startingMovePlayer = player1;
			if (!whitePlayersTurn) {
				startingMovePlayer = player2;
			}

			GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, startingMovePlayer, game);

			int whiteWallsPlaced = 0;
			int blackWallsPlaced = 0;
			// Wall stock and placement on board.
			// First, get 2 lists for all walls.
			String[] whiteWalls = null;
			String[] blackWalls = null;
			
			if (whitePlayerStr.length() > 2) {
				whiteWalls = whitePlayerStr.substring(3).replace(" ", "").split(",");
				whiteWallsPlaced = whiteWalls.length;
			}
			if (blackPlayerStr.length() > 2) {
				blackWalls = blackPlayerStr.substring(3).replace(" ", "").split(",");
				blackWallsPlaced = blackWalls.length;
			}
			
			// Add the walls as in stock for the players
			for (int j = 0; j < 10 - whiteWallsPlaced; j++) {
				Wall wall = Wall.getWithId(j);
				gamePosition.addWhiteWallsInStock(wall);
			}
			for (int j = 0; j < 10 - blackWallsPlaced; j++) {
				Wall wall = Wall.getWithId(j + 10);
				gamePosition.addBlackWallsInStock(wall);
			}
			// Create move number counter
			int moveCounter = 1;
			int roundNumCounter = 1;
			Direction tempDir;
			String tempStr = "";
			String tmpRow = "";
			String dirStr = "";
			int col = 1;
			int row = 1;
			Tile tempTile = null;
			Boolean positionValidated = true;
			Boolean overlapPositionValidated = true;

			// Maybe change value inside getWalls(), so we grab from end of pile??
			for (int j = 0; j < whiteWallsPlaced; j++) {
				Wall wall = Wall.getWithId(9 - j);
				tempStr = whiteWalls[j];
				tmpRow = tempStr.substring(1, 2);
				dirStr = tempStr.substring(2, 3);
				col = ((int) tempStr.charAt(0)) - 96;
				row = Integer.parseInt(tmpRow);
				if (dirStr.equals("h")) {
					tempDir = Direction.Horizontal;
				} else {
					tempDir = Direction.Vertical;
				}
				//If j is 0, calling validate position throws nullpointerexception since list of walls is empty. So at 0, only check if it is on board, not if it hits other walls.
				if (j > 0) {
					positionValidated = validatePosition(row, col, tempDir, tempQ);
					overlapPositionValidated = validateWallBoundaryPosition(row, col, tempDir, tempQ);
				} else {
					positionValidated = validateWallBoundaryPosition(row, col, tempDir, tempQ);
				}
				
				if (!(positionValidated && overlapPositionValidated)) {
					statusOfPosition = false;
					throw new UnsupportedOperationException(" * Invalid position being loaded...");
				}

				for (Tile curTile : tempTileList) {
					if (curTile.getColumn() == col && curTile.getRow() == row) {
						tempTile = curTile;
					}
				}

				game.addMove(new WallMove(moveCounter, roundNumCounter, player1, tempTile, game, tempDir, wall));
				gamePosition.addWhiteWallsOnBoard(wall);
				moveCounter = moveCounter + 2;
				roundNumCounter++;
			}

			// Reset this stuff for black player
			moveCounter = 2;
			roundNumCounter = 1;
			for (int j = 0; j < blackWallsPlaced; j++) {
				Wall wall = Wall.getWithId(19 - j);
				tempStr = blackWalls[j];
				tmpRow = tempStr.substring(1, 2);
				dirStr = tempStr.substring(2, 3);
				col = ((int) tempStr.charAt(0)) - 96;
				row = Integer.parseInt(tmpRow);
				if (dirStr.equals("h")) {
					tempDir = Direction.Horizontal;
				} else {
					tempDir = Direction.Vertical;
				}

				if (j > 0) {
					positionValidated = validatePosition(row, col, tempDir, tempQ);
					overlapPositionValidated = validateWallBoundaryPosition(row, col, tempDir, tempQ);
				} else {
					positionValidated = validateWallBoundaryPosition(row, col, tempDir, tempQ);
				}
				
				if (!(positionValidated && overlapPositionValidated)) {
					statusOfPosition = false;
					throw new UnsupportedOperationException(" * Invalid position being loaded...");
					
				}

				for (Tile curTile : tempTileList) {
					if (curTile.getColumn() == col && curTile.getRow() == row) {
						tempTile = curTile;
					}
				}
				game.addMove(new WallMove(moveCounter, roundNumCounter, player2, tempTile, game, tempDir, wall));
				gamePosition.addBlackWallsOnBoard(wall);
				moveCounter = moveCounter + 2;
				roundNumCounter++;
			}

			game.setCurrentPosition(gamePosition);

			// Now check to see if the position is valid booleans are still true. If yes, go
			// into REAL quoridor object and set currentGame. If not, return false?
			if (statusOfPosition) {
				quoridor.setCurrentGame(game);
				quoridor.setBoard(board);
			} else {
				throw new UnsupportedOperationException(" * Invalid position being loaded...");
			}
			
			return (statusOfPosition);

		} catch (IOException e) {
			throw new UnsupportedOperationException(" * Invalid position being loaded...");
		}

	}

	/**
	 * 11. Validate Position
	 * 
	 * This method (along with the methods it calls) validates a potential pawn or
	 * wall move position.
	 * 
	 * @param row       row of the move position
	 * @param col       col of the move position
	 * @param direction direction of the move position -- null if pawn,
	 *                  horizontal/vertical if wall
	 * @throws UnsupportedOperationException
	 * @return boolean -- true if valid position, false if invalid position
	 * @author Sami Junior Kahil, 260834568
	 */
	public static boolean validatePosition(int row, int col, Direction direction) throws UnsupportedOperationException {
		boolean position;
		if (direction == null) {
			return position = validatePawnPosition(row, col);
			// return validatePawnPosition(row, col);
		} else {
			return validateWallBoundaryPosition(row, col, direction)
					&& validateWallOverlapPosition(row, col, direction);
		}
	}

	private static boolean validatePawnPosition(int row, int col) throws UnsupportedOperationException {
		if (row <= 0 || row >= 10 || col <= 0 || col >= 10) {
			throw new UnsupportedOperationException(" * Invalid position for pawn...");
		}
		return true;
	}

	private static boolean validateWallBoundaryPosition(int row, int col, Direction direction)
			throws UnsupportedOperationException {
		if (row == 9 || col == 9)
			throw new UnsupportedOperationException(" * Out of bounds position for wall...");
		else
			return true;
	}

	private static boolean validateWallOverlapPosition(int row, int col, Direction direction)
			throws UnsupportedOperationException {
		List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getBlackWallsOnBoard();
		List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getWhiteWallsOnBoard();

		List<Wall> allWalls = new ArrayList<Wall>();
		allWalls.addAll(blackWalls);
		allWalls.addAll(whiteWalls);

		for (int i = 0; i <= allWalls.size(); i++) {
			int currRow = allWalls.get(i).getMove().getTargetTile().getRow();
			int currCol = allWalls.get(i).getMove().getTargetTile().getColumn();
			Direction currDir = allWalls.get(i).getMove().getWallDirection();

			if (currRow == row && currCol == col) {
				throw new UnsupportedOperationException(" * Wall already exists in this position...");
			}

			if (direction == Direction.Vertical) {
				if ((currRow == row - 1 || currRow == row + 1) && currCol == col && currDir == Direction.Vertical) {
					throw new UnsupportedOperationException(" * Wall overlaps with another wall...");
				}
			}

			if (direction == Direction.Horizontal) {
				if (currRow == row && (currCol == col - 1 || currCol == col + 1) && currDir == Direction.Horizontal) {
					throw new UnsupportedOperationException(" * Wall overlaps with another wall...");
				}
			}
		}

		return true;
	}

	/**
	 * 11B. Validate Position
	 * 
	 * This method (along with the methods it calls) validates a potential pawn or
	 * wall move position.
	 * 
	 * @param row       row of the move position
	 * @param col       col of the move position
	 * @param direction direction of the move position -- null if pawn,
	 *                  horizontal/vertical if wall
	 * @param quoridor  quoridor object that the game exists in
	 * @throws UnsupportedOperationException
	 * @return boolean -- true if valid position, false if invalid position
	 * @author Sami Junior Kahil, 260834568/Shayne Leitman, 260688512
	 */
	public static boolean validatePosition(int row, int col, Direction direction, Quoridor quoridor) throws UnsupportedOperationException {
		boolean position;
		if (direction == null) {
			return position = validatePawnPosition(row, col, quoridor);
			// return validatePawnPosition(row, col);
		} else {
			return validateWallBoundaryPosition(row, col, direction, quoridor)
					&& validateWallOverlapPosition(row, col, direction, quoridor);
		}
	}

	
	private static boolean validatePawnPosition(int row, int col, Quoridor quoridor) throws UnsupportedOperationException {
		if (row <= 0 || row >= 10 || col <= 0 || col >= 10) {
			return false;
		}
		return true;
	}
	

	private static boolean validateWallBoundaryPosition(int row, int col, Direction direction, Quoridor quoridor)
			throws UnsupportedOperationException {
		if (row >= 9 || row <= 0 || col <= 0 || col >= 9)
			return false;
		else
			return true;
	}

	private static boolean validateWallOverlapPosition(int row, int col, Direction direction, Quoridor quoridor)
			throws UnsupportedOperationException {
		List<Wall> blackWalls = quoridor.getCurrentGame().getCurrentPosition()
				.getBlackWallsOnBoard();
		List<Wall> whiteWalls = quoridor.getCurrentGame().getCurrentPosition()
				.getWhiteWallsOnBoard();

		List<Wall> allWalls = new ArrayList<Wall>();
		allWalls.addAll(blackWalls);
		allWalls.addAll(whiteWalls);

		for (int i = 0; i <= allWalls.size(); i++) {
			int currRow = allWalls.get(i).getMove().getTargetTile().getRow();
			int currCol = allWalls.get(i).getMove().getTargetTile().getColumn();
			Direction currDir = allWalls.get(i).getMove().getWallDirection();

			if (currRow == row && currCol == col) {
				return false;
			}

			if (direction == Direction.Vertical) {
				if ((currRow == row - 1 || currRow == row + 1) && currCol == col && currDir == Direction.Vertical) {
					return false;
				}
			}

			if (direction == Direction.Horizontal) {
				if (currRow == row && (currCol == col - 1 || currCol == col + 1) && currDir == Direction.Horizontal) {
					return false;
				}
			}
		}

		return true;
	}
	
	/**
	 * 12. Switch Current Player
	 * 
	 * This method switches the current player and updates each player's timer.
	 * 
	 * @throws UnsupportedOperationException
	 * @return true if successful, false otherwise
	 * @author Sami Junior Kahil, 260834568
	 */
	public static void switchCurrentPlayer() throws UnsupportedOperationException {
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player otherPlayer = player.getNextPlayer();

		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(otherPlayer);
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().setNextPlayer(player);
	}

	// helper methods

	/**
	 * Helper method to convert time provided by user to Time.
	 * 
	 * @param min number of min
	 * @param sec number of seconds
	 * @return Time time
	 * @author Helen Lin 260715521
	 */
	public static Time getIntToTime(int min, int sec) {
		int ms = (min * 60 + sec) * 1000;
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

	/**
	 * Helper method to get the row number for the current position of a pawn
	 * 
	 * @author Helen
	 * @param forBlackPawn True to get row position of black pawn, false for white
	 * @return int value of row position
	 */
	public static int getCurrentRowForPawn(boolean forBlackPawn) {
		int row = -1;
		try {
			row = forBlackPawn
					? QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition()
							.getTile().getRow()
					: QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition()
							.getTile().getRow();

		} catch (RuntimeException e) {
			// do nothing, the game has not started yet
		}

		return row;
	}

	/**
	 * Helper method to get the column number for the current position of a pawn
	 * 
	 * @author Helen
	 * @param forBlackPawn True to get row position of black pawn, false for white
	 * @return int value of row position
	 */
	public static int getCurrentColForPawn(boolean forBlackPawn) {
		int col = -1;
		try {
			col = forBlackPawn
					? QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition()
							.getTile().getRow()
					: QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition()
							.getTile().getRow();

		} catch (RuntimeException e) {
			// do nothing, the game has not started yet
		}

		return col;
	}

	/**
	 * Helper method to get the associated PLAYER for a given username
	 * 
	 * @author Helen
	 * @param forBlackPawn True to get row position of black pawn, false for white
	 * @return int value of row position
	 */
	public static User getUserByUsername(String username) {

		if (QuoridorApplication.getQuoridor().hasUsers()) {
			for (User user : QuoridorApplication.getQuoridor().getUsers()) {
				if (user.getName().equals(username)) {
					return user;
				}
			}
		}

		return null; // return null if no user found

	}

	/**
	 * Helper method to return a string in format "MM:SS" for a given integer number
	 * of seconds
	 * 
	 * @author Helen
	 * @param seconds
	 * @return String in "MM:SS"
	 */
	public static String getDisplayTimeString(int seconds) {
		int min = seconds / 60;
		int sec = seconds % 60;
		String display = (min < 10) ? ("0" + min + ((sec < 10) ? (":0" + sec) : (":" + sec)))
				: (min + ((sec < 10) ? (":0" + sec) : (":" + sec)));
		return display;
	}

	/**
	 * Helper method for view to get the remaining time for a white or black player
	 * 
	 * @author Helen
	 * @param forBlackPawn True to get time of black pawn, false for white
	 * @return Time remainingTime of the player
	 */
	public static Time getTimeForPlayer(boolean forBlackPlayer) {
		Time remainingTime = null;
		try {
			remainingTime = forBlackPlayer
					? QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime()
					: QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime();

		} catch (RuntimeException e) {
			// do nothing, the game has not started yet
		}

		return remainingTime; // return null if no time found

	}

	/**
	 * Helper method to set timer for each player and keeps track of the timers.
	 * @author Sami Junior Kahil, 260834568
	 */
	private static void setTimers() {
		Timer whitePlayerTimer = new Timer();
		Timer blackPlayerTimer = new Timer();
		Timer checker = new Timer();

		Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();

		TimerTask ttWhitePlayer = new TimerTask() {
			@Override  
			public void run() {
				int remainingTime = getTimeToSeconds( whitePlayer.getRemainingTime() );
				for (int i = 0; i <= remainingTime; i++) {
					whitePlayer.setRemainingTime( getIntToTime(0, remainingTime - i) );
				}
			};
		}; 

		whitePlayerTimer.scheduleAtFixedRate(ttWhitePlayer,0,1000);
		try {
			whitePlayerTimer.wait();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		TimerTask ttBlackPlayer = new TimerTask() {
			@Override  
			public void run() {
				int remainingTime = getTimeToSeconds( blackPlayer.getRemainingTime() );
				for (int i = 0; i <= remainingTime; i++) {
					blackPlayer.setRemainingTime( getIntToTime(0, remainingTime - i) );
				}
			};
		}; 

		blackPlayerTimer.scheduleAtFixedRate(ttBlackPlayer,0,1000);
		try {
			blackPlayerTimer.wait();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		TimerTask check = new TimerTask() {
			@Override  
			public void run() {
				GameStatus currentStatus = QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus();
				Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();

				while (currentStatus == GameStatus.Running) {
					if (currentPlayer == QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()) {
						whitePlayerTimer.notify();
						try {
							blackPlayerTimer.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else {
						blackPlayerTimer.notify();
						try {
							whitePlayerTimer.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}; 

		checker.scheduleAtFixedRate(check, 0, 100);
	}

}

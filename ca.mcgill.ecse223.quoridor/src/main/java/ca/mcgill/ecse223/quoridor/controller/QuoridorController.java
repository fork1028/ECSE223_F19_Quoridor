
package ca.mcgill.ecse223.quoridor.controller;


import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
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
	public static void grabWall(Player player, WallMove move) throws UnsupportedOperationException {

	 
		 /*ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				  if(grabButton.getModel().isPressed())  
				        
			}};*/
		 List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock();
		 List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock();
		 Direction startDir = Direction.Horizontal;
		 
		  Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		  Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		  int wallIdxForPlayer = 1;  
	 
		  if(player == whitePlayer) {
			Tile whiteStart = QuoridorApplication.getQuoridor().getBoard().getTile(36);
			
			 Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock(wallIdxForPlayer);
			 WallMove nextmove = new WallMove(0, 0, player, whiteStart, null, startDir, wall);
			 QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(nextmove);
			 if (!whiteWalls.isEmpty()) {
				 QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
			 }
			
			 }
			
		
		if(player == blackPlayer) {
			Tile blackStart = QuoridorApplication.getQuoridor().getBoard().getTile(44);
			Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock(wallIdxForPlayer);
			 WallMove nextmove = new WallMove(0, 0, player, blackStart, null, startDir, wall);
			 QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(nextmove);
			

			 if (!blackWalls.isEmpty()) {
				 QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);;
			 }
		
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

	public static void rotateWall  (Wall wall, WallMove move, Direction dir) throws UnsupportedOperationException {
		
		
		/*ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				  if(rotateButton.getModel().isPressed())  
				        
			}};*/
			
		if(dir == Direction.Horizontal) {
		 Direction newDir = Direction.Vertical;
		 QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(newDir);
		// dir = newDir;
		 //g2d.translate(wall.x+(wall.width/2), wall.y+(wall.height/2));
		// g2d.rotate(Math.toRadians(90));
	}
	if(dir == Direction.Vertical) {
		 Direction newDir = Direction.Horizontal;
		// dir = newDir ;
		 QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(newDir);
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
	 * This method enables the user to overwrite an existing file
	 * 
	 * @param newFileName String representing the name to be given to the text file
	 *                    being saved.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */
	public static Boolean overWriteSavePosition(String newFileName, Boolean overWrite) throws UnsupportedOperationException  {
		
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
	public static Boolean attemptToSavePosition(String newFileName) throws UnsupportedOperationException  {
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
		
		//throw new UnsupportedOperationException("Controller feature not fully implemented yet!");\

		
		try {
			
			FileWriter write = new FileWriter(newFileName, false);
			PrintWriter printW = new PrintWriter(write);
			
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			Game curGame = quoridor.getCurrentGame();
			GamePosition curGamePos = curGame.getCurrentPosition();
			//new Bool to keep track of which player's turn it is, for text file.
			Boolean whitePlayersTurn = false;
			Player whitePlayer = curGame.getWhitePlayer();
			if (curGamePos.getPlayerToMove() == curGame.getWhitePlayer()) {
				whitePlayersTurn = true;
			}
			
			String temp = "";
			PlayerPosition whitePlayerPos = curGamePos.getWhitePosition();
			PlayerPosition blackPlayerPos = curGamePos.getBlackPosition();
			Tile whiteTile = whitePlayerPos.getTile();
			Tile blackTile = blackPlayerPos.getTile();
			
			int whiteCol = whiteTile.getColumn();
			int blackCol = blackTile.getColumn();
			char whiteChar = (char)(whiteCol + 96);
			char blackChar = (char)(blackCol + 96);
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
			
			//Loop through white walls first
			for (Wall curWall : whiteWalls) {
				whiteString = whiteString + ", ";
				moveString = "";
				curWallMove = curWall.getMove();
				tempTile = curWallMove.getTargetTile();
				tempCol = tempTile.getColumn();
				tempRow = tempTile.getRow();
				tempChar = (char)(tempCol + 96);
				moveString = Character.toString(tempChar) + tempRow;
				if (curWallMove.getWallDirection() == Direction.Horizontal) {
					moveString = moveString + "h";
				} else {
					moveString = moveString + "v";
				}
				whiteString = whiteString + moveString;
			}
			
			//Loop through black walls second
			for (Wall curWall : blackWalls) {
				blackString = blackString + ", ";
				moveString = "";
				curWallMove = curWall.getMove();
				tempTile = curWallMove.getTargetTile();
				tempCol = tempTile.getColumn();
				tempRow = tempTile.getRow();
				tempChar = (char)(tempCol + 96);
				moveString = Character.toString(tempChar) + tempRow;
				if (curWallMove.getWallDirection() == Direction.Horizontal) {
					moveString = moveString + "h";
				} else {
					moveString = moveString + "v";
				}
				blackString = blackString + moveString;
			}

			//At this point, both lines for the text file should exist.
			
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
			return false;//CHANGE LATER!
		}	
	}
	
	/*  This code will be re-used for Save GAME!
	 	Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game curGame = quoridor.getCurrentGame();
		GamePosition curGamePos = curGame.getCurrentPosition();
		//new Bool to keep track of which player's turn it is, for text file.
		Boolean whitePlayersTurn = false;
		Player whitePlayer = curGame.getWhitePlayer();
		if (curGamePos.getPlayerToMove() == curGame.getWhitePlayer()) {
			whitePlayersTurn = true;
		}
		Tile tempTile = null;
		Direction tempDir = null;
		String temp = "";
		//String wallTemp = "";
		int tempCol = 0;
		char tempChar;
		String whiteString = "W: ";
		String blackString = "B: ";
		//The following assumes the moves are added in order!
		List<Move> allMoves = curGame.getMoves();
		for (Move curMove : allMoves) {
			
			tempTile = curMove.getTargetTile();
			tempCol = tempTile.getColumn();
			tempChar = (char)(tempCol + 96);
			temp = ", " + Character.toString(tempChar) + tempTile.getRow();
			
			if (curMove instanceof WallMove) {
				tempDir = ((WallMove)curMove).getWallDirection();
				if (tempDir == Direction.Horizontal) {
					temp = temp + "h";
				} else {
					temp = temp + "v";
				}
			}
			
			if (curMove.getPlayer() == whitePlayer) {
				whiteString = whiteString + ", " + temp;
			} else {
				blackString = blackString + ", " + temp;
			}
			temp = "";
		}
	 
	 
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
	 * @param fileName String representing the name of the file that you wish to
	 *                 import.
	 * @throws UnsupportedOperationException
	 * @author Shayne Leitman, 260688512
	 */
	
	public static void loadSavedPosition(String fileName) throws UnsupportedOperationException {

		BufferedReader reader;
		try {
			
			reader = new BufferedReader(new FileReader(fileName));
			String fileLine = reader.readLine();
			Boolean whitePlayersTurn = false;
			String whitePlayerStr = "";
			String blackPlayerStr = "";
			//First, check if first line is white or black
			if (fileLine.substring(0, 1) == "W") {
				whitePlayersTurn = true;
				whitePlayerStr = fileLine;
				fileLine = reader.readLine();
				blackPlayerStr = fileLine;
			} else {
				blackPlayerStr = fileLine;
				fileLine = reader.readLine();
				whitePlayerStr = fileLine;
			}
			
			whitePlayerStr = whitePlayerStr.substring(3);
			blackPlayerStr = blackPlayerStr.substring(3);
			
			String whitePlayerTile = whitePlayerStr.substring(0, 2);
			String blackPlayerTile = blackPlayerStr.substring(0, 2);
			int whitePawnCol = ((int) whitePlayerTile.charAt(0)) - 96;
			int whitePawnRow = Integer.parseInt(whitePlayerTile.substring(1, 2));
			int blackPawnCol = ((int) blackPlayerTile.charAt(0)) - 96;
			int blackPawnRow = Integer.parseInt(blackPlayerTile.substring(1, 2));
			
			//We need to create a new "fake" game, add walls one by one, and validate position each time.
			//If the validation fails, then nothing really happens.
			//If it IS valid, then replace the current game with the game we just created.
			
			Quoridor tempQ = new Quoridor();
			Board board = new Board(tempQ);
			// Creating tiles by rows, i.e., the column index changes with every tile
			// creation
			for (int i = 1; i <= 9; i++) { // rows
				for (int j = 1; j <= 9; j++) { // columns
					board.addTile(i, j);
				}
			}
			//Copied
			Tile player1StartPos = tempQ.getBoard().getTile(36);
			Tile player2StartPos = tempQ.getBoard().getTile(44);
			
			//We want to set the players at the actual tiles they are on:
			List<Tile> tempTileList = board.getTiles();
			for (Tile curTile : tempTileList) {
				if (curTile.getColumn() == whitePawnCol && curTile.getRow() == whitePawnRow) {
					player1StartPos = curTile;
				}
				if (curTile.getColumn() == blackPawnCol && curTile.getRow() == blackPawnRow) {
					player2StartPos = curTile;
				}
			}
			
			User user1 = tempQ.addUser("tmpUser1");
			User user2 = tempQ.addUser("tmpUser2");
			Player player1 = new Player(new Time(180), user1, 9, Direction.Horizontal);
			Player player2 = new Player(new Time(180), user2, 1, Direction.Horizontal);
			
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
			
			//Wall stock and placement on board.
			//First, get 2 lists for all walls.
			String[] whiteWalls = whitePlayerStr.substring(4).replace(" ", "").split(",");
			String[] blackWalls = blackPlayerStr.substring(4).replace(" ", "").split(",");
			//Next, get size, so we know how many walls to place for each player.
			int whiteWallsPlaced = whiteWalls.length;
			int blackWallsPlaced = blackWalls.length;
			// Add the walls as in stock for the players
			for (int j = 0; j < 10 - whiteWallsPlaced; j++) {
				Wall wall = Wall.getWithId(j);
				gamePosition.addWhiteWallsInStock(wall);
			}
			for (int j = 0; j < 10 - blackWallsPlaced; j++) {
				Wall wall = Wall.getWithId(j + 10);
				gamePosition.addBlackWallsInStock(wall);
			}
			//Create move number counter
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
			
			//Maybe change value inside getWalls(), so we grab from end of pile??
			for (int j = 0; j < whiteWallsPlaced; j++) {
				Wall wall = Wall.getWithId(9 - j);
				tempStr = whiteWalls[j];
				tmpRow = tempStr.substring(1, 2);
				dirStr = tempStr.substring(2, 3);
				col = ((int) tempStr.charAt(0)) - 96;
				row = Integer.parseInt(tmpRow);
				if (dirStr == "h") {
					tempDir = Direction.Horizontal;
				} else {
					tempDir = Direction.Vertical;
				}
				
				positionValidated = validatePosition(row, col, tempDir);
				//overlapPositionValidated = validateWallBoundaryPosition(row, col, tempDir); UNCOMMENT LATER!
				
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
			
			//Reset this stuff for black player
			moveCounter = 2;
			roundNumCounter = 1;
			for (int j = 0; j < blackWallsPlaced; j++) {
				Wall wall = Wall.getWithId(19 - j);
				tempStr = whiteWalls[j];
				tmpRow = tempStr.substring(1, 2);
				dirStr = tempStr.substring(2, 3);
				col = ((int) tempStr.charAt(0)) - 96;
				row = Integer.parseInt(tmpRow);
				if (dirStr == "h") {
					tempDir = Direction.Horizontal;
				} else {
					tempDir = Direction.Vertical;
				}
				
				positionValidated = validatePosition(row, col, tempDir);
				//overlapPositionValidated = validateWallBoundaryPosition(row, col, tempDir); UNCOMMENT LATER!
				
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
			
			
			//Now check to see if the position is valid booleans are still true. If yes, go into REAL quoridor object and set currentGame. If not, return false?
			if (positionValidated && overlapPositionValidated) {
			 	Quoridor quoridor = QuoridorApplication.getQuoridor();
				quoridor.setCurrentGame(game);
			}
			
			
			
			
			reader.close();
			
		} catch (IOException e) {
			
		}
		
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

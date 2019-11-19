
package ca.mcgill.ecse223.quoridor.controller;

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
import java.util.List;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.MoveDirection;

/**
 * @author user
 *
 */
public class QuoridorController {
	private static boolean boardWasInitiated = false;

	private static boolean nextPlayerToSetUsername = false;

	/**
	 * This method initializes a game that is ready to start. The game is
	 * initialized from the view, after all validation.
	 * 
	 * @author Helen Lin 260715521
	 * @param blackUsername
	 * @param whiteUsername
	 * @param min
	 * @param sec
	 * @throws InvalidInputException
	 */
	public static void createReadyGame(String blackUsername, String whiteUsername, int min, int sec)
			throws InvalidInputException {
		// assumes users have been created and selected into the appropriate playerlist
		// in UI

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		initializeNewGame();
		setUserToPlayer(blackUsername, true);
		setUserToPlayer(whiteUsername, false);
		setTotalThinkingTime(min, sec);
		quoridor.getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
	}

	/**
	 * This method initializes the board, starts the clocks for the appropriate
	 * players, and also starts the game.
	 * 
	 * @throws InvalidInputException
	 * @author Helen
	 */
	public static void startGameAndClocks() throws InvalidInputException {
		initializeBoard();
		// update status of game
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
		pawnBehaviourSetUp();
	}

	/**
	 * 
	 * This method initializes a new game
	 * 
	 * @throws IllegalArgumemtException
	 * @author matteo barbieri 260805184
	 */
	public static void initializeNewGame() throws InvalidInputException {
		try {
			new Game(GameStatus.Initializing, MoveMode.PlayerMove, QuoridorApplication.getQuoridor());
		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to create initialize new Game");
		}
	}

	/**
	 * This method Provides or Selects a User to be used in a game
	 * 
	 * @param username desired to create new User
	 * @throws InvalidInputException
	 * @author Matteo Barbieri 260805184
	 */
	public static void provideSelectUser(String username) throws InvalidInputException {
		if (getUserByUsername(username) != null) {
			throw new InvalidInputException("User already exists");
		} else {
			// creates user without warning
			createUser(username);
			setUserToPlayer(username, nextPlayerToSetUsername);
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
	public static User createUser(String username) throws InvalidInputException {
		try {
			// check if pre-existing
			User user = getUserByUsername(username);
			if (user != null) {

				return user; // return pre-existing user
			} else // or create new user
				return QuoridorApplication.getQuoridor().addUser(username);
		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to create a user");
		}

	}

	public static void setNextPlayerToSetUsername(boolean forBlack) {
		nextPlayerToSetUsername = forBlack;
		// sets variable to true if its blacks turn and false if its whites turn

	}

	public static boolean getNextPlayerToSetUsername() {
		return nextPlayerToSetUsername;
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
	 * @throws InvalidInputException
	 */
	public static boolean setUserToPlayer(String username, boolean forBlackPlayer) throws InvalidInputException {
		try {
			User user = getUserByUsername(username);
			//NOTE: sets Up/Down (vertical) playing mode on default
			Player player = forBlackPlayer ? new Player(null, user, 1, Direction.Vertical) // black
					: new Player(null, user, 9, Direction.Vertical); // white
			return forBlackPlayer ? QuoridorApplication.getQuoridor().getCurrentGame().setBlackPlayer(player)
					: QuoridorApplication.getQuoridor().getCurrentGame().setWhitePlayer(player);
		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to set user to player");
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
			if (blackPlayer.getUser() != null && whitePlayer.getUser() != null) {
				QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
			}
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	/**
	 * This method initializes a new board only when the game is ready to start, and
	 * user clicks start clock
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
			throw new InvalidInputException("Unable to create board with 81 tiles");
		}

		// set white and black initial position , and set white player to move
		try {
			Player white = quoridor.getCurrentGame().getWhitePlayer();


			// white initial position
			Tile whiteStart = (white.getDestination().getDirection().equals(Direction.Horizontal))
					? quoridor.getBoard().getTile(36)
							: quoridor.getBoard().getTile(4);

					PlayerPosition whitePos = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), whiteStart);

					// black initial position
					Tile blackStart = (white.getDestination().getDirection().equals(Direction.Horizontal))
							? quoridor.getBoard().getTile(41)
									: quoridor.getBoard().getTile(76);
							PlayerPosition blackPos = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), blackStart);

							GamePosition gamePosition = new GamePosition(0, whitePos, blackPos, white, quoridor.getCurrentGame());
							quoridor.getCurrentGame().setCurrentPosition(gamePosition);

		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to add white player as next player to move");
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
							// if wall with id already exists (happens from some games or tests)
							if (Wall.hasWithId(i * 10 + j)) {
								Wall.getWithId(i * 10 + j).setOwner(player);
							} else {
								new Wall(i * 10 + j, player);
							}

						}
					}
				}

				// add walls to stock for each player if needed
				for (int j = 0; j < 10; j++) {
					quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(Wall.getWithId(j));
					quoridor.getCurrentGame().getCurrentPosition().addBlackWallsInStock(Wall.getWithId(j + 10));
				}
			}

			// set controller local var to show board was successfully
			// initiated, necessary because usually the game is started right away after and
			// state changes to running
			boardWasInitiated = true;

		} catch (RuntimeException e) {
			throw new InvalidInputException("Unable to add initial stock for players.");
		}

		// TODO: White clock counting down
		// GUI TODO: clock countdown gui
		// GUI TODO: show that this is white turn
		// start clock for white player

	}

	/**
	 * This method allows a user to grab a wall.
	 * 
	 * @param player that is going to grab the wall
	 * @param move   that is going to grab the wall
	 * @param wall   that is on the stack
	 * @throws UnsupportedOperationException
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
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		int wallIdxForPlayer = 1;
		System.out.println(player);
		if (player == whitePlayer) {
			// Tile whiteStart = QuoridorApplication.getQuoridor().getBoard().getTile(36);
			Tile whiteStart = new Tile(5, 4, QuoridorApplication.getQuoridor().getBoard());
			System.out.println("whiteStockSize:" + QuoridorApplication.getQuoridor().getCurrentGame()
					.getCurrentPosition().getWhiteWallsInStock().size());
			Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
					.getWhiteWallsInStock(wallIdxForPlayer);

			System.out.println(wall);
			WallMove nextmove = new WallMove(0, 0, player, whiteStart, game, startDir, wall);

			QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(nextmove);
			if (!whiteWalls.isEmpty()) {
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
			} else
				throw new UnsupportedOperationException(" There are no more white walls");

		}

		if (player == blackPlayer) {
			// Tile blackStart = QuoridorApplication.getQuoridor().getBoard().getTile(44);
			Tile blackStart = new Tile(5, 4, QuoridorApplication.getQuoridor().getBoard());
			Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
					.getBlackWallsInStock(wallIdxForPlayer);
			WallMove nextmove = new WallMove(0, 0, player, blackStart, game, startDir, wall);
			nextmove.setWallPlaced(wall);
			QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(nextmove);

			if (!blackWalls.isEmpty()) {
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
			} else
				throw new UnsupportedOperationException(" There are no more black walls");

		}

	}

	/**
	 * This method rotates a wall.
	 * 
	 * @param player    that is going to rotate the wall
	 * @param move      that is going to rotate the wall
	 * @param wall      that is on the stack
	 * @param direction that is the orientation of the wall
	 * @throws UnsupportedOperationException
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
		// Wall wall =
		// QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock(wallIdxForPlayer);
		// WallMove nextmove = new WallMove(0, 0, player, whiteStart, null, startDir,
		// wall);
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

		boolean movable = true;
		Board board = QuoridorApplication.getQuoridor().getBoard();
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		System.out.println(candidate);
		// Wall
		// wall=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		if (candidate == null) {
			throw new InvalidInputException("WallMove doesn't exist");
		}
		if (board == null) {
			throw new InvalidInputException("Board doesn't exist");
		}
		int row = candidate.getTargetTile().getRow();
		int col = candidate.getTargetTile().getColumn();
//		System.out.println("row:::" + row);
//		System.out.println("col:::" + col);
		Tile newTile = null;

		if (moveDirection.equals("right")) {
			col = col + 1;
			if (candidate.getWallDirection() == Direction.Horizontal) {
				if (col == 9) {
					throw new java.lang.UnsupportedOperationException("You can't move the wall further.");
				} else {
					newTile = new Tile(row, col, board);
				}
			}

		}
		if (moveDirection.equals("left")) {
			col = col - 1;
			if (candidate.getWallDirection() == Direction.Horizontal) {
				if (col == 0) {
					throw new java.lang.UnsupportedOperationException("You can't move the wall further.");
				} else {
					newTile = new Tile(row, col, board);
				}
			}
		}
		if (moveDirection.equals("up")) {
			row = row - 1;
			if (candidate.getWallDirection() == Direction.Horizontal) {
				if (row == 0) {
					throw new java.lang.UnsupportedOperationException("You can't move the wall further.");
				} else {
					newTile = new Tile(row, col, board);
				}
			}
		}
		if (moveDirection.equals("down")) {
			row = row + 1;
			if (candidate.getWallDirection() == Direction.Horizontal) {
				if (row == 9) {
					throw new java.lang.UnsupportedOperationException("You can't move the wall further.");
				} else {
					newTile = new Tile(row, col, board);
				}
			}
		}

		if (newTile != null) {
			candidate.setTargetTile(newTile);
		} else
			throw new InvalidInputException("New target tile doesn't exist");

//		System.out.println("row:" + row);
//		System.out.println("col" + col);
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

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();

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
		int row = wall.getMove().getTargetTile().getRow();
		int col = wall.getMove().getTargetTile().getColumn();
		Direction dir = wall.getMove().getWallDirection();
		boolean isValid = true;
		List<Wall> whiteWallsOnBoard = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getWhiteWallsOnBoard();
		List<Wall> blackWallsOnBoard = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getBlackWallsOnBoard();
//		System.out.println("testrow:" + row);
//		System.out.println("textcol:" + col);
		for (int i = 0; i < whiteWallsOnBoard.size(); i++) {
			if (row == whiteWallsOnBoard.get(i).getMove().getTargetTile().getRow()) {
				if (col == whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn()
						|| col == whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn() + 1
						|| col == whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn() - 1) {
//					System.out.println("blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn()"
//							+ whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn());
//					System.out.println("blackWallsOnBoard.get(i).getMove().getTargetTile().getRow()"
//							+ whiteWallsOnBoard.get(i).getMove().getTargetTile().getRow());
					isValid = false;
				}
			}

		}
		//System.out.println("isvalid:" + isValid);

		for (int i = 0; i < blackWallsOnBoard.size(); i++) {
			if (row == blackWallsOnBoard.get(i).getMove().getTargetTile().getRow()) {
				if (col == blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn()
						|| col == blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn() + 1
						|| col == blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn() - 1) {
//					System.out.println("blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn()"
//							+ blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn());
//					System.out.println("blackWallsOnBoard.get(i).getMove().getTargetTile().getRow()"
//							+ blackWallsOnBoard.get(i).getMove().getTargetTile().getRow());
					isValid = false;
				}
			}

		}
		//System.out.println("isvalid:" + isValid);
		if (isValid) {

			if (playerToMove == whitePlayer) {
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			}
			if (playerToMove == blackPlayer) {
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			}
		} else {
			throw new java.lang.UnsupportedOperationException("You can't drop the wall here.");
		}

		//System.out.println(playerToMove);

	}

	/**
	 * This method helps link transfer objects with models (black walls)
	 * 
	 * @author Xinyue Chen
	 * @return
	 */
	public static List<TOWall> getBlackWalls() {
		ArrayList<TOWall> walls = new ArrayList<TOWall>();
		for (Wall wall : QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getWalls()) {
			TOWall toWall = new TOWall(wall.getId());
			walls.add(toWall);
		}
		return walls;
	}

	/**
	 * This method helps link transfer objects with models (white walls)
	 * 
	 * @author Xinyue Chen
	 * @return
	 */
	public static List<TOWall> getWhiteWalls() {
		ArrayList<TOWall> walls = new ArrayList<TOWall>();
		for (Wall wall : QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getWalls()) {
			TOWall toWall = new TOWall(wall.getId());
			walls.add(toWall);
		}
		return walls;
	}

	public static TOWall getWallCandidate() {
		Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
		TOWall toWall = new TOWall(wall.getId());
		return toWall;
	}

	public static TOPlayer getCurrentPlayer() {
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		TOPlayer currentPlayer = new TOPlayer(player);
		currentPlayer.getPlayerToMove();
		return currentPlayer;
	}

	public static TOPlayer getWhitePlayer() {
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		TOPlayer currentPlayer = new TOPlayer(player);
		return currentPlayer;
	}

	public static TOPlayer getBlackPlayer() {
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		TOPlayer currentPlayer = new TOPlayer(player);
		return currentPlayer;
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
			Time time = getIntToTime(10, 0);
			player1.setRemainingTime(time);
			player2.setRemainingTime(time);

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
				// If j is 0, calling validate position throws nullpointerexception since list
				// of walls is empty. So at 0, only check if it is on board, not if it hits
				// other walls.
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

			boardWasInitiated = true;
			return (statusOfPosition);

		} catch (IOException e) {
			throw new UnsupportedOperationException(" * Invalid position being loaded...");
		}

	}

	/**
	 * 11. Validate Position
	 * 
	 * This method (along with the helper methods it calls) validates a potential pawn or wall move position.
	 * 
	 * @param row       row of the move position
	 * @param col       column of the move position
	 * @param dir       direction of the move position -- "horizontal", "vertical", or "pawn"
	 * @return boolean  true if valid position, false if invalid position
	 * @author Sami Junior Kahil, 260834568
	 */
	public static boolean validatePosition(int row, int col, String dir) {
		if (dir.equals("pawn")) {
			return validatePawnPosition(row, col);
		} else {
			Direction direction = (dir.equals("horizontal")) ? Direction.Horizontal : Direction.Vertical;
			return validateWallBoundaryPosition(row, col) && validateWallOverlapPosition(row, col, direction);
		}
	}

	/**
	 * This method is called if validating position of pawn. Only checks if pawn is within bounds of the board.
	 * 
	 * @param row       row of move position 
	 * @param col       column of move position
	 * @return boolean  false if outside of boundary, true otherwise
	 */
	private static boolean validatePawnPosition(int row, int col) {
		if (row <= 0 || row >= 10 || col <= 0 || col >= 10) {
			return false;
		}
		return true;
	}

	/**
	 * This method (along with validateWallOverlapPosition) is called if validating position of wall. Checks boundary of wall on board.
	 * 
	 * @param row      row of wall position
	 * @param col      column of wall position
	 * @return boolean false if outside of boundary, true otherwise
	 */
	private static boolean validateWallBoundaryPosition(int row, int col) {
		if (row == 9 || col == 9)
			return false;
		else
			return true;
	}

	/**
	 * This method is called with validateWallBoundaryPosition to check if there are any overlaps on the board.
	 * 
	 * @param row         row of wall position
	 * @param col         column of wall position
	 * @param direction   direction of wall
	 * @return boolean    false if overlap, true otherwise
	 */
	private static boolean validateWallOverlapPosition(int row, int col, Direction direction) {		
		List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
		List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();

		List<Wall> allWallsOnBoard = new ArrayList<Wall>();
		allWallsOnBoard.addAll(blackWalls);
		allWallsOnBoard.addAll(whiteWalls);

		for (Wall w : allWallsOnBoard) {
			System.out.println("Row is: " + w.getMove().getTargetTile().getRow());
			System.out.println("Col is: " + w.getMove().getTargetTile().getColumn());
			System.out.println("Direction is: " + w.getMove().getWallDirection().toString());
		}

		for (Wall currWall : allWallsOnBoard) {
			int currRow = currWall.getMove().getTargetTile().getRow();
			int currCol = currWall.getMove().getTargetTile().getColumn();
			Direction currDir = currWall.getMove().getWallDirection();

			if (currRow == row && currCol == col) {
				return false;
			}

			if (direction.equals( Direction.Vertical )) {
				if ((currRow == row - 1 || currRow == row + 1) && currCol == col && currDir.equals( Direction.Vertical )) {
					return false;
				}
			}

			if (direction.equals( Direction.Horizontal )) {
				if (currRow == row && (currCol == col - 1 || currCol == col + 1) && currDir.equals( Direction.Horizontal )) {
					return false;
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
	public static boolean validatePosition(int row, int col, Direction direction, Quoridor quoridor)
			throws UnsupportedOperationException {
		boolean position;
		if (direction == null) {
			return position = validatePawnPosition(row, col, quoridor);
			// return validatePawnPosition(row, col);
		} else {
			return validateWallBoundaryPosition(row, col, direction, quoridor)
					&& validateWallOverlapPosition(row, col, direction, quoridor);
		}
	}

	private static boolean validatePawnPosition(int row, int col, Quoridor quoridor)
			throws UnsupportedOperationException {
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
		List<Wall> blackWalls = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
		List<Wall> whiteWalls = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();

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
	 * This method switches the current player with the next player.
	 * 
	 * @author Sami Junior Kahil, 260834568
	 */
	public static void switchCurrentPlayer() {
		Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();

		if (currentPlayer.equals(whitePlayer)) {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(blackPlayer);
		} else {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(whitePlayer);
		}
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
							.getTile().getColumn()
							: QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition()
							.getTile().getColumn();

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
	 * helper method to get all existing usernames as a list of strings. Used in
	 * view.
	 * 
	 * @author Helen
	 * @return List(String) of existing usernames from model
	 */
	public static List<String> getAllUsernames() {
		ArrayList<String> list = new ArrayList<String>();
		if (QuoridorApplication.getQuoridor().hasUsers()) {
			for (User user : QuoridorApplication.getQuoridor().getUsers()) {
				list.add(user.getName());
			}
		}

		return list;

	}

	/**
	 * helper method to get number in stock of a player for current game
	 * 
	 * @author Helen
	 * @return int number of walls in stock
	 */
	public static int getStockOfPlayer(boolean isForBlack) {
		try {
			return isForBlack
					? QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
							.numberOfBlackWallsInStock()
							: QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
							.numberOfWhiteWallsInStock();
		} catch (RuntimeException e) {
			// if error, because game has not started
			return -1;
		}

	}

	/**
	 * Helper method to check if the board was successfully initiated.
	 * 
	 * @return
	 */
	public static boolean boardWasInitiated() {
		return boardWasInitiated;
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
	 * This method checks if it is the Black player's current turn.
	 * 
	 * @return True if currentPlayerToMove is black player, false for white OR for
	 *         invalid
	 */
	public static boolean isBlackTurn() {
		try {
			Player toMove = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
			Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			if (blackPlayer.equals(toMove)) {
				return true;
			} else
				return false;
		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * This method checks if it is the White player's current turn. Need this method
	 * because even if isBlackTurn returns false, can't guarantee that
	 * 
	 * @return True if currentPlayerToMove is white player, false for white OR for
	 *         invalid
	 */
	public static boolean isWhiteTurn() {
		try {
			Player toMove = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
			Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			if (whitePlayer.equals(toMove)) {
				return true;
			} else
				return false;
		} catch (RuntimeException e) {
			return false;
		}
	}

	public static boolean setPauseGame() {
		return false;
	}

	/**
	 * Helper method to allow view and other methods to update remaining time for a
	 * player.
	 * 
	 * @param remainingTime for a player
	 * @param True          for settingBlackPlayer, false otherwise
	 * @return True if successful
	 */
	public static boolean setRemainingTime(Time time, boolean isForBlack) {
		try {
			return isForBlack
					? (QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setRemainingTime(time))
							: (QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setRemainingTime(time));
		} catch (RuntimeException e) {
			// if fail, return false
			return false;
		}

	}

	/**
	 * Helper method to allow view and other methods to get column of wall at a
	 * given index
	 * 
	 * @param index      index of wall in list of walls
	 * @param isForBlack is it black players walls we are looking at
	 * @return String first character is the row, second is the column, and third
	 *         represents the orientation of the wall
	 */
	public static String infoOfWallAtIndex(int i, boolean isForBlack) {
		String row = "";
		String col = "";
		String dir = "";
		Wall tempWall = null;

		try {
			if (isForBlack) {
				tempWall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
						.getBlackWallsOnBoard(i);
			} else {
				tempWall = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
						.getWhiteWallsOnBoard(i);
			}
		} catch (RuntimeException e) {
			return "error";
		}

		row = Integer.toString(tempWall.getMove().getTargetTile().getRow());
		col = Integer.toString(tempWall.getMove().getTargetTile().getColumn());

		if (tempWall.getMove().getWallDirection() == Direction.Horizontal) {
			dir = "h";
		} else {
			dir = "v";
		}
		return row + col + dir;
	}

	// Logic for determining if a pawn can move:
	// Check all 4 adjacent tile. For each tile:
	// NOTE: We should look at the tiles in counter clock-wise order, starting at
	// directly above the pawn (not facing forward, ABOVE)
	// Step 0: Check to see that the tile being looked at is actually on the board!
	// If not, skip to next tile!
	// Step 1: Check to see if there is a wall between. If there is, it is not an
	// option. Skip to next tile. If there is not ->
	// Step 2: Check to see if the other pawn is there. If no pawn there, add the
	// tile to the list! If there is a pawn there...
	// Step 3: Check to see if there is a wall OR EDGE OF BOARD behind the other
	// pawn. If no wall/edge, Add tile behind pawn to the list.
	// Step 4: If there is a wall/edge, check above/below or left/right of other
	// pawn, depending on config.

	//THIS IS WHERE WE DECLARE THE 2 STATEMACHINES!
    private static PawnBehavior whitePB;
    private static PawnBehavior blackPB;

    /**
    * Setup method for pawn bahaviour state machines for black and white pawns
    * @author Helen
    * 
     */
    public static void pawnBehaviourSetUp() {           
	    whitePB = new PawnBehavior();
	    whitePB.setPlayer(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
	    whitePB.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
	    whitePB.startGame();
	          
	    blackPB = new PawnBehavior();
	    blackPB.setPlayer(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
	    blackPB.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
	    blackPB.startGame();
    }
    
    /**
	 * Getter for pawn behavior state machine
	 * @param forBlackPawn true to get blackPB, false to get whitePB
	 * @author Helen
	 */
	public static PawnBehavior getPB(boolean forBlackPawn) {		
		return (forBlackPawn) ? blackPB : whitePB;
	}
	

	
	/**
	 * Controller method to be called initially that redirects to either move pawn or jump pawn depending on the appropriate situation
	 * 
	 * @Author Shayne Leitamn, 260688512
	 * 
	 * @param dir	Direction of the move being attempted
	 * @return void
	 */
	public void typeofMove(MoveDirection dir) {
		
		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		boolean isForBlack = (currentPlayer == blackPlayer);
		int curRow = 0;
		int curCol = 0;
		int opRow = 0;
		int opCol = 0;
		
		if (isForBlack) {
			curRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
			curCol = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
			opRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
			opCol = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		} else {
			curRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
			curCol = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
			opRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
			opCol = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
		}
		
		switch (dir) {

		case North:
			if (curCol == opCol && curRow == opCol + 1) {
				jumpPawn(dir, isForBlack);
			} else {
				movePawn(dir, isForBlack);
			}
			break;
		case South:
			if (curCol == opCol && curRow + 1 == opCol) {
				jumpPawn(dir, isForBlack);
			} else {
				movePawn(dir, isForBlack);
			}
			break;
		case East:
			if (curCol + 1 == opCol && curRow == opCol) {
				jumpPawn(dir, isForBlack);
			} else {
				movePawn(dir, isForBlack);
			}
			break;
		case West:
			if (curCol == opCol + 1 && curRow == opCol) {
				jumpPawn(dir, isForBlack);
			} else {
				movePawn(dir, isForBlack);
			}
			break;
		case NorthEast:
			jumpPawn(dir, isForBlack);
			break;
		case NorthWest:
			jumpPawn(dir, isForBlack);
			break;
		case SouthEast:
			jumpPawn(dir, isForBlack);
			break;
		case SouthWest:
			jumpPawn(dir, isForBlack);
			break;
		}
	}
	
	
	/**
	 * New controller method to move pawn
	 * @Author Shayne
	 * @param dir
	 */
	public static void movePawn (MoveDirection dir, boolean isForBlack) {
		
		Player curPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		PlayerPosition curPPos = null;
		Game curGame = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition curGamePos = curGame.getCurrentPosition();
		Move recentMove = curGame.getMove(curGame.numberOfMoves() - 1);
		if (isForBlack) {
			curPPos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
		} else {
			curPPos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
		}
		Tile newTile = null;
		int newRow = 0;
		int newCol = 0;
		//To do: run the pawn behavior method associated with the direction. If it returns true, then:
		//Get the tile to be updated. Create a new move, update player pos, update current game, update game pos

		switch (dir) {
		
		  case North:
			if (isForBlack) {
				if (blackPB.moveUp()) {
					newCol = curPPos.getTile().getColumn();
					newRow = curPPos.getTile().getRow() - 1;
					for (Tile tile : QuoridorApplication.getQuoridor().getBoard().getTiles()) {
						if (tile.getRow() == newRow && tile.getColumn() == newCol) {
							newTile = tile;
						}
					}
					
					curPPos.setTile(newTile);
					StepMove newMove = new StepMove(curGame.numberOfMoves() + 1, ((curGame.numberOfMoves() + 1) % 2), curPlayer, newTile, curGame);
					recentMove.setNextMove(newMove);
					newMove.setPrevMove(recentMove);
					curGame.addMove(newMove);
					GamePosition newGamePos = new GamePosition(curGame.numberOfPositions() + 1, curGame.getCurrentPosition().getWhitePosition(), curPPos, curGame.getWhitePlayer(), curGame);
					for (Wall wall : curGamePos.getBlackWallsInStock()) {
						newGamePos.addBlackWallsInStock(wall);
					}
					for (Wall wall : curGamePos.getWhiteWallsInStock()) {
						newGamePos.addWhiteWallsInStock(wall);
					}
					for (Wall wall : curGamePos.getBlackWallsOnBoard()) {
						newGamePos.addBlackWallsOnBoard(wall);
					}
					for (Wall wall : curGamePos.getWhiteWallsOnBoard()) {
						newGamePos.addWhiteWallsOnBoard(wall);
					}
					curGame.addPosition(newGamePos);
					curGame.setCurrentPosition(newGamePos);
					
					
				} else {
					// MOVE FAILED. ILLEGAL MOVE!
				}
			} else {
				if (whitePB.moveUp()) {
					newCol = curPPos.getTile().getColumn();
					newRow = curPPos.getTile().getRow() - 1;
					for (Tile tile : QuoridorApplication.getQuoridor().getBoard().getTiles()) {
						if (tile.getRow() == newRow && tile.getColumn() == newCol) {
							newTile = tile;
						}
					}

					
					
				} else {
					// MOVE FAILED. ILLEGAL MOVE!
				}
			}
			  
			  
			  break;
		  case South:
			  
				  break;
		  case West:
			  
				  break;
		  case East:
			  
				  break;

		}
	}

	/**
	 * New controller method to jump pawn
	 * @Author Shayne
	 * @param dir
	 */
	public static void jumpPawn (MoveDirection dir, boolean isForBlack) {
		PawnBehavior pawnBehavior=new PawnBehavior();
		Game game=QuoridorApplication.getQuoridor().getCurrentGame();
		Player currentPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		Board board=QuoridorApplication.getQuoridor().getBoard();
		pawnBehavior.setPlayer(currentPlayer);
		pawnBehavior.setCurrentGame(game);
		

		switch (dir) {
			 
			case North:
				if(currentPlayer==whitePlayer) {
					int currentRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
					int currentCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
					int oppoRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
					int oppoCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
					if(currentCol==oppoCol&&Math.abs(currentRow-oppoRow)==1) {
						Tile tile=new Tile(pawnBehavior.getCurrentPawnRow()-2,pawnBehavior.getCurrentPawnColumn(),board);
						QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
					}
				}
				break;
				
			case South:
				if(currentPlayer==whitePlayer) {
					int currentRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
					int currentCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
					int oppoRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
					int oppoCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
					if(currentCol==oppoCol&&Math.abs(currentRow-oppoRow)==1) {
						Tile tile=new Tile(pawnBehavior.getCurrentPawnRow()+2,pawnBehavior.getCurrentPawnColumn(),board);
						QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
					}
				}
				break;
				
			case East:
				if(currentPlayer==whitePlayer) {
					int currentRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
					int currentCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
					int oppoRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
					int oppoCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
					if(currentRow==oppoRow&&Math.abs(currentCol-oppoCol)==1) {
						Tile tile=new Tile(pawnBehavior.getCurrentPawnRow(),pawnBehavior.getCurrentPawnColumn()+2,board);
						QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
					}
				}
				break;
				
			case West:
				if(currentPlayer==whitePlayer) {
					int currentRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
					int currentCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
					int oppoRow=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
					int oppoCol=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
					if(currentRow==oppoRow&&Math.abs(currentCol-oppoCol)==1) {
						Tile tile=new Tile(pawnBehavior.getCurrentPawnRow(),pawnBehavior.getCurrentPawnColumn()-2,board);
						QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
					}
				}
				break;
				
			case NorthEast:
				
			case NorthWest:
				
			case SouthEast:
				
			case SouthWest:
		}
	}
	
	/**
	 * Helper method to convert string "side" of a pawnMove or pawnJump to a MoveDirection.
	 * Used for gherkin scenarios.
	 * @param side String
	 * @return
	 */
	public static MoveDirection stringSideToDirection(String side) {
		MoveDirection dir;
		switch (side) {
		case "up":
			dir = MoveDirection.North;
			break;
		case "down":
			dir= MoveDirection.South;
			break;
		case "left":
			dir= MoveDirection.West;
			break;
		case "right":
			dir= MoveDirection.East;
			break;
		case "upleft":
			dir= MoveDirection.NorthWest;
			break;
		case "downleft":
			dir= MoveDirection.SouthWest;
			break;
		case "upright":
			dir= MoveDirection.NorthEast;
			break;
		case "downright":
		default:
			dir= MoveDirection.SouthEast;
			break;
		}
		
		return dir;
	}
	
	
	/**
	 * TEST New controller method to move pawn - will be removed later
	 * 
	 * @Author Helen
	 * @param dir
	 */
	public static void movePawnTest(MoveDirection dir) {
		PawnBehavior pb = (isBlackTurn()) ? blackPB : whitePB;
		
		//first check legal
		if (!pb.isLegalStep(dir)) {
			return; //if not legal, simply return. It is still that player's turn.
		}
		
		//if legal, make the move
		switch (dir) {
		case North:
			pb.moveUp();
			break;
		case South:
			pb.moveDown();
			break;
		case West:
			pb.moveLeft();
			break;
		case East:
			pb.moveRight();
			break;
		case NorthWest:
			pb.moveUpLeft();
			break;
		case SouthWest:
			pb.moveDownLeft();
			break;
		case NorthEast:
			pb.moveUpRight();
			break;
		case SouthEast:
			pb.moveDownRight();
			break;

		}
		
		//switch to next player
		switchCurrentPlayer();
	}

}

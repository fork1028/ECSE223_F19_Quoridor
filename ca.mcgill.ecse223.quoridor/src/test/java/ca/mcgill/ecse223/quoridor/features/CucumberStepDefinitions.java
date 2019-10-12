package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Time;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

	private Quoridor quoridor;
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private Game game;

	// ***********************************************
	// Background step definitions
	// ***********************************************

	@Given("^The game is not running$")
	public void theGameIsNotRunning() {
		initQuoridorAndBoard();
		createUsersAndPlayers("user1", "user2");
	}

	@Given("^The game is running$")
	public void theGameIsRunning() {
		theGameIsNotRunning();
		createAndStartGame();
	}

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		currentPlayer = player1;
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { player1, player2 };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);

			String dir = map.get("wdir");

			Direction direction;
			switch (dir) {
			case "horizontal":
				direction = Direction.Horizontal;
				break;
			case "vertical":
				direction = Direction.Vertical;
				break;
			default:
				throw new IllegalArgumentException("Unsupported wall direction was provided");
			}
			new WallMove(0, 1, players[playerIdx], board.getTile((wrow - 1) * 9 + wcol - 1), game, direction, wall);
			if (playerIdx == 0) {
				game.getCurrentPosition().removeWhiteWallsInStock(wall);
				game.getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				game.getCurrentPosition().removeBlackWallsInStock(wall);
				game.getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();
	}

	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// Walls are in stock for all players
	}


	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later

	
	/**
	 * Precondition for SetTotalThinkingTime.feature
	 * @author Helen Lin
	 */
	@Given("A new game is initializing")
	public void aNewGameIsInitializing() {
		quoridor.getCurrentGame().setGameStatus(GameStatus.Initializing);

	}

	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	/*
	 * TODO Insert your missing step definitions here
	 * 
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 * 
	 */
	

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Given("A game position is supplied with pawn coordinate <row>:<col>")
	public void gamePositionSuppliedWithPawnPos (int row, int col) {	
		Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row - 1) * 9 + col - 1);
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		currentGame.setMoveMode(MoveMode.PlayerMove);
		Player currentPlayer = currentGame.getCurrentPosition().getPlayerToMove();

		if (currentPlayer == currentGame.getBlackPlayer()) {
			currentGame.getCurrentPosition().getBlackPosition().setTile(targetTile);
		}
		else {
			currentGame.getCurrentPosition().getWhitePosition().setTile(targetTile);
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@When("Validation of the position is initiated")
	public void initateValidatePosition() throws UnsupportedOperationException {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		MoveMode moveMode = currentGame.getMoveMode();
		Player currentPlayer = currentGame.getCurrentPosition().getPlayerToMove();

		if (currentPlayer == currentGame.getBlackPlayer()) {
			Tile targetTile = currentGame.getCurrentPosition().getBlackPosition().getTile();
			if (moveMode == MoveMode.PlayerMove) {
				QuoridorController.validatePosition(targetTile.getRow(), targetTile.getColumn(), null);
			}
			else {
				Direction direction = currentGame.getWallMoveCandidate().getWallDirection();
				QuoridorController.validatePosition(targetTile.getRow(), targetTile.getColumn(), direction);
			}
		}
		else {
			Tile targetTile = currentGame.getCurrentPosition().getWhitePosition().getTile();
			if (moveMode == MoveMode.PlayerMove) {
				QuoridorController.validatePosition(targetTile.getRow(), targetTile.getColumn(), null);
			}
			else {
				Direction direction = currentGame.getWallMoveCandidate().getWallDirection();
				QuoridorController.validatePosition(targetTile.getRow(), targetTile.getColumn(), direction);
			}
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Then("The position shall be \"<result>\"")
	public void positionShallBe(int row, int col, Direction direction) {
		boolean result = QuoridorController.validatePosition(row, col, direction);
		assertEquals(true, result);
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Given("A game position is supplied with wall coordinate <row>:<col>-\"<dir>\"")
	public void gamePositionSuppliedWithWallPos (int row, int col, Direction direction) {
		Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row - 1) * 9 + col - 1);
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		currentGame.setMoveMode(MoveMode.WallMove);
		Player currentPlayer = currentGame.getCurrentPosition().getPlayerToMove();

		Wall wall = new Wall(-1, currentPlayer);
		WallMove wallMove = new WallMove(-1, currentGame.getCurrentPosition().getId(), currentPlayer, targetTile, currentGame, direction, wall);
		currentGame.setWallMoveCandidate(wallMove);
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Given("The player to move is \"<player>\"")
	public void thePlayerToMoveIs(Player player) {
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(player);
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The clock of \"<player>\" is running")
	public void clockOfPlayerIsRunning() {
		assert (QuoridorApplication.getQuoridor().getCurrentGame()
				.getCurrentPosition().getPlayerToMove().getRemainingTime()) != null;
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The clock of \"<other>\" is stopped")
	public void clockOfOtherIsStopped() {
		try {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
			.getPlayerToMove().getNextPlayer().getRemainingTime().wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@When("Player \"<player>\" completes his move")
	public void playerCompletesMove() {
		assertEquals(true, QuoridorController.switchCurrentPlayer());
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Then("The user interface shall be showing it is \"<other>\" turn")
	public void showItIsOtherTurn() {
		// TO DO later GUI Feature
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The clock of \"<player>\" shall be stopped")
	public void clockOfPlayerStopped(Time timeElapsed) {
		QuoridorApplication.getQuoridor().getCurrentGame()
		.getCurrentPosition().getPlayerToMove().setRemainingTime(timeElapsed);
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The clock of \"<other>\" shall be running")
	public void clockOfOtherRunning() {
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
		.getNextPlayer().getRemainingTime().notify();
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The next player to move shall be \"<other>\"")
	public void nextPlayerToMoveIsOther() {
		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded


	
	// MoveWall Scenario Outline 1
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Given("A wall move candidate exists with \"<dir>\" at position (<row>, <col>)")
	public void aWallMoveCandidateExistsWithDirectionAtPosition(int row, int col) {
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn(), col);
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow(), row);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("The wall candidate is not at the \"<side>\" edge of the board")
	public void theWallCandidateIsNotAtTheEdgeOfTheBoard() {
		int col=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
		int row=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
		assertEquals(col,1);
		assertEquals(col,9);
		assertEquals(row,1);
		assertEquals(row,9);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@When("I try to move the wall \"<side>\"")
	public void iTryToMoveTheWall(Player player, Wall wall, WallMove move) {
//		Player player=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
//		Wall wall=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
//		WallMove move=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		QuoridorController.moveWall(player, wall, move);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Then("The wall shall be moved over the board to position (<nrow>, <ncol>)")
	public void theWallShallBeMovedOverTheBoardToPosition(int nrow, int ncol) {
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn(), ncol);
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow(), nrow);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("A wall move candidate shall exist with \"<dir>\" at position (<nrow>, <ncol>)")
	public void aWallMoveCandidateShallExistWithDirectionAtPosition(int row, int col) {
		aWallMoveCandidateExistsWithDirectionAtPosition(row,col);
	}
	
	// MoveWall Scenario Outline 2
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Then("I shall be notified that my move is illegal ")
	public void iShallBeNotifiedThatMyMoveIsIllegal() {
		// GUI related
	}
	
	// DropWall Scenario Outline 1
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")
	public void theWallMoveCandidateWithDirectionAtPositionIsValid(int row, int col) {
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn(), col);
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow(), row);
	}

	/**
	 * Action of SetTotalThinkingTime.feature to call controller
	 * @param min
	 * @param sec
	 * @throws InvalidInputException
	 * @author Owner
	 */
	@When("<min>:<sec> is set as the thinking time")
	public void MinSecIsSetAsTheThinkingTime(int min, int sec) throws InvalidInputException {
		QuoridorController.setTotalThinkingTime(min, sec);
	}
	
	/**
	 * Postcondition of SetTotalThinkingTime feature
	 */
	@Then("Both players shall have <min>:<sec> remaining time left")
	public void bothPlayersShallHaveMinSecRemainingTimeLeft(int min, int sec) {
		int seconds = min*60 + sec;
		
		//check black player's time
		Time t = player1.getRemainingTime();
		int tSec = (int) (t.getTime()/1000);

		if (tSec == seconds) { //black player's time is correct
			//check white player
			t = player2.getRemainingTime();
			tSec = (int) (t.getTime()/1000);
			assertTrue(tSec == seconds);
		} else {
			fail();
		}
			
	}


	/**
	 * @author Xinyue Chen, 260830761
	 */
	@When("I release the wall in my hand")
	public void iReleaseTheWallInMyHand(Wall wall) {
		Player player=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		QuoridorController.dropWall(player, wall);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Then("A wall move shall be registered with \"<dir>\" at position (<row>, <col>)")
	public void aWallMoveShallBeRegisteredWithDirectionAtPosition(Wall wall, Tile targetTile) {
		QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallPlaced(wall);
		QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setTargetTile(targetTile);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("I shall not have a wall in my hand")
	public void iShallNotHaveAWallInMyHand() {
		// GUI related
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("My move shall be completed")
	public void myMoveShallBeCompleted() {
		// GUI related
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("It shall not be my turn to move")
	public void itShallNotBeMyTurnToMove(Player player) {
		assert(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()==player);
	}
	
	// DropWall Scenario Outline 2
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Given("The wall move candidate with \"<dir>\" at position (<row>, <col>) is invalid")
	public void theWallMoveCandidateWithDirectionAtPositionIsInvalid() {
		int col=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
		int row=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
		assert(col<1||col>9);
		assert(row<1||row>9);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		// GUI related
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		// GUI related
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove(Player player) {
		assert(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()==player);
	}
	
	/**
	 * @author Xinyue Chen, 260830761
	 */
	@But("No wall move shall be registered with \"<dir>\" at position (<row>, <col>)")
	public void noWallMoveShallBeRegisteredWithDirectionAtPosition() {
		assert(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced()==null);
	}
	
	
	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
	@After
	public void tearDown() {
		quoridor.delete();
		quoridor = null;
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below


	private void initQuoridorAndBoard() {
		quoridor = QuoridorApplication.getQuoridor();
		board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}



	private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();

	private void createUsersAndPlayers(String userName1, String userName2) {

		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

		int thinkingTime = 180;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		//@formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 * 
		 */
		//@formatter:on
		player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}


		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);

		return playersList;
	}



	}

	private void createAndStartGame() {
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = board.getTile(36);
		Tile player2StartPos = board.getTile(44);


		PlayerPosition player1Position = new PlayerPosition(player1, player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(player2, player2StartPos);

		game = new Game(GameStatus.Running, MoveMode.PlayerMove, player1, player2, quoridor);
		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, player1, game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
	}

}

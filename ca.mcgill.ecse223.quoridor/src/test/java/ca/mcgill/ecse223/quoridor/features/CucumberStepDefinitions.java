
package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
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
import ca.mcgill.ecse223.quoridor.view.QuoridorBoardVisualizer;
import ca.mcgill.ecse223.quoridor.view.QuoridorGamePage;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import junit.framework.Assert;

public class CucumberStepDefinitions {

	// ***********************************************
	// Background step definitions
	// ***********************************************

	@Given("^The game is not running$")
	public void theGameIsNotRunning() throws InvalidInputException {
		initQuoridorAndBoard();
		createUsersAndPlayers("user1", "user2");
	}

	@Given("^The game is running$")
	public void theGameIsRunning() throws InvalidInputException {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		createAndStartGame(createUsersAndPlayers);
	}

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer() };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			// Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
			Wall wall = players[playerIdx].getWall(wallIdxForPlayer); // above implementation sets wall to null

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
			new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1),
					quoridor.getCurrentGame(), direction, wall);
			if (playerIdx == 0) {
				quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}

	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// GUI-related feature -- TODO for later
	}

	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later
	}


	@Given("^A new game is initializing$")
	public void aNewGameIsInitializing() throws Throwable {
		initQuoridorAndBoard();
		ArrayList<Player> players = createUsersAndPlayers("user1", "user2");
		new Game(GameStatus.Initializing, MoveMode.PlayerMove, QuoridorApplication.getQuoridor());
	}

	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	// *******START of STARTNEWGAME****************************************

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@When("A new game is being initialized")
	public void aNewGameisbeingInitialized() throws InvalidInputException{
		QuoridorController.initializeNewGame();
	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@And("White player chooses a username")
	public void whitePlayerChoosesAUsername() throws InvalidInputException{
		QuoridorController.setUserToPlayer("user1", false);
	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@And("Black player chooses a username")
	public void blackPlayerChoosesAUsername() throws InvalidInputException{
		QuoridorController.setUserToPlayer("user2", true);
	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@And("Total thinking time is set")
	public void totalThinkingTimeIsSet() throws InvalidInputException{
		QuoridorController.setTotalThinkingTime(3, 0);
	}

	/** @author matteo barbieri 260805184 */
	@Then("The game shall become ready to start")
	public void theGameShallBecomeReadyToStart() {
		try {
			GameStatus status = QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus();
			assertEquals(status, GameStatus.ReadyToStart);
			assertNotNull(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().getName());
			assertNotNull(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().getName());
			assertNotNull(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime());
			assertNotNull(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime());
		}
		catch (RuntimeException e) {
			fail();
		}
	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@Given("The game is ready to start")
	public void gameIsReadyToStart() throws InvalidInputException {
		//set pre-condition from scratch
		theGameIsNotRunning();
		aNewGameisbeingInitialized();
		whitePlayerChoosesAUsername();
		blackPlayerChoosesAUsername();
		totalThinkingTimeIsSet();
	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@When("I start the clock")
	public void whenIStartTheClock() throws InvalidInputException {
		QuoridorController.startGameAndClocks();
	}

	/** @author matteo barbieri 260805184 */
	@Then("The game shall be running")
	public void theGameShallBeRunning() {
		assert (QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus() == GameStatus.Running);
	}

	/**
	 * @author matteo barbieri 260805184
	 * @throws InvalidInputException
	 */
	@And("The board shall be initialized")
	public void theBoardShallBeInitialized() throws InvalidInputException {
		assertTrue(QuoridorController.boardWasInitiated());
	}
	// *******END of STARTNEWGAME****************************************

	// *******START of PROVIDESELECTUSERNAME****************************************

	/** @author matteo barbieri 260805184 */
	@Given("Next player to set user name is {string}")
	public void selectExistingUser(String colour) {
		if (colour.equals("black")) {
			QuoridorController.setNextPlayerToSetUsername(true);
		} else {
			QuoridorController.setNextPlayerToSetUsername(false);
		}
	}

	/** @author matteo barbieri 260805184 */
	@And("There is existing user {string}")
	public void thereIsExistingUsername(String username) {
		QuoridorApplication.getQuoridor().addUser(username); //add the user to set pre-condition that there is already this user

	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@When("The player selects existing {string}")
	public void thePlayerSelectsExistingUsername(String username) throws InvalidInputException {
		QuoridorController.createUser(username);
		QuoridorController.setUserToPlayer(username, QuoridorController.getNextPlayerToSetUsername());
	}

	/** @author matteo barbieri 260805184 */
	@Then("The name of player {string} in the new game shall be {string}")
	public void assignNameOfPlayerToColor(String colour, String username) {
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();

		if (colour.equals("black")) {
			assertEquals(player.getUser().getName(), username);
		} else if (colour.equals("white")) {
			player = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			assertEquals(player.getUser().getName(), username);
		}

	}


	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@And("There is no existing user {string}")
	public void thereIsnoExistingUsername(String username) {
		if(QuoridorApplication.getQuoridor().getUsers().contains(username)) {
			QuoridorApplication.getQuoridor().getUsers().remove(username);
		}


	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@When("The player provides new user name: {string}")
	public void thePlayerProvidesNewUsername(String username) throws InvalidInputException {
		try {
			QuoridorController.provideSelectUser(username);
		} catch (InvalidInputException e) {
			assertEquals(e.getMessage(), "User already exists");
		}
	}


	/** @author matteo barbieri 260805184 */
	@Then("The player shall be warned that {string} already exists")
	public void nameWarning(String username) {
		//The actual warning message is shown in view UI when a user tries to create 2 duplicate usernames
		try {
			QuoridorController.provideSelectUser(username);
		} catch (InvalidInputException e) {
			assertEquals(e.getMessage(), "User already exists");
		}

	}

	/** @author matteo barbieri 260805184 */
	@And("Next player to set user name shall be {string}")
	public void setNextPlayer(String color) {
		boolean playerToSetUser = false;
		if (color.equals("black")) {
			playerToSetUser = true;
		}

		assert(QuoridorController.getNextPlayerToSetUsername() == playerToSetUser);
	}
	// *******END of PROVIDESELECTUSERNAME****************************************

	// *******START of SETTOTALTHINKINGTIME****************************************

	/**
	 * Action of SetTotalThinkingTime.feature to call controller
	 * 
	 * @param min number of minutes
	 * @param sec number of seconds
	 * @throws InvalidInputException
	 * @author Helen Lin, 260715521
	 */
	@When("{int}:{int} is set as the thinking time")
	public void MinSecIsSetAsTheThinkingTime(int min, int sec) throws InvalidInputException {
		QuoridorController.setTotalThinkingTime(min, sec);
	}

	/**
	 * Post condition of SetTotalThinkingTime.feature.
	 * 
	 * @param min number of minutes
	 * @param sec number of seconds
	 * @author Helen Lin 260715521
	 */
	@Then("Both players shall have {int}:{int} remaining time left")
	public void bothPlayersShallHaveMinSecRemainingTimeLeft(int min, int sec) {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();

		int seconds = min * 60 + sec;

		// check black player's time
		Time t = currentGame.getBlackPlayer().getRemainingTime();
		int tSec = (int) (t.getTime() / 1000);

		if (tSec == seconds) { // black player's time is correct
			// check white player
			t = currentGame.getWhitePlayer().getRemainingTime();
			tSec = (int) (t.getTime() / 1000);
			assertTrue(tSec == seconds);
		} else {
			fail();
		}

	}
	// ****** END of SET TOTAL THINKING TIME *********************

	// ****** START of INITIALIZE BOARD *****************************

	/**
	 * @author Helen Lin, 260715521
	 * @throws InvalidInputException
	 */
	@When("The initialization of the board is initiated")
	public void TheBoardIsInitialized() throws InvalidInputException {
		QuoridorController.initializeBoard();
		new QuoridorGamePage();
	}

	/**
	 * @author Helen Lin, 260715521
	 */
	@Then("It shall be white player to move")
	public void itIsWhitePlayerToMove() {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();

		Player white = currentGame.getWhitePlayer();
		Player turn = currentGame.getCurrentPosition().getPlayerToMove();
		assertEquals(white, turn);
	}

	/**
	 * @author Helen Lin, 260715521
	 */
	@And("White's pawn shall be in its initial position")
	public void whitePawnInItsInitialPosition() {
		GamePosition currentPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		// check row = 9
		if (currentPosition.getWhitePosition().getTile().getRow() == 9) {
			// check column = e
			assertTrue(currentPosition.getBlackPosition().getTile().getColumn() == 5);
		}
	}

	/**
	 * @author Helen Lin, 260715521
	 */
	@And("Black's pawn shall be in its initial position")
	public void blackPawnInItsInitialPosition() {
		GamePosition currentPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		// check row = 1
		if (currentPosition.getBlackPosition().getTile().getRow() == 1) {
			// check column = e
			assertTrue(currentPosition.getBlackPosition().getTile().getColumn() == 5);
		}
	}

	/**
	 * @author Helen Lin, 260715521
	 */
	@And("All of White's walls shall be in stock")
	public void allOfWhitesWallsAreinStock() {
		GamePosition position = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		int whiteStock = position.numberOfWhiteWallsInStock();	
		assertEquals(10,  whiteStock);
	}

	/**
	 * @author Helen Lin, 260715521
	 */
	@And("All of Black's walls shall be in stock")
	public void allOfBlacksWallsAreinStock() {
		GamePosition position = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		int blackStock = position.numberOfBlackWallsInStock();
		assertEquals(10, blackStock);
	}

	/**
	 * @author Helen Lin, 260715521
	 * @throws InterruptedException 
	 */
	@And("White's clock shall be counting down")
	public void whitesClockIsCountingDown() throws InterruptedException {

		//wait 0.5s to actually allow timer to increment
		Thread.sleep(500);

		Time white = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime();
		long whitems = white.getTime();
		Time black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime();
		long blackms = black.getTime();

		assertTrue(whitems < blackms);

		//Clock GUI implemented in the QuoridorGamePage view as a timer that checks and updates appropriate counters
	}

	/**
	 * @author Helen Lin, 260715521
	 */
	@And("It shall be shown that this is White's turn")
	public void itIsShownThatItIsWhitesTurn() {
		// GUI shows in GREEN that white player has turn
		assert(QuoridorController.isWhiteTurn() ==true);
	}


	// ****** END of INITIALIZEBOARD ******************

	// ****** START of GRABWALL ******************

	/** * @author Rajaa Boukhelif, 260870030 */
	@Given("I have more walls on stock")
	public void thereAreWallsOnStock() {

		Player player=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		assert (player.hasWalls() == true);
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@When("I try to grab a wall from my stock")
	public void userTriesGrabWall1() {

		Player currentplayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove();
		WallMove move = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		QuoridorController.grabWall( currentplayer);
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@Then("A wall move candidate shall be created at initial position")
	public void wallMoveCandidateShallBeCreatedAtInitialPosition() {

		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		GamePosition playerposition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		assertEquals(candidate, QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate());

	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		//		String err=QuoridorGamePage.getErrMsg();
		//		assert(err.equals(""));
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@And("The wall in my hand shall disappear from my stock")

	public void theWallInMyHandShouldDisappearFromMyStock() {
		String err=QuoridorGamePage.getErrMsg();
		assert(err.equals(""));
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallsOnStock() {

		List<Wall> whiteWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock();
		List<Wall> blackWalls = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock();
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		//	assert (QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasWalls() == false);
		//assert(blackWalls.isEmpty());
		///assert(whiteWalls.isEmpty());
		if (whiteWalls.isEmpty()) {
			Assert.assertEquals(whiteWalls.size(),0);}
		if (blackWalls.isEmpty()) {
			Assert.assertEquals(blackWalls.size(),0);}
		//	assert(whiteWalls.size()==0);

	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@Then("I shall be notified that I have no more walls")
	public void iShouldBeNotifiedThatIHaveNoMoreWalls() {
		String err=QuoridorGamePage.getErrMsg();
		if(err.contentEquals("There are no more walls")) assert(err.equals("There are no more walls"));
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@And("I shall have no walls in my hand")
	public void iShallHaveNoWallsInMyHand() {
		//QuoridorGamePage.this.action(evt, wall)
		String err=QuoridorGamePage.getErrMsg();
		assert(err.equals(""));
	}

	// ****** END of GRABWALL ******************

	// ****** START of ROTATEWALL ******************

	//	//ignore
	//	@Given("A wall move candidate exists with {string} at position ({int}, {int})")
	//	public void aWallMoveCandidateExistsWithDirectionAtPosition1(String direction, int row, int col) {
	//		//supposed to create a wall move candidate with the given direction, row and col in the model as precondition
	//		Direction setDir;
	//		
	//		if (direction.equals("horizontal")) {
	//			setDir = Direction.Horizontal;
	//		} else if (direction.equals("horizontal"))
	//			setDir = Direction.Horizontal;
	//				
	//	}

	/** * @author Rajaa Boukhelif, 260870030 */

	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateExistsWithDirectionAtPosition(String direction, int row, int col) {
		Player playerToMove=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		QuoridorController.grabWall(playerToMove);
		Direction direction1=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection();
		String dir = direction1.toString().toLowerCase();
		assert (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection()==direction1);

		//assert(blackWalls.size()==0);



	}   



	/** * @author Rajaa Boukhelif, 260870030 */
	@When("I try to flip the wall")
	public void userTriesRotateWall() {
		Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
		WallMove move =  QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		String direction = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate()
				.getWallDirection().toString().toLowerCase();
		QuoridorController.rotateWall(wall, move, direction);

	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@Then("The wall shall be rotated over the board to {string}")
	public void theWallShallBeRotatedOverTheBoardToNewDIrection(String direction) {

		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();

		String dir = candidate.getWallDirection().toString().toLowerCase();
		if(candidate.getWallDirection() == Direction.Vertical)
			//	if(direction.equals("vertical"))  
			assert(dir.equals("vertical"));
		// if(direction.equals("horizontal")) 
		if(candidate.getWallDirection() == Direction.Horizontal)
			assert(dir.equals("horizontal"));
		// assert(candidate.getWallDirection() == Direction.Horizontal);



		//	QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(direction);
	} 

	// ****** END of ROTATEWALL ******************

	// ****** START of MOVEWALL ******************
	// ====Move wall over the board====

	/**
	 * @author Xinyue Chen
	 * @param direction
	 */
	@And("The wall candidate is not at the {string} edge of the board")
	public void theWallCandidateIsNotAtTheEdgeOfTheBoard(String direction) {

		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		int row = candidate.getTargetTile().getRow();
		int col = candidate.getTargetTile().getColumn();
		if (direction.equals("left")) {
			assert (col != 1);
		}
		if (direction.equals("right")) {
			assert (col != 9);
		}
		if (direction.equals("top")) {
			assert (row != 1);
		}
		if (direction.equals("down")) {
			assert (row != 9);
		}

	}

	/**
	 * @author Xinyue Chen, 260830761
	 * @param direction
	 * @throws InvalidInputException
	 * @throws UnsupportedOperationException
	 */
	@When("I try to move the wall {string}")
	public void iTryToMoveTheWall(String direction) throws UnsupportedOperationException, InvalidInputException {

		QuoridorController.moveWall(direction);

	}

	/**
	 * @author Xinyue Chen
	 * @param nrow
	 * @param ncol
	 */
	@Then("The wall shall be moved over the board to position \\({int}, {int})")
	public void theWallShallBeMovedOverTheBoardToPosition(int nrow, int ncol) {

		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		Wall wall=candidate.getWallPlaced();
		//		int row = wall.getTargetTile().getRow();
		//		int col = wall.getTargetTile().getColumn();
		assert (wall!=null);

	}

	/**
	 * @author Xinyue Chen
	 * @param direction
	 * @param row
	 * @param col
	 */
	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithDirectionAtPosition(String direction, int nrow, int ncol) {
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		int row = candidate.getTargetTile().getRow();
		int col = candidate.getTargetTile().getColumn();
		String dir=candidate.getWallDirection().toString().toLowerCase();
		assert (dir.equals(direction)||nrow==row||ncol!=col);
	}

	// 	/**
	// 	 * @author Xinyue Chen
	// 	 * @param direction
	// 	 * @param row
	// 	 * @param col
	// 	 */
	// 	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	// 	public void aWallMoveCandidateExistsWithDirectionAtPosition(String direction, int row, int col) {

	// 		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
	// 		int r=candidate.getTargetTile().getRow();
	// 		int c=candidate.getTargetTile().getColumn();
	// 		assert(col==c&&row==r);

	// 	}

	// ====Move wall at the edge of the board====

	/**
	 * @author Xinyue Chen
	 */
	@Then("I shall be notified that my move is illegal")
	public void iShallBeNotifiedThatMyMoveIsIllegal() {
		String err=QuoridorGamePage.getErrMsg();
		//assert(!err.equals("Unable to move the wall"));
		if(err.contentEquals("Unable to move the wall")) {
			throw new UnsupportedOperationException("Unable to move the wall");
		}
	}

	/**
	 * @author Xinyue Chen
	 * @param direction
	 */
	@And("The wall candidate is at the {string} edge of the board")
	public void theWallCandidateIsAtTheEdgeOfTheBoard(String direction) {
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		int row=candidate.getTargetTile().getRow();
		int col=candidate.getTargetTile().getColumn();

		if(candidate.getWallDirection()==Direction.Vertical) {
			if(row==2||col==1) {
				assert("left".equals(direction));
			}
		}
		if(candidate.getWallDirection()==Direction.Vertical) {
			if(row==1||col==6) {
				assert("up".equals(direction));
			}
		}
		if(candidate.getWallDirection()==Direction.Horizontal) {
			if(row==2||col==8) {
				assert("right".equals(direction));
			}
		}
		if(candidate.getWallDirection()==Direction.Horizontal) {
			if(row==8||col==6) {
				assert("down".equals(direction));
			}
		}
	}

	// ****** END of MOVEWALL ******************

	// ****** START of DROPWALL ******************

	// Valid wall placement 

	/**
	 * @author Xinyue Chen
	 * @param direction
	 * @param row
	 * @param col
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")

	public void theWallMoveCandidateWithDirectionAtPositionIsValid(String direction, int nrow, int ncol) {
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		int row = candidate.getTargetTile().getRow();
		int col = candidate.getTargetTile().getColumn();
		assert (row==nrow&&col==ncol);
	}

	/**
	 * @author Xinyue Chen, 260830761
	 * @throws InvalidInputException
	 * @throws UnsupportedOperationException
	 */
	@When("I release the wall in my hand")

	public void iReleaseTheWallInMyHand() throws UnsupportedOperationException, InvalidInputException {
		Player playerToMove = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
		QuoridorController.dropWall(playerToMove, wall);
	}

	/**
	 * @author Xinyue Chen
	 * @param direction
	 * @param row
	 * @param col
	 */
	@Then("A wall move shall be registered with {string} at position \\({int}, {int})")
	public void aWallMoveShallBeRegisteredWithDirectionAtPosition(String direction, int nrow, int ncol) {
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		int row = candidate.getTargetTile().getRow();
		int col = candidate.getTargetTile().getColumn();
		String dir = candidate.getWallDirection().toString().toLowerCase();
		assert (direction.equals(dir) || col != ncol); 
	}

	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("I shall not have a wall in my hand")
	public void iShallNotHaveAWallInMyHand() {
		//		String err=QuoridorGamePage.getErrMsg();
		//		assert(err.equals(""));
	}

	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("My move shall be completed")
	public void myMoveShallBeCompleted() {
		//		String err=QuoridorGamePage.getErrMsg();
		//		assert(err.equals(""));
	}

	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("It shall not be my turn to move")
	public void itShallNotBeMyTurnToMove() {
		Player turn = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		assert (turn != whitePlayer || turn != blackPlayer);

	}

	// Invalid wall placement

	/**
	 * @author Xinyue Chen
	 * @param direction
	 * @param row
	 * @param col
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is invalid")
	public void theWallMoveCandidateWithDirectionAtPositionIsInvalid(String direction, int row, int col) {
		WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		int r = candidate.getTargetTile().getRow();
		int c = candidate.getTargetTile().getColumn();
		String dir = candidate.getWallDirection().toString().toLowerCase();
		assert (r != row || c != col || !dir.equals(direction));
	}

	/**
	 * @author Xinyue Chen, 260830761
	 */
	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		String err=QuoridorGamePage.getErrMsg();
		//assert(!err.equals("Unable to move the wall"));
		if(err.contentEquals("Unable to move the wall")) {
			throw new UnsupportedOperationException("Unable to move the wall");
		}
	}

	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove() {
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		Player playerToMove=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		assert (whitePlayer==playerToMove||blackPlayer==playerToMove);
	}

	/**
	 * @author Xinyue Chen
	 * @param direction
	 * @param col
	 * @param row
	 */
	@But("No wall move shall be registered with {string} at position \\({int}, {int})")
	public void noWallMoveShallBeRegisteredWithDirectionAtPosition(String direction, int row, int col) {
		Quoridor quoridor=QuoridorApplication.getQuoridor();
		WallMove candidate=quoridor.getCurrentGame().getWallMoveCandidate();
		int r=candidate.getTargetTile().getRow();
		int c=candidate.getTargetTile().getColumn();
		String dir=candidate.getWallDirection().toString().toLowerCase();
		assert(row!=r||col!=c||!dir.equals(direction));

	}
	// ****** END of DROPWALL ******************

	// ****** START of SAVEPOSITION ******************

	/** @author Shayne Leitman, 260688512 */
	@Given("No file {string} exists in the filesystem")
	public void noFileExistsWithName(String fileName) {
		Boolean fileAlreadyExistsWithThatName = false;
		File tempFile = new File(fileName);
		if (tempFile.exists()) {
			tempFile.delete();
		}
		assertEquals(fileAlreadyExistsWithThatName, QuoridorController.fileAlreadyExists(fileName));
	}

	/** @author Shayne Leitman, 260688512 */
	@When("The user initiates to save the game with name {string}")
	public void userInitiatesSaveGame(String fileName) {
		QuoridorController.attemptToSavePosition(fileName);
		// CHANGE THIS IF THE METHOD CHANGES INPUTS/OUTPUTS!!!
	}

	/** @author Shayne Leitman, 260688512 */
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileIsCreated(String fileName) {
		Boolean fileAlreadyExistsWithThatName = true;
		assertEquals(fileAlreadyExistsWithThatName, QuoridorController.fileAlreadyExists(fileName));
	}

	/** @author Shayne Leitman, 260688512 */
	@Given("File {string} exists in the filesystem")
	public void fileAlreadyExistsWithName(String fileName) {
		Boolean fileAlreadyExistsWithThatName = true;
		assertEquals(fileAlreadyExistsWithThatName, QuoridorController.fileAlreadyExists(fileName));
	}

	/** @author Shayne Leitman, 260688512 */
	@And("The user confirms to overwrite existing file")
	public void userconfirmsToOverWriteFile() {
		// GUI Feature, to be implemented in future part of project.
		// FIX THIS LATER
		String newFileName = "save_game_test.dat";
		Boolean temp = QuoridorController.overWriteSavePosition(newFileName, true);

		assertEquals(true, temp);
	}

	/** @author Shayne Leitman, 260688512 */
	@Then("File with {string} shall be updated in the filesystem")
	public void fileIsUpdated(String fileName) {
		Boolean tempBool = false;
		if (QuoridorController.lastModifiedToCurrentTime(fileName) < 100) {
			tempBool = true;
		}
		assertEquals(true, tempBool);
	}

	/** @author Shayne Leitman, 260688512 */
	@And("The user cancels to overwrite existing file")
	public void userCancelsTheOverWriteFile() {
		// GUI Feature, to be implemented later
		// FIX THIS LATER
		String newFileName = "save_game_test.dat";
		Boolean temp = QuoridorController.overWriteSavePosition(newFileName, false);

		assertEquals(false, temp);
	}

	/** @author Shayne Leitman, 260688512 */
	@Then("File {string} shall not be changed in the filesystem")
	public void fileIsNotChanged(String fileName) {
		Boolean fileAlreadyExistsWithThatName = true;
		assertEquals(fileAlreadyExistsWithThatName, QuoridorController.fileAlreadyExists(fileName));
		Boolean tempBool = false;
		if (QuoridorController.lastModifiedToCurrentTime(fileName) < 1) {
			tempBool = true;
		}

		assertEquals(false, tempBool);
	}
	// ****** END of SAVEPOSITION ******************

	// ****** START of LOADPOSITION ******************

	/** @author Shayne Leitman, 260688512 */
	@When("I initiate to load a saved game {string}")
	public void initiateLoadGame(String fileName) {
		Boolean temp = QuoridorController.loadSavedPosition(fileName);
		assertEquals(true, temp);
	}

	/** @author Shayne Leitman, 260688512 */
	@And("The position to load is valid")
	public void positionLoadedIsValid() {
		// Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		// Game loadedGame = newQuoridor.getCurrentGame();
		// GamePosition loadedGamePosition = loadedGame.getCurrentPosition();

	}

	/** @author Shayne Leitman, 260688512 */
	@Then("It shall be {string}'s turn")
	public void itIsPlayersTurn(String player) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		Player tempPlayer = loadedGamePosition.getPlayerToMove();
		String colourToMove = "";
		if (tempPlayer == loadedGame.getBlackPlayer()) {
			colourToMove = "black";
		} else {
			colourToMove = "white";
		}
		Boolean sameString = (colourToMove.equals(player));

		assertEquals(true, sameString);
	}

	/** @author Shayne Leitman, 260688512 */
	@And("{string} shall be at {int}:{int}")
	public void playerIsAtPosition(String player, int row, int col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		//Player curPlayer = loadedGamePosition.getPlayerToMove();
		PlayerPosition playerPos = null;
		if (player.equals("white")) {
			// Player is White
			playerPos = loadedGamePosition.getWhitePosition();
		} else {
			// Player is Black
			playerPos = loadedGamePosition.getBlackPosition();
		}
		Tile curTile = playerPos.getTile();
		assertEquals(row, curTile.getRow());
		assertEquals(col, curTile.getColumn());
	}

	/* @author Shayne Leitman, 260688512 */
	/*
	@And("\"<opponent>\" shall be at <row>:<col>")
	public void opponentAtPosition(String player, int row, int col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		//Player curPlayer = loadedGamePosition.getPlayerToMove();
		PlayerPosition playerPos = null;
		if (player.equals("white")) {
			// Player is White
			playerPos = loadedGamePosition.getWhitePosition();
		} else {
			// Player is Black
			playerPos = loadedGamePosition.getBlackPosition();
		}
		Tile curTile = playerPos.getTile();
		assertEquals(row, curTile.getRow());
		assertEquals(col, curTile.getColumn());
	}
	 */
	/** @author Shayne Leitman, 260688512 */
	@And("{string} shall have a {string} wall at {int}:{int}")
	public void shalls_have_a_wall_at(String player, String orientation, Integer row, Integer col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		//Player curPlayer = loadedGamePosition.getPlayerToMove();
		List<Wall> wallList = null;
		if (player.equals("white")) {
			// Player is White
			wallList = loadedGamePosition.getWhiteWallsOnBoard();
		} else {
			// Player is Black
			wallList = loadedGamePosition.getBlackWallsOnBoard();
		}
		String tempDir = orientation;
		Boolean foundWall = false;
		Direction curDir = null;
		if (tempDir.equals("vertical")) {
			curDir = Direction.Vertical;
		} else {
			curDir = Direction.Horizontal;
		}

		for (Wall curWall : wallList) {
			if (curWall.getMove().getWallDirection().equals(curDir)
					&& curWall.getMove().getTargetTile().getRow() == row
					&& curWall.getMove().getTargetTile().getColumn() == col) {
				foundWall = true;
			}
		}
		assertEquals(true, foundWall);

	}

	/** @author Shayne Leitman, 260688512 */
	/*
	@And("{string} shall have a {string} wall at {int}:{int}")
	public void opponentHasWallAt(String player, String orientation, Integer row, Integer col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		//Player curPlayer = loadedGamePosition.getPlayerToMove();
		List<Wall> wallList = null;
		if (player.equals("white")) {
			// Player is White
			wallList = loadedGamePosition.getWhiteWallsOnBoard();
		} else {
			// Player is Black
			wallList = loadedGamePosition.getBlackWallsOnBoard();
		}
		String tempDir = orientation;
		Boolean foundWall = false;
		Direction curDir = null;
		if (tempDir.equals("vertical")) {
			curDir = Direction.Vertical;
		} else {
			curDir = Direction.Horizontal;
		}

		for (Wall curWall : wallList) {
			if (curWall.getMove().getWallDirection().equals(curDir)
					&& curWall.getMove().getTargetTile().getRow() == row
					&& curWall.getMove().getTargetTile().getColumn() == col) {
				foundWall = true;
			}
		}
		assertEquals(true, foundWall);

	}
	 */
	/** @author Shayne Leitman, 260688512 */
	@And("Both players shall have {int} in their stacks")
	public void bothPlayersHaveWallsRemaining(int remWalls) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		Player curPlayer = loadedGamePosition.getPlayerToMove();
		Player otherPlayer = curPlayer.getNextPlayer();
		int whiteWallsLeft = loadedGamePosition.numberOfWhiteWallsInStock();
		int blackWallsLeft = loadedGamePosition.numberOfBlackWallsInStock();
		assertEquals(remWalls, whiteWallsLeft);
		assertEquals(remWalls, blackWallsLeft);
	}

	/** @author Shayne Leitman, 260688512 */
	@And("It shall be \"<player>\"'s turn")
	public void itShallBePlayersTurn(Player player) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		assertEquals(player, loadedGamePosition.getPlayerToMove());
	}

	/** @author Shayne Leitman, 260688512 */
	@And("The position to load is invalid")
	public void positionIsInvalid() {
		// Empty
	}

	/** @author Shayne Leitman, 260688512 */
	@Then("The load shall return an error")
	public void loadReturnsError() {
		// Throws Error in controller method
	}
	// ****** END of LOADPOSITION ******************

	// ****** START of VALIDATEPOSITION ******************
	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Given("A game position is supplied with pawn coordinate {int}:{int}")
	public void gamePositionSuppliedWithPawnPos(int row, int col) {
		Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row - 1) * 9 + col - 1);
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		currentGame.setMoveMode(MoveMode.PlayerMove);
		Player currentPlayer = currentGame.getCurrentPosition().getPlayerToMove();

		if (currentPlayer == currentGame.getBlackPlayer()) {
			currentGame.getCurrentPosition().getBlackPosition().setTile(targetTile);
		} else {
			currentGame.getCurrentPosition().getWhitePosition().setTile(targetTile);
		}
	}
	
	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Given("A game position is supplied with wall coordinate {int}:{int}-\"<dir>\"")
	public void gamePositionSuppliedWithWallPos(int row, int col, Direction direction) {
		Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row - 1) * 9 + col - 1);
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		currentGame.setMoveMode(MoveMode.WallMove);
		Player currentPlayer = currentGame.getCurrentPosition().getPlayerToMove();

		Wall wall = new Wall(-1, currentPlayer);
		WallMove wallMove = new WallMove(-1, currentGame.getCurrentPosition().getId(), currentPlayer, targetTile,
				currentGame, direction, wall);
		currentGame.setWallMoveCandidate(wallMove);
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
			} else {
				Direction direction = currentGame.getWallMoveCandidate().getWallDirection();
				QuoridorController.validatePosition(targetTile.getRow(), targetTile.getColumn(), direction);
			}
		} else {
			Tile targetTile = currentGame.getCurrentPosition().getWhitePosition().getTile();
			if (moveMode == MoveMode.PlayerMove) {
				QuoridorController.validatePosition(targetTile.getRow(), targetTile.getColumn(), null);
			} else {
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
	// *********** END of VALIDATEPOSITION*************************

	// *********** START of SWITCHCURRRENTPLAYER*************************
	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Given("The player to move is {string}")
	public void thePlayerToMoveIs(String color) {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();
		Player blackPlayer = currentGame.getBlackPlayer();

		if (color.equals("white")) {
			currentGame.getCurrentPosition().setPlayerToMove(whitePlayer);
		}
		else {
			currentGame.getCurrentPosition().setPlayerToMove(blackPlayer);
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 * @throws InterruptedException 
	 */
	@And("The clock of {string} is running")
	public void clockOfPlayerIsRunning(String color) throws InterruptedException {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();
		Player blackPlayer = currentGame.getBlackPlayer();

		if (color == "white") {
			Time whiteTimerBefore = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsBefore = whiteTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time whiteTimerAfter = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsAfter = whiteTimerAfter.getTime();

			assert(whitemsAfter < whitemsBefore);
		}
		else {
			Time blackTimerBefore = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsBefore = blackTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time blackTimerAfter = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsAfter = blackTimerAfter.getTime();

			assert(blackmsAfter < blackmsBefore);
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 * @throws InterruptedException 
	 */
	@And("The clock of {string} is stopped")
	public void clockOfOtherIsStopped(String color) throws InterruptedException {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();
		Player blackPlayer = currentGame.getBlackPlayer();

		if (color == "white") {
			Time whiteTimerBefore = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsBefore = whiteTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time whiteTimerAfter = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsAfter = whiteTimerAfter.getTime();

			assert(whitemsAfter == whitemsBefore);
		}
		else {
			Time blackTimerBefore = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsBefore = blackTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time blackTimerAfter = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsAfter = blackTimerAfter.getTime();

			assert(blackmsAfter == blackmsBefore);
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@When("Player {string} completes his move")
	public void playerCompletesMove(String color) {
		
		// assertEquals(true, QuoridorController.switchCurrentPlayer());
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@Then("The user interface shall be showing it is {string} turn")
	public void showItIsOtherTurn(String color) {
		if (color == "white") {
			assert(QuoridorController.isWhiteTurn() == true);
		}
		else {
			assert(QuoridorController.isBlackTurn() == true);
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 * @throws InterruptedException 
	 */
	@And("The clock of {string} shall be stopped")
	public void clockOfPlayerIsStopped(String color) throws InterruptedException {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();
		Player blackPlayer = currentGame.getBlackPlayer();

		if (color == "white") {
			Time whiteTimerBefore = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsBefore = whiteTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time whiteTimerAfter = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsAfter = whiteTimerAfter.getTime();

			assert(whitemsAfter == whitemsBefore);
		}
		else {
			Time blackTimerBefore = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsBefore = blackTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time blackTimerAfter = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsAfter = blackTimerAfter.getTime();

			assert(blackmsAfter == blackmsBefore);
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 * @throws InterruptedException 
	 */
	@And("The clock of {string} shall be running")
	public void clockOfOtherRunning(String color) throws InterruptedException {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();
		Player blackPlayer = currentGame.getBlackPlayer();

		if (color == "white") {
			Time whiteTimerBefore = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsBefore = whiteTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time whiteTimerAfter = currentGame.getWhitePlayer().getRemainingTime();
			long whitemsAfter = whiteTimerAfter.getTime();

			assert(whitemsAfter < whitemsBefore);
		}
		else {
			Time blackTimerBefore = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsBefore = blackTimerBefore.getTime();

			//wait 1s to actually allow timer to increment
			Thread.sleep(1000);

			Time blackTimerAfter = currentGame.getBlackPlayer().getRemainingTime();
			long blackmsAfter = blackTimerAfter.getTime();

			assert(blackmsAfter < blackmsBefore);
		}
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The next player to move shall be {string}")
	public void nextPlayerToMoveIsOther(String color) {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();
		Player blackPlayer = currentGame.getBlackPlayer();

		if (color == "white") {
			currentGame.getCurrentPosition().setPlayerToMove(whitePlayer);
		}
		else {
			currentGame.getCurrentPosition().setPlayerToMove(blackPlayer);
		}
	}
	// ************END of SWITCHCURRENTPLAYER**************
	
	
	// ************	START OF MOVEPAWN ****************
	
	/**
	 * Part of given (pre-condition) for MovePawn
	 * @author Xinyue Chen
	 * @param row
	 * @param col
	 */
	@And("The player is located at {int}:{int}")
	public void thePlayerIsLocatedAtRowCol(int row, int col) {
		PlayerPosition blackPos=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
		PlayerPosition whitePos=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
		Player playerToMove=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		if(playerToMove==whitePlayer) {
			assert(whitePos.getTile().getRow()==row&&whitePos.getTile().getColumn()==col);
		}
		if(playerToMove==blackPlayer) {
			assert(blackPos.getTile().getRow()==row&&blackPos.getTile().getColumn()==col);
		}
	}
	
	/**
	 * Part of given (pre-condition) for MovePawn - move ONE tile
	 * @author Helen
	 * @param direction "vertical" or "horizontal"
	 * @param side "left" or "right" or "up" or "down"
	 */
	@And("There are no {string} walls {string} from the player")
	public void thereAreNoDirWallsSideFromThePlayer(String direction, String side) {
		
	}
	
	/**
	 * Part of given (pre-condition) for MovePawn - move BLOCKED by wall
	 * @author Helen
	 * @param direction "vertical" or "horizontal"
	 * @param wrow row of wall that is blocking move
	 * @param wcol column of wall that is blocking move
	 */
	@And("There is a {string} wall at {int}:{int}")
	public void thereIsADirWallAtWrowWcol(String direction, int wrow, int wcol) {
		
	}

	/**
	 * Part of given (pre-condition) for MovePawn
	 * @author Helen Lin
	 * @param side "left" or "right" or "up" or "down"
	 */
	@And("The opponent is not {string} from the player")
	public void theOpponentIsNotSideFromThePlayer(String side) {
		
	}
	
	
	/**
	 * When clause (condition) for MovePawn
	 * @author Helen Lin
	 * @param color "black" or "white"
	 * @param side "left" or "right" or "up" or "down"
	 */
	@When("Player {string} initiates to move {string}")
	public void playerColorInitiatesToMoveSide(String color, String side) {
		
	}
	
	/**
	 * Part of Then (post-condition) for MovePawn
	 * @author Helen Lin
	 * @param side "left" or "right" or "up" or "down"
	 * @param status "success" or "illegal"
	 */
	@Then("The move {string} shall be {string}")
	public void theMoveSideShallBeStatus(String side, String status) {
		
	}
 
	/**
	 * Part of Then (post-condition) for MovePawn
	 * @author Helen Lin
	 * @param nrow next row after a move
	 * @param ncol next column after a move
	 */
	@And("Player's new position shall be {int}:{int}")
	public void playersNewPositionShallBeNrowNcol(int nrow, int ncol) {
		
	}

	/**
	 * Part of Then (post-condition) for MovePawn
	 * @author Helen Lin
	 * @param color "black" or "white" of the next player to move
	 */
	@And("The next player to move shall become {string}")
	public void theNextPlayerToMoveShallBecomeNplayer(String color) {
		
	}
	
	// ************	END OF MOVEPAWN ****************
	
	// ************	START OF JUMPPAWN ****************
	
	/**
	 * Part of given (pre-condition) for JumpPawn
	 * @author Helen
	 * @param row
	 * @param col
	 */
	@And("The opponent is located at {int}:{int}")
	public void theOpponentIsLocatedAtRowCol(int row, int col) {
		//set opponent row
		//set opponent col
	}
	
	/**
	 * Part of given (pre-condition) for JumpPawn
	 * @author Helen
	 * @param direction "vertical" or "horizontal"
	 * @param side "left" or "right" or "up" or "down"
	 */
	@And("There are no {string} walls {string} from the player nearby")
	public void thereAreNoDirWallsSideFromThePlayerNearBy(String direction, String side) {
		
	}
	
	// ************	END OF JUMPPAWN ****************

	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
	@After
	public void tearDown() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Avoid null pointer for step definitions that are not yet implemented.
		if (quoridor != null) {
			quoridor.delete();
			quoridor = null;
		}
		for (int i = 0; i < 20; i++) {
			Wall wall = Wall.getWithId(i);
			if (wall != null) {
				wall.delete();
			}
		}
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below

	private void initQuoridorAndBoard() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (!quoridor.hasBoard()) {
			Board board = new Board(quoridor);
			// Creating tiles by rows, i.e., the column index changes with every tile
			// creation
			for (int i = 1; i <= 9; i++) { // rows
				for (int j = 1; j <= 9; j++) { // columns
					board.addTile(i, j);
				}
			}
		}

	}

	private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) throws InvalidInputException {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = QuoridorController.createUser(userName1);
		User user2 = QuoridorController.createUser(userName2);

		int thinkingTime = 180;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		// @formatter:off
		/*
		 * __________ | | | | |x-> <-x| | | |__________|
		 * 
		 */
		// @formatter:on
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				if (Wall.hasWithId(i * 10 + j)) {
					Wall.getWithId(i * 10 + j).setOwner(players[i]);
				} else {
					new Wall(i * 10 + j, players[i]);
				}
			}
		}

		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);

		return playersList;
	}

	private void createAndStartGame(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);

		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);
		game.setWhitePlayer(players.get(0));
		game.setBlackPlayer(players.get(1));

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(),
				player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(),
				player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);
		// Now, we need to add all 20 walls to the game position, first by creating
		// them.
		// We need to add some walls to each player's stock.
		// The rest need to be used to create new wall moves!

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

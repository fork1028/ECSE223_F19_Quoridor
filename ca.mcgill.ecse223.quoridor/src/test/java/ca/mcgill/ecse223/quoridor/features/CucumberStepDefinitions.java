package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
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
import ca.mcgill.ecse223.quoridor.view.QuoridorGamePage;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

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
		// GUI-related feature 
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

	/** @author matteo barbieri 260805184 */
	@When("A new game is being initialized")
	public void aNewGameisbeingInitialized() {
		Time zero = new Time(0);
		User user1 = new User("user01", QuoridorApplication.getQuoridor());
		User user2 = new User("user02", QuoridorApplication.getQuoridor());
		QuoridorController.startGame(user1, user2, zero);
	}

	/** @author matteo barbieri 260805184 */
	@And("White player chooses a username")
	public void whitePlayerChoosesAUsername() {
		QuoridorController.setUserToPlayer("user1", false);
	}

	/** @author matteo barbieri 260805184 */
	@And("Black player chooses a username")
	public void blackPlayerChoosesAUsername() {
		QuoridorController.setUserToPlayer("user2", true);
	}

	/** @author matteo barbieri 260805184 */
	@And("Total thinking time is set")
	public void totalThinkingTimeIsSet(Time thinkingTime) {
		QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setRemainingTime(thinkingTime);
		QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setRemainingTime(thinkingTime);
	}

	/** @author matteo barbieri 260805184 */
	@Then("The game shall become ready to start")
	public void theGameShallBecomeReadyToStart(User userBlack, User userWhite, Time thinkingTime) {
		Player black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		Player white = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		GameStatus status = QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus();
		assert (status == GameStatus.Initializing);
		assert (black.getUser() == userBlack);
		assert (white.getUser() == userWhite);
		assert (black.getRemainingTime() == white.getRemainingTime());
		assert (white.getRemainingTime() == thinkingTime);
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.ReadyToStart);

	}

	/** @author matteo barbieri 260805184 
	 * @throws InvalidInputException */
	@Given("The game is ready to start")
	public void gameIsReadyToStart() throws InvalidInputException {
		initQuoridorAndBoard();
		List<Player> players = createUsersAndPlayers("user1", "user2");
		Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, QuoridorApplication.getQuoridor());
		QuoridorApplication.getQuoridor().getCurrentGame().setWhitePlayer(players.get(0));
		QuoridorApplication.getQuoridor().getCurrentGame().setBlackPlayer(players.get(1));
		QuoridorController.setTotalThinkingTime(2,0);
	}

	/** @author matteo barbieri 260805184 */
	@When("I start the clock")
	public void whenIStartTheClock() {
		// Start the clock GUI
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
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
		QuoridorController.initializeBoard();
	}
	// *******END of STARTNEWGAME****************************************

	// *******START of PROVIDESELECTUSERNAME****************************************

	/** @author matteo barbieri 260805184 */
	@Given("Next player to set user name is {string}")
	public void selectExistingUser(String colour) {
		//// GUI black player decides to click on drop down menu of users or type in a
		//// username
	}

	/** @author matteo barbieri 260805184 */
	@And("There is existing user {string}")
	public void thereIsExistingUsername(String username) {
		QuoridorApplication.getQuoridor().addUser(username); // add the user to set pre-condition that there is already
																// this user

	}

	/** @author matteo barbieri 260805184 */
	@When("The player selects existing {string}")
	public void thePlayerSelectsExistingUsername(String username) {
		QuoridorController.provideSelectUser(username);
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

	/** @author matteo barbieri 260805184 */
	@Given("Next player to set user name is black3")
	public void selectExistingUser2() {
		// GUI black player decides to click on drop down menu of users or type in a
		// username
	}

	/** @author matteo barbieri 260805184 */
	@And("There is no existing user username username1")
	public void thereIsnoExistingUsername(int idx) {
		QuoridorApplication.getQuoridor().getUsers();
		// GUI the username Black player wants to use does not exist in the drop down
		// menu

	}

	/** @author matteo barbieri 260805184 */
	@When("The player provides new username username1")
	public void thePlayerProvidesNewUsername(String username1) {
		QuoridorController.provideSelectUser(username1);
		QuoridorApplication.getQuoridor().addUser(username1);
	}

	/** @author matteo barbieri 260805184 */
	@Then("The name of player black in the new game shall be username1")
	public void assignNewNameOfPlayerToColor(int idxusername1) {

		Player black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		black.setUser(QuoridorApplication.getQuoridor().getUser(idxusername1));

	}

	/** @author matteo barbieri 260805184 */
	@Given("Next player to set user name is black")
	public void selectExistingUser3() {
		// GUI black player decides to click on drop down menu of users or type in a
		// username
	}

	/** @author matteo barbieri 260805184 */
	@And("There is existing username username2")
	public void userNameExists(int idx) {
		QuoridorApplication.getQuoridor().getUsers();
		// GUI the username Black player wants to use exists in the drop down window
		// idx is the number in the list of usernames
		QuoridorApplication.getQuoridor().getUser(idx);
	}

	/** @author matteo barbieri 260805184 */
	@When("The player provides username username2")
	public void playerProvidesUser(String username2) {
		QuoridorController.provideSelectUser(username2);
		User user2 = new User(username2, QuoridorApplication.getQuoridor());
		assert (QuoridorApplication.getQuoridor().getUsers().contains(user2));
	}

	/** @author matteo barbieri 260805184 */
	@Then("The player shall be warned ")
	public void nameWarning() {
		// GUI pop up window with warning appears
	}

	/** @author matteo barbieri 260805184 */
	@And("The next player to set user name shall be white")
	public void setNextPlayer() {
		// GUI now the screen is ready to select white players username
		QuoridorApplication.getQuoridor().getCurrentGame().setWhitePlayer(null);
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
		assertEquals(10, whiteStock);
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
	 */
	@And("White's clock shall be counting down")
	public void whitesClockIsCountingDown() {

		//TODO: depends on start clock in start game
		Time white = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime();
		long whitems = white.getTime();
		Time black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime();
		long blackms = black.getTime();
		assertTrue(whitems < blackms);
		
//		//GUI TODO: check countdown gui

	}

	/**
	 * @author Helen Lin, 260715521
	 */
	@And("It shall be shown that this is White's turn")
	public void itIsShownThatItIsWhitesTurn() {
		// TODO GUI for later
	}

	// ****** END of INITIALIZEBOARD ******************

	// ****** START of GRABWALL ******************

	/** * @author Rajaa Boukhelif, 260870030 */
	@Given("I have more walls on stock")
	public void thereAreWallsOnStock() {

		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		assert (player.hasWalls() == true);
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@When("I try to grab a wall from my stock")
	public void userTriesGrabWall1() {

		Player currentplayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove();
		// WallMove move =
		// QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		QuoridorController.grabWall(currentplayer);
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
		// GUI
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@And("The wall in my hand shall disappear from my stock")

	public void theWallInMyHandShouldDisappearFromMyStock() {
		// GUI
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallsOnStock() {
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		assert (player.hasWalls() == false);
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@Then("I shall be notified that I have no more walls")
	public void iShouldBeNotifiedThatIHaveNoMoreWalls() {
		// GUI
	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@And("I shall have no walls in my hand")
	public void iShallHaveNoWallsInMyHand() {
		// GUI
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
		Direction direction1 = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate()
				.getWallDirection();
		String dir = direction1.toString();
		assert (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate()
				.getWallDirection() == direction1);

	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@When("I try to flip the wall")
	public void userTriesRotateWall() {
		Direction direction = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate()
				.getWallDirection();

	}

	/** * @author Rajaa Boukhelif, 260870030 */
	@Then("The wall shall be rotated over the board to {string}")
	public void theWallShallBeRotatedOverTheBoardToNewDIrection(String direction) {
		// QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(direction);
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
		// GUI related
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
		assert (candidate != null);
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
		String err=QuoridorGamePage.getErrMsg();
		assert(err.equals(""));
	}

	/**
	 * @author Xinyue Chen, 260830761
	 */
	@And("My move shall be completed")
	public void myMoveShallBeCompleted() {
		String err=QuoridorGamePage.getErrMsg();
		assert(err.equals(""));
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
	@Then("It shall be \"<players>\"'s turn")
	public void itIsPlayersTurn(Player players) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		assertEquals(players, loadedGamePosition.getPlayerToMove());
	}

	/** @author Shayne Leitman, 260688512 */
	@And("\"<player>\" shall be at <row>:<col>")
	public void playerIsAtPosition(Player player, int row, int col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		Player curPlayer = loadedGamePosition.getPlayerToMove();
		PlayerPosition playerPos = null;
		if (curPlayer.equals(loadedGame.getWhitePlayer())) {
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

	/** @author Shayne Leitman, 260688512 */
	@And("\"<opponent>\" shall be at <row>:<col>")
	public void opponentAtPosition(Player player, int row, int col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		Player curPlayer = loadedGamePosition.getPlayerToMove().getNextPlayer();
		PlayerPosition playerPos = null;
		if (curPlayer.equals(loadedGame.getWhitePlayer())) {
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

	/** @author Shayne Leitman, 260688512 */
	@And("\"<player>\" shall have a \"<orientation>\" wall at <row>:<col>")
	public void playerHasWallAt(Player player, Direction orientation, int row, int col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		Player curPlayer = loadedGamePosition.getPlayerToMove();
		List<Wall> wallList = null;
		if (curPlayer.equals(loadedGame.getWhitePlayer())) {
			// Player is White
			wallList = loadedGamePosition.getWhiteWallsOnBoard();
		} else {
			// Player is Black
			wallList = loadedGamePosition.getBlackWallsOnBoard();
		}
		Boolean foundWall = false;
		for (Wall curWall : wallList) {
			if (curWall.getMove().getWallDirection().equals(orientation)
					&& curWall.getMove().getTargetTile().getRow() == row
					&& curWall.getMove().getTargetTile().getColumn() == col) {
				foundWall = true;
			}
		}
		assertEquals(true, foundWall);

	}

	/** @author Shayne Leitman, 260688512 */
	@And("\"<opponent>\" shall have a \"<orientation>\" wall at <row>:<col>")
	public void opponentHasWallAt(Player player, Direction orientation, int row, int col) {
		Quoridor newQuoridor = QuoridorApplication.getQuoridor();
		Game loadedGame = newQuoridor.getCurrentGame();
		GamePosition loadedGamePosition = loadedGame.getCurrentPosition();
		Player curPlayer = loadedGamePosition.getPlayerToMove().getNextPlayer();
		List<Wall> wallList = null;
		if (curPlayer.equals(loadedGame.getWhitePlayer())) {
			// Player is White
			wallList = loadedGamePosition.getWhiteWallsOnBoard();
		} else {
			// Player is Black
			wallList = loadedGamePosition.getBlackWallsOnBoard();
		}
		Boolean foundWall = false;
		for (Wall curWall : wallList) {
			if (curWall.getMove().getWallDirection().equals(orientation)
					&& curWall.getMove().getTargetTile().getRow() == row
					&& curWall.getMove().getTargetTile().getColumn() == col) {
				foundWall = true;
			}
		}
		assertEquals(true, foundWall);

	}

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

	// ****** END of VALIDATEPOSITION ******************
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
	@Given("The player to move is \"<player>\"")
	public void thePlayerToMoveIs(Player player) {
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(player);
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The clock of \"<player>\" is running")
	public void clockOfPlayerIsRunning() {
		assert (QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
				.getRemainingTime()) != null;
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The clock of \"<other>\" is stopped")
	public void clockOfOtherIsStopped() {
		try {
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getNextPlayer()
					.getRemainingTime().wait();
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
		// assertEquals(true, QuoridorController.switchCurrentPlayer());
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
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
				.setRemainingTime(timeElapsed);
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The clock of \"<other>\" shall be running")
	public void clockOfOtherRunning() {
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().getNextPlayer()
				.getRemainingTime().notify();
	}

	/**
	 * @author Sami Junior Kahil, 260834568
	 */
	@And("The next player to move shall be \"<other>\"")
	public void nextPlayerToMoveIsOther() {
		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}
	// ************END of SWITCHCURRENTPLAYER**************

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
		Board board = new Board(quoridor);
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
		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

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
				new Wall(i * 10 + j, players[i]);
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

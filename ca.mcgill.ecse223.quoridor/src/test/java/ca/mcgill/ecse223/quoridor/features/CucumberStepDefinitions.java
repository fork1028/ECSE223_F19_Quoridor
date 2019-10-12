package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
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
			//Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
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
			new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1), quoridor.getCurrentGame(), direction, wall);
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
	/** @author matteo barbieri 260805184 */
	@When ("A new game is being initialized")
	public void aNewGameisbeingInitialized() {
		Time zero = new Time(0);
		User user1 = new User("user01", QuoridorApplication.getQuoridor());
		User user2 = new User("user02", QuoridorApplication.getQuoridor());	
		QuoridorController.startGame(user1, user2, zero);
	}
	/** @author matteo barbieri 260805184 */
	@And("White player chooses a username")
	public void whitePlayerChoosesAUsername(User userWhite) {
		QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setUser(userWhite);	
	}
	/** @author matteo barbieri 260805184 */
	@And("Black player chooses a username")
	public void blackPlayerChoosesAUsername(User userBlack) {
		QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setUser(userBlack);		
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
	/** @author matteo barbieri 260805184 */
	@Given("The Game is ready to start")
		public void gameIsReadyToStart() {
		assert(QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus() == GameStatus.ReadyToStart);
	}
	/** @author matteo barbieri 260805184 */
	@When (" I start the clock")
	public void whenIStartTheClock() {
		//Start the clock GUI
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
	}
	/** @author matteo barbieri 260805184 */
	@Then ("The game shall be running")
	public void theGameShallBeRunning(){
		assert(QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus() == GameStatus.Running);
	}
	/** @author matteo barbieri 260805184 */
	@And("he board shall be initialized")
	public void theBoardShallBeInitialized() {
		QuoridorController.initializeBoard();
	}
	/** @author matteo barbieri 260805184 */
	@Given("Next player to set user name is black2")
	public void selectExistingUser() {
		////GUI black player decides to click on drop down menu of users or type in a username
	}
	/** @author matteo barbieri 260805184 */
	@And("There is existing username username0")
	public void thereIsExistingUsername(int idx) {
		QuoridorApplication.getQuoridor().getUsers();
		//GUI the username Black player wants to use exists in the drop down window
		//idx is the number in the list of usernames
		QuoridorApplication.getQuoridor().getUser(idx);
	}
	/** @author matteo barbieri 260805184 */
	@When("The player selects existing username0")
	public void thePlayerSelectsExistingUsername(int idx) {
		QuoridorController.ProvideSelectUser();
		QuoridorApplication.getQuoridor().getUser(idx);
	}
	/** @author matteo barbieri 260805184 */
	@Then("Then The name of player black in the new game shall be username0")
	public void assignNameOfPlayerToColor(int idx) {
		
		Player black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		black.setUser(QuoridorApplication.getQuoridor().getUser(idx));
		
	}
	
	/** @author matteo barbieri 260805184 */
	@Given("Next player to set user name is black3")
	public void selectExistingUser2() {
		//GUI black player decides to click on drop down menu of users or type in a username
	}
	/** @author matteo barbieri 260805184 */
	@And("There is no existing user username username1")
	public void thereIsnoExistingUsername(int idx) {
		QuoridorApplication.getQuoridor().getUsers();
		//GUI the username Black player wants to use does not exist in the drop down menu
		
	}
	/** @author matteo barbieri 260805184 */
	@When("The player provides new username username1")
	public void thePlayerProvidesNewUsername(String username1) {
		QuoridorController.ProvideSelectUser();
		QuoridorApplication.getQuoridor().addUser(username1);
	}
	/** @author matteo barbieri 260805184 */
	@Then("Then The name of player black in the new game shall be username1")
	public void assignNewNameOfPlayerToColor(int idxusername1) {
		
		Player black = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		black.setUser(QuoridorApplication.getQuoridor().getUser(idxusername1));
		
	}
	/** @author matteo barbieri 260805184 */
	@Given("Next player to set user name is black")
	public void selectExistingUser3() {
		//GUI black player decides to click on drop down menu of users or type in a username
	}

	/** @author matteo barbieri 260805184 */
	@And("There is existing username username2")
	public void userNameExists(int idx) {
		QuoridorApplication.getQuoridor().getUsers();
		//GUI the username Black player wants to use exists in the drop down window
		//idx is the number in the list of usernames
		QuoridorApplication.getQuoridor().getUser(idx);
	}
	
	/** @author matteo barbieri 260805184 */
	@When("The player provides username username2")
	public void playerProvidesUser(String username2) {
		QuoridorController.ProvideSelectUser();
		User user2 = new User(username2, QuoridorApplication.getQuoridor());
		assert(QuoridorApplication.getQuoridor().getUsers().contains(user2));
	}
	
	/** @author matteo barbieri 260805184 */
	@Then("The player shall be warned ")
	public void nameWarning() {
		//GUI pop up window with warning appears
	}
	/** @author matteo barbieri 260805184 */
	@And("The next player to set user name shall be white")
	public void setNextPlayer() {
		//GUI now the screen is ready to select white players username
		QuoridorApplication.getQuoridor().getCurrentGame().setWhitePlayer(null);
	}
	
	
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
			if(wall != null) {
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
		
		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, players.get(0), players.get(1), quoridor);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

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

package ca.mcgill.ecse223.quoridor.controller;

import java.sql.Time;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;


/*Instructions from Iteration 2 part 3:
 * You need to individually specify all operations for your assigned features in the Controller interface (which interface needs to be placed in the ca.mcgill.ecse223.quoridor.controller package). The interface consists of the full method declaration (incl. parameters, return type, etc.) relevant for your assigned features. In addition to all modifier methods, do not forget to include all query methods required for the features. The same query method may be used for several features. As a team, ensure the consistent use of query methods across features.
Each controller method needs to be briefly documented using JavaDoc (see public tutorials, e.g.: ), but you are not required to generate standalone HTML files. The JavaDoc specification should clearly state the name of the corresponding Gherkin feature and the name of the team member who is responsible for it.
Moreover, you need to create a default implementation of the operations present in the Control interface where the method body consists of throwing an java.lang.UnsupportedOperationException (i.e. the method body is almost empty).
*/

public class QuoridorController {
	//feature list of 12 features to be implemented:
	
	/**
	 * This method starts the game 
	 * @param blackPlayer  the user assigned to the black pawn
	 * @param whitePlayer  the user assigned to the black pawn
	 * @param time  total thinking time, in seconds
	 * @throws InvalidInputException
	 * @author Matteo Barbieri 260805184
	 */
	public static Game startGame(User blackPlayer, User whitePlayer,Time time) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Unable to start Game");
		
	}
	
	
	//2. Provide or select user name -- Matteo
	/**
	 * This method Provides or Selects a User to be used in a game
	 * @param username desired to create new User
	 * @throws InvalidInputException
	 * @author Matteo Barbieri 260805184
	 */
	public static boolean ProvideSelectUser() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Unable to set Username");
	}

	//3. Set total thinking time -- Helen
	/**
	 * This method sets the total thinking time (i.e. remaining time) that a white or black player has for a game.
	 * @param time	total thinking time, in seconds
	 * @param setForBlackPlayer	true if we are setting the black player's remaining time, false for a white player
	 * @throws InvalidInputException
	 * @author Helen Lin, 260715521
	 */
	public static void setTotalThinkingTime(Time time, boolean setForBlackPlayer) throws UnsupportedOperationException {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		try {
			if (setForBlackPlayer) {
				Player player = quoridor.getCurrentGame().getBlackPlayer();
				if (!player.setRemainingTime(time))
					throw new UnsupportedOperationException("Unable to set thinking time for BLACK player.");
			} else if (!setForBlackPlayer) {
				Player player = quoridor.getCurrentGame().getWhitePlayer();
				if (!player.setRemainingTime(time))
					throw new UnsupportedOperationException("Unable to set thinking time for WHITE player.");
			}
		}
		catch (RuntimeException e) {
			throw new UnsupportedOperationException(e.getMessage());
		}
		
	}
	
	//4. Initialize board -- Helen
	/**
	 * This method initializes a new board.
	 * @throws InvalidInputException
	 * @author Helen Lin, 260715521
	 */
	public static void initializeBoard() throws UnsupportedOperationException {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		try {
			if (!quoridor.hasBoard()) {
				Board newBoard = new Board(quoridor); //creates new board and adds it to current quoridor
				
				// Creating tiles by rows, i.e., the column index changes with every tile creation
				for (int i = 1; i <= 9; i++) { // rows
					for (int j = 1; j <= 9; j++) { // columns
						newBoard.addTile(i, j);
					}
				}
			} else
				//quoridor board is already initialized
				throw new UnsupportedOperationException("Quordior already has an initialized board.");
		}
		catch (RuntimeException e) {
			throw new UnsupportedOperationException(e.getMessage());
		}
		
	}
	
	//5. Rotate wall -- Raja
	//6. Grab wall -- Raja
	//7. Move wall -- Grace
	//8. Drop wall -- Grace
	//9. Save position -- Shayne
	//10. Load position -- Shayne
	//11. Validate position --Sami
	//12. Switch player (aka. Update board) --Sami

		
}

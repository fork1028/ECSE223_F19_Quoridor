package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

import ca.mcgill.ecse223.quoridor.view.*;


public class QuoridorApplication {

	private static Quoridor quoridor;
	
	public static void main(String[] args) {
		
		System.out.println(QuoridorController.loadSavedPosition("quoridor_test_game_invalid_wall_out-of-track.dat"));
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new QuoridorStartGame().setVisible(true);
				//new QuoridorGamePage().setVisible(true);
			}
			
		});
		
		
	}
	
	
	

	public static Quoridor getQuoridor() {
		if (quoridor == null) {
			quoridor = new Quoridor();
		}
 		return quoridor;
	}

}

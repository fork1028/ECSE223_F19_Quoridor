package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.model.Quoridor;

import ca.mcgill.ecse223.quoridor.view.QuoridorStartGame;

import ca.mcgill.ecse223.quoridor.view.QuoridorPage;


public class QuoridorApplication {

	private static Quoridor quoridor;
	
	public static void main(String[] args) {
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new QuoridorStartGame().setVisible(true);
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

package ca.mcgill.ecse223.quoridor.view;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.User;

public class QuoridorStartGame extends JFrame {
	
	private static final long serialVersionUID = -453453453453245345L;
	
	// UI elements
	private JLabel errorMsg;
	//Game
	private JButton createNewGameButton;
	private JButton loadGameButton;
	private JTextField loadGameTextField;
	private JButton resumeGameButton;
	private JLabel loadGameLabel;
	//User
	private JComboBox<String> whiteUserList;
	private JComboBox<String> blackUserList;
	private JTextField createUserTextField;
	private JButton createUserButton;
	private JLabel createUserLabel;
	private JLabel blackPlayerLabel;
	private JLabel whitePlayerLabel;
	private JButton readyButton;
	//Timer
	private JLabel timerLabel;
	private JComboBox<String> minuteList;
	private JComboBox<String> secondList;
	private JLabel setMinuteLabel;
	private JLabel setSecondLabel;
	
	// data elements
	private String error = null;
	//pick user
	private HashMap<Integer, String> availableUser;
	
	/** Creates new QuoridorPage */
	public QuoridorStartGame() {
		initComponents();
		refreshData();
	}
	
	
	/** This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// elements for error message
		errorMsg = new JLabel();
		errorMsg.setForeground(Color.blue);
		//elements for Game
		createNewGameButton = new JButton();
		createNewGameButton.setText("Create New Game");
		loadGameButton = new JButton();
		loadGameButton.setText("Load a Game:");
		loadGameTextField = new JTextField();
		loadGameLabel = new JLabel();
		loadGameLabel.setText("Load Game File Name:");
		resumeGameButton = new JButton();
		resumeGameButton.setText("Resume Game");
		//elements for User
		createUserButton = new JButton();
		createUserButton.setText("Create User");
		whiteUserList = new JComboBox<String>(new String[0]);
		blackUserList = new JComboBox<String>(new String[0]);
		createUserTextField = new JTextField();
		createUserLabel = new JLabel();
		createUserLabel.setText("Create New User:");
		blackPlayerLabel = new JLabel();
		blackPlayerLabel.setText("Black Player:");
		whitePlayerLabel = new JLabel();
		whitePlayerLabel.setText("White Player:");
		readyButton  = new JButton();;
		//maybe useless button??
		readyButton.setText("Ready (USELESS BUTTON MAYBE????)");
		//elements for Timer
		timerLabel = new JLabel();
		timerLabel.setText("Set Time");
		minuteList = new JComboBox<String>(new String[0]);
		secondList = new JComboBox<String>(new String[0]);
		setMinuteLabel = new JLabel();
		setMinuteLabel.setText("Set Minutes:");
		setSecondLabel = new JLabel();
		setSecondLabel.setText("Set Seconds:");
		
		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Quoridor Application Group 13");
		
		// listeners for Game
		//TODO: map all listeners to correct actionPerformed method
		createNewGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createNewGameButtonActionPerformed(evt);
			}
		});
				
		loadGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadGameButtonActionPerformed(evt);
			}
		});
		
		//listeners for User
		createUserButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createUserButtonActionPerformed(evt);
			}
		});
		
		JSeparator horizontalLineMid = new JSeparator();
		
		//Layout
		
		//Horizontal Layout first.
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(errorMsg)
						.addComponent(horizontalLineMid)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(createUserLabel)
										.addComponent(whitePlayerLabel)
										.addComponent(blackPlayerLabel)
										.addComponent(timerLabel)
										.addComponent(createNewGameButton)
										.addComponent(loadGameLabel))
								.addGroup(layout.createParallelGroup()
										.addComponent(createUserTextField)
										.addComponent(whiteUserList)
										.addComponent(blackUserList)
										.addComponent(setMinuteLabel)
										.addComponent(minuteList)								
										.addComponent(loadGameTextField))
								.addGroup(layout.createParallelGroup()
										.addComponent(createUserButton)
										.addComponent(setSecondLabel)
										.addComponent(secondList)
										.addComponent(loadGameButton))
						)
				)
				
		);
		
		//Vertical Layout now.
		layout.setVerticalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(errorMsg)
						.addGroup(layout.createParallelGroup()
								.addComponent(createUserLabel)
								.addComponent(createUserTextField)						
								.addComponent(createUserButton))
						.addGroup(layout.createParallelGroup()
								.addComponent(whitePlayerLabel)
								.addComponent(whiteUserList))
						.addGroup(layout.createParallelGroup()
								.addComponent(blackPlayerLabel)
								.addComponent(blackUserList))					
						.addGroup(layout.createParallelGroup()
								.addComponent(setMinuteLabel)
								.addComponent(setSecondLabel))	
						.addGroup(layout.createParallelGroup()
								.addComponent(timerLabel)
								.addComponent(minuteList)						
								.addComponent(secondList))
						.addGroup(layout.createParallelGroup()
								.addComponent(createNewGameButton))
						.addGroup(layout.createParallelGroup()
								.addComponent(horizontalLineMid))
						.addGroup(layout.createParallelGroup()
								.addComponent(loadGameLabel)
								.addComponent(loadGameTextField)						
								.addComponent(loadGameButton))
				)
		);
		
		pack();
	}
	
	private void refreshData() {
		errorMsg.setText(error);
		
		//set all text fields to be empty.
		loadGameTextField.setText("");
		createUserTextField.setText("");
		
		//Filling minutes and seconds list
		minuteList.removeAllItems();
		String tmp = null;
		for (int i = 0 ; i < 60 ; i++) {
			tmp = Integer.toString(i);
			minuteList.addItem(tmp);
		}
		minuteList.setSelectedIndex(-1);
		
		secondList.removeAllItems();
		tmp = null;
		for (int i = 0 ; i < 60 ; i++) {
			tmp = Integer.toString(i);
			secondList.addItem(tmp);
		}
		secondList.setSelectedIndex(-1);
		
		whiteUserList.removeAllItems();
		//TODO replace model obbj w controller method
		for (User tmpUser : QuoridorApplication.getQuoridor().getUsers()) {
			whiteUserList.addItem(tmpUser.getName());
		}
		whiteUserList.setSelectedIndex(-1);
		
		blackUserList.removeAllItems();
		for (User tmpUser : QuoridorApplication.getQuoridor().getUsers()) {
			blackUserList.addItem(tmpUser.getName());
		}
		blackUserList.setSelectedIndex(-1);
		
		
	}

	//TODO: create action performed methods to handle each listener
	//ACTION PERFORMED METHODS
	
	private void createNewGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;
		//controller method
		if (blackUserList.getSelectedItem() == whiteUserList.getSelectedItem()) {
			error = ("You cannot select the same user to be both players!");
		} else if (minuteList.getSelectedItem() == "0" && secondList.getSelectedItem() == "0") {
			error = ("You must give the players each a real thinking time!");
		} else if (minuteList.getSelectedItem() == null || secondList.getSelectedItem() == null || blackUserList.getSelectedItem() == null || whiteUserList.getSelectedItem() ==null) {
			error = ("You must give a value for all players and both time boxes");
		} else {
			//ADD SOMETHING TO CREATE A NEW GAME!
			
		//QuoridorController.createNewGame();
			//Game new Game(,,)
			//set username to players
			//initboard
		}
		
		
		
	}
	
	private void loadGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;
		//controller method
		try {
			QuoridorController.loadSavedPosition(loadGameTextField.getText());
			new QuoridorGamePage().setVisible(true);
		} catch (UnsupportedOperationException e) {
			error = e.getMessage();
			refreshData();
			return;
		}
		//QuoridorStartGame().setVisible(false);
		//new QuoridorGamePage().setVisible(true);
		
	}
	
	private void createUserButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;
		//controller method
		try {
			QuoridorController.createUser(createUserTextField.getText());
		} catch (RuntimeException e) {
			error = e.getMessage();
		}
		refreshData();
		
	}
	
}

package ca.mcgill.ecse223.quoridor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;

public class QuoridorStartGame extends JFrame {

	private static final long serialVersionUID = -453453453453245345L;

	// JLabels
	private JLabel errorMsg;
	private JLabel welcomeMsg;
	private JLabel loadPositionLabel;
	private JLabel loadGameLabel;
	private JLabel createUserLabel;
	private JLabel blackPlayerLabel;
	private JLabel whitePlayerLabel;
	private JLabel timerLabel;

	// JButton
	private JButton createNewGameButton;
	private JButton loadPositionButton;
	private JButton loadGameButton;
	private JButton resumeGameButton;
	private JButton createUserButton;

	// JComboBox
	private JComboBox<String> whiteUserList;
	private JComboBox<String> blackUserList;
	private JComboBox<String> minuteList;
	private JComboBox<String> secondList;

	// JTextField
	private JTextField createUserTextField;
	private JTextField loadPositionTextField;
	private JTextField loadGameTextField;
	
	// data elements
	private String error = "Welcome to Quoridor!";
	
	//graphics
	private static final Font BIG_FONT = new Font("Verdana", Font.BOLD, 50);
	private static final Font NORMAL_FONT = new Font("Verdana", Font.PLAIN, 18);
	private static final Font SMALL_FONT = new Font("Verdana", Font.PLAIN, 14);
	private static final Color BUTTON_COLOUR_DEFAULT = new Color(99,255,252); //cyan blue colour
	private static final Color BUTTON_COLOUR_GREEN = new Color(208,240,192); //light green
	
	/** Creates new QuoridorPage */
	public QuoridorStartGame() {
		super("Quoridor Game - Group 13");
		initComponents();
		refreshData();
	}

	/** This method is called from within the constructor to initialize the form. */
	private void initComponents() {
		this.setSize(900,700);
		this.setLocation(100,40);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		
		// JLabels
		welcomeMsg = new JLabel("Quoridor");
		welcomeMsg.setFont(BIG_FONT);
		loadPositionLabel = new JLabel("Load Position File Name");
		loadPositionLabel.setFont(NORMAL_FONT);
		loadGameLabel = new JLabel("Load Game File Name");
		loadGameLabel.setFont(NORMAL_FONT);
		createUserLabel = new JLabel("Create New Player: ");
		createUserLabel.setFont(NORMAL_FONT);
		whitePlayerLabel = new JLabel("Set White Player: ");
		whitePlayerLabel.setFont(NORMAL_FONT);
		blackPlayerLabel = new JLabel("Set Black Player: ");
		blackPlayerLabel.setFont(NORMAL_FONT);
		timerLabel = new JLabel("Set Thinking Time (min:sec): ");
		timerLabel.setFont(NORMAL_FONT);
		errorMsg = new JLabel();
		errorMsg.setFont(SMALL_FONT);
		errorMsg.setForeground(Color.BLUE);
		errorMsg.setBackground(Color.GRAY);
		
		// JButtons
		createUserButton = new JButton("Create Player");
		createUserButton.setFont(NORMAL_FONT);
		createUserButton.setBackground(BUTTON_COLOUR_DEFAULT);
		createNewGameButton = new JButton("Start Game");
		createNewGameButton.setFont(NORMAL_FONT);
		createNewGameButton.setBackground(BUTTON_COLOUR_GREEN);
		loadPositionButton = new JButton("Load Position");
		loadPositionButton.setFont(NORMAL_FONT);
		loadPositionButton.setBackground(BUTTON_COLOUR_DEFAULT);
		loadGameButton = new JButton("Load Game");
		loadGameButton.setFont(NORMAL_FONT);
		loadGameButton.setBackground(BUTTON_COLOUR_DEFAULT);
		resumeGameButton = new JButton("Resume Game");
		resumeGameButton.setFont(NORMAL_FONT);
		resumeGameButton.setBackground(BUTTON_COLOUR_DEFAULT);

		// JTextFields
		createUserTextField = new JTextField();
		createUserTextField.setFont(NORMAL_FONT);
		loadPositionTextField = new JTextField();
		loadPositionTextField.setFont(NORMAL_FONT);
		loadGameTextField = new JTextField();
		loadGameTextField.setFont(NORMAL_FONT);

		// JComboBoxs
		whiteUserList = new JComboBox<String>(new String[0]);
		blackUserList = new JComboBox<String>(new String[0]);
		minuteList = new JComboBox<String>(new String[0]);
		secondList = new JComboBox<String>(new String[0]);


		//minuteList.setSelectedItem(minuteList.getItemAt(3));

		
		Container mainContainer = this.getContentPane();
		mainContainer.setLayout(new FlowLayout(10,10,10));
		mainContainer.setBackground(Color.white);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8,3,5,10));
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.add(new JLabel(""));
		panel.add(welcomeMsg);
		panel.add(new JLabel(""));
		panel.add(createUserLabel);
		panel.add(createUserTextField);
		panel.add(createUserButton);
		panel.add(whitePlayerLabel);
		panel.add(whiteUserList);
		panel.add(new JLabel(""));
		panel.add(blackPlayerLabel);
		panel.add(blackUserList);
		panel.add(new JLabel(""));
		panel.add(timerLabel);
		panel.add(minuteList);
		panel.add(secondList);
		panel.add(new JLabel(""));
		panel.add(createNewGameButton);
		panel.add(new JLabel(""));
		panel.add(loadPositionLabel);
		panel.add(loadPositionTextField);
		panel.add(loadPositionButton);
		panel.add(loadGameLabel);
		panel.add(loadGameTextField);
		panel.add(loadGameButton);
		panel.setBackground(Color.white);

		// bottom pane
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(5, 1));
		bottom.add(new JLabel());
		bottom.add(errorMsg);
		bottom.add(new JLabel());

		// outer panel
		JPanel outer = new JPanel();
		outer.setLayout(new BorderLayout());
		outer.add(panel, BorderLayout.NORTH);
		
		outer.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);
		outer.add(bottom, BorderLayout.SOUTH);

		this.add(outer, BorderLayout.CENTER);

		// listeners for Game
		createNewGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createNewGameButtonActionPerformed(evt);
			}
		});

		loadPositionButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadPositionButtonActionPerformed(evt);
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

			public void mouseEntered(java.awt.event.MouseEvent evt) {
				createUserButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				createUserButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
			}
		});

		this.setVisible(true);
	}

	private void refreshData() {
		errorMsg.setText(error);

		//set all text fields to be empty.
		loadPositionTextField.setText("");
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
		for (String username : QuoridorController.getAllUsernames()) {
			whiteUserList.addItem(username);
		}
		whiteUserList.setSelectedIndex(-1);

		blackUserList.removeAllItems();
		for (String username : QuoridorController.getAllUsernames()) {
			blackUserList.addItem(username);
		}
		blackUserList.setSelectedIndex(-1);


	}

	private void createNewGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;

		//validation to see if we can actually start new game
		if (minuteList.getSelectedIndex() <0 || secondList.getSelectedIndex() <0 || blackUserList.getSelectedIndex() <0 || whiteUserList.getSelectedIndex() <0 ) {
			error = "You must select a username for both players and select a total thinking time!";
			refreshData();
			return;
		} else if (blackUserList.getSelectedIndex() == whiteUserList.getSelectedIndex()) {
			//index of users is always the same because they are readded in refreshData each time
			error = ("You cannot select the same user for both players!");
			refreshData();
			return;
		} else if (minuteList.getSelectedIndex() == 0 && secondList.getSelectedIndex() == 0) {
			//cannot select 00:00 for thinking time
			error = ("You cannot select 00:00 as the total thinking time!");
			refreshData();
			return;
		}else {
			try {
				//creates a READYTOSTART game
				QuoridorController.createReadyGame(blackUserList.getSelectedItem().toString(), whiteUserList.getSelectedItem().toString(), minuteList.getSelectedIndex(), secondList.getSelectedIndex());
				error = "Game is ready to start!";
				QuoridorGamePage gamePage = new QuoridorGamePage(); //create and display new GamePage!
				gamePage.setUsernameToDisplay(blackUserList.getSelectedItem().toString(), true);
				gamePage.setUsernameToDisplay(whiteUserList.getSelectedItem().toString(), false);
				gamePage.setVisible(true);
				QuoridorController.startGameAndClocks();
				refreshData();
				return;
			} catch (InvalidInputException e) {
				error = "Unable to create a new game.";
				refreshData();
				return;
			}

		}



	}

	private void loadPositionButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;
		//controller method
		if (minuteList.getSelectedIndex() <0 || secondList.getSelectedIndex() <0 || blackUserList.getSelectedIndex() <0 || whiteUserList.getSelectedIndex() <0 ) {
			error = "You must select a username for both players and select a total thinking time!";
			refreshData();
			return;
		} else if (blackUserList.getSelectedIndex() == whiteUserList.getSelectedIndex()) {
			//index of users is always the same because they are readded in refreshData each time
			error = ("You cannot select the same user for both players!");
			refreshData();
			return;
		} else if (minuteList.getSelectedIndex() == 0 && secondList.getSelectedIndex() == 0) {
			//cannot select 00:00 for thinking time
			error = ("You cannot select 00:00 as the total thinking time!");
			refreshData();
			return;
		} else {
			try {
				//QuoridorController.loadSavedPosition(loadGameTextField.getText());
				boolean temp = QuoridorController.loadSavedPosition(loadPositionTextField.getText());
				if (temp) {
					QuoridorController.setTotalThinkingTime(minuteList.getSelectedIndex(), secondList.getSelectedIndex());
					QuoridorGamePage gamePage = new QuoridorGamePage(); //create and display new GamePage!
					gamePage.setVisible(true);
					//gamePage.repaint();
					//gamePage.refreshData();
					refreshData();
					return;
				} else {
					error = ("The file you are attempting to load contains an invalid move!");
					refreshData();
					return;
				}
				
			} catch (UnsupportedOperationException | InvalidInputException e) {
				error = e.getMessage();
				refreshData();
				return;
			}
		}
	}

	private void loadGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;
		//controller method
		if (minuteList.getSelectedIndex() <0 || secondList.getSelectedIndex() <0 || blackUserList.getSelectedIndex() <0 || whiteUserList.getSelectedIndex() <0 ) {
			error = "You must select a username for both players and select a total thinking time!";
			refreshData();
			return;
		} else if (blackUserList.getSelectedIndex() == whiteUserList.getSelectedIndex()) {
			//index of users is always the same because they are readded in refreshData each time
			error = ("You cannot select the same user for both players!");
			refreshData();
			return;
		} else if (minuteList.getSelectedIndex() == 0 && secondList.getSelectedIndex() == 0) {
			//cannot select 00:00 for thinking time
			error = ("You cannot select 00:00 as the total thinking time!");
			refreshData();
			return;
		} else {
			try {
				//QuoridorController.loadSavedPosition(loadGameTextField.getText());
				boolean temp = QuoridorController.loadGame(loadGameTextField.getText(), whiteUserList.getSelectedItem().toString(), blackUserList.getSelectedItem().toString());
				if (temp) {
					QuoridorController.setTotalThinkingTime(minuteList.getSelectedIndex(), secondList.getSelectedIndex());
					QuoridorGamePage gamePage = new QuoridorGamePage(); //create and display new GamePage!
					gamePage.setVisible(true);
					refreshData();
					return;
				} else {
					error = ("The file you are attempting to load contains an invalid move!");
					refreshData();
					return;
				}
			} catch (UnsupportedOperationException | IOException | InvalidInputException e) {
				error = e.getMessage();
				refreshData();
				return;
			}
		}
	}
	
	private void createUserButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;
		//if already exists, give warning to view
		String newUser = createUserTextField.getText();

		if (QuoridorController.getUserByUsername(newUser) !=null ) {
			//user already exists
			error = "Username " + newUser + " already exists!";
			refreshData();
			return;
		}
		//controller method
		try {
			QuoridorController.createUser(createUserTextField.getText());
		} catch (Exception e) {
			error = e.getMessage();
		} 
		refreshData();

	}

}

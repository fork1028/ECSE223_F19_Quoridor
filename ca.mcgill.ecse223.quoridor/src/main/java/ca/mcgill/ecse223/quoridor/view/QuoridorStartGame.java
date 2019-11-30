package ca.mcgill.ecse223.quoridor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;

public class QuoridorStartGame extends JFrame {

	private static final long serialVersionUID = -453453453453245345L;

	// JLabels
	private JLabel errorMsg;
	private JLabel welcomeMsg;
	private JLabel loadGameLabel;
	private JLabel createUserLabel;
	private JLabel blackPlayerLabel;
	private JLabel whitePlayerLabel;
	private JLabel timerLabel;

	// JButton
	private JButton createNewGameButton;
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
	private JTextField loadGameTextField;

	// data elements
	private String error = null;

	/** Creates new QuoridorPage */
	public QuoridorStartGame() {
		super("Quoridor Game - Group 13");
		initComponents();
		refreshData();
	}

	/** This method is called from within the constructor to initialize the form. */
	private void initComponents() {
		this.setSize(900,600);
		this.setLocation(100,40);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);

		// JLabels
		welcomeMsg = new JLabel("Quoridor");
		welcomeMsg.setFont(new Font("Verdana", Font.BOLD, 50));
		loadGameLabel = new JLabel("Load Game File Name");
		loadGameLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
		createUserLabel = new JLabel("Create New Player: ");
		createUserLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
		whitePlayerLabel = new JLabel("Set White Player: ");
		whitePlayerLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
		blackPlayerLabel = new JLabel("Set Black Player: ");
		blackPlayerLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
		timerLabel = new JLabel("Set Thinking Time (min:sec): ");
		timerLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

		// JButtons
		createUserButton = new JButton("Create Player");
		createUserButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		createUserButton.setBackground(new Color(99,255,252));
		createNewGameButton = new JButton("Start Game");
		createNewGameButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		createNewGameButton.setBackground(new Color(99,255,252));
		loadGameButton = new JButton("Load Game");
		loadGameButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		loadGameButton.setBackground(new Color(99,255,252));
		resumeGameButton = new JButton("Resume Game");
		resumeGameButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		resumeGameButton.setBackground(new Color(99,255,252));

		// JTextFields
		createUserTextField = new JTextField();
		createUserTextField.setFont(new Font("Verdana", Font.PLAIN, 18));
		loadGameTextField = new JTextField();
		loadGameTextField.setFont(new Font("Verdana", Font.PLAIN, 18));

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
		panel.setLayout(new GridLayout(7,3,5,10));
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
		panel.add(loadGameLabel);
		panel.add(loadGameTextField);
		panel.add(loadGameButton);
		panel.setBackground(Color.white);
		this.add(panel, BorderLayout.CENTER);

		// elements for error message
		errorMsg = new JLabel();
		errorMsg.setForeground(Color.red);

		// listeners for Game
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

	private void loadGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		//Clear error msg
		error = null;
		//controller method
		try {
			//QuoridorController.loadSavedPosition(loadGameTextField.getText());
			QuoridorController.loadGame(loadGameTextField.getText(), whiteUserList.getSelectedItem().toString(), blackUserList.getSelectedItem().toString());
			new QuoridorGamePage().setVisible(true);
		} catch (UnsupportedOperationException | IOException | InvalidInputException e) {
			error = e.getMessage();
			refreshData();
			return;
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

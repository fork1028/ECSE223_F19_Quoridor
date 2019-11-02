package ca.mcgill.ecse223.quoridor.view;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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
		createNewGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
			}
		});
				
		loadGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
			}
		});
		
		//listeners for User
		readyButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
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
		
		
	}

	
	
	
}
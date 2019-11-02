package ca.mcgill.ecse223.quoridor.view;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class QuoridorPage extends JFrame{

	// UI elements
	private JLabel errorMsg;
	//Game
	private JButton createNewGameButton;
	private JButton loadGameButton;
	private JTextField loadGameTextField;
	private JButton resumeGameButton;
	//User
	private JComboBox<String> userList;
	private JTextField createUserTextField;
	private JLabel createUserLabel;
	private JLabel blackPlayerLabel;
	private JLabel whitePlayerLabel;
	private JButton readyButton;
	//Timer
	private JComboBox<String> minuteList;
	private JComboBox<String> secondList;
	private JLabel setMinuteLabel;
	private JLabel setSecondLabel;
	
	// data elements
	private String error = null;
	//pick user
	private HashMap<Integer, String> availableUser;
	
	/** Creates new QuoridorPage */
	public QuoridorPage() {
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
		resumeGameButton = new JButton();
		resumeGameButton.setText("Resume Game");
		//elements for User
		userList = new JComboBox<String>(new String[0]);
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
		
		//Layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		//layout.setHorizontalGroup(
				
				//);
		
		
	}
	
	private void refreshData() {
		
		
	}
	
}

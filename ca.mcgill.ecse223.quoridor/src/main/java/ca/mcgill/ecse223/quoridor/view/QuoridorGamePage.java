package ca.mcgill.ecse223.quoridor.view;

import java.awt.Color;

import java.util.HashMap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JTextField;
import javax.swing.WindowConstants;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class QuoridorGamePage extends JFrame {

	private static final long serialVersionUID = -45345345345345345L;

	// UI elements

	// players
	private JLabel playerWhiteNameLabel;
	private JLabel playerBlackNameLabel;
	private JLabel playerWhiteTurnLabel;
	private JLabel playerBlackTurnLabel;
	// TODO: countdown clocks and stock

	// Wall
	private JButton moveWall;
	private JButton dropWall;
	private JButton grabWall;
	private JButton rotateWall;

	// save game
	private JButton saveGame;
	private JTextField saveGameAs;

	// board visualizer
	private QuoridorBoardVisualizer boardVisualizer;
	private static final int WIDTH_BOARD = 600;
	private static final int HEIGHT_BOARD = 600;

	// data elements
	private String messageStr = "TEST message";
	private JLabel messageLabel;

	// graphics
	Color customGreen = new Color(0, 204, 0);

	/** Constructor to create QuoridorBoardPage */
	public QuoridorGamePage() {
		initComponents();
		refreshData();
		refreshBoardVisualizer();
	}

	/************ INITIALIZATION AND LAYOUT ***************/

	/** This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// elements for error message
		messageLabel = new JLabel();
		messageLabel.setText(messageStr);
		messageLabel.setForeground(Color.blue);
		
		//elements for players
		playerWhiteNameLabel = new JLabel();
		playerWhiteNameLabel.setText("PLAYER WHITE"); //TODO: get username from startpage
		playerWhiteNameLabel.setFont(new Font(null, Font.BOLD, 18));
		
		playerWhiteTurnLabel = new JLabel();
		playerWhiteTurnLabel.setText("  YOUR TURN  ");
		playerWhiteTurnLabel.setFont(new Font(null, Font.BOLD, 16));
		playerWhiteTurnLabel.setBackground(customGreen);
		playerWhiteTurnLabel.setOpaque(true);
		
		playerBlackNameLabel = new JLabel();
		playerBlackNameLabel.setText("PLAYER BLACK"); //TODO: get username from startpage
		playerBlackNameLabel.setFont(new Font(null, Font.BOLD, 18));
		playerBlackTurnLabel = new JLabel();
		playerBlackTurnLabel.setText("       WAIT        ");
		playerBlackTurnLabel.setFont(new Font(null, Font.BOLD, 16));
		playerBlackTurnLabel.setBackground(Color.LIGHT_GRAY);
		playerBlackTurnLabel.setOpaque(true);
		
		//TODO: countdown clocks and stock
		
		//elements for Wall buttons
		moveWall=new JButton();
		moveWall.setText("MOVE");
		dropWall=new JButton();
		dropWall.setText("DROP");
		rotateWall=new JButton();
		rotateWall.setText("ROTATE");
		grabWall=new JButton();
		grabWall.setText("GRAB");
		
		//save and pause game
		saveGame=new JButton();
		saveGame.setText("SAVE GAME");
		saveGame.setToolTipText("Enter a filename and click SAVE GAME to save current game as a .dat file");
		saveGameAs =new JTextField();
		saveGameAs.setToolTipText("Enter the filename for your saved game .dat file");
		
		//visualizer for board
		boardVisualizer = new QuoridorBoardVisualizer();
		boardVisualizer.setMinimumSize(new Dimension(WIDTH_BOARD, HEIGHT_BOARD));
		
		
		
		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Quoridor Board and Game - Group 13");
		

		//action listeners
		//TODO: complete actionlisteners, map to correct action performed methods at bottom
		moveWall.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				moveIsClicked(evt);
			}
		});
		dropWall.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dropIsClicked(evt);
			}
		});
		grabWall.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				grabIsClicked(evt);
			}
		});
		rotateWall.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				rotateIsClicked(evt);
			}
		});
		saveGame.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveGameIsClicked(evt);
			}
		});
		

	// Layout
	GroupLayout layout = new GroupLayout(getContentPane());

	getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		// horizontal line elements
		JSeparator horizontalLineTop = new JSeparator();
		JSeparator horizontalLineMiddle = new JSeparator();
		JSeparator horizontalLineBottom = new JSeparator();
		//add players' buttons on each left or right side
		//board in middle
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					//main controls (save, pause)
					.addGroup(layout.createSequentialGroup()
							.addComponent(saveGameAs)	
							.addComponent(saveGame)
									//TODO save game name	
						
					)
					//player1, board, player2
					.addGroup(layout.createSequentialGroup()
						//player1 controls etc on left
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerWhiteNameLabel)	
								.addComponent(playerWhiteTurnLabel)
								//TODO add countdown clock and stock
						)	
								
						//board in middle
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(boardVisualizer)
								.addGroup(layout.createSequentialGroup()
										//walls and pawn buttons
										.addComponent(grabWall)	
										.addComponent(rotateWall)	
										.addComponent(moveWall)	
										.addComponent(dropWall)	
								)
						)
						//player2 controls etc on right
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerBlackNameLabel)	
								.addComponent(playerBlackTurnLabel)
								//TODO add countdown clock and stock
						)
					)
					
					.addGroup(layout.createSequentialGroup()
							//error msg
							.addComponent(messageLabel)	
					)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				//main controls (save, pause)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(saveGameAs)	
					.addComponent(saveGame)
							//TODO save game name					
				)
				//player1, board, player2
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					//player1 controls etc on left
					.addGroup(layout.createSequentialGroup()
							.addComponent(playerWhiteNameLabel)	
							.addComponent(playerWhiteTurnLabel)
							//TODO add countdown clock and stock
					)	
							
					//board in middle
					.addGroup(layout.createSequentialGroup()
							.addComponent(boardVisualizer)
							//buttons and error msg
							.addGroup(layout.createParallelGroup()
									//walls and pawn buttons
									.addComponent(grabWall)	
									.addComponent(rotateWall)	
									.addComponent(moveWall)	
									.addComponent(dropWall)	
							)
					)
					//player2 controls etc on right
					.addGroup(layout.createSequentialGroup()
							.addComponent(playerBlackNameLabel)	
							.addComponent(playerBlackTurnLabel)
							//TODO add countdown clock and stock
					)
				)
				
				
				.addGroup(layout.createParallelGroup()
						//error msg
						.addComponent(messageLabel)	
				)
		);
		
		
		pack();

	}

	/************ REFRESH METHODS ***************/
	private void refreshData() {
		//TODO ???
		// if turn changes
		// if stock changes
		// countdown
		// moves

	}

	private void refreshBoardVisualizer() {
		// TODO
		// board visualizer already automatically detects which tile is clicked
		// implement for walls
	}

	/************ ACTION PERFORMED METHODS ***************/
	private void moveIsClicked(java.awt.event.ActionEvent evt) {
		//TODO
	}
	
	private void grabIsClicked(java.awt.event.ActionEvent evt) {
		//TODO
	}
	
	private void rotateIsClicked(java.awt.event.ActionEvent evt) {
		//TODO
	}
	
	private void dropIsClicked(java.awt.event.ActionEvent evt) {
		//TODO
	}
	
	private void saveGameIsClicked(java.awt.event.ActionEvent evt) {
		//TODO
	}

}

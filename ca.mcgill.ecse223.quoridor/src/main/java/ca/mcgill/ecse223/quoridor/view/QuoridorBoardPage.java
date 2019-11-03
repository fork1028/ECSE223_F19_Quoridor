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


public class QuoridorBoardPage extends JFrame{


	private static final long serialVersionUID = -45345345345345345L;

	
	// UI elements

	private QuoridorBoardVisualizer boardVisualizer;
	
	//buttons
	private boolean moveIsClicked;
	private boolean dropIsClicked;
	private boolean grabIsClicked;
	private boolean rotateIsClicked;
	
	//players
	private JLabel playerWhiteNameLabel;
	private JLabel playerBlackNameLabel;
	private JLabel playerWhiteTurnLabel;
	private JLabel playerBlackTurnLabel;
	//TODO: countdown clocks and stock
	
	//Wall
	private JButton moveWall;
	private JButton dropWall;
	private JButton grabWall;
	private JButton rotateWall;
	
	//board visualizer
	private static final int WIDTH_BOARD = 600;
	private static final int HEIGHT_BOARD = 600;
	
	// data elements
	private String error = "TEST message";
	private JLabel errorMsg;
	
	
	//graphics
	Color customGreen = new Color(0,204,0);
	/** Creates new QuoridorBoardPage */
	public QuoridorBoardPage() {
		initComponents();
		refreshData();
		refreshBoardVisualizer();
	}
	
	
	/** This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// elements for error message
		errorMsg = new JLabel();
		errorMsg.setText(error);
		errorMsg.setForeground(Color.blue);
		
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
		moveWall.setActionCommand("move");
		moveIsClicked=false;
		dropWall=new JButton();
		dropWall.setText("DROP");
		dropWall.setActionCommand("drop");
		dropIsClicked=false;
		rotateWall=new JButton();
		rotateWall.setText("ROTATE");
		rotateWall.setActionCommand("drop");
		rotateIsClicked=false;
		grabWall=new JButton();
		grabWall.setText("GRAB");
		grabWall.setActionCommand("drop");
		grabIsClicked=false;
		
		boardVisualizer = new QuoridorBoardVisualizer();
		boardVisualizer.setMinimumSize(new Dimension(WIDTH_BOARD, HEIGHT_BOARD));
		
		
		
		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Quoridor Board and Game - Group 13");
		

		//listeners for Wall buttons
		moveWall.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(evt.getActionCommand().equals("move")) {
					moveIsClicked=true;
				}
			}
		});
		dropWall.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(evt.getActionCommand().equals("drop")) {
					dropIsClicked=true;
				}
			}
		});
		
		
		//Layout
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
							.addComponent(errorMsg)	
					)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
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
						.addComponent(errorMsg)	
				)
		);
		
		
		pack();

	}
	
	private void refreshData() {
		moveIsClicked=false;
		dropIsClicked=false;
		//if turn changes
		//if stock changes
		//countdown
		//moves
		
	}
	
	private void refreshBoardVisualizer() {
		//TODO pass where the board is clicked to the board visualizer?
		
	}
	
	
	
	private boolean getMoveIsClicked() {
		return moveIsClicked;
	}
	
	private boolean getDropIsClicked() {
		return dropIsClicked;
	}
	
}

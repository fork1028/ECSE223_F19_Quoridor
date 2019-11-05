package ca.mcgill.ecse223.quoridor.view;

import java.awt.Color;

import java.util.HashMap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Time;
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
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public class QuoridorGamePage extends JFrame {

	private static final long serialVersionUID = -45345345345345345L;

	// UI elements

	// players
	private JLabel playerWhiteNameLabel;
	private JLabel playerWhiteTurnLabel;
	private JLabel playerWhiteClockLabel;

	private JLabel playerBlackNameLabel;
	private JLabel playerBlackTurnLabel;
	private JLabel playerBlackClockLabel;
	private static final int refreshClockMS = 100;

	// Wall
	private JButton moveWall;
	private JButton dropWall;
	private JButton grabWall;
	private JButton rotateWall;

	// save game
	private JButton saveGame;
	private JTextField saveGameAs;
	private JButton overwriteYes;
	private JButton overwriteCancel;

	// board visualizer
	private QuoridorBoardVisualizer boardVisualizer;
	private static final int WIDTH_BOARD = 600;
	private static final int HEIGHT_BOARD = 600;

	// data elements
	private static String error = "";
	private JLabel errorMsg;

	// graphics
	Color customGreen = new Color(0, 204, 0);

	/** Constructor to create QuoridorBoardPage */
	public QuoridorGamePage() {
		initComponents();
		refreshData();
		refreshBoardVisualizer();
		overwriteYes.setEnabled(false);
		overwriteCancel.setEnabled(false);
	}

	/************ INITIALIZATION AND LAYOUT ***************/

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// elements for error message
		errorMsg = new JLabel();
		errorMsg.setText(error);
		errorMsg.setForeground(Color.blue);

		// elements for white player
		playerWhiteNameLabel = new JLabel();
		playerWhiteNameLabel.setText("PLAYER WHITE");
		// TODO: get username from startpage
		playerWhiteNameLabel.setFont(new Font(null, Font.BOLD, 18));

		playerWhiteTurnLabel = new JLabel();
		playerWhiteTurnLabel.setText("  YOUR TURN  ");
		playerWhiteTurnLabel.setFont(new Font(null, Font.BOLD, 16));
		playerWhiteTurnLabel.setBackground(customGreen);
		playerWhiteTurnLabel.setOpaque(true);

		playerWhiteClockLabel = new JLabel();
		playerWhiteClockLabel.setText("MM:SS");
		playerWhiteClockLabel.setFont(new Font(null, Font.BOLD, 25));
		playerWhiteClockLabel.setBackground(Color.LIGHT_GRAY);
		playerWhiteClockLabel.setOpaque(true);

		// elements for black player
		playerBlackNameLabel = new JLabel();
		playerBlackNameLabel.setText("PLAYER BLACK");
		// TODO: get username from startpage
		playerBlackNameLabel.setFont(new Font(null, Font.BOLD, 18));

		playerBlackTurnLabel = new JLabel();
		playerBlackTurnLabel.setText("       WAIT        ");
		playerBlackTurnLabel.setFont(new Font(null, Font.BOLD, 16));
		playerBlackTurnLabel.setBackground(Color.LIGHT_GRAY);
		playerBlackTurnLabel.setOpaque(true);

		playerBlackClockLabel = new JLabel();
		playerBlackClockLabel.setText("MM:SS");
		playerBlackClockLabel.setFont(new Font(null, Font.BOLD, 25));
		playerBlackClockLabel.setBackground(Color.LIGHT_GRAY);
		playerBlackClockLabel.setOpaque(true);

		// elements for Wall buttons
		moveWall = new JButton();
		moveWall.setText("MOVE");
		dropWall = new JButton();
		dropWall.setText("DROP");
		rotateWall = new JButton();
		rotateWall.setText("ROTATE");
		grabWall = new JButton();
		grabWall.setText("GRAB");

		// save and pause game
		saveGame = new JButton();
		saveGame.setText("SAVE GAME");
		saveGame.setToolTipText("Enter a filename and click SAVE GAME to save current game as a .dat file");
		saveGameAs = new JTextField();
		saveGameAs.setToolTipText("Enter the filename for your saved game .dat file");
		overwriteYes = new JButton();
		overwriteYes.setText("Overwrite Existing File");
		overwriteCancel = new JButton();
		overwriteCancel.setText("Do NOT overwrite existing file");
		// visualizer for board
		boardVisualizer = new QuoridorBoardVisualizer();
		boardVisualizer.setMinimumSize(new Dimension(WIDTH_BOARD, HEIGHT_BOARD));

		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Quoridor Board and Game - Group 13");

		// action listeners

		moveWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getActionCommand().equals("MOVE")) {
					// TODO make a wall appear at the default location on the board
					error = "I have a wall in my hand now";
				}
			}
		});
		dropWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getActionCommand().equals("DROP")) {
					// Player
					// player=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
					// Wall
					// wall=QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
//					try {
//						QuoridorController.dropWall(player, wall);
//					} catch (UnsupportedOperationException | InvalidInputException e) {
//						errorMsg="Unable to drop a wall";
//					}
				}
			}
		});
		grabWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				grabIsClicked(evt);
				error = "I have a wall in my hand now";
			}
		});
		rotateWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				rotateIsClicked(evt);
			}
		});
		saveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveGameIsClicked(evt);
			}
		});

		overwriteYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				overwriteYesIsClicked(evt);
			}
		});

		overwriteCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				overwriteCancelIsClicked(evt);
			}
		});
		
		/**
		 * Timer to decrement remaining player time for current player, and also to set counter ui.
		 * Timer refreshes every 100ms.
		 * @author Helen
		 */
		new Timer(refreshClockMS, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// refresh clocks every 100ms
				String blackStr = "MM:SS"; // default
				String whiteStr = "MM:SS";

				if (QuoridorController.getTimeForPlayer(true) != null) {
					int blackSec = QuoridorController.getTimeToSeconds(QuoridorController.getTimeForPlayer(true));
					blackStr = QuoridorController.getDisplayTimeString(blackSec); // new black remaining time
					
					//if current player to move is black, decrement its remaining time
					if (QuoridorController.isBlackTurn()) {
						//also display player to move display
						playerBlackTurnLabel.setText("  YOUR TURN  ");
						playerBlackTurnLabel.setBackground(customGreen);
						playerWhiteTurnLabel.setText("       WAIT        ");
						playerWhiteTurnLabel.setBackground(Color.LIGHT_GRAY);
						
						//decrement remaining time
						QuoridorController.setRemainingTime(new Time (QuoridorController.getTimeForPlayer(true).getTime() - refreshClockMS), true);
					}
					
				}
				if (QuoridorController.getTimeForPlayer(false) != null) {
					int whiteSec = QuoridorController.getTimeToSeconds(QuoridorController.getTimeForPlayer(false));
					whiteStr = QuoridorController.getDisplayTimeString(whiteSec); // new white remaining time
					
					//if current player to move is white, decrement its remaining time
					if (QuoridorController.isWhiteTurn()) {
						//also update player to move display
						playerWhiteTurnLabel.setText("  YOUR TURN  ");
						playerWhiteTurnLabel.setBackground(customGreen);
						playerBlackTurnLabel.setText("       WAIT        ");
						playerBlackTurnLabel.setBackground(Color.LIGHT_GRAY);
						
						//decrement remaining time
						QuoridorController.setRemainingTime(new Time (QuoridorController.getTimeForPlayer(false).getTime() - refreshClockMS), false);
					}
				}

				//blackStr = QuoridorController.getDisplayTimeString(testSec);
				//whiteStr = QuoridorController.getDisplayTimeString(testSec);

				playerBlackClockLabel.setText(blackStr);
				playerWhiteClockLabel.setText(whiteStr);
			}
		}).start();

		// Layout
		GroupLayout layout = new GroupLayout(getContentPane());

		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		// horizontal line elements
		JSeparator horizontalLineTop = new JSeparator();
		JSeparator horizontalLineMiddle = new JSeparator();
		JSeparator horizontalLineBottom = new JSeparator();
		// add players' buttons on each left or right side
		// board in middle
		layout.setHorizontalGroup(layout.createParallelGroup()
				// main controls (save, pause)
				.addGroup(layout.createSequentialGroup().addComponent(saveGameAs).addComponent(saveGame)
						.addComponent(overwriteYes).addComponent(overwriteCancel)

				)
				// player1, board, player2
				.addGroup(layout.createSequentialGroup()
						// player1 controls etc on left
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerWhiteNameLabel).addComponent(playerWhiteTurnLabel)
								.addComponent(playerWhiteClockLabel)
						)

						// board in middle
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(boardVisualizer)
								.addGroup(layout.createSequentialGroup()
										// walls and pawn buttons
										.addComponent(grabWall).addComponent(rotateWall).addComponent(moveWall)
										.addComponent(dropWall)))
						// player2 controls etc on right
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerBlackNameLabel).addComponent(playerBlackTurnLabel)
								.addComponent(playerBlackClockLabel)
						))

				.addGroup(layout.createSequentialGroup()
						// error msg
						.addComponent(errorMsg)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				// main controls (save, pause)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(saveGameAs)
						.addComponent(saveGame)
				// TODO save game name
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(overwriteYes)
						.addComponent(overwriteCancel))
				// player1, board, player2
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						// player1 controls etc on left
						.addGroup(layout.createSequentialGroup().addComponent(playerWhiteNameLabel)
								.addComponent(playerWhiteTurnLabel).addComponent(playerWhiteClockLabel)
						// TODO add stock
						)

						// board in middle
						.addGroup(layout.createSequentialGroup().addComponent(boardVisualizer)
								// buttons and error msg
								.addGroup(layout.createParallelGroup()
										// walls and pawn buttons
										.addComponent(grabWall).addComponent(rotateWall).addComponent(moveWall)
										.addComponent(dropWall)))
						// player2 controls etc on right
						.addGroup(layout.createSequentialGroup().addComponent(playerBlackNameLabel)
								.addComponent(playerBlackTurnLabel).addComponent(playerBlackClockLabel)
						// TODO add stock
						))

				.addGroup(layout.createParallelGroup()
						// error msg
						.addComponent(errorMsg)));

		pack();

	}

	/************ REFRESH METHODS ***************/
	private void refreshData() {
		// TODO ???
		// if turn changes
		// if stock changes
		// countdown
		// moves
		errorMsg.setText(error);

	}

	private void refreshBoardVisualizer() {
		// TODO
		// board visualizer already automatically detects which tile is clicked
		// implement for walls
	}

	/************ ACTION PERFORMED METHODS ***************/
	private void moveIsClicked(java.awt.event.ActionEvent evt) {
		// TODO
	}

	private void grabIsClicked(java.awt.event.ActionEvent evt) {

	}

	private void rotateIsClicked(java.awt.event.ActionEvent evt) {

		try {
			// g2d.translate(wall.x+(wall.width/2), wall.y+(wall.height/2));
			// g2d.rotate(Math.toRadians(90));
			Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
			Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
			WallMove move = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
			String dir = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection()
					.toString().toLowerCase();

			QuoridorController.rotateWall(wall, move, dir);
		} catch (Exception e) {
			error = "error rotating wall";
		}
		refreshData();
	}

	private void dropIsClicked(java.awt.event.ActionEvent evt) {
		try {

			Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
			QuoridorController.grabWall(player);
		} catch (RuntimeException e) {
			error = "Unable to move the wall";
		}
		refreshData();
	}

	private void saveGameIsClicked(java.awt.event.ActionEvent evt) {
		error = "";
		Boolean tmp = false;
		if (saveGameAs.getText() == "") {
			error = "Must include the file name you wish to save";
		} else {
			tmp = QuoridorController.attemptToSavePosition(saveGameAs.getText());
		}

		if (error == "" && !tmp) {
			overwriteYes.setEnabled(true);
			overwriteCancel.setEnabled(true);
		}
	}

	private void overwriteYesIsClicked(java.awt.event.ActionEvent evt) {
		QuoridorController.overWriteSavePosition(saveGameAs.getText(), true);
		overwriteYes.setEnabled(false);
		overwriteCancel.setEnabled(false);
	}

	private void overwriteCancelIsClicked(java.awt.event.ActionEvent evt) {
		QuoridorController.overWriteSavePosition(saveGameAs.getText(), false);
		overwriteYes.setEnabled(false);
		overwriteCancel.setEnabled(false);
	}

	private void keyTyped(KeyEvent event) throws UnsupportedOperationException, InvalidInputException {
		error = "";
		try {
			if (event.getKeyCode() == KeyEvent.VK_UP) {
				QuoridorController.moveWall("up");
			}
			if (event.getKeyCode() == KeyEvent.VK_DOWN) {
				QuoridorController.moveWall("down");
			}
			if (event.getKeyCode() == KeyEvent.VK_LEFT) {
				QuoridorController.moveWall("left");
			}
			if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
				QuoridorController.moveWall("right");
			}
		} catch (Exception e) {
			error = "Unable to move the wall";
		}
	}

	public static String getErrMsg() {
		return error;
	}

	public static String getInfoMsg() {
		return error;
	}

}

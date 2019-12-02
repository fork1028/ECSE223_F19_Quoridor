package ca.mcgill.ecse223.quoridor.view;


import java.awt.Color;

import java.util.HashMap;
import java.util.List;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
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
import ca.mcgill.ecse223.quoridor.controller.TOWall;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.MoveDirection;

public class QuoridorGamePage extends JFrame implements KeyListener{

	private static final long serialVersionUID = -45345345345345345L;

	// UI elements

	// players
	private JLabel playerWhiteNameLabel;
	private JLabel playerWhiteTurnLabel;
	private static JLabel playerWhiteClockLabel;

	private JLabel playerBlackNameLabel;
	private JLabel playerBlackTurnLabel;
	private static JLabel playerBlackClockLabel;
	private static final int refreshClockMS = 100;

	private String playerWhiteUsername;
	private String playerBlackUsername;

	private static JLabel blackWon;
	private static JLabel whiteWon;
	private static JLabel draw;

	// Wall
	private JButton moveWall;
	private JButton dropWall;
	private JButton grabWall;
	private JButton rotateWall;
	private JButton cancel;

	// Pawn
	private JButton moveLeft;
	private JButton moveRight;
	private JButton moveUp;
	private JButton moveDown;
	private JButton moveUpRight;
	private JButton moveDownRight;
	private JButton moveUpLeft;
	private JButton moveDownLeft;

	// save game
	private JButton savePosition;
	private JTextField savePositionAs;
	private static JLabel saveTypeLabel;
	private static JComboBox<String> saveTypeList;
	private JButton overwriteYes;
	private JButton overwriteCancel;

	// resign game
	private JButton resignGame;
	
	// step backward
	private static JButton stepBackward;
	private static JButton stepForward;
	
	// board visualizer
	private QuoridorBoardVisualizer boardVisualizer;
	//private QuoridorWallMoveVisualizer wallVisualizer;
	private static final int WIDTH_BOARD = 850;
	private static final int HEIGHT_BOARD = 600;

	// data elements
	private static String error = "";
	private static JLabel errorMsg;

	// WALLs
	private HashMap<TOWall,Rectangle2D> blackWalls;
	private HashMap<TOWall,Rectangle2D> whiteWalls;
	private List<Rectangle2D> rectanglesForWhiteWalls;
	private List<Rectangle2D> rectanglesForBlackWalls;
	private boolean droppedIsClicked=false;
	private static String direction;
	private static boolean directionIsClicked=false;
	private boolean grabClicked=false;
	private boolean moveClicked=false;
	private static int timesTyped=0;
	private static boolean dropIsClicked=false;
	private static boolean dropFail=false;
	private int grabClickedTimes=0;
	private static boolean dropBtnIsClicked=false;
	private static boolean cancelIsClicked=false;
	private static boolean moveIsClicked=false;
	private static boolean moveUpRightIsClicked=false;
	private static boolean moveDownRightIsClicked=false;
	private static boolean moveUpLeftIsClicked=false;
	private static boolean moveDownLeftIsClicked=false;
	private static boolean moveUpIsClicked=false;
	private static boolean moveDownIsClicked=false;
	private static boolean moveLeftIsClicked=false;
	private static boolean moveRightIsClicked=false;
	private static Timer timer;
	private static boolean isBlackWon=false;
	private static boolean isWhiteWon=false;
	private static boolean isDraw = false;
	private static boolean gameStopped=false;


	// graphics
	private static final Color CUSTOM_GREEN = new Color(0, 204, 0);

	/** Constructor to create QuoridorBoardPage */
	public QuoridorGamePage() {
		initComponents();
		refreshData();
		refreshBoardVisualizer();
		overwriteYes.setEnabled(false);
		overwriteCancel.setEnabled(false);
	}

	/**
	 * This method will allow the usernames for the current game to be displayed on Game Page.
	 * Optionally called when the game is initiated.
	 * @param username
	 * @param forBlackPlayer
	 */
	public void setUsernameToDisplay(String username, Boolean forBlackPlayer) {
		if (forBlackPlayer) {
			this.playerBlackUsername = username;
			playerWhiteNameLabel.setText("<html><center><b><u>PLAYER BLACK</u></b><br/>Username: <b>" + username + "</b></html>");
		} else {
			this.playerWhiteUsername = username;
			playerBlackNameLabel.setText("<html><center><b><u>PLAYER WHITE</u></b><br/>Username: <b>" + username + "</b></html>");
		}

	}

	/************ INITIALIZATION AND LAYOUT ***************/

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// elements for error message
		errorMsg = new JLabel();
		errorMsg.setText("error");
		errorMsg.setForeground(Color.blue);

		// elements for white player
		playerWhiteNameLabel = new JLabel();
		playerWhiteNameLabel.setText("PLAYER WHITE");
		// TODO: get username from startpage
		playerWhiteNameLabel.setFont(new Font(null, Font.BOLD, 18));

		playerWhiteTurnLabel = new JLabel();
		playerWhiteTurnLabel.setText("  YOUR TURN  ");
		playerWhiteTurnLabel.setFont(new Font(null, Font.BOLD, 16));
		playerWhiteTurnLabel.setBackground(CUSTOM_GREEN);
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

		blackWon=new JLabel();
		blackWon.setText("BLACK WON!");
		blackWon.setFont(new Font(null, Font.BOLD, 25));
		blackWon.setVisible(false);
		whiteWon=new JLabel();
		whiteWon.setText("WHITE WON!");
		whiteWon.setFont(new Font(null, Font.BOLD, 25));
		whiteWon.setVisible(false);
		draw=new JLabel();
		draw.setText("DRAW!");
		draw.setFont(new Font(null, Font.BOLD, 25));
		draw.setVisible(false);

		// elements for Wall buttons
		moveWall = new JButton();
		moveWall.setText("MOVE WALL");
		dropWall = new JButton();
		dropWall.setText("DROP WALL");
		rotateWall = new JButton();
		rotateWall.setText("ROTATE WALL");
		grabWall = new JButton();
		grabWall.setText("GRAB WALL");
		cancel=new JButton();
		cancel.setText("cancel");

		// pawn
		moveUpRight=new JButton();
		moveUpRight.setText("PAWN NORTH-EAST");
		moveUpLeft=new JButton();
		moveUpLeft.setText("PAWN NORTH-WEST");
		moveDownRight=new JButton();
		moveDownRight.setText("PAWN SOUTH-EAST");
		moveDownLeft=new JButton();
		moveDownLeft.setText("PAWN SOUTH-WEST");

		moveUp=new JButton();
		moveUp.setText("PAWN UP");
		moveDown=new JButton();
		moveDown.setText("PAWN DOWN");
		moveLeft=new JButton();
		moveLeft.setText("PAWN LEFT");
		moveRight=new JButton();
		moveRight.setText("PAWN RIGHT");


		// save and pause game
		savePosition = new JButton();
		savePosition.setText("SAVE GAME/POSITION");
		savePosition.setToolTipText("Enter a filename and click SAVE GAME/POSITION to save current game as a .dat file");
		savePositionAs = new JTextField();
		savePositionAs.setToolTipText("Enter the filename for your saved game .dat file");
		saveTypeLabel = new JLabel();
		saveTypeLabel.setText("Select Save Type:");
		saveTypeList = new JComboBox<String>(new String[0]);
		overwriteYes = new JButton();
		overwriteYes.setText("Overwrite Existing File");
		overwriteCancel = new JButton();
		overwriteCancel.setText("Do NOT overwrite existing file");
		//For save type combo box:
		saveTypeList.removeAllItems();
		saveTypeList.addItem("Position");
		saveTypeList.addItem("Game");
		saveTypeList.setSelectedIndex(-1);

		// resign game
		resignGame = new JButton();
		resignGame.setText("RESIGN GAME");
		
		//forward & backward
		// resign game
		stepBackward = new JButton();
		stepBackward.setText("Previous step");
		stepForward = new JButton();
		stepForward.setText("Next step");

		// visualizer for board
		boardVisualizer = new QuoridorBoardVisualizer();
		boardVisualizer.setMinimumSize(new Dimension(WIDTH_BOARD, HEIGHT_BOARD));

		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Quoridor Board and Game - Group 13");

		// action listeners

		moveWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					if (evt.getActionCommand().equals("MOVE")) {
						// TODO make a wall appear at the default location on the board
						error = "I have a wall in my hand now";
					}
				}
			}
		});
		dropWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					try {
						dropIsClicked(evt);
					} catch (UnsupportedOperationException | InvalidInputException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					droppedIsClicked=true;
				}
			}
		});
		cancel.addKeyListener(this);
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					cancelIsClicked(evt);
				}
			}
		});
		grabWall.addKeyListener(this);
		grabWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					grabIsClicked(evt);
					droppedIsClicked=false;
					error = "I have a wall in my hand now";
				}
			}
		});
		rotateWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					rotateIsClicked(evt);
				}
			}
		});

		moveUpRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					moveUpRightIsClicked(evt);
				}
			}
		});
		moveUpLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if(gameStopped==false) {
					moveUpLeftIsClicked(evt);
				}
			}
		});
		moveDownRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					moveDownRightIsClicked(evt);
				}
			}
		});
		moveDownLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if(gameStopped==false) {
					moveDownLeftIsClicked(evt);
				}
			}
		});
		moveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if(gameStopped==false) {
					moveUpIsClicked(evt);
				}
			}
		});
		moveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					moveDownIsClicked(evt);
				}
			}
		});
		moveLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					moveLeftIsClicked(evt);
				}
			}
		});
		moveRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameStopped==false) {
					moveRightIsClicked(evt);
				}
			}
		});
		savePosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveGameIsClicked(evt);
			}
		});
		resignGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				resignGameIsClicked(evt);
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
		
		stepForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				stepForwardIsClicked(evt);
				}
		});
				
		stepBackward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				stepBackwardIsClicked(evt);
				}
		});

		/**
		 * Timer to decrement remaining player time for current player, and also to set counter ui.
		 * Timer refreshes every 100ms.
		 * @author Helen
		 */
		ActionListener al=new ActionListener() {
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
						playerBlackTurnLabel.setBackground(CUSTOM_GREEN);
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
						playerWhiteTurnLabel.setBackground(CUSTOM_GREEN);
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

				QuoridorController.initiateGameResult();
				refreshData();
			}
		};
		timer=new Timer(refreshClockMS,al);
		timer.start();

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
				.addGroup(layout.createSequentialGroup().addComponent(savePositionAs).addComponent(savePosition).addComponent(resignGame)
						.addComponent(saveTypeLabel).addComponent(saveTypeList).addComponent(overwriteYes).addComponent(overwriteCancel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(blackWon).addComponent(whiteWon).addComponent(draw))
				// player1, board, player2
				.addGroup(layout.createSequentialGroup()
						// player1 controls etc on left
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerWhiteNameLabel)
								.addComponent(playerWhiteTurnLabel)
								.addComponent(playerWhiteClockLabel)
								)

						// board in middle
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(boardVisualizer)
								.addGroup(layout.createSequentialGroup()
										// walls and pawn buttons
										.addComponent(grabWall).addComponent(rotateWall)
										.addComponent(dropWall).addComponent(cancel))
								.addGroup(layout.createSequentialGroup().addComponent(moveUp).addComponent(moveDown).addComponent(moveLeft).addComponent(moveRight))
								.addGroup(layout.createSequentialGroup().addComponent(moveUpLeft).addComponent(moveUpRight).addComponent(moveDownLeft).addComponent(moveDownRight)))
						//.addComponent(wallVisualizer)
						// player2 controls etc on right
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerBlackNameLabel)
								.addComponent(playerBlackTurnLabel)
								.addComponent(playerBlackClockLabel)
								))
					/*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(stepBackward)
						.addComponent(stepForward)
						)*/
				.addGroup(layout.createSequentialGroup()
						// error msg
						.addComponent(errorMsg)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				// main controls (save, pause)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(savePositionAs)
						.addComponent(saveTypeLabel).addComponent(saveTypeList)
						.addComponent(savePosition).addComponent(resignGame)
						// TODO save game name
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(overwriteYes)
						.addComponent(overwriteCancel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(blackWon).addComponent(whiteWon).addComponent(draw))
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
										.addComponent(grabWall).addComponent(rotateWall)
										.addComponent(dropWall).addComponent(cancel))
								.addGroup(layout.createParallelGroup().addComponent(moveUp).addComponent(moveDown).addComponent(moveLeft).addComponent(moveRight))
								.addGroup(layout.createParallelGroup().addComponent(moveUpLeft).addComponent(moveUpRight).addComponent(moveDownLeft).addComponent(moveDownRight)))
						//.addComponent(wallVisualizer)
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

	/**
	 * This method refreshes any components on the game page.
	 * @author Helen Lin, 260715521
	 */
	public static void refreshData() {
		// TODO ???
		// update error message
		errorMsg.setText(error);
		if(isBlackWon==true) {
			blackWon.setVisible(true);
			gameStopped=true;
			playerWhiteClockLabel.setText("00:00");
			playerBlackClockLabel.setText("00:00");
		}
		if(isWhiteWon==true) {
			whiteWon.setVisible(true);
			gameStopped=true;
			playerWhiteClockLabel.setText("00:00");
			playerBlackClockLabel.setText("00:00");
		}
		if(isDraw==true) {
			draw.setVisible(true);
			gameStopped=true;
			playerWhiteClockLabel.setText("00:00");
			playerBlackClockLabel.setText("00:00");
		}

		

	}

	private void refreshBoardVisualizer() {
		// TODO - NOTE (Helen): nov22, I don't think we need this --> board is automatically
		//refreshed whenever this class or boardVisualizer calls repaint();
		// board visualizer already automatically detects which tile is clicked
		// implement for walls
	}

	/************ ACTION PERFORMED METHODS ***************/


	/**
	 * action method for grabbing a wall
	 * @author Xinyue Chen
	 * @param evt
	 */
	private void grabIsClicked(java.awt.event.ActionEvent evt) {
		grabClickedTimes++;
		if(grabClickedTimes>1) {
			//QuoridorController.switchCurrentPlayer();
		}

		grabClicked=true;
		QuoridorBoardVisualizer.grabIsClicked(true);

		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		QuoridorController.grabWall(player);
		repaint();

	}

	/**
	 * action method for rotating the wall
	 * @author Xinyue Chen
	 * @param evt
	 */
	private void rotateIsClicked(java.awt.event.ActionEvent evt) {

		try {
			Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
			Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
			WallMove move = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
			String dir = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection()
					.toString().toLowerCase();

			QuoridorController.rotateWall(wall, move, dir);
			QuoridorBoardVisualizer.rotateIsClicked(true);



		} catch (Exception e) {
			error = "error rotating wall";
		}
		repaint();
		refreshData();
	}

	/**
	 * method for calling the controller method to update the model if the user clicked on drop button
	 * @author Xinyue Chen
	 * @param evt
	 * @throws UnsupportedOperationException
	 * @throws InvalidInputException
	 */
	private void dropIsClicked(java.awt.event.ActionEvent evt) throws UnsupportedOperationException, InvalidInputException {
		Player player = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Wall wall = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallPlaced();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		//TOWall wall=QuoridorController.getWallCandidate();
		grabClickedTimes++;

		try {
			if(player==whitePlayer) {
				QuoridorController.dropWall(player, wall);
				dropFail=false;
				error="";
				QuoridorBoardVisualizer.incrementWhiteDropDone();
				QuoridorBoardVisualizer.setDropIsClicked(true);
				repaint();
			}
			else {
				QuoridorController.dropWall(player, wall);
				dropFail=false;
				error="";
				QuoridorBoardVisualizer.incrementBlackDropDone();
				QuoridorBoardVisualizer.setDropIsClicked(true);
				repaint();
			}

		} catch (UnsupportedOperationException e1) {
			// TODO Auto-generated catch block
			error="Unable to drop the wall here";
			dropFail=true;
			e1.printStackTrace();
		}
		refreshData();
		dropBtnIsClicked=true;
		if(grabClickedTimes>=2) {
			if(!error.contentEquals("Unable to drop the wall here")) {
				if(dropBtnIsClicked==true) {
					QuoridorController.switchCurrentPlayer();
				}
			}
		}
		dropBtnIsClicked=false;
		grabClicked=false;

	}

	/**
	 * method for canceling grabbing a wall
	 * @author Xinyue Chen
	 * @param evt
	 */
	private void cancelIsClicked(java.awt.event.ActionEvent evt) {
		cancelIsClicked=true;
		repaint();

	}



	/*******************MOVE AND JUMP PAWN ACTION RESPONSES**************************************/

	/*
	 * The following methods are called whenever a player attempts to move their pawn using the buttons.
	 * @author Xinyue + Helen
	 */

	private void moveUpRightIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.NorthEast);
	}

	/**
	 * This method is called whenever a player attempts to move their pawn UP-LEFT.
	 * @author Xinyue + Helen
	 * @param evt
	 */
	private void moveUpLeftIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.NorthWest);
	}

	private void moveDownLeftIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.SouthWest);
	}

	private void moveDownRightIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.SouthEast);
	}

	private void moveUpIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.North);
	}

	private void moveDownIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.South);
	}

	private void moveLeftIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.West);
	}

	private void moveRightIsClicked(java.awt.event.ActionEvent evt) {
		this.moveAction(MoveDirection.East);
	}

	/**
	 * Moves the pawn in the appropriate direction by calling controller method and updates GUI elements.
	 * @param dir
	 * @author Helen Lin, Xinyue Chen
	 */
	private void moveAction(MoveDirection dir) {
		String currentPlayer = (QuoridorController.isBlackTurn()) ? "black player" : "white player";
		try {
			//try the move and display the result
			if (QuoridorController.movePawn(dir)) {
				//move success
				error = "Successful move, " + currentPlayer + "!";
			} else {
				//move was illegal, display that to the user
				error="Illegal move, " + currentPlayer + ". Try again :)";
			}
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error="Unable to move the pawn for " + currentPlayer;
		}
		QuoridorController.initiateGameResult();
		repaint(); //refresh board
		refreshData(); //update game page

	}

	/*******************SAVE POSITION**************************************/

	private void saveGameIsClicked(java.awt.event.ActionEvent evt) {
		error = "";
		Boolean tmp = false;
		if (savePositionAs.getText() == "") {
			error = "Must include the file name you wish to save";
		} else if (saveTypeList.getSelectedIndex() <0) {
			error = "Must specify whether saving as a position or game";
		}else if (saveTypeList.getSelectedItem().toString().equals("Position")){
			tmp = QuoridorController.attemptToSavePosition(savePositionAs.getText());
		} else if (saveTypeList.getSelectedItem().toString().equals("Game")) {
			tmp = QuoridorController.attemptToSaveGame(savePositionAs.getText());
		}

		if (error == "" && !tmp) {
			overwriteYes.setEnabled(true);
			overwriteCancel.setEnabled(true);
		}
	}

	private void overwriteYesIsClicked(java.awt.event.ActionEvent evt) {
		
		if (saveTypeList.getSelectedItem().toString().equals("Position")){
			QuoridorController.overWriteSavePosition(savePositionAs.getText(), true);
		} else if (saveTypeList.getSelectedItem().toString().equals("Game")) {
			QuoridorController.overWriteSaveGame(savePositionAs.getText(), true);
		}
		overwriteYes.setEnabled(false);
		overwriteCancel.setEnabled(false);
	}

	private void overwriteCancelIsClicked(java.awt.event.ActionEvent evt) {
		
		if (saveTypeList.getSelectedItem().toString().equals("Position")){
			QuoridorController.overWriteSavePosition(savePositionAs.getText(), false);
		} else if (saveTypeList.getSelectedItem().toString().equals("Game")) {
			QuoridorController.overWriteSaveGame(savePositionAs.getText(), false);
		}
		overwriteYes.setEnabled(false);
		overwriteCancel.setEnabled(false);
	}

	/**************** RESIGN GAME ****************/	
	private void resignGameIsClicked(java.awt.event.ActionEvent evt) {
		QuoridorController.resignGame(QuoridorController.getCurrentPlayer().getPlayerToMove());
		resignGame.setEnabled(false);
	}

	public static String getErrMsg() {
		return error;
	}

	public static String getInfoMsg() {
		return error;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * method for drawing updated pawns/walls triggered by arrow keys
	 * @author Xinyue Chen
	 *
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_UP) {

			if(grabClicked==true) {
				QuoridorBoardVisualizer.setMoveClicked(true);
				direction="up";
				try {
					QuoridorController.moveWall("up");
					error="";
				} catch (UnsupportedOperationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					error="Unable to move wall further";
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			repaint();

			refreshData();

		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			QuoridorBoardVisualizer.setMoveClicked(true);
			direction="down";

			if(grabClicked==true) {
				try {
					QuoridorController.moveWall("down");
					error="";
				} catch (UnsupportedOperationException e1) {
					// TODO Auto-generated catch block
					error="Unable to move wall further";
					e1.printStackTrace();
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			refreshData();
			repaint();


		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			QuoridorBoardVisualizer.setMoveClicked(true);
			direction="left";
			if(grabClicked==true) {
				try {
					QuoridorController.moveWall("left");
					error="";
				} catch (UnsupportedOperationException e1) {
					// TODO Auto-generated catch block
					error="Unable to move wall further";
					e1.printStackTrace();
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}

			repaint();

			refreshData();



		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			QuoridorBoardVisualizer.setMoveClicked(true);
			direction="right";
			if(grabClicked==true) {
				try {
					QuoridorController.moveWall("right");
					error="";
				} catch (UnsupportedOperationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					error="Unable to move wall further";
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				QuoridorBoardVisualizer.setMoveClicked(true);

			}

			repaint();
			refreshData();

		}


	}
	
	/** 
	 * replay
	 * @throws InvalidInputException 
	 * */
	private void stepForwardIsClicked(java.awt.event.ActionEvent evt) {
		QuoridorController.stepForward();
	}
	
	private void stepBackwardIsClicked(java.awt.event.ActionEvent evt) {
		QuoridorController.stepBackward();
	}

	/**
	 * helper methods below to receive/send messages to view
	 * @author Xinyue Chen
	 * @return
	 */

	public static boolean getDropFailed() {
		return dropFail;
	}

	public static int getTimesTyped() {
		return timesTyped;
	}

	public static String getDirection() {
		//System.out.println("direction:"+direction);
		return direction;
	}

	public static boolean getDirectionIsClicked() {
		return directionIsClicked;
	}

	public static boolean getDropIsClicked() {
		return dropIsClicked;
	}

	public static void setError(String input) {
		error=input;
	}

	public static boolean cancelIsClicked() {
		return cancelIsClicked;
	}

	public static void setCancel(boolean input) {
		cancelIsClicked=false;
	}

	public static boolean moveIsClicked() {
		return moveIsClicked;
	}

	public static boolean getMoveDownRightIsClicked() {
		return moveDownRightIsClicked;
	}
	public static boolean getMoveUpRightIsClicked() {
		return moveUpRightIsClicked;
	}
	public static boolean getMoveDownLeftIsClicked() {
		return moveDownLeftIsClicked;
	}
	public static boolean getMoveUpLeftIsClicked() {
		return moveUpLeftIsClicked;
	}

	public static boolean getMoveUpIsClicked() {
		return moveUpIsClicked;
	}

	public static boolean getMoveDownIsClicked() {
		return moveDownIsClicked;
	}

	public static boolean getMoveLeftIsClicked() {
		return moveLeftIsClicked;
	}

	public static boolean getMoveRightIsClicked() {
		return moveRightIsClicked;
	}

	public static void setMoveUpIsClicked(boolean input) {
		moveUpIsClicked=input;
	}
	public static void setMoveDownIsClicked(boolean input) {
		moveDownIsClicked=input;
	}
	public static void setMoveLeftIsClicked(boolean input) {
		moveLeftIsClicked=input;
	}
	public static void setMoveRightIsClicked(boolean input) {
		moveRightIsClicked=input;
	}

	public static Timer getTimer() {
		return timer;
	}

	public static void setBlackWon(boolean input) {
		isBlackWon=input;
	}

	public static void setWhiteWon(boolean input) {
		isWhiteWon=input;
	}

	public static void setDraw(boolean input) {
		isDraw=input;
	}
	
	public static boolean getBlackWon() {
		return isBlackWon;
	}

	public static boolean getWhiteWon() {
		return isWhiteWon;
	}

	public static boolean getGameStopped() {
		return gameStopped;
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}

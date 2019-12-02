package ca.mcgill.ecse223.quoridor.view;


import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JTextField;
import javax.swing.WindowConstants;

import javax.swing.Timer;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.InvalidInputException;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Player;
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

	// game controls
	private JButton resignGame;
	private JButton pauseGame;
	private static boolean pauseGameOn = false;
	
	// step backward
	private static JButton stepBackward;
	private static JButton stepForward;
	
	//replay mode
	private JButton replayMode;
	private JButton jumpToFinal;
	private JButton jumpToStart;
	private static boolean replayModeOn = false;
	
	// board visualizer
	private QuoridorBoardVisualizer boardVisualizer;

	// data elements
	private static String error = "Welcome to Quoridor!";
	private static JLabel errorMsg;

	// WALLs
	private boolean droppedIsClicked=false;
	private static String direction;
	private static boolean directionIsClicked=false;
	private boolean grabClicked=false;
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
	private static final int WIDTH_BOARD = 900;
	private static final int HEIGHT_BOARD = 600;
	private static final int WIDTH_GAME_PAGE= 1500;
	private static final int HEIGHT_GAMEPAGE = 900;
	private static final Font BIGGEST_FONT = new Font("Verdana", Font.BOLD, 50);
	private static final Font BIG_FONT = new Font("Verdana", Font.PLAIN, 30);
	private static final Font NORMAL_FONT = new Font("Verdana", Font.PLAIN, 18);
	private static final Font SMALL_FONT = new Font("Verdana", Font.PLAIN, 14);
	private static final Color CUSTOM_GREEN = new Color(0, 204, 0);
	private static final Color BUTTON_COLOUR_DEFAULT = new Color(99,255,252); //cyan blue colour
	private static final Color BUTTON_COLOUR_RED = new Color(255,204,203); //light red colour
	private static final Color BUTTON_COLOUR_GRAY = Color.LIGHT_GRAY; //light red colour
	private static final Color BUTTON_COLOUR_PURPLE = new Color(229,204,255); //light purple
	private static final Color BUTTON_COLOUR_GREEN = new Color(208,240,192); //light green
	
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
			playerBlackNameLabel.setText("<html><center><b><u>PLAYER BLACK</u></b><br/>Username: <b>" + username + "</b></html>");
		} else {
			this.playerWhiteUsername = username;
			playerWhiteNameLabel.setText("<html><center><b><u>PLAYER WHITE</u></b><br/>Username: <b>" + username + "</b></html>");
		}

	}

	/************************** INITIALIZATION AND LAYOUT ********************************/

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(new Dimension(WIDTH_GAME_PAGE, HEIGHT_GAMEPAGE));
		// elements for error message
		errorMsg = new JLabel();
		errorMsg.setText(error);
		errorMsg.setFont(SMALL_FONT);
		errorMsg.setForeground(Color.blue);
		errorMsg.setBackground(Color.GRAY);

		// elements for white player
		playerWhiteNameLabel = new JLabel();
		playerWhiteNameLabel.setText("PLAYER WHITE");
		playerWhiteNameLabel.setFont(BIG_FONT);

		playerWhiteTurnLabel = new JLabel();
		playerWhiteTurnLabel.setText("  YOUR TURN  ");
		playerWhiteTurnLabel.setFont(BIG_FONT);
		playerWhiteTurnLabel.setBackground(CUSTOM_GREEN);
		playerWhiteTurnLabel.setOpaque(true);

		playerWhiteClockLabel = new JLabel();
		playerWhiteClockLabel.setText("MM:SS");
		playerWhiteClockLabel.setFont(BIGGEST_FONT);
		playerWhiteClockLabel.setBackground(Color.LIGHT_GRAY);
		playerWhiteClockLabel.setOpaque(true);

		// elements for black player
		playerBlackNameLabel = new JLabel();
		playerBlackNameLabel.setText("PLAYER BLACK");
		playerBlackNameLabel.setFont(BIG_FONT);

		playerBlackTurnLabel = new JLabel();
		playerBlackTurnLabel.setText("       WAIT        ");
		playerBlackTurnLabel.setFont(BIG_FONT);
		playerBlackTurnLabel.setBackground(Color.LIGHT_GRAY);
		playerBlackTurnLabel.setOpaque(true);

		playerBlackClockLabel = new JLabel();
		playerBlackClockLabel.setText("MM:SS");
		playerBlackClockLabel.setFont(BIGGEST_FONT);
		playerBlackClockLabel.setBackground(Color.LIGHT_GRAY);
		playerBlackClockLabel.setOpaque(true);

		blackWon=new JLabel();
		blackWon.setText("BLACK WON!");
		blackWon.setFont(BIG_FONT);
		blackWon.setVisible(false);
		whiteWon=new JLabel();
		whiteWon.setText("WHITE WON!");
		whiteWon.setFont(BIG_FONT);
		whiteWon.setVisible(false);
		draw=new JLabel();
		draw.setText("DRAW!");
		draw.setFont(BIG_FONT);
		draw.setVisible(false);

		// elements for Wall buttons
		moveWall = new JButton();
		moveWall.setText("MOVE WALL");
		moveWall.setFont(NORMAL_FONT);
		moveWall.setBackground(BUTTON_COLOUR_DEFAULT);
		dropWall = new JButton();
		dropWall.setText("DROP WALL");
		dropWall.setFont(NORMAL_FONT);
		dropWall.setBackground(BUTTON_COLOUR_DEFAULT);
		rotateWall = new JButton();
		rotateWall.setText("ROTATE WALL");
		rotateWall.setFont(NORMAL_FONT);
		rotateWall.setBackground(BUTTON_COLOUR_DEFAULT);
		grabWall = new JButton();
		grabWall.setText("GRAB WALL");
		grabWall.setFont(NORMAL_FONT);
		grabWall.setBackground(BUTTON_COLOUR_DEFAULT);
		cancel=new JButton();
		cancel.setText("Cancel Wall Move");
		cancel.setFont(NORMAL_FONT);
		cancel.setBackground(BUTTON_COLOUR_RED);

		// pawn
		moveUpRight=new JButton();
		moveUpRight.setText("PAWN NORTH-EAST");
		moveUpRight.setFont(NORMAL_FONT);
		moveUpRight.setBackground(BUTTON_COLOUR_GRAY);
		moveUpLeft=new JButton();
		moveUpLeft.setText("PAWN NORTH-WEST");
		moveUpLeft.setFont(NORMAL_FONT);
		moveUpLeft.setBackground(BUTTON_COLOUR_GRAY);
		moveDownRight=new JButton();
		moveDownRight.setText("PAWN SOUTH-EAST");
		moveDownRight.setFont(NORMAL_FONT);
		moveDownRight.setBackground(BUTTON_COLOUR_GRAY);
		moveDownLeft=new JButton();
		moveDownLeft.setText("PAWN SOUTH-WEST");
		moveDownLeft.setFont(NORMAL_FONT);
		moveDownLeft.setBackground(BUTTON_COLOUR_GRAY);
		moveUp=new JButton();
		moveUp.setText("PAWN UP");
		moveUp.setFont(NORMAL_FONT);
		moveUp.setBackground(BUTTON_COLOUR_DEFAULT);
		moveDown=new JButton();
		moveDown.setText("PAWN DOWN");
		moveDown.setFont(NORMAL_FONT);
		moveDown.setBackground(BUTTON_COLOUR_DEFAULT);
		moveLeft=new JButton();
		moveLeft.setText("PAWN LEFT");
		moveLeft.setFont(NORMAL_FONT);
		moveLeft.setBackground(BUTTON_COLOUR_DEFAULT);
		moveRight=new JButton();
		moveRight.setText("PAWN RIGHT");
		moveRight.setFont(NORMAL_FONT);
		moveRight.setBackground(BUTTON_COLOUR_DEFAULT);


		// save and pause game
		savePosition = new JButton();
		savePosition.setText("SAVE GAME/POSITION");
		savePosition.setFont(NORMAL_FONT);
		savePosition.setBackground(BUTTON_COLOUR_GREEN);
		savePosition.setToolTipText("Enter a filename and click SAVE GAME/POSITION to save current game as a .dat file");
		savePositionAs = new JTextField();
		savePositionAs.setToolTipText("Enter the filename for your saved game .dat file");
		saveTypeLabel = new JLabel();
		saveTypeLabel.setText("Select Save Type:");
		saveTypeLabel.setFont(NORMAL_FONT);
		saveTypeList = new JComboBox<String>(new String[0]);
		overwriteYes = new JButton();
		overwriteYes.setText("Overwrite Existing File");
		overwriteYes.setFont(NORMAL_FONT);
		overwriteYes.setBackground(BUTTON_COLOUR_GREEN);
		overwriteCancel = new JButton();
		overwriteCancel.setText("Do NOT overwrite existing file");
		overwriteCancel.setFont(NORMAL_FONT);
		overwriteCancel.setBackground(BUTTON_COLOUR_PURPLE);
		//For save type combo box:
		saveTypeList.removeAllItems();
		saveTypeList.addItem("Position");
		saveTypeList.addItem("Game");
		saveTypeList.setSelectedIndex(-1);

		// game controls
		resignGame = new JButton();
		resignGame.setText("RESIGN GAME");
		resignGame.setFont(NORMAL_FONT);
		resignGame.setBackground(BUTTON_COLOUR_PURPLE);
		
		pauseGame = new JButton();
		pauseGame.setText("PAUSE");
		pauseGame.setFont(NORMAL_FONT);
		pauseGame.setBackground(BUTTON_COLOUR_PURPLE);
		
		//replay mode
		replayMode = new JButton();
		replayMode.setText("REPLAY MODE");
		replayMode.setFont(NORMAL_FONT);
		replayMode.setBackground(BUTTON_COLOUR_PURPLE);
		jumpToFinal = new JButton();
		jumpToFinal.setText("JUMP TO FINAL");
		jumpToFinal.setFont(NORMAL_FONT);
		jumpToFinal.setBackground(BUTTON_COLOUR_DEFAULT);
		jumpToFinal.setEnabled(false);
		jumpToStart = new JButton();
		jumpToStart.setText("JUMP TO START");
		jumpToStart.setFont(NORMAL_FONT);
		jumpToStart.setBackground(BUTTON_COLOUR_DEFAULT);
		jumpToStart.setEnabled(false);
		
		stepBackward = new JButton();
		stepBackward.setText("STEP BACKWARD");
		stepBackward.setFont(NORMAL_FONT);
		stepBackward.setBackground(BUTTON_COLOUR_DEFAULT);
		stepBackward.setEnabled(false);
		stepForward = new JButton();
		stepForward.setText("STEP FORWARD");
		stepForward.setFont(NORMAL_FONT);
		stepForward.setBackground(BUTTON_COLOUR_DEFAULT);
		stepForward.setEnabled(false);
		
		// visualizer for board
		boardVisualizer = new QuoridorBoardVisualizer();
		boardVisualizer.setMinimumSize(new Dimension(WIDTH_BOARD, HEIGHT_BOARD));

		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Quoridor Board and Game - Group 13");

		// ***************action listeners************************

		moveWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (gameStopped==false) {
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

		pauseGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pauseGameIsClicked(evt);
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

		replayMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				replayModeIsClicked(evt);
			}
		});
		
		jumpToFinal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jumpToFinalIsClicked(evt);
			}
		});
		
		jumpToStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jumpToStartIsClicked(evt);
			}
		});


		/**
		 * Timer to decrement remaining player time for current player, and also to set counter ui.
		 * Timer refreshes every 100ms.
		 * @author Helen Lin, 260715521
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
		// add players' buttons on each left or right side
		// board in middle
		layout.setHorizontalGroup(layout.createParallelGroup()
				// main controls (save, pause)
				.addGroup(layout.createSequentialGroup()
						.addComponent(savePositionAs)
						.addComponent(savePosition)
						.addComponent(saveTypeLabel)
						.addComponent(saveTypeList)
						.addComponent(overwriteYes)
						.addComponent(overwriteCancel))
				.addGroup(layout.createSequentialGroup()
						.addComponent(resignGame)
						.addComponent(pauseGame)
						.addComponent(replayMode)
						.addComponent(jumpToStart)
						.addComponent(jumpToFinal)
				.addComponent(stepBackward)
				.addComponent(stepForward)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(blackWon)
						.addComponent(whiteWon)
						.addComponent(draw))
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
										.addComponent(grabWall)
										.addComponent(rotateWall)
										.addComponent(dropWall)
										.addComponent(cancel))
								.addGroup(layout.createSequentialGroup()
										.addComponent(moveUp)
										.addComponent(moveDown)
										.addComponent(moveLeft)
										.addComponent(moveRight))
								.addGroup(layout.createSequentialGroup()
										.addComponent(moveUpLeft)
										.addComponent(moveUpRight)
										.addComponent(moveDownLeft).addComponent(moveDownRight)))
						// player2 controls etc on right
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerBlackNameLabel)
								.addComponent(playerBlackTurnLabel)
								.addComponent(playerBlackClockLabel)
								))
					
				.addGroup(layout.createSequentialGroup()
						// error msg
						.addComponent(errorMsg)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				// main controls (save, pause)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(savePositionAs)
						.addComponent(saveTypeLabel)
						.addComponent(saveTypeList)
						.addComponent(savePosition)
						.addComponent(overwriteYes)
						.addComponent(overwriteCancel)
						// TODO save game name
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(resignGame)
						.addComponent(pauseGame)
						.addComponent(replayMode)
						.addComponent(jumpToStart)
						.addComponent(jumpToFinal)
						.addComponent(stepBackward)
						.addComponent(stepForward))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(blackWon)
						.addComponent(whiteWon)
						.addComponent(draw))
				// player1, board, player2
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						// player1 controls etc on left
						.addGroup(layout.createSequentialGroup().addComponent(playerWhiteNameLabel)
								.addComponent(playerWhiteTurnLabel).addComponent(playerWhiteClockLabel)
								// TODO add stock
								)

						// board in middle
						.addGroup(layout.createSequentialGroup()
								.addComponent(boardVisualizer)
								// buttons and error msg
								.addGroup(layout.createParallelGroup()
										// walls and pawn buttons
										.addComponent(grabWall)
										.addComponent(rotateWall)
										.addComponent(dropWall)
										.addComponent(cancel))
								.addGroup(layout.createParallelGroup()
										.addComponent(moveUp)
										.addComponent(moveDown)
										.addComponent(moveLeft)
										.addComponent(moveRight))
								.addGroup(layout.createParallelGroup()
										.addComponent(moveUpLeft)
										.addComponent(moveUpRight)
										.addComponent(moveDownLeft)
										.addComponent(moveDownRight)))
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
		// NOTE (Helen): nov22, I don't think we need this --> board is automatically
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

	/**
	 * This method allows the user to pause or unpause a game
	 * @param evt
	 */
	private void pauseGameIsClicked(java.awt.event.ActionEvent evt) {
		
		if (pauseGameOn) {
			pauseGameOn = false;
		} else {
			pauseGameOn = true;
		}
		if (pauseGameOn && getTimer()!=null) {
			//pause game and disable certain buttons
			getTimer().stop();

			resignGame.setEnabled(false);
			moveUp.setEnabled(false);
			moveDown.setEnabled(false);
			moveLeft.setEnabled(false);
			moveRight.setEnabled(false);
			moveUpRight.setEnabled(false);
			moveDownLeft.setEnabled(false);
			moveUpLeft.setEnabled(false);
			moveDownRight.setEnabled(false);
			dropWall.setEnabled(false);
			grabWall.setEnabled(false);
			rotateWall.setEnabled(false);
			dropWall.setEnabled(false);
			cancel.setEnabled(false);
			//change display to unpause
			pauseGame.setText("UNPAUSE");
		} else if (!pauseGameOn && getTimer()!=null){
			//unpause game and disable certain buttons
			getTimer().restart();
			
			resignGame.setEnabled(true);
			pauseGame.setEnabled(true);
			moveUp.setEnabled(true);
			moveDown.setEnabled(true);
			moveLeft.setEnabled(true);
			moveRight.setEnabled(true);
			moveUpRight.setEnabled(true);
			moveDownLeft.setEnabled(true);
			moveUpLeft.setEnabled(true);
			moveDownRight.setEnabled(true);
			dropWall.setEnabled(true);
			grabWall.setEnabled(true);
			rotateWall.setEnabled(true);
			dropWall.setEnabled(true);
			cancel.setEnabled(true);
			//change display to REPLAY MODE
			replayMode.setText("PAUSE");
			
		}
		
		repaint(); //refresh board
		refreshData(); //update game page
		
	}
	
	/******************REPLAY MODE***********************************/
	/**
	 * This method allows a user to enter replay mode for a current game
	 * @param evt
	 * @author Helen Lin, 260715521
	 */
	private void replayModeIsClicked(java.awt.event.ActionEvent evt) {
		//toggle replay mode status
		if (replayModeOn) {
			replayModeOn = false;
		} else {
			replayModeOn = true;
		}
		try {
			if (replayModeOn) {
				//if game was paused, reset it to default
				if (pauseGameOn) {
					pauseGameOn = false;
					pauseGame.setText("PAUSE");
				}
				//start replay mode
				QuoridorController.enterReplayMode();
				jumpToFinal.setEnabled(true);
				jumpToStart.setEnabled(true);
				stepForward.setEnabled(true);
				stepBackward.setEnabled(true);
				
				resignGame.setEnabled(false);
				pauseGame.setEnabled(false);
				moveUp.setEnabled(false);
				moveDown.setEnabled(false);
				moveLeft.setEnabled(false);
				moveRight.setEnabled(false);
				moveUpRight.setEnabled(false);
				moveDownLeft.setEnabled(false);
				moveUpLeft.setEnabled(false);
				moveDownRight.setEnabled(false);
				dropWall.setEnabled(false);
				grabWall.setEnabled(false);
				rotateWall.setEnabled(false);
				dropWall.setEnabled(false);
				cancel.setEnabled(false);
				//change display to CONTINUE GAME
				replayMode.setText("CONTINUE GAME FROM HERE");
			} else {
				//exit replay mode and continue game from here
				QuoridorController.continueGameFromCurrent();
				jumpToFinal.setEnabled(false);
				jumpToStart.setEnabled(false);
				stepForward.setEnabled(false);
				stepBackward.setEnabled(false);
				
				resignGame.setEnabled(true);
				pauseGame.setEnabled(true);
				moveUp.setEnabled(true);
				moveDown.setEnabled(true);
				moveLeft.setEnabled(true);
				moveRight.setEnabled(true);
				moveUpRight.setEnabled(true);
				moveDownLeft.setEnabled(true);
				moveUpLeft.setEnabled(true);
				moveDownRight.setEnabled(true);
				dropWall.setEnabled(true);
				grabWall.setEnabled(true);
				rotateWall.setEnabled(true);
				dropWall.setEnabled(true);
				cancel.setEnabled(true);
				//change display to REPLAY MODE
				replayMode.setText("REPLAY MODE");
				
			}
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		repaint(); //refresh board
		refreshData(); //update game page
		
	}
	
	/**
	 * This method allows the user to jump to the final position from the previous game
	 * when in replay mode
	 * @param evt
	 * @author Helen Lin, 260715521
	 */
	private void jumpToFinalIsClicked(java.awt.event.ActionEvent evt) {
		try {
			QuoridorController.jumpToFinal();
			jumpToFinal.setEnabled(false);
			jumpToStart.setEnabled(true);
			stepForward.setEnabled(false);
			stepBackward.setEnabled(true);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		repaint(); //refresh board
		refreshData(); //update game page
		
	}
	
	/**
	 * This method allows the user to jump to the first position from the previous game
	 * when in replay mode
	 * @param evt
	 * @author Helen Lin, 260715521
	 */
	private void jumpToStartIsClicked(java.awt.event.ActionEvent evt) {
		try {
			QuoridorController.jumpToStart();
			jumpToFinal.setEnabled(true);
			jumpToStart.setEnabled(false);
			stepForward.setEnabled(true);
			stepBackward.setEnabled(false);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		repaint(); //refresh board
		refreshData(); //update game page
	}
	
	
	/*****************************************/

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
	 * Replay
	 * 
	 * */
	private void stepForwardIsClicked(java.awt.event.ActionEvent evt) {
		try{QuoridorController.stepForward();}
		catch (Exception e) {
			error = "The user is not in replay mode";
		}
		
	}
	
	private void stepBackwardIsClicked(java.awt.event.ActionEvent evt) {
		try{QuoridorController.stepBackward();}
		catch (Exception e) {
			error = "The user is not in replay mode";
		}
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

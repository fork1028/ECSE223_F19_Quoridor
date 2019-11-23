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
	private JButton cancel;
	
	// Pawn
	private JButton grabPawn; 
	//private JButton dropPawn;
	private JButton moveLeft;
	private JButton moveRight;
	private JButton moveUp;
	private JButton moveDown;
	private JButton moveUpRight;
	private JButton moveDownRight;
	private JButton moveUpLeft;
	private JButton moveDownLeft;

	// save game
	private JButton saveGame;
	private JTextField saveGameAs;
	private JButton overwriteYes;
	private JButton overwriteCancel;

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
	private static boolean grabPawnIsClicked=false;
	private static boolean moveIsClicked=false;
	private static boolean dropPawnIsClicked=false;
	private static boolean dropPawnBtnIsClicked=false;
	private static boolean moveUpRightIsClicked=false;
	private static boolean moveDownRightIsClicked=false;
	private static boolean moveUpLeftIsClicked=false;
	private static boolean moveDownLeftIsClicked=false;
	private static boolean moveUpIsClicked=false;
	private static boolean moveDownIsClicked=false;
	private static boolean moveLeftIsClicked=false;
	private static boolean moveRightIsClicked=false;
	
	
	

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
		grabPawn=new JButton();
		grabPawn.setText("GRAB PAWN");
		moveUpRight=new JButton();
		moveUpRight.setText("MOVE UP RIGHT");
		moveUpLeft=new JButton();
		moveUpLeft.setText("MOVE UP LEFT");
		moveDownRight=new JButton();
		moveDownRight.setText("MOVE DOWN RIGHT");
		moveDownLeft=new JButton();
		moveDownLeft.setText("MOVE DOWN LEFT");
		
		moveUp=new JButton();
		moveUp.setText("MOVE UP");
		moveDown=new JButton();
		moveDown.setText("MOVE DOWN");
		moveLeft=new JButton();
		moveLeft.setText("MOVE LEFT");
		moveRight=new JButton();
		moveRight.setText("MOVE RIGHT");
		

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
//		wallVisualizer = new QuoridorWallMoveVisualizer();
//		wallVisualizer.setMinimumSize(new Dimension(WIDTH_BOARD, HEIGHT_BOARD));
		
		//WALLS
//		blackWalls=QuoridorWallMoveVisualizer.getBlackWalls();
//		whiteWalls=QuoridorBoardVisualizer.getWhiteWalls();
//		rectanglesForWhiteWalls=QuoridorBoardVisualizer.getWhiteRectangles();
//		rectanglesForBlackWalls=QuoridorBoardVisualizer.getBlackRectangles();

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
				try {
					dropIsClicked(evt);
				} catch (UnsupportedOperationException | InvalidInputException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				droppedIsClicked=true;
			}
		});
		cancel.addKeyListener(this);
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				cancelIsClicked(evt);
			}
		});
		grabWall.addKeyListener(this);
		grabWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				grabIsClicked(evt);
				droppedIsClicked=false;
				error = "I have a wall in my hand now";
			}
		});
		rotateWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				rotateIsClicked(evt);
			}
		});
		grabPawn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				grabPawnIsClicked(evt);
			}
		});
		moveUpRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveUpRightIsClicked(evt);
			}
		});
		moveUpLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveUpLeftIsClicked(evt);
			}
		});
		moveDownRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveDownRightIsClicked(evt);
			}
		});
		moveDownLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveDownLeftIsClicked(evt);
			}
		});
		moveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveUpIsClicked(evt);
			}
		});
		moveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveDownIsClicked(evt);
			}
		});
		moveLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveLeftIsClicked(evt);
			}
		});
		moveRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				moveRightIsClicked(evt);
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
										.addComponent(grabWall).addComponent(rotateWall)
										.addComponent(dropWall).addComponent(cancel))
								.addGroup(layout.createSequentialGroup().addComponent(grabPawn).addComponent(moveUp).addComponent(moveDown).addComponent(moveLeft).addComponent(moveRight))
								.addGroup(layout.createSequentialGroup().addComponent(moveUpLeft).addComponent(moveUpRight).addComponent(moveDownLeft).addComponent(moveDownRight)))
						//.addComponent(wallVisualizer)
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
										.addComponent(grabWall).addComponent(rotateWall)
										.addComponent(dropWall).addComponent(cancel))
								.addGroup(layout.createParallelGroup().addComponent(grabPawn).addComponent(moveUp).addComponent(moveDown).addComponent(moveLeft).addComponent(moveRight))
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
	private static void refreshData() {
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

	private void rotateIsClicked(java.awt.event.ActionEvent evt) {

		try {
			// g2d.translate(wall.x+(wall.width/2), wall.y+(wall.height/2));
			// g2d.rotate(Math.toRadians(90));
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
	
	private void cancelIsClicked(java.awt.event.ActionEvent evt) {
		cancelIsClicked=true;
		repaint();
		
	}
	
	private void grabPawnIsClicked(java.awt.event.ActionEvent evt) {
		grabPawnIsClicked=true;
		repaint();
	}
	
	
	private void moveUpRightIsClicked(java.awt.event.ActionEvent evt) {
		moveUpRightIsClicked=true;
		try {
			QuoridorController.movePawn(MoveDirection.NorthEast);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error="Unable to move the pawn";
		}
		repaint();
		refreshData();
	}
	
	private void moveUpLeftIsClicked(java.awt.event.ActionEvent evt) {
		moveUpLeftIsClicked=true;
		try {
			QuoridorController.movePawn(MoveDirection.NorthWest);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error="Unable to move the pawn";
		}
		repaint();
		refreshData();
	}
	
	private void moveDownLeftIsClicked(java.awt.event.ActionEvent evt) {
		moveDownLeftIsClicked=true;
		try {
			QuoridorController.movePawn(MoveDirection.SouthWest);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error="Unable to move the pawn";
		}
		repaint();
		refreshData();
	}
	
	private void moveDownRightIsClicked(java.awt.event.ActionEvent evt) {
		moveDownRightIsClicked=true;
		try {
			QuoridorController.movePawn(MoveDirection.SouthEast);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error="Unable to move the pawn";
			e.printStackTrace();
		}
		repaint();
		refreshData();
	}
	
	private void moveUpIsClicked(java.awt.event.ActionEvent evt) {
		moveUpIsClicked=true;
		try {
			QuoridorController.movePawn(MoveDirection.North);
			PlayerPosition position=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
			QuoridorController.initiateGameResult(position, QuoridorApplication.getQuoridor().getCurrentGame());
		} catch (InvalidInputException e) {
			error="Unable to move the pawn";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
		refreshData();
	}
	
	private void moveDownIsClicked(java.awt.event.ActionEvent evt) {
		moveDownIsClicked=true;
		try {
			
			QuoridorController.movePawn(MoveDirection.South);
			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error="Unable to move the pawn";
			e.printStackTrace();
		}
		repaint();
		refreshData();
	}
	
	private void moveLeftIsClicked(java.awt.event.ActionEvent evt) {
		moveLeftIsClicked=true;
		try {
			QuoridorController.movePawn(MoveDirection.West);
		} catch (InvalidInputException e) {
			error="Unable to move the pawn";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
		refreshData();
	}
	
	private void moveRightIsClicked(java.awt.event.ActionEvent evt) {
		moveRightIsClicked=true;
		try {
			QuoridorController.movePawn(MoveDirection.East);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error="Unable to move the pawn";
			e.printStackTrace();
		}
		repaint();
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
	
	public static boolean getGrabPawnIsClicked() {
		return grabPawnIsClicked;
	}
	
	public static void setGrabPawnIsClicked(boolean input) {
		grabPawnIsClicked=input;
	}
	
	public static boolean moveIsClicked() {
		return moveIsClicked;
	}
	
	public static boolean getDropPawnIsClicked() {
		return dropPawnIsClicked;
	}
	
	public static void setDropPawnIsClicked(boolean input) {
		dropPawnIsClicked=false;
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}

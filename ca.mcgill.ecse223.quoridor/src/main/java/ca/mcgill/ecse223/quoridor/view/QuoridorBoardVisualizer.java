package ca.mcgill.ecse223.quoridor.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.*;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Wall;

public class QuoridorBoardVisualizer extends JPanel {

	private static final long serialVersionUID = 5765666411683246454L;

	// UI elements
	private List<Rectangle2D> squaresForTiles = new ArrayList<Rectangle2D>();
	private List<Rectangle2D> rectanglesForWalls = new ArrayList<Rectangle2D>();
	private static List<Rectangle2D> rectanglesForBlackWalls = new ArrayList<Rectangle2D>();
	private static List<Rectangle2D> rectanglesForWhiteWalls = new ArrayList<Rectangle2D>();
	private static final int SQUAREWIDTH = 50;
	private static final int WALLWIDTH = 7;
	private static final int WALLHEIGHT = 110;
	private static final int SPACING = 10;
	private static final int MAXROWS = 9;
	private static final int MAXCOLS = 9;
	private static final int WALLSPACING= 130;
	private static int r=6;
	private static int c=5;
	private static int indexCurrentWhiteWall=-1;
	private static int indexCurrentBlackWall=-1;

	private static String dir;
	private static boolean keyOff=true;
	private static boolean grabIsClicked;
	private static boolean dropIsClicked=false;
	private static boolean rotateIsClicked;
	private static boolean moveIsClicked=false;
	private static String error="";
	
	// data elements
	private int selectedTileRow;
	private int selectedTileCol;
	private TOBoard board;
	private HashMap<Rectangle2D, TOTile> tiles;
	private static HashMap<TOWall, Rectangle2D> blackWalls;
	private static HashMap<TOWall, Rectangle2D> whiteWalls;
	private static List<Rectangle2D> droppedWalls;

	private boolean enabled=true;
	private static int dropBlackDone=0;
	private static int dropWhiteDone=0;
	private static List<Rectangle2D> covers=new ArrayList<Rectangle2D>();
	private static int timesWhiteGrabClicked=0;
	private static int timesBlackGrabClicked=0;
	
	//custom DRAW tools
	private static final Color CUSTOM_GREEN = new Color(0, 204, 0);
	private static final Color CUSTOM_LIGHT_YELLOW = new Color(255,255,153);
	private static final Color CUSTOM_BROWN = new Color(181, 101, 29);
	private static final BasicStroke thinStroke = new BasicStroke(1);
	private static final BasicStroke mediumStroke = new BasicStroke(2);
	private static final BasicStroke thickStroke = new BasicStroke(3);

	
	/**
	 * Constructor
	 * @author Helen Lin, 260715521
	 */
	public QuoridorBoardVisualizer() {
		super();
		init();
	}

	/**
	 * This method initializes the TOBoard with 81 TOTiles, the walls, and adds a mouseListener to update the 
	 * currently selected tile row and column vars.
	 * @author Helen Lin, 260715521
	 */
	private void init() {
		// TODO add walls visualization

		initBoardAndTiles();
		initWalls();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				for (Rectangle2D square : squaresForTiles) {
					if (square.contains(x, y)) {
						selectedTileRow = tiles.get(square).getRow();
						selectedTileCol = tiles.get(square).getColumn();
						break;
					}
				}
				if(enabled) 
					repaint();
			}
		});


	}


	/**
	 * This method initializes a new TOBoard with 81 TOTiles and sets them in the class variables. It also paints the starting game positions.
	 * @author Helen Lin, 260715521
	 */
	public void initBoardAndTiles() {
		this.board = new TOBoard();
		this.tiles = new HashMap<Rectangle2D, TOTile>();

		// create tiles UI by rows and cols
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				this.board.addTOTile(i, j);
			}
		}

		repaint(); //repaint with doDrawingForBoardAndTiles
	}


	/**
	 * @author Xinyue Chen
	 */
	public void initWalls() {
		blackWalls=new HashMap<TOWall,Rectangle2D>();
		whiteWalls=new HashMap<TOWall,Rectangle2D>();
		droppedWalls=new ArrayList<Rectangle2D>();
	}



	/**
	 * This method is called whenever the gamePage or this boardVisualizer calls repaint().
	 * It draws all tiles, displays the current black and white pawn positions as colored tiles (the current player's
	 * pawn is outlined in bright teal, and allows currently selected tiles to be highlighted (currently disabled feature).
	 * 
	 * It also draw all walls in stock.
	 * @author Helen Lin, 260715521
	 */
	public void doDrawingForBoardAndTiles(Graphics g) {
		//only create graphics for board and tiles if we have a board
		if (board != null) {
			Graphics2D g2d = (Graphics2D) g.create();

			g2d.setStroke(thinStroke);
			squaresForTiles.clear();
			tiles.clear();

			//draw tiles
			for (TOTile tile : board.getTOTiles()) {
				//add by row and col
				if (tile.getRow() <= MAXROWS && tile.getColumn() <= MAXCOLS) {
					//create new tile as a square and add to list of squares and hashmap of tiles 
					Rectangle2D square = new Rectangle2D.Float(
							-SQUAREWIDTH/2 + tile.getColumn()*(SQUAREWIDTH + SPACING)+WALLSPACING,
							-SQUAREWIDTH/2 + tile.getRow()*(SQUAREWIDTH + SPACING),
							SQUAREWIDTH,
							SQUAREWIDTH);
					squaresForTiles.add(square);
					tiles.put(square, tile);

					//set visuals for square tiles (grey)
					g2d.setColor(CUSTOM_BROWN);
					g2d.fill(square);
					g2d.setColor(Color.BLACK);
					g2d.draw(square);
//					String testLabel = "[" + tile.getRow() + ", " + tile.getColumn() + "]";
//					g2d.drawString(testLabel,
//							-SQUAREWIDTH/4 + tile.getColumn()* (SQUAREWIDTH + SPACING)+WALLSPACING,
//							-SQUAREWIDTH/4 + tile.getRow()* (SQUAREWIDTH + SPACING));

					//if currently selected, highlight it in yellow
//					if (selectedTileRow == tile.getRow() && selectedTileCol == tile.getColumn()) {
//						g2d.setColor(CUSTOM_LIGHT_YELLOW);
//						g2d.fill(square);
//					}

					//draw current pawn positions as black and white tiles, and highlight current player's tile
					int blackRow = QuoridorController.getCurrentRowForPawn(true);
					int blackCol = QuoridorController.getCurrentColForPawn(true);
					int whiteRow = QuoridorController.getCurrentRowForPawn(false);
					int whiteCol = QuoridorController.getCurrentColForPawn(false);

					if (blackRow == tile.getRow() && blackCol == tile.getColumn()) {
						g2d.setStroke(thickStroke);
						g2d.draw(square); //redo a thicker outline
						g2d.setColor(Color.BLACK);
						g2d.fill(square);
						
						//outline it in green if current player
						if (QuoridorController.isBlackTurn()) { 
							g2d.setColor(CUSTOM_GREEN);
							g2d.draw(square);
						}
						g2d.setStroke(thinStroke); //reset stroke
					} else if (whiteRow == tile.getRow() && whiteCol == tile.getColumn()) {
						g2d.setStroke(thickStroke);
						g2d.draw(square); //redo a thicker outline
						g2d.setColor(Color.WHITE);
						g2d.fill(square);

						//outline it if current player
						if (QuoridorController.isWhiteTurn()) { 
							g2d.setColor(CUSTOM_GREEN);
							g2d.draw(square);
						}
						g2d.setStroke(thinStroke); //reset stroke
					}
				}
			}

			//****************DRAW WALLS IN STOCK**************
			rectanglesForWhiteWalls.clear();
			rectanglesForBlackWalls.clear();
			whiteWalls.clear();
			blackWalls.clear();
			List<TOWall> whiteWallsInStock = QuoridorController.getWallsInStock(false);
			List<TOWall> blackWallsInStock = QuoridorController.getWallsInStock(true);
			
			if (whiteWallsInStock != null) {
				int i = 0;
				for (TOWall wall : whiteWallsInStock) {
					// white walls in stock
					int x = 0;
					int y = 0;
					int w = 0;
					int h = 0;
					x = -SQUAREWIDTH / 2 + (SQUAREWIDTH + SPACING);
					y = SQUAREWIDTH + i * (SQUAREWIDTH) + 20;
					w = WALLHEIGHT;
					h = WALLWIDTH;
					Rectangle2D rectangle = new Rectangle2D.Float(x, y, w, h);
					rectanglesForWhiteWalls.add(rectangle);
					whiteWalls.put(wall, rectangle);
					Rectangle2D cover = new Rectangle2D.Float(x, y, w, h);
					covers.add(cover);
					g2d.setColor(Color.WHITE);
					g2d.fill(rectangle);
					g2d.setColor(Color.BLACK);
					g2d.draw(rectangle);
					i++;
				}
			}
			
			if (blackWallsInStock != null) {
				int i = 0;
				for (TOWall wall : blackWallsInStock) {
					// black walls in stock
					int x = 0;
					int y = 0;
					int w = 0;
					int h = 0;
					x = -SQUAREWIDTH / 2 + (SQUAREWIDTH + SPACING) + 680;
					y = SQUAREWIDTH + i * (SQUAREWIDTH) + 20;
					w = WALLHEIGHT;
					h = WALLWIDTH;
					Rectangle2D rectangle = new Rectangle2D.Float(x, y, w, h);

					rectanglesForBlackWalls.add(rectangle);
					blackWalls.put(wall, rectangle);
					Rectangle2D cover = new Rectangle2D.Float(x, y, w, h);
					covers.add(cover);
					g2d.setColor(Color.BLACK);
					g2d.fill(rectangle);
					g2d.setColor(Color.WHITE);
					g2d.draw(rectangle);
					i++;

				}
			}
			

			//****************DRAW WALLS ON BOARD**************
			List<TOWall> whiteWallsOnBoard = QuoridorController.getWallsOnBoard(false);
			List<TOWall> blackWallsOnBoard = QuoridorController.getWallsOnBoard(true);
			
			if (whiteWallsOnBoard != null) {
				for (TOWall wall : whiteWallsOnBoard) {
					// white walls on board
					int x = 0;
					int y = 0;
					int w = 0;
					int h = 0;
					
					if (wall.getIsHorizontal()) {
						x = -SQUAREWIDTH/2 + wall.getCol()*(SQUAREWIDTH + SPACING)+WALLSPACING;
						y = SQUAREWIDTH/2 + wall.getRow()*(SQUAREWIDTH + SPACING);
						w = WALLHEIGHT;
						h = WALLWIDTH;
					} else {
						x = SQUAREWIDTH/2 + wall.getCol()*(SQUAREWIDTH + SPACING)+WALLSPACING;
						y = -SQUAREWIDTH/2 + wall.getRow()*(SQUAREWIDTH + SPACING);
						w = WALLWIDTH;
						h = WALLHEIGHT;
					}
					Rectangle2D rectangle = new Rectangle2D.Float(x,y,w,h);
					rectanglesForWhiteWalls.add(rectangle);
					whiteWalls.put(wall, rectangle);
					g2d.setColor(Color.ORANGE);
					g2d.fill(rectangle);
					g2d.setColor(CUSTOM_GREEN);
					g2d.draw(rectangle);
				}
			}
			
			if (blackWallsOnBoard != null) {
				for (TOWall wall : blackWallsOnBoard) {
					//EACH TO WALL IS SET UP IN CONTROLLER METHOD ALREADY WITH
					//wall.getIsHorizontal --> boolean
					//wall.getRow --> int
					//wall.getColumn --> int 
					
					// black walls on board
					int x = 0;
					int y = 0;
					int w = 0;
					int h = 0;
					
					if (wall.getIsHorizontal()) {
						x = -SQUAREWIDTH/2 + wall.getCol()*(SQUAREWIDTH + SPACING)+WALLSPACING;
						y = SQUAREWIDTH/2 + wall.getRow()*(SQUAREWIDTH + SPACING);
						w = WALLHEIGHT;
						h = WALLWIDTH;
					} else {
						x = SQUAREWIDTH/2 + wall.getCol()*(SQUAREWIDTH + SPACING)+WALLSPACING;
						y = -SQUAREWIDTH/2 + wall.getRow()*(SQUAREWIDTH + SPACING);
						w = WALLWIDTH;
						h = WALLHEIGHT;
					}
					Rectangle2D rectangle = new Rectangle2D.Float(x,y,w,h);
					rectanglesForBlackWalls.add(rectangle);
					blackWalls.put(wall, rectangle);
					g2d.setColor(Color.ORANGE);
					g2d.fill(rectangle);
					g2d.setColor(CUSTOM_GREEN);
					g2d.draw(rectangle);

				}
			}
		}
	}

	public void doDrawingForWallsOnLoad(Graphics g) {
		if (board!=null && QuoridorController.boardWasInitiated()) {
			Graphics2D g2d = (Graphics2D) g.create();

			int whiteStock = (QuoridorController.getStockOfPlayer(false) < 0) ? 10: QuoridorController.getStockOfPlayer(false); //if game not initialised, still display 10 original walls
			int blackStock = (QuoridorController.getStockOfPlayer(true) < 0) ? 10 : QuoridorController.getStockOfPlayer(true);
			List<Wall> tmp = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
			//White walls first

			for (int i = 0 ; i < 10 - whiteStock ; i++) {
				String wallInfo = QuoridorController.infoOfWallAtIndex(i, false);
				String tempDir = wallInfo.substring(2, 3);
				int column = Integer.parseInt(wallInfo.substring(1, 2));
				int row = Integer.parseInt(wallInfo.substring(0, 1));
				int x = 0;
				int y = 0;
				int w = 0;
				int h = 0;
				if (tempDir.equals("h")) {
					x = -SQUAREWIDTH/2 + column*(SQUAREWIDTH + SPACING);
					y = SQUAREWIDTH/2 + row*(SQUAREWIDTH + SPACING);
					w = WALLHEIGHT;
					h = WALLWIDTH;
				} else {
					x = SQUAREWIDTH/2 + column*(SQUAREWIDTH + SPACING);
					y = -SQUAREWIDTH/2 + row*(SQUAREWIDTH + SPACING);
					w = WALLWIDTH;
					h = WALLHEIGHT;
				}
				Rectangle2D rectangle = new Rectangle2D.Float(x,y,w,h);
				rectanglesForWalls.add(rectangle);
				g2d.setColor(Color.ORANGE);
				g2d.fill(rectangle);
				g2d.setColor(CUSTOM_GREEN);
				g2d.draw(rectangle);
			}

			for (int i = 0 ; i < 10 - blackStock ; i++) {
				String wallInfo = QuoridorController.infoOfWallAtIndex(i, true);
				String tempDir = wallInfo.substring(2, 3);
				int column = Integer.parseInt(wallInfo.substring(1, 2));
				int row = Integer.parseInt(wallInfo.substring(0, 1));
				int x = 0;
				int y = 0;
				int w = 0;
				int h = 0;
				if (tempDir.equals("h")) {
					x = -SQUAREWIDTH/2 + column*(SQUAREWIDTH + SPACING);
					y = SQUAREWIDTH/2 + row*(SQUAREWIDTH + SPACING);
					w = WALLHEIGHT;
					h = WALLWIDTH;
				} else {
					x = SQUAREWIDTH/2 + column*(SQUAREWIDTH + SPACING);
					y = -SQUAREWIDTH/2 + row*(SQUAREWIDTH + SPACING);
					w = WALLWIDTH;
					h = WALLHEIGHT;
				}
				Rectangle2D rectangle = new Rectangle2D.Float(x,y,w,h);
				rectanglesForWalls.add(rectangle);
				g2d.setColor(Color.ORANGE);
				g2d.fill(rectangle);
				g2d.setColor(CUSTOM_GREEN);
				g2d.draw(rectangle);
			}



		}


	}

	/**
	 * helper methods
	 * @author Xinyue Chen
	 * @return
	 */
	public static HashMap<TOWall,Rectangle2D> getBlackWalls(){
		return blackWalls;
	}

	public static HashMap<TOWall, Rectangle2D> getWhiteWalls(){
		return whiteWalls;
	}

	public static List<Rectangle2D> getWhiteRectangles(){
		return rectanglesForWhiteWalls;
	}

	public static  List<Rectangle2D> getBlackRectangles(){
		return rectanglesForBlackWalls;
	}


	/**
	 * method for drawing grab wall on board
	 * @author Xinyue Chen
	 * @param g
	 */
	int k=0;
	public void drawGrab(Graphics g) {

		enabled=false;
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(thinStroke);
		Player currentPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		if(currentPlayer==whitePlayer) {
			boolean dropFail=QuoridorGamePage.getDropFailed();
			timesWhiteGrabClicked++;
			if(dropFail==true) {
				indexCurrentWhiteWall=indexCurrentWhiteWall;
				timesWhiteGrabClicked--;
			}
			else {
				indexCurrentWhiteWall++;

			}
			Rectangle2D rec=rectanglesForWhiteWalls.get(indexCurrentWhiteWall);


			int x = 0;
			int y = 0;
			int w = 0;
			int h = 0;
			r=6;
			c=5;
			x = -SQUAREWIDTH/2 + r*(SQUAREWIDTH + SPACING)+10;
			y = SQUAREWIDTH/2 + c*(SQUAREWIDTH + SPACING)+1;
			w = WALLHEIGHT;
			h = WALLWIDTH;
			rec.setRect(x,y,w,h);
			g2d.setColor(Color.GRAY);
			g2d.fill(rec);
			g2d.setColor(Color.MAGENTA);
			g2d.draw(rec);
			grabIsClicked=true;
		}
		if(currentPlayer==blackPlayer) {
			boolean dropFail=QuoridorGamePage.getDropFailed();
			timesBlackGrabClicked++;
			if(dropFail==true) {
				indexCurrentBlackWall=indexCurrentBlackWall;
				timesBlackGrabClicked--;
			}
			else {
				indexCurrentBlackWall++;
			}
			Rectangle2D rec=rectanglesForBlackWalls.get(indexCurrentBlackWall);

			int x = 0;
			int y = 0;
			int w = 0;
			int h = 0;
			r=6;
			c=5;
			x = -SQUAREWIDTH/2 + r*(SQUAREWIDTH + SPACING)+10;
			y = SQUAREWIDTH/2 + c*(SQUAREWIDTH + SPACING)+1;
			w = WALLHEIGHT;
			h = WALLWIDTH;
			rec.setRect(x,y,w,h);
			g2d.setColor(Color.GRAY);
			g2d.fill(rec);
			g2d.setColor(Color.MAGENTA);
			g2d.draw(rec);
			grabIsClicked=true;

		}
//		for(int i=0;i<timesWhiteGrabClicked;i++) {
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.fill(covers.get(i));
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.draw(covers.get(i));
//
//		}	
//		for(int i=0;i<timesBlackGrabClicked;i++) {
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.fill(covers.get(i+10));
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.draw(covers.get(i+10));
//			k++;
//
//		}

	}

	/**
	 * this method draws rotated walls on board
	 * @author Xinyue Chen
	 * @param g
	 */
	public void drawRotate(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(thinStroke);
		boolean rotateIsClicked = QuoridorGamePage.getDirectionIsClicked();
		Player currentPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		if(currentPlayer==whitePlayer) {
			Rectangle2D rec=rectanglesForWhiteWalls.get(getCurrentWhiteWall());
			int x = 0;
			int y = 0;
			int w = 0;
			int h = 0;
			
			x = -SQUAREWIDTH/2 + (r+1)*(SQUAREWIDTH + SPACING)+2;
			y = SQUAREWIDTH/2 + (c-1)*(SQUAREWIDTH + SPACING)+10;
			w = WALLWIDTH;
			h = WALLHEIGHT;
			rec.setRect(x,y,w,h);
			g2d.setColor(Color.GRAY);
			g2d.fill(rec);
			g2d.setColor(Color.MAGENTA);
			g2d.draw(rec);
		}
		else {
			Rectangle2D rec=rectanglesForBlackWalls.get(getCurrentBlackWall());
			int x = 0;
			int y = 0;
			int w = 0;
			int h = 0;
			
			x = -SQUAREWIDTH/2 + (r+1)*(SQUAREWIDTH + SPACING)+2;
			y = SQUAREWIDTH/2 + (c-1)*(SQUAREWIDTH + SPACING)+10;
			w = WALLWIDTH;
			h = WALLHEIGHT;
			rec.setRect(x,y,w,h);
			g2d.setColor(Color.GRAY);
			g2d.fill(rec);
			g2d.setColor(Color.MAGENTA);
			g2d.draw(rec);
		}



	}

	/**
	 * method for drawing wall move on board
	 * @author Xinyue Chen
	 * @param g
	 * @param dir
	 */

	public void drawMove(Graphics g, String dir) {
		enabled=false;
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(thinStroke);
		Player currentPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		if(currentPlayer==whitePlayer) {
			//System.out.println("this means move wall is triggered");
			Rectangle2D rec=rectanglesForWhiteWalls.get(getCurrentWhiteWall());
			int x = 0;
			int y = 0;
			int w = 0;
			int h = 0;
			error="";
			if(dir.equals("up")) {

				if(c!=1) {
					c=c-1;
					error="";
				}
				else {
					c=c;
				}
			}
			if(dir.equals("down")) {
				if(c!=8) {
					c=c+1;
					error="";
				}
				else {
					c=c;
				}
			}
			if(dir.equals("left")) {
				if(r!=3) {r=r-1;
				error="";}

				else {
					r=r;
				}

			}
			if(dir.equals("right")) {
				if(r!=10) { r=r+1;
				error="";}

				else {
					r=r;

				}
			}
			x = -SQUAREWIDTH/2 + r*(SQUAREWIDTH + SPACING)+10;
			y = SQUAREWIDTH/2 + c*(SQUAREWIDTH + SPACING)+1;
			w = WALLHEIGHT;
			h = WALLWIDTH;
			rec.setRect(x,y,w,h);
			g2d.setColor(Color.GRAY);
			g2d.fill(rec);
			g2d.setColor(Color.MAGENTA);
			g2d.draw(rec);
		}
		else {
			Rectangle2D rec=rectanglesForBlackWalls.get(getCurrentBlackWall());
			int x = 0;
			int y = 0;
			int w = 0;
			int h = 0;
			error="";
			if(dir.equals("up")) {

				if(c!=1) {
					c=c-1;
					error="";
				}

				else {
					c=c;
				}
			}
			if(dir.equals("down")) {
				if(c!=8) {
					c=c+1;
					error="";
				}

				else {
					c=c;
				}
			}
			if(dir.equals("left")) {
				if(r!=3) {r=r-1;
				error="";}

				else {
					r=r;
				}

			}
			if(dir.equals("right")) {
				if(r!=10) { r=r+1;
				error="";}

				else {
					r=r;
				}
			}
			x = -SQUAREWIDTH/2 + r*(SQUAREWIDTH + SPACING)+10;
			y = SQUAREWIDTH/2 + c*(SQUAREWIDTH + SPACING)+1;
			w = WALLHEIGHT;
			h = WALLWIDTH;
			rec.setRect(x,y,w,h);
			g2d.setColor(Color.GRAY);
			g2d.fill(rec);
			g2d.setColor(Color.MAGENTA);
			g2d.draw(rec);
		}


	}

	/**
	 * method for drawing dropped walls on board
	 * @author Xinyue Chen
	 * @param g
	 */
	public void drawDrop(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(thinStroke);
		Player currentPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		if(dropIsClicked==true&&indexCurrentWhiteWall!=-1) {
			error="";
			int k=dropWhiteDone;
			int index=0;
			Rectangle2D rec;
			Rectangle2D currentRec=rectanglesForWhiteWalls.get(indexCurrentWhiteWall);

			droppedWalls.add(currentRec);

			for(index=0;index<dropWhiteDone;index++) {
				g2d.setColor(Color.WHITE);
				g2d.fill(rectanglesForWhiteWalls.get(index));
				g2d.setStroke(thinStroke);
				g2d.setColor(Color.MAGENTA);
				g2d.draw(rectanglesForWhiteWalls.get(index));


			}

		}

		if(dropIsClicked==true&&indexCurrentBlackWall!=-1) {
			error="";
			int k=dropBlackDone;
			int index=0;
			Rectangle2D rec;
			Rectangle2D currentBlackRec=rectanglesForBlackWalls.get(indexCurrentBlackWall);
			droppedWalls.add(currentBlackRec);
			for(index=0;index<dropBlackDone;index++) {
				g2d.setColor(Color.BLACK);
				g2d.fill(rectanglesForBlackWalls.get(index));
				g2d.setColor(Color.MAGENTA);
				g2d.draw(rectanglesForBlackWalls.get(index));



			}
			enabled=true;


		}
//		int k=0;
//		for(int i=0;i<timesWhiteGrabClicked;i++) {
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.fill(covers.get(i));
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.draw(covers.get(i));
//
//		}	
//		for(int i=0;i<timesBlackGrabClicked;i++) {
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.fill(covers.get(i+10));
//			g2d.setColor(Color.LIGHT_GRAY);
//			g2d.draw(covers.get(i+10));
//			k++;
//
//		}

	}



	/**
	 * helper methods below to receive/send messages to view
	 * @author Xinyue Chen
	 * @return
	 */
	public static TOWall getCurrentWall() {
		return QuoridorController.getWhiteWalls().get(indexCurrentWhiteWall);
	}

	public static void setDir(String input) {
		dir=input;
	}

	public static void rotateIsClicked(boolean input) {
		rotateIsClicked=input;
	}

	public static void grabIsClicked(boolean input) {
		grabIsClicked=input;
	}

	public static void setDropIsClicked(boolean input) {
		dropIsClicked=input;
	}

	public static void setKeyOff(boolean input) {
		keyOff=input;
	}

	public static void incrementBlackDropDone() {
		dropBlackDone++;
	}
	public static void incrementWhiteDropDone() {
		dropWhiteDone++;
	}

	public static void setMoveClicked(boolean input) {
		moveIsClicked=input;
	}

	public static int getCurrentWhiteWall() {
		return indexCurrentWhiteWall;
	}

	public static int getCurrentBlackWall() {
		return indexCurrentBlackWall;
	}
	public static String getError() {
		return error;
	}




	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawingForBoardAndTiles(g);
		//doDrawingForWallsOnLoad(g);

		 drawDrop(g);
		if (grabIsClicked == true) {
			drawGrab(g);
			grabIsClicked = false;
		}
		if (rotateIsClicked == true) {
			drawRotate(g);
			rotateIsClicked = false;
		}

		if (moveIsClicked == true) {
			drawMove(g, QuoridorGamePage.getDirection());

			moveIsClicked = false;
		}



	}


}
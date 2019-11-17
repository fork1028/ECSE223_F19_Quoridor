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
	private static int droppedR=0;
	private static int droppedC=0;
	private static int indexCurrentWhiteWall=-1;
	private static int indexCurrentBlackWall=-1;
	//private static QuoridorWallMoveVisualizer visualizer=new QuoridorWallMoveVisualizer();
	private static Graphics g;
	private static String dir;
	private static boolean grabIsClicked;
	private static boolean dropIsClicked=false;
	private static boolean rotateIsClicked;
	private static boolean keyOff=true;
	private static int timesMoved=0;
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
	private boolean moveComplete=false;
	private static int dropBlackDone=0;
	private static int dropWhiteDone=0;
	private static List<Rectangle2D> whiteCovers=new ArrayList<Rectangle2D>();
	private static List<Rectangle2D> covers=new ArrayList<Rectangle2D>();
	private static int timesGrabClicked=0;
	private static Rectangle2D blackPawn;
	private static Rectangle2D whitePawn;
	private static int whiteTileCol=1;
	private static int whiteTileRow=5;
	private static int blackTileCol=9;
	private static int blackTileRow=5;
	private static int whiteDroppedRow=5;
	private static int whiteDroppedCol=1;
	private static int calledDrawPos=0;
	private static boolean checkDropClicked=false;
	

	public QuoridorBoardVisualizer() {
		super();
		init();
	}

	/**
	 * @author Helen
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
	 * @author Helen
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
	 * @author Helen
	 */
	public void doDrawingForBoardAndTiles(Graphics g) {
		//only create graphics for board and tiles if we have a board
		if (board != null) {
			Graphics2D g2d = (Graphics2D) g.create();

			BasicStroke thinStroke = new BasicStroke(2);
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

					//set visuals for a square tile
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fill(square);
					g2d.setColor(Color.BLACK);
					g2d.draw(square);
					String testLabel = "["+ "r" + ":" + tile.getRow() + ", c" + ":" + tile.getColumn() + "]";
					g2d.drawString(testLabel,
							-SQUAREWIDTH/4 + tile.getColumn()* (SQUAREWIDTH + SPACING)+WALLSPACING,
							-SQUAREWIDTH/4 + tile.getRow()* (SQUAREWIDTH + SPACING));

					//if currently selected
					if (selectedTileRow == tile.getRow() && selectedTileCol == tile.getColumn()) {
						g2d.setColor(Color.YELLOW);
						g2d.fill(square);
					}


					//draw current pawn position
					//set current pawn positions as a coloured tile
					int blackRow = QuoridorController.getCurrentRowForPawn(true);
					int blackCol = QuoridorController.getCurrentColForPawn(true);
					int whiteRow = QuoridorController.getCurrentRowForPawn(false);
					int whiteCol = QuoridorController.getCurrentColForPawn(false);

					if (blackRow == tile.getRow() && blackCol == tile.getColumn()) {
//						g2d.setColor(Color.BLACK);
//						g2d.fill(square);
						blackPawn=square;
					} else if (whiteRow == tile.getRow() && whiteCol == tile.getColumn()) {
//						g2d.setColor(Color.WHITE);
//						g2d.fill(square);
						whitePawn=square;
					}
				}
			}


			//draw walls left in stock
			for (TOTile tile : board.getTOTiles()) {
				//add by row and col
				if (tile.getRow() <= MAXROWS && tile.getColumn() <= MAXCOLS) {
					//create new tile as a square and add to list of squares and hashmap of tiles 

					//SHAYNE CHANGES: NEED TO ONLY LOAD WALLS THAT AREN'T PLACED!
					int whiteStock = (QuoridorController.getStockOfPlayer(false) < 0) ? 10: QuoridorController.getStockOfPlayer(false); //if game not initialised, still display 10 original walls
					int blackStock = (QuoridorController.getStockOfPlayer(true) < 0) ? 10 : QuoridorController.getStockOfPlayer(true);

					//create white walls
					int i=0;
					for(TOWall wall : QuoridorController.getWhiteWalls()) {
						int x = 0;
						int y = 0;
						int w = 0;
						int h = 0;
						x = -SQUAREWIDTH/2 + (SQUAREWIDTH + SPACING);
						y = SQUAREWIDTH + i*(SQUAREWIDTH)+20;
						w = WALLHEIGHT;
						h = WALLWIDTH;
						Rectangle2D rectangle = new Rectangle2D.Float(x,y,w,h);
						rectanglesForWhiteWalls.add(rectangle);
						whiteWalls.put(wall, rectangle);
						Rectangle2D cover=new Rectangle2D.Float(x,y,w,h);
						covers.add(cover);
						g2d.setColor(Color.WHITE);
						g2d.fill(rectangle);
						g2d.setColor(Color.BLACK);
						g2d.draw(rectangle);
						i++;

					}

					int j=0;
					//create black walls
					for(TOWall wall : QuoridorController.getBlackWalls()) {
						int x = 0;
						int y = 0;
						int w = 0;
						int h = 0;
						x = -SQUAREWIDTH/2 + (SQUAREWIDTH + SPACING)+680;
						y = SQUAREWIDTH + j*(SQUAREWIDTH)+20;
						w = WALLHEIGHT;
						h = WALLWIDTH;
						Rectangle2D rectangle = new Rectangle2D.Float(x,y,w,h);
						
						rectanglesForBlackWalls.add(rectangle);
						blackWalls.put(wall,rectangle);
						Rectangle2D cover=new Rectangle2D.Float(x,y,w,h);
						covers.add(cover);
						g2d.setColor(Color.BLACK);
						g2d.fill(rectangle);
						g2d.setColor(Color.WHITE);
						g2d.draw(rectangle);
						j++;
						
					}

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
				g2d.setColor(Color.GREEN);
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
				g2d.setColor(Color.GREEN);
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
		
		timesGrabClicked++;
		enabled=false;
		Graphics2D g2d = (Graphics2D) g.create();
		BasicStroke thinStroke = new BasicStroke(1);
		g2d.setStroke(thinStroke);
		Player currentPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		if(currentPlayer==whitePlayer) {
			boolean dropFail=QuoridorGamePage.getDropFailed();
			
//			if(QuoridorGamePage.cancelIsClicked()==true) {
//				indexCurrentWhiteWall=indexCurrentWhiteWall;
//				repaint();
//				timesGrabClicked--;
//			}
			if(dropFail==true) {
				indexCurrentWhiteWall=indexCurrentWhiteWall;
				timesGrabClicked--;
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
			g2d.setColor(Color.CYAN);
			g2d.draw(rec);
			grabIsClicked=true;
		}
		else {
			boolean dropFail=QuoridorGamePage.getDropFailed();
			if(dropFail==true) {
				indexCurrentBlackWall=indexCurrentBlackWall;
				timesGrabClicked--;
			}
//			if(QuoridorGamePage.cancelIsClicked()==true) {
//				indexCurrentBlackWall=indexCurrentBlackWall;
//				repaint();
//			}
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
			g2d.setColor(Color.CYAN);
			g2d.draw(rec);
			grabIsClicked=true;

		}
		int k=0;
		for(int i=0;i<timesGrabClicked;i++) {
			if(i%2==0) {
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.fill(covers.get(i/2));
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.draw(covers.get(i/2));
			}
			else {
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.fill(covers.get(i+9-k));
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.draw(covers.get(i+9-k));
				k++;
			}
		}	

		}
		
		public void drawRotate(Graphics g) {
			Graphics2D g2d = (Graphics2D) g.create();
			BasicStroke thinStroke = new BasicStroke(1);
			g2d.setStroke(thinStroke);
			Boolean rotateIsClicked = QuoridorGamePage.getDirectionIsClicked();
			if(rotateIsClicked =true) {
				Rectangle2D rec=rectanglesForWhiteWalls.get(indexCurrentWhiteWall);
	
				int x = -SQUAREWIDTH/2 + r*(SQUAREWIDTH + SPACING)+10;
				int y = SQUAREWIDTH/2 + c*(SQUAREWIDTH + SPACING)+1;
				int w = WALLHEIGHT;
				int h = WALLWIDTH;
				g2d.rotate(Math.toRadians(90));
				rec.setRect(x,y,w,h);
				g2d.setColor(Color.PINK);
				g2d.fill(rec);
				g2d.setColor(Color.CYAN);
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
		BasicStroke thinStroke = new BasicStroke(1);
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
			g2d.setColor(Color.CYAN);
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
			g2d.setColor(Color.CYAN);
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
		BasicStroke thinStroke = new BasicStroke(1);
		g2d.setStroke(thinStroke);
		Player currentPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer=QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
		Player blackPlayer=QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
		System.out.println("dropWhiteDone:"+dropWhiteDone);
		System.out.println("dropBlackDone:"+dropBlackDone);
			if(dropIsClicked==true) {
				error="";
				int k=dropWhiteDone;
				int index=0;
				Rectangle2D rec;
				Rectangle2D currentRec=rectanglesForWhiteWalls.get(indexCurrentWhiteWall);
		
					droppedWalls.add(currentRec);
		
				for(index=0;index<dropWhiteDone;index++) {
						g2d.setColor(Color.WHITE);
						g2d.fill(rectanglesForWhiteWalls.get(index));
						g2d.setColor(Color.CYAN);
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
						g2d.setColor(Color.CYAN);
						g2d.draw(rectanglesForBlackWalls.get(index));
				
				
				
			}
				enabled=true;
				
				
			}
			int k=0;
			for(int i=0;i<timesGrabClicked;i++) {
				if(i%2==0) {
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fill(covers.get(i/2));
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.draw(covers.get(i/2));
				}
				else {
					
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fill(covers.get(i+9-k));
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.draw(covers.get(i+9-k));
					k++;
				}
			}	
	
	}
	
	public void drawPawn(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		BasicStroke thinStroke = new BasicStroke(2);
		g2d.setStroke(thinStroke);
		int x=0;
		int y=0;
		int w=0;
		int h=0;
			if(moveIsClicked==true) {
				String dir=QuoridorGamePage.getDirection();
				if(dir.equals("up")) {
					if(whiteTileRow!=1) {
						whiteTileRow=whiteTileRow-1;
						error="";
					}
					else {
						whiteTileRow=whiteTileRow;
					}
				}
				if(dir.equals("down")) {
					if(whiteTileRow!=9) {
						whiteTileRow=whiteTileRow+1;
						error="";
					}
					else {
						whiteTileRow=whiteTileRow;
					}
				}
				if(dir.equals("left")) {
					if(whiteTileCol!=1) {whiteTileCol=whiteTileCol-1;
					error="";}
					else {
						whiteTileCol=whiteTileCol;
					}
				}
				if(dir.equals("right")) {
					if(whiteTileCol!=9) { whiteTileCol=whiteTileCol+1;
					error="";}

					else {
						whiteTileCol=whiteTileCol;
					}
				}
//				System.out.println("whiteTileRow:"+whiteTileRow);
//				System.out.println("whiteTileCol:"+whiteTileCol);
				
				
				x =whiteTileCol*(SQUAREWIDTH + SPACING)+105;
				y =whiteTileRow*(SQUAREWIDTH + SPACING)-25;
				w = SQUAREWIDTH;
				h = SQUAREWIDTH;
				
				whitePawn.setRect(x,y,w,h);
				g2d.setColor(Color.GRAY);
				g2d.fill(whitePawn);
				g2d.setColor(Color.CYAN);
				g2d.draw(whitePawn);
			}
		
	}
	
	public void drawPawnPos(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		BasicStroke thinStroke = new BasicStroke(2);
		g2d.setStroke(thinStroke);
		int x =whiteDroppedCol*(SQUAREWIDTH + SPACING)+105;
		int y =whiteDroppedRow*(SQUAREWIDTH + SPACING)-25;
		int w = SQUAREWIDTH;
		int h = SQUAREWIDTH;
		whitePawn.setRect(x,y,w,h);
		g2d.setColor(Color.BLACK);
		g2d.fill(blackPawn);
		g2d.setColor(Color.WHITE);
		g2d.fill(whitePawn);
	}
	
	public void drawHighlight(Graphics g) {
		boolean movePawnIsClicked=QuoridorGamePage.getMovePawnIsClicked();
		Graphics2D g2d = (Graphics2D) g.create();
		BasicStroke thinStroke = new BasicStroke(2);
		g2d.setStroke(thinStroke);
		if(movePawnIsClicked==true) {
			int x =whiteDroppedCol*(SQUAREWIDTH + SPACING)+105;
			int y =whiteDroppedRow*(SQUAREWIDTH + SPACING)-25;
			int w = SQUAREWIDTH;
			int h = SQUAREWIDTH;
			whitePawn.setRect(x,y,w,h);
			g2d.setColor(Color.GRAY);
			g2d.fill(whitePawn);
			g2d.setColor(Color.CYAN);
			g2d.fill(whitePawn);
		}
	}
	
	public void drawDroppedPawn(Graphics g) {
		checkDropClicked=true;
		Graphics2D g2d = (Graphics2D) g.create();
		BasicStroke thinStroke = new BasicStroke(2);
		g2d.setStroke(thinStroke);
		boolean dropPawnIsClicked=QuoridorGamePage.getDropPawnIsClicked();
		//System.out.println("dropPawnIsClicked:"+dropPawnIsClicked);
		if(dropPawnIsClicked==true) {
			System.out.println("x:"+whitePawn.getX());
			System.out.println("y:"+whitePawn.getY());
			int x =whiteTileCol*(SQUAREWIDTH + SPACING)+105;
			int y =whiteTileRow*(SQUAREWIDTH + SPACING)-25;
			int w = SQUAREWIDTH;
			int h = SQUAREWIDTH;
			whitePawn.setRect(x,y,w,h);
			g2d.setColor(Color.WHITE);
			g2d.fill(whitePawn);
			whiteDroppedRow=whiteTileRow;
			whiteDroppedCol=whiteTileCol;
		}
		QuoridorGamePage.setDropPawnIsClicked(false);
	}
	

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
		//doDrawingForWallsOnLoad(g);

		if(grabIsClicked==true) {
			drawGrab(g);
			grabIsClicked=false;
		}
		if(moveIsClicked==true) {
			if(QuoridorGamePage.getMovePawnIsClicked()==true) {
				drawPawn(g);
			}
			else {
				drawMove(g, QuoridorGamePage.getDirection());
			}
		
			moveIsClicked=false;
		}

		if(rotateIsClicked==true) {
			drawRotate(g);
		}
		drawDrop(g);
		drawDroppedPawn(g);
		drawPawnPos(g);
		drawHighlight(g);


	}


}
package ca.mcgill.ecse223.quoridor.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.*;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Wall;

public class QuoridorBoardVisualizer extends JPanel {

	private static final long serialVersionUID = 5765666411683246454L;

	// UI elements
	private List<Rectangle2D> squaresForTiles = new ArrayList<Rectangle2D>();
	private List<Rectangle2D> rectanglesForWalls = new ArrayList<Rectangle2D>();
	private static final int SQUAREWIDTH = 50;
	private static final int WALLWIDTH = 8;
	private static final int WALLHEIGHT = 110;
	private static final int SPACING = 10;
	private static final int MAXROWS = 9;
	private static final int MAXCOLS = 9;

	
	// data elements
	private int selectedTileRow;
	private int selectedTileCol;
	private TOBoard board;
	private HashMap<Rectangle2D, TOTile> tiles;

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
							-SQUAREWIDTH/2 + tile.getColumn()*(SQUAREWIDTH + SPACING),
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
							-SQUAREWIDTH/4 + tile.getColumn()* (SQUAREWIDTH + SPACING),
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
 						g2d.setColor(Color.BLACK);
 						g2d.fill(square);
 					} else if (whiteRow == tile.getRow() && whiteCol == tile.getColumn()) {
 						g2d.setColor(Color.WHITE);
 						g2d.fill(square);
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
								
					int space=8;
					for(int i=0;i<whiteStock;i++) {
						Rectangle rec=new Rectangle(0,space*5,10,45);
						g2d.setColor(Color.WHITE);
						g2d.fill(rec);
						space+=10;
						g2d.setColor(Color.BLACK);
						g2d.drawRect(rec.x,rec.y,rec.width,rec.height);
					}
					
					//create black walls
					int spacing=8;
					for(int i=0;i<blackStock;i++) {
						Rectangle rec=new Rectangle(580,spacing*5,10,45);
						g2d.setColor(Color.BLACK);
						g2d.fill(rec);
						spacing+=10;
						g2d.setColor(Color.WHITE);
						g2d.drawRect(rec.x,rec.y,rec.width,rec.height);
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


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawingForBoardAndTiles(g);
		doDrawingForWallsOnLoad(g);
	}

}
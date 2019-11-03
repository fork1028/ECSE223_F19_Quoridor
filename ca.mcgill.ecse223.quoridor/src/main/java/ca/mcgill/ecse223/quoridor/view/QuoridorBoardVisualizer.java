package ca.mcgill.ecse223.quoridor.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ca.mcgill.ecse223.quoridor.controller.*;

public class QuoridorBoardVisualizer extends JPanel {

	private static final long serialVersionUID = 5765666411683246454L;

	// UI elements
	private List<Rectangle2D> squaresForTiles = new ArrayList<Rectangle2D>();
	private List<Rectangle2D> rectanglesForWalls = new ArrayList<Rectangle2D>();
	private static final int SQUAREWIDTH = 50;
	private static final int SPACING = 10;
	private static final int MAXROWS = 9;
	private static final int MAXCOLS = 9;

	//TODO: pawn
	
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
						selectedTileCol = tiles.get(square).getRow();
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
//					BasicStroke thickStroke = new BasicStroke(4);
//					g2d.setStroke(thickStroke);
//					lineHeight = (number - 1) * (RECTHEIGHT + SPACING);
//					g2d.drawLine(LINEX, LINETOPY, LINEX, LINETOPY + lineHeight);
			
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

					//set visuals
					g2d.setColor(Color.WHITE);
					g2d.fill(square);
					g2d.setColor(Color.BLACK);
					g2d.draw(square);
					String testLabel = "["+ "r" + ":" + tile.getRow() + ", c" + ":" + tile.getColumn() + "]";
					g2d.drawString(testLabel,
							-SQUAREWIDTH/4 + tile.getColumn()* (SQUAREWIDTH + SPACING),
							-SQUAREWIDTH/4 + tile.getRow()* (SQUAREWIDTH + SPACING));

					if (selectedTileRow == tile.getRow() && selectedTileCol == tile.getColumn()) {
						g2d.setColor(Color.YELLOW);
						g2d.fill(square);
					}
				}
			}
			
			
			//draw walls in current Position
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

					//set visuals
					g2d.setColor(Color.lightGray);
					g2d.fill(square);
					g2d.setColor(Color.BLACK);
					g2d.draw(square);
					String testLabel = "["+ "r" + ":" + tile.getRow() + ", c" + ":" + tile.getColumn() + "]";
					g2d.drawString(testLabel,
							-SQUAREWIDTH/4 + tile.getColumn()* (SQUAREWIDTH + SPACING),
							-SQUAREWIDTH/4 + tile.getRow()* (SQUAREWIDTH + SPACING));
					
					//get selected tile
					if (selectedTileRow == tile.getRow() && selectedTileCol == tile.getColumn()) {
						g2d.setColor(Color.YELLOW);
						g2d.fill(square);
					}
					
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
		}
			
		
	}
	
	
//	public void setRoute(TORoute route) {
//		this.route = route;
//		busStops = new HashMap<Rectangle2D, TOBusStop>();
//		selectedBusStop = 0;
//		firstVisibleBusStop = 0;
//		repaint();
//	}

//	public void moveUp() {
//		if (firstVisibleBusStop > 0) {
//			firstVisibleBusStop--;
//			repaint();
//		}
//	}
//
//	public void moveDown() {
//		if (route != null && firstVisibleBusStop < route.getNrBusStops() - MAXNUMBEROFBUSSTOPSSHOWN) {
//			firstVisibleBusStop++;
//			repaint();
//		}
//	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawingForBoardAndTiles(g);
	}

}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;
import java.sql.Time;

// line 27 "../../../../../Quoridor.ump"
public class Pawn
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Boolean, Pawn> pawnsByIsBlack = new HashMap<Boolean, Pawn>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Pawn Attributes
  private String currentRow;
  private int currentColumn;
  private boolean isBlack;
  private Time timeElapsed;

  //Pawn Associations
  private List<Wall> walls;
  private Player player;
  private Game game;
  private List<Move> moves;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Pawn(String aCurrentRow, int aCurrentColumn, boolean aIsBlack, Time aTimeElapsed, Player aPlayer, Game aGame)
  {
    currentRow = aCurrentRow;
    currentColumn = aCurrentColumn;
    timeElapsed = aTimeElapsed;
    if (!setIsBlack(aIsBlack))
    {
      throw new RuntimeException("Cannot create due to duplicate isBlack");
    }
    walls = new ArrayList<Wall>();
    if (aPlayer == null || aPlayer.getPawn() != null)
    {
      throw new RuntimeException("Unable to create Pawn due to aPlayer");
    }
    player = aPlayer;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create pawn due to game");
    }
    moves = new ArrayList<Move>();
  }

  public Pawn(String aCurrentRow, int aCurrentColumn, boolean aIsBlack, Time aTimeElapsed, String aUserNameForPlayer, Game aGame)
  {
    currentRow = aCurrentRow;
    currentColumn = aCurrentColumn;
    isBlack = aIsBlack;
    timeElapsed = aTimeElapsed;
    walls = new ArrayList<Wall>();
    player = new Player(aUserNameForPlayer, this);
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create pawn due to game");
    }
    moves = new ArrayList<Move>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCurrentRow(String aCurrentRow)
  {
    boolean wasSet = false;
    currentRow = aCurrentRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentColumn(int aCurrentColumn)
  {
    boolean wasSet = false;
    currentColumn = aCurrentColumn;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsBlack(boolean aIsBlack)
  {
    boolean wasSet = false;
    Boolean anOldIsBlack = getIsBlack();
    if (hasWithIsBlack(aIsBlack)) {
      return wasSet;
    }
    isBlack = aIsBlack;
    wasSet = true;
    if (anOldIsBlack != null) {
      pawnsByIsBlack.remove(anOldIsBlack);
    }
    pawnsByIsBlack.put(aIsBlack, this);
    return wasSet;
  }

  public boolean setTimeElapsed(Time aTimeElapsed)
  {
    boolean wasSet = false;
    timeElapsed = aTimeElapsed;
    wasSet = true;
    return wasSet;
  }

  public String getCurrentRow()
  {
    return currentRow;
  }

  public int getCurrentColumn()
  {
    return currentColumn;
  }

  public boolean getIsBlack()
  {
    return isBlack;
  }
  /* Code from template attribute_GetUnique */
  public static Pawn getWithIsBlack(boolean aIsBlack)
  {
    return pawnsByIsBlack.get(aIsBlack);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithIsBlack(boolean aIsBlack)
  {
    return getWithIsBlack(aIsBlack) != null;
  }

  public Time getTimeElapsed()
  {
    return timeElapsed;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsBlack()
  {
    return isBlack;
  }
  /* Code from template association_GetMany */
  public Wall getWall(int index)
  {
    Wall aWall = walls.get(index);
    return aWall;
  }

  public List<Wall> getWalls()
  {
    List<Wall> newWalls = Collections.unmodifiableList(walls);
    return newWalls;
  }

  public int numberOfWalls()
  {
    int number = walls.size();
    return number;
  }

  public boolean hasWalls()
  {
    boolean has = walls.size() > 0;
    return has;
  }

  public int indexOfWall(Wall aWall)
  {
    int index = walls.indexOf(aWall);
    return index;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public Move getMove(int index)
  {
    Move aMove = moves.get(index);
    return aMove;
  }

  public List<Move> getMoves()
  {
    List<Move> newMoves = Collections.unmodifiableList(moves);
    return newMoves;
  }

  public int numberOfMoves()
  {
    int number = moves.size();
    return number;
  }

  public boolean hasMoves()
  {
    boolean has = moves.size() > 0;
    return has;
  }

  public int indexOfMove(Move aMove)
  {
    int index = moves.indexOf(aMove);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfWallsValid()
  {
    boolean isValid = numberOfWalls() >= minimumNumberOfWalls() && numberOfWalls() <= maximumNumberOfWalls();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfWalls()
  {
    return 10;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWalls()
  {
    return 10;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWalls()
  {
    return 10;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Wall addWall(String aRow, int aColumn, boolean aIsHorizontal, Game aGame)
  {
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return null;
    }
    else
    {
      return new Wall(aRow, aColumn, aIsHorizontal, aGame, this);
    }
  }

  public boolean addWall(Wall aWall)
  {
    boolean wasAdded = false;
    if (walls.contains(aWall)) { return false; }
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return wasAdded;
    }

    Pawn existingPawn = aWall.getPawn();
    boolean isNewPawn = existingPawn != null && !this.equals(existingPawn);

    if (isNewPawn && existingPawn.numberOfWalls() <= minimumNumberOfWalls())
    {
      return wasAdded;
    }

    if (isNewPawn)
    {
      aWall.setPawn(this);
    }
    else
    {
      walls.add(aWall);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWall(Wall aWall)
  {
    boolean wasRemoved = false;
    //Unable to remove aWall, as it must always have a pawn
    if (this.equals(aWall.getPawn()))
    {
      return wasRemoved;
    }

    //pawn already at minimum (10)
    if (numberOfWalls() <= minimumNumberOfWalls())
    {
      return wasRemoved;
    }
    walls.remove(aWall);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to pawn
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (2)
    if (aGame.numberOfPawns() >= Game.maximumNumberOfPawns())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePawn(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPawn(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfMoves()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Move addMove(int aId, String aMovePosition, Tile aTile, Game aGame)
  {
    return new Move(aId, aMovePosition, this, aTile, aGame);
  }

  public boolean addMove(Move aMove)
  {
    boolean wasAdded = false;
    if (moves.contains(aMove)) { return false; }
    Pawn existingPawn = aMove.getPawn();
    boolean isNewPawn = existingPawn != null && !this.equals(existingPawn);
    if (isNewPawn)
    {
      aMove.setPawn(this);
    }
    else
    {
      moves.add(aMove);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMove(Move aMove)
  {
    boolean wasRemoved = false;
    //Unable to remove aMove, as it must always have a pawn
    if (!this.equals(aMove.getPawn()))
    {
      moves.remove(aMove);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addMoveAt(Move aMove, int index)
  {  
    boolean wasAdded = false;
    if(addMove(aMove))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMoves()) { index = numberOfMoves() - 1; }
      moves.remove(aMove);
      moves.add(index, aMove);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMoveAt(Move aMove, int index)
  {
    boolean wasAdded = false;
    if(moves.contains(aMove))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMoves()) { index = numberOfMoves() - 1; }
      moves.remove(aMove);
      moves.add(index, aMove);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMoveAt(aMove, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    pawnsByIsBlack.remove(getIsBlack());
    for(int i=walls.size(); i > 0; i--)
    {
      Wall aWall = walls.get(i - 1);
      aWall.delete();
    }
    Player existingPlayer = player;
    player = null;
    if (existingPlayer != null)
    {
      existingPlayer.delete();
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePawn(this);
    }
    for(int i=moves.size(); i > 0; i--)
    {
      Move aMove = moves.get(i - 1);
      aMove.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "currentRow" + ":" + getCurrentRow()+ "," +
            "currentColumn" + ":" + getCurrentColumn()+ "," +
            "isBlack" + ":" + getIsBlack()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "timeElapsed" + "=" + (getTimeElapsed() != null ? !getTimeElapsed().equals(this)  ? getTimeElapsed().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}
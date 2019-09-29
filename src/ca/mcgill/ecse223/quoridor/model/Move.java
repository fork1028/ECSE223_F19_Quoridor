/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;

// line 37 "../../../../../Quoridor.ump"
public class Move
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Move> movesById = new HashMap<Integer, Move>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Move Attributes
  private int id;
  private String movePosition;

  //Move Associations
  private Pawn pawn;
  private Tile tile;
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Move(int aId, String aMovePosition, Pawn aPawn, Tile aTile, Game aGame)
  {
    movePosition = aMovePosition;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id");
    }
    boolean didAddPawn = setPawn(aPawn);
    if (!didAddPawn)
    {
      throw new RuntimeException("Unable to create move due to pawn");
    }
    boolean didAddTile = setTile(aTile);
    if (!didAddTile)
    {
      throw new RuntimeException("Unable to create move due to tile");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create move due to game");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    Integer anOldId = getId();
    if (hasWithId(aId)) {
      return wasSet;
    }
    id = aId;
    wasSet = true;
    if (anOldId != null) {
      movesById.remove(anOldId);
    }
    movesById.put(aId, this);
    return wasSet;
  }

  public boolean setMovePosition(String aMovePosition)
  {
    boolean wasSet = false;
    movePosition = aMovePosition;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Move getWithId(int aId)
  {
    return movesById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public String getMovePosition()
  {
    return movePosition;
  }
  /* Code from template association_GetOne */
  public Pawn getPawn()
  {
    return pawn;
  }
  /* Code from template association_GetOne */
  public Tile getTile()
  {
    return tile;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_SetOneToMany */
  public boolean setPawn(Pawn aPawn)
  {
    boolean wasSet = false;
    if (aPawn == null)
    {
      return wasSet;
    }

    Pawn existingPawn = pawn;
    pawn = aPawn;
    if (existingPawn != null && !existingPawn.equals(aPawn))
    {
      existingPawn.removeMove(this);
    }
    pawn.addMove(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setTile(Tile aTile)
  {
    boolean wasSet = false;
    if (aTile == null)
    {
      return wasSet;
    }

    Tile existingTile = tile;
    tile = aTile;
    if (existingTile != null && !existingTile.equals(aTile))
    {
      existingTile.removeMove(this);
    }
    tile.addMove(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removeMove(this);
    }
    game.addMove(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    movesById.remove(getId());
    Pawn placeholderPawn = pawn;
    this.pawn = null;
    if(placeholderPawn != null)
    {
      placeholderPawn.removeMove(this);
    }
    Tile placeholderTile = tile;
    this.tile = null;
    if(placeholderTile != null)
    {
      placeholderTile.removeMove(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removeMove(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "movePosition" + ":" + getMovePosition()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "tile = "+(getTile()!=null?Integer.toHexString(System.identityHashCode(getTile())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}
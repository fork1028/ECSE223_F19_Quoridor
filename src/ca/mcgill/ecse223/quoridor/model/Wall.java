/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 19 "../../../../../Quoridor.ump"
public class Wall
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wall Attributes
  private String row;
  private int column;
  private boolean isHorizontal;
  private boolean isPlaced;

  //Wall Associations
  private Game game;
  private Pawn pawn;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(String aRow, int aColumn, boolean aIsHorizontal, Game aGame, Pawn aPawn)
  {
    row = aRow;
    column = aColumn;
    isHorizontal = aIsHorizontal;
    isPlaced = false;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create wall due to game");
    }
    boolean didAddPawn = setPawn(aPawn);
    if (!didAddPawn)
    {
      throw new RuntimeException("Unable to create wall due to pawn");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRow(String aRow)
  {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setColumn(int aColumn)
  {
    boolean wasSet = false;
    column = aColumn;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsHorizontal(boolean aIsHorizontal)
  {
    boolean wasSet = false;
    isHorizontal = aIsHorizontal;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsPlaced(boolean aIsPlaced)
  {
    boolean wasSet = false;
    isPlaced = aIsPlaced;
    wasSet = true;
    return wasSet;
  }

  public String getRow()
  {
    return row;
  }

  public int getColumn()
  {
    return column;
  }

  public boolean getIsHorizontal()
  {
    return isHorizontal;
  }

  public boolean getIsPlaced()
  {
    return isPlaced;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsHorizontal()
  {
    return isHorizontal;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsPlaced()
  {
    return isPlaced;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public Pawn getPawn()
  {
    return pawn;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to wall
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (20)
    if (aGame.numberOfWalls() >= Game.maximumNumberOfWalls())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removeWall(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addWall(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setPawn(Pawn aPawn)
  {
    boolean wasSet = false;
    //Must provide pawn to wall
    if (aPawn == null)
    {
      return wasSet;
    }

    //pawn already at maximum (10)
    if (aPawn.numberOfWalls() >= Pawn.maximumNumberOfWalls())
    {
      return wasSet;
    }
    
    Pawn existingPawn = pawn;
    pawn = aPawn;
    if (existingPawn != null && !existingPawn.equals(aPawn))
    {
      boolean didRemove = existingPawn.removeWall(this);
      if (!didRemove)
      {
        pawn = existingPawn;
        return wasSet;
      }
    }
    pawn.addWall(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removeWall(this);
    }
    Pawn placeholderPawn = pawn;
    this.pawn = null;
    if(placeholderPawn != null)
    {
      placeholderPawn.removeWall(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "row" + ":" + getRow()+ "," +
            "column" + ":" + getColumn()+ "," +
            "isHorizontal" + ":" + getIsHorizontal()+ "," +
            "isPlaced" + ":" + getIsPlaced()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null");
  }
}
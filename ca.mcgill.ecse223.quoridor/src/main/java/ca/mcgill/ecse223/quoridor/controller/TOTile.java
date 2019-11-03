package ca.mcgill.ecse223.quoridor.controller;

/**
 * Data transfer object for Tile
 * @author helen
 *
 */
public class TOTile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tile Attributes
  private int row;
  private int column;

  //Tile Associations
  private TOBoard TOBoard;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOTile(int aRow, int aColumn , TOBoard aTOBoard)
  {
    row = aRow;
    column = aColumn;
    boolean didAddTOBoard = setTOBoard(aTOBoard);
    if (!didAddTOBoard)
    {
      throw new RuntimeException("Unable to create tile due to TOBoard");
    }
    if (aRow<1||aRow>9)
    {
      throw new RuntimeException("Please provide a valid row [row>=1&&row<=9]");
    }
    if (aColumn<1||aColumn>9)
    {
      throw new RuntimeException("Please provide a valid column [column>=1&&column<=9]");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getRow()
  {
    return row;
  }

  public int getColumn()
  {
    return column;
  }
  /* Code from template association_GetOne */
  public TOBoard getTOBoard()
  {
    return TOBoard;
  }
  /* Code from template association_SetOneToMany */
  public boolean setTOBoard(TOBoard aTOBoard)
  {
    boolean wasSet = false;
    if (aTOBoard == null)
    {
      return wasSet;
    }

    TOBoard existingTOBoard = TOBoard;
    TOBoard = aTOBoard;
    if (existingTOBoard != null && !existingTOBoard.equals(aTOBoard))
    {
      existingTOBoard.removeTOTile(this);
    }
    TOBoard.addTOTile(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    TOBoard placeholderTOBoard = TOBoard;
    this.TOBoard = null;
    if(placeholderTOBoard != null)
    {
      placeholderTOBoard.removeTOTile(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "row" + ":" + getRow()+ "," +
            "column" + ":" + getColumn()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "TOBoard = "+(getTOBoard()!=null?Integer.toHexString(System.identityHashCode(getTOBoard())):"null");
  }
}
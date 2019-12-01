/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;

// line 3 "../../../../../QuoridorTransferObject.ump"
public class TOWall
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOWall Attributes
  private int id;
  private boolean isHorizontal;
  private int row;
  private int col;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOWall(int aId)
  {
    id = aId;
  }

  //------------------------
  // INTERFACE
  //------------------------
  public boolean setWallMoveInfo(boolean h, int r, int c)
  {
    boolean wasSet = false;
    isHorizontal = h;
    row = r;
    col = c;
    wasSet = true;
    return wasSet;
  }
  
  public boolean getIsHorizontal()
  {
    return isHorizontal;
  }
  
  public int getRow()
  {
    return row;
  }
  
  public int getCol()
  {
    return col;
  }
  
  

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]";
  }
}
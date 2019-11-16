/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Player;

// line 13 "../../../../../QuoridorTransferObject.ump"
public class TOPlayer
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOPlayer Attributes
  private Player PlayerToMove;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOPlayer(Player aPlayerToMove)
  {
    PlayerToMove = aPlayerToMove;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPlayerToMove(Player aPlayerToMove)
  {
    boolean wasSet = false;
    PlayerToMove = aPlayerToMove;
    wasSet = true;
    return wasSet;
  }

  public Player getPlayerToMove()
  {
    return PlayerToMove;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "PlayerToMove" + "=" + (getPlayerToMove() != null ? !getPlayerToMove().equals(this)  ? getPlayerToMove().toString().replaceAll("  ","    ") : "this" : "null");
  }
}
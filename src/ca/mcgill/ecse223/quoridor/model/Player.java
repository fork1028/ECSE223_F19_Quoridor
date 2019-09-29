/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;
import java.sql.Time;

// line 52 "../../../../../Quoridor.ump"
public class Player
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Player> playersByUserName = new HashMap<String, Player>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private String userName;

  //Player Associations
  private Pawn pawn;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(String aUserName, Pawn aPawn)
  {
    if (!setUserName(aUserName))
    {
      throw new RuntimeException("Cannot create due to duplicate userName");
    }
    if (aPawn == null || aPawn.getPlayer() != null)
    {
      throw new RuntimeException("Unable to create Player due to aPawn");
    }
    pawn = aPawn;
  }

  public Player(String aUserName, String aCurrentRowForPawn, int aCurrentColumnForPawn, boolean aIsBlackForPawn, Time aTimeElapsedForPawn, Game aGameForPawn)
  {
    userName = aUserName;
    pawn = new Pawn(aCurrentRowForPawn, aCurrentColumnForPawn, aIsBlackForPawn, aTimeElapsedForPawn, this, aGameForPawn);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUserName(String aUserName)
  {
    boolean wasSet = false;
    String anOldUserName = getUserName();
    if (hasWithUserName(aUserName)) {
      return wasSet;
    }
    userName = aUserName;
    wasSet = true;
    if (anOldUserName != null) {
      playersByUserName.remove(anOldUserName);
    }
    playersByUserName.put(aUserName, this);
    return wasSet;
  }

  public String getUserName()
  {
    return userName;
  }
  /* Code from template attribute_GetUnique */
  public static Player getWithUserName(String aUserName)
  {
    return playersByUserName.get(aUserName);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithUserName(String aUserName)
  {
    return getWithUserName(aUserName) != null;
  }
  /* Code from template association_GetOne */
  public Pawn getPawn()
  {
    return pawn;
  }

  public void delete()
  {
    playersByUserName.remove(getUserName());
    Pawn existingPawn = pawn;
    pawn = null;
    if (existingPawn != null)
    {
      existingPawn.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "userName" + ":" + getUserName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null");
  }
}
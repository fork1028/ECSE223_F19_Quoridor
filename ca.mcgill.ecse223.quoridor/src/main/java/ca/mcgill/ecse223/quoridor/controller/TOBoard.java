package ca.mcgill.ecse223.quoridor.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Data transfer object for TOBoard
 * @author helen
 *
 */
public class TOBoard
{

	//------------------------
	  // MEMBER VARIABLES
	  //------------------------

	  //TOBoard Associations
	  private List<TOTile> TOTiles;
//	  private Quoridor quoridor;

	  //------------------------
	  // CONSTRUCTOR
	  //------------------------

	  public TOBoard(/*Quoridor aQuoridor*/)
	  {
	    TOTiles = new ArrayList<TOTile>();
//	    boolean didAddQuoridor = setQuoridor(aQuoridor);
//	    if (!didAddQuoridor)
//	    {
//	      throw new RuntimeException("Unable to create TOBoard due to quoridor");
//	    }
	  }

	  //------------------------
	  // INTERFACE
	  //------------------------
	  /* Code from template association_GetMany */
	  public TOTile getTOTile(int index)
	  {
	    TOTile aTOTile = TOTiles.get(index);
	    return aTOTile;
	  }

	  public List<TOTile> getTOTiles()
	  {
	    List<TOTile> newTOTiles = Collections.unmodifiableList(TOTiles);
	    return newTOTiles;
	  }

	  public int numberOfTOTiles()
	  {
	    int number = TOTiles.size();
	    return number;
	  }

	  public boolean hasTOTiles()
	  {
	    boolean has = TOTiles.size() > 0;
	    return has;
	  }

	  public int indexOfTOTile(TOTile aTOTile)
	  {
	    int index = TOTiles.indexOf(aTOTile);
	    return index;
	  }
//	  /* Code from template association_GetOne */
//	  public Quoridor getQuoridor()
//	  {
//	    return quoridor;
//	  }
	  /* Code from template association_MinimumNumberOfMethod */
	  public static int minimumNumberOfTOTiles()
	  {
	    return 0;
	  }
	  /* Code from template association_AddManyToOne */
	  public TOTile addTOTile(int aRow, int aColumn)
	  {
	    return new TOTile(aRow, aColumn, this);
	  }

	  public boolean addTOTile(TOTile aTOTile)
	  {
	    boolean wasAdded = false;
	    if (TOTiles.contains(aTOTile)) { return false; }
	    TOBoard existingTOBoard = aTOTile.getTOBoard();
	    boolean isNewTOBoard = existingTOBoard != null && !this.equals(existingTOBoard);
	    if (isNewTOBoard)
	    {
	      aTOTile.setTOBoard(this);
	    }
	    else
	    {
	      TOTiles.add(aTOTile);
	    }
	    wasAdded = true;
	    return wasAdded;
	  }

	  public boolean removeTOTile(TOTile aTOTile)
	  {
	    boolean wasRemoved = false;
	    //Unable to remove aTOTile, as it must always have a TOBoard
	    if (!this.equals(aTOTile.getTOBoard()))
	    {
	      TOTiles.remove(aTOTile);
	      wasRemoved = true;
	    }
	    return wasRemoved;
	  }
	  /* Code from template association_AddIndexControlFunctions */
	  public boolean addTOTileAt(TOTile aTOTile, int index)
	  {  
	    boolean wasAdded = false;
	    if(addTOTile(aTOTile))
	    {
	      if(index < 0 ) { index = 0; }
	      if(index > numberOfTOTiles()) { index = numberOfTOTiles() - 1; }
	      TOTiles.remove(aTOTile);
	      TOTiles.add(index, aTOTile);
	      wasAdded = true;
	    }
	    return wasAdded;
	  }

	  public boolean addOrMoveTOTileAt(TOTile aTOTile, int index)
	  {
	    boolean wasAdded = false;
	    if(TOTiles.contains(aTOTile))
	    {
	      if(index < 0 ) { index = 0; }
	      if(index > numberOfTOTiles()) { index = numberOfTOTiles() - 1; }
	      TOTiles.remove(aTOTile);
	      TOTiles.add(index, aTOTile);
	      wasAdded = true;
	    } 
	    else 
	    {
	      wasAdded = addTOTileAt(aTOTile, index);
	    }
	    return wasAdded;
	  }
//	  /* Code from template association_SetOneToOptionalOne */
//	  public boolean setQuoridor(Quoridor aNewQuoridor)
//	  {
//	    boolean wasSet = false;
//	    if (aNewQuoridor == null)
//	    {
//	      //Unable to setQuoridor to null, as TOBoard must always be associated to a quoridor
//	      return wasSet;
//	    }
//	    
//	    TOBoard existingTOBoard = aNewQuoridor.getTOBoard();
//	    if (existingTOBoard != null && !equals(existingTOBoard))
//	    {
//	      //Unable to setQuoridor, the current quoridor already has a TOBoard, which would be orphaned if it were re-assigned
//	      return wasSet;
//	    }
//	    
//	    Quoridor anOldQuoridor = quoridor;
//	    quoridor = aNewQuoridor;
//	    quoridor.setTOBoard(this);
//
//	    if (anOldQuoridor != null)
//	    {
//	      anOldQuoridor.setTOBoard(null);
//	    }
//	    wasSet = true;
//	    return wasSet;
//	  }

	  public void delete()
	  {
	    while (TOTiles.size() > 0)
	    {
	      TOTile aTOTile = TOTiles.get(TOTiles.size() - 1);
	      aTOTile.delete();
	      TOTiles.remove(aTOTile);
	    }
	    
//	    Quoridor existingQuoridor = quoridor;
//	    quoridor = null;
//	    if (existingQuoridor != null)
//	    {
//	      existingQuoridor.setTOBoard(null);
//	    }
	  }
}
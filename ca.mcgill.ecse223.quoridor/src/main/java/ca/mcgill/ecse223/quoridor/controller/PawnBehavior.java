/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;
import java.util.ArrayList;

import ca.mcgill.ecse223.quoridor.model.*;

// line 5 "../../../../../PawnStateMachine.ump"
public class PawnBehavior
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PawnBehavior State Machines
  public enum PawnSM { SM }
  public enum PawnSMSMPawnNS { Null, pawnNS }
  public enum PawnSMSMPawnNSPawnNS { Null, Setup, NorthEdge, NorthBorder, NSMiddle, SouthEdge, SouthBorder }
  public enum PawnSMSMPawnEW { Null, pawnEW }
  public enum PawnSMSMPawnEWPawnEW { Null, Setup, EastEdge, EastBorder, EWMiddle, WestEdge, WestBorder }
  private PawnSM pawnSM;
  private PawnSMSMPawnNS pawnSMSMPawnNS;
  private PawnSMSMPawnNSPawnNS pawnSMSMPawnNSPawnNS;
  private PawnSMSMPawnEW pawnSMSMPawnEW;
  private PawnSMSMPawnEWPawnEW pawnSMSMPawnEWPawnEW;

  //PawnBehavior Associations
  private Game currentGame;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PawnBehavior()
  {
    setPawnSMSMPawnNS(PawnSMSMPawnNS.Null);
    setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Null);
    setPawnSMSMPawnEW(PawnSMSMPawnEW.Null);
    setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Null);
    setPawnSM(PawnSM.SM);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getPawnSMFullName()
  {
    String answer = pawnSM.toString();
    if (pawnSMSMPawnNS != PawnSMSMPawnNS.Null) { answer += "." + pawnSMSMPawnNS.toString(); }
    if (pawnSMSMPawnNSPawnNS != PawnSMSMPawnNSPawnNS.Null) { answer += "." + pawnSMSMPawnNSPawnNS.toString(); }
    if (pawnSMSMPawnEW != PawnSMSMPawnEW.Null) { answer += "." + pawnSMSMPawnEW.toString(); }
    if (pawnSMSMPawnEWPawnEW != PawnSMSMPawnEWPawnEW.Null) { answer += "." + pawnSMSMPawnEWPawnEW.toString(); }
    return answer;
  }

  public PawnSM getPawnSM()
  {
    return pawnSM;
  }

  public PawnSMSMPawnNS getPawnSMSMPawnNS()
  {
    return pawnSMSMPawnNS;
  }

  public PawnSMSMPawnNSPawnNS getPawnSMSMPawnNSPawnNS()
  {
    return pawnSMSMPawnNSPawnNS;
  }

  public PawnSMSMPawnEW getPawnSMSMPawnEW()
  {
    return pawnSMSMPawnEW;
  }

  public PawnSMSMPawnEWPawnEW getPawnSMSMPawnEWPawnEW()
  {
    return pawnSMSMPawnEWPawnEW;
  }

  public boolean startGame()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS = pawnSMSMPawnNSPawnNS;
    PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW = pawnSMSMPawnEWPawnEW;
    switch (aPawnSMSMPawnNSPawnNS)
    {
      case Setup:
        if (getPlayer().getGameAsWhite().equals(getCurrentGame()))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
          wasEventProcessed = true;
          break;
        }
        if (getPlayer().getGameAsBlack().equals(getCurrentGame()))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aPawnSMSMPawnEWPawnEW)
    {
      case Setup:
        exitPawnSMSMPawnEWPawnEW();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveUp()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS = pawnSMSMPawnNSPawnNS;
    switch (aPawnSMSMPawnNSPawnNS)
    {
      case NorthEdge:
        exitPawnSMSMPawnNSPawnNS();
        // line 18 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
        wasEventProcessed = true;
        break;
      case NorthBorder:
        if (isLegalStep(MoveDirection.North))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      case NSMiddle:
        if (isLegalStep(MoveDirection.North)&&(getCurrentPawnRow()==3))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.North))&&(getCurrentPawnRow()==4))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.North))&&(getCurrentPawnRow()==3))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
          wasEventProcessed = true;
          break;
        }
        exitPawnSMSMPawnNSPawnNS();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
        wasEventProcessed = true;
        break;
      case SouthEdge:
        if (isLegalStep(MoveDirection.North))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.North)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case SouthBorder:
        if (isLegalStep(MoveDirection.North)||(isLegalJump(MoveDirection.North)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveDown()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS = pawnSMSMPawnNSPawnNS;
    switch (aPawnSMSMPawnNSPawnNS)
    {
      case NorthEdge:
        if (isLegalStep(MoveDirection.South))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.South)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case NorthBorder:
        if (isLegalStep(MoveDirection.South)||(isLegalJump(MoveDirection.South)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case NSMiddle:
        if (isLegalStep(MoveDirection.South)&&(getCurrentPawnRow()==7))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.South))&&(getCurrentPawnRow()==6))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.South))&&(getCurrentPawnRow()==7))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
          wasEventProcessed = true;
          break;
        }
        exitPawnSMSMPawnNSPawnNS();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
        wasEventProcessed = true;
        break;
      case SouthEdge:
        exitPawnSMSMPawnNSPawnNS();
        // line 54 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
        wasEventProcessed = true;
        break;
      case SouthBorder:
        if (isLegalStep(MoveDirection.South))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveUpRight()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS = pawnSMSMPawnNSPawnNS;
    PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW = pawnSMSMPawnEWPawnEW;
    switch (aPawnSMSMPawnNSPawnNS)
    {
      case NorthEdge:
        exitPawnSMSMPawnNSPawnNS();
        // line 21 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
        wasEventProcessed = true;
        break;
      case NorthBorder:
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      case NSMiddle:
        if ((isLegalJump(MoveDirection.NorthEast))&&(getCurrentPawnRow()==3))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case SouthEdge:
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case SouthBorder:
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aPawnSMSMPawnEWPawnEW)
    {
      case EastEdge:
        exitPawnSMSMPawnEWPawnEW();
        // line 80 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastEdge);
        wasEventProcessed = true;
        break;
      case EastBorder:
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      case EWMiddle:
        if ((isLegalJump(MoveDirection.NorthEast))&&(getCurrentPawnColumn()==7))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case WestEdge:
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case WestBorder:
        if ((isLegalJump(MoveDirection.NorthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveUpLeft()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS = pawnSMSMPawnNSPawnNS;
    PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW = pawnSMSMPawnEWPawnEW;
    switch (aPawnSMSMPawnNSPawnNS)
    {
      case NorthEdge:
        exitPawnSMSMPawnNSPawnNS();
        // line 22 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
        wasEventProcessed = true;
        break;
      case NorthBorder:
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      case NSMiddle:
        if ((isLegalJump(MoveDirection.NorthWest))&&(getCurrentPawnRow()==3))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case SouthEdge:
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case SouthBorder:
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aPawnSMSMPawnEWPawnEW)
    {
      case EastEdge:
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case EastBorder:
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case EWMiddle:
        if ((isLegalJump(MoveDirection.NorthWest))&&(getCurrentPawnColumn()==3))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case WestEdge:
        exitPawnSMSMPawnEWPawnEW();
        // line 116 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestEdge);
        wasEventProcessed = true;
        break;
      case WestBorder:
        if ((isLegalJump(MoveDirection.NorthWest)))
        {
          exitPawnSMSMPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
          setPawnSMSMPawnEW(PawnSMSMPawnEW.pawnEW);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveDownRight()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS = pawnSMSMPawnNSPawnNS;
    PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW = pawnSMSMPawnEWPawnEW;
    switch (aPawnSMSMPawnNSPawnNS)
    {
      case NorthEdge:
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case NorthBorder:
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case NSMiddle:
        if ((isLegalJump(MoveDirection.SouthEast))&&(getCurrentPawnRow()==7))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case SouthEdge:
        exitPawnSMSMPawnNSPawnNS();
        // line 57 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
        wasEventProcessed = true;
        break;
      case SouthBorder:
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aPawnSMSMPawnEWPawnEW)
    {
      case EastEdge:
        exitPawnSMSMPawnEWPawnEW();
        // line 82 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastEdge);
        wasEventProcessed = true;
        break;
      case EastBorder:
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      case EWMiddle:
        if ((isLegalJump(MoveDirection.SouthEast))&&(getCurrentPawnColumn()==7))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case WestEdge:
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case WestBorder:
        if ((isLegalJump(MoveDirection.SouthEast)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveDownLeft()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS = pawnSMSMPawnNSPawnNS;
    PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW = pawnSMSMPawnEWPawnEW;
    switch (aPawnSMSMPawnNSPawnNS)
    {
      case NorthEdge:
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NorthBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case NorthBorder:
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case NSMiddle:
        if ((isLegalJump(MoveDirection.SouthWest))&&(getCurrentPawnRow()==7))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.NSMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case SouthEdge:
        exitPawnSMSMPawnNSPawnNS();
        // line 58 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
        wasEventProcessed = true;
        break;
      case SouthBorder:
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnNSPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aPawnSMSMPawnEWPawnEW)
    {
      case EastEdge:
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case EastBorder:
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case EWMiddle:
        if ((isLegalJump(MoveDirection.SouthWest))&&(getCurrentPawnColumn()==3))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case WestEdge:
        exitPawnSMSMPawnEWPawnEW();
        // line 118 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestEdge);
        wasEventProcessed = true;
        break;
      case WestBorder:
        if ((isLegalJump(MoveDirection.SouthWest)))
        {
          exitPawnSMSMPawnNS();
          setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.SouthEdge);
          setPawnSMSMPawnEW(PawnSMSMPawnEW.pawnEW);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveRight()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW = pawnSMSMPawnEWPawnEW;
    switch (aPawnSMSMPawnEWPawnEW)
    {
      case EastEdge:
        exitPawnSMSMPawnEWPawnEW();
        // line 77 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastEdge);
        wasEventProcessed = true;
        break;
      case EastBorder:
        if (isLegalStep(MoveDirection.East))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      case EWMiddle:
        if (isLegalStep(MoveDirection.East)&&(getCurrentPawnColumn()==7))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.East))&&(getCurrentPawnColumn()==6))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.East))&&(getCurrentPawnColumn()==7))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastEdge);
          wasEventProcessed = true;
          break;
        }
        exitPawnSMSMPawnEWPawnEW();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
        wasEventProcessed = true;
        break;
      case WestEdge:
        if (isLegalStep(MoveDirection.East))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.East)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case WestBorder:
        if (isLegalStep(MoveDirection.East)||(isLegalJump(MoveDirection.East)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveLeft()
  {
    boolean wasEventProcessed = false;
    
    PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW = pawnSMSMPawnEWPawnEW;
    switch (aPawnSMSMPawnEWPawnEW)
    {
      case EastEdge:
        if (isLegalStep(MoveDirection.West))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EastBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.West)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case EastBorder:
        if (isLegalStep(MoveDirection.West)||(isLegalJump(MoveDirection.West)))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
          wasEventProcessed = true;
          break;
        }
        break;
      case EWMiddle:
        if (isLegalStep(MoveDirection.West)&&(getCurrentPawnColumn()==3))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.West))&&(getCurrentPawnColumn()==4))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestBorder);
          wasEventProcessed = true;
          break;
        }
        if ((isLegalJump(MoveDirection.West))&&(getCurrentPawnColumn()==3))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestEdge);
          wasEventProcessed = true;
          break;
        }
        exitPawnSMSMPawnEWPawnEW();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.EWMiddle);
        wasEventProcessed = true;
        break;
      case WestEdge:
        exitPawnSMSMPawnEWPawnEW();
        // line 112 "../../../../../PawnStateMachine.ump"
        illegalMove();
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestEdge);
        wasEventProcessed = true;
        break;
      case WestBorder:
        if (isLegalStep(MoveDirection.West))
        {
          exitPawnSMSMPawnEWPawnEW();
          setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.WestEdge);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void exitPawnSM()
  {
    switch(pawnSM)
    {
      case SM:
        exitPawnSMSMPawnNS();
        exitPawnSMSMPawnEW();
        break;
    }
  }

  private void setPawnSM(PawnSM aPawnSM)
  {
    pawnSM = aPawnSM;

    // entry actions and do activities
    switch(pawnSM)
    {
      case SM:
        if (pawnSMSMPawnNS == PawnSMSMPawnNS.Null) { setPawnSMSMPawnNS(PawnSMSMPawnNS.pawnNS); }
        if (pawnSMSMPawnEW == PawnSMSMPawnEW.Null) { setPawnSMSMPawnEW(PawnSMSMPawnEW.pawnEW); }
        break;
    }
  }

  private void exitPawnSMSMPawnNS()
  {
    switch(pawnSMSMPawnNS)
    {
      case pawnNS:
        exitPawnSMSMPawnNSPawnNS();
        setPawnSMSMPawnNS(PawnSMSMPawnNS.Null);
        break;
    }
  }

  private void setPawnSMSMPawnNS(PawnSMSMPawnNS aPawnSMSMPawnNS)
  {
    pawnSMSMPawnNS = aPawnSMSMPawnNS;
    if (pawnSM != PawnSM.SM && aPawnSMSMPawnNS != PawnSMSMPawnNS.Null) { setPawnSM(PawnSM.SM); }

    // entry actions and do activities
    switch(pawnSMSMPawnNS)
    {
      case pawnNS:
        if (pawnSMSMPawnNSPawnNS == PawnSMSMPawnNSPawnNS.Null) { setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Setup); }
        break;
    }
  }

  private void exitPawnSMSMPawnNSPawnNS()
  {
    switch(pawnSMSMPawnNSPawnNS)
    {
      case Setup:
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Null);
        break;
      case NorthEdge:
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Null);
        break;
      case NorthBorder:
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Null);
        break;
      case NSMiddle:
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Null);
        break;
      case SouthEdge:
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Null);
        break;
      case SouthBorder:
        setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS.Null);
        break;
    }
  }

  private void setPawnSMSMPawnNSPawnNS(PawnSMSMPawnNSPawnNS aPawnSMSMPawnNSPawnNS)
  {
    pawnSMSMPawnNSPawnNS = aPawnSMSMPawnNSPawnNS;
    if (pawnSMSMPawnNS != PawnSMSMPawnNS.pawnNS && aPawnSMSMPawnNSPawnNS != PawnSMSMPawnNSPawnNS.Null) { setPawnSMSMPawnNS(PawnSMSMPawnNS.pawnNS); }
  }

  private void exitPawnSMSMPawnEW()
  {
    switch(pawnSMSMPawnEW)
    {
      case pawnEW:
        exitPawnSMSMPawnEWPawnEW();
        setPawnSMSMPawnEW(PawnSMSMPawnEW.Null);
        break;
    }
  }

  private void setPawnSMSMPawnEW(PawnSMSMPawnEW aPawnSMSMPawnEW)
  {
    pawnSMSMPawnEW = aPawnSMSMPawnEW;
    if (pawnSM != PawnSM.SM && aPawnSMSMPawnEW != PawnSMSMPawnEW.Null) { setPawnSM(PawnSM.SM); }

    // entry actions and do activities
    switch(pawnSMSMPawnEW)
    {
      case pawnEW:
        if (pawnSMSMPawnEWPawnEW == PawnSMSMPawnEWPawnEW.Null) { setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Setup); }
        break;
    }
  }

  private void exitPawnSMSMPawnEWPawnEW()
  {
    switch(pawnSMSMPawnEWPawnEW)
    {
      case Setup:
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Null);
        break;
      case EastEdge:
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Null);
        break;
      case EastBorder:
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Null);
        break;
      case EWMiddle:
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Null);
        break;
      case WestEdge:
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Null);
        break;
      case WestBorder:
        setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW.Null);
        break;
    }
  }

  private void setPawnSMSMPawnEWPawnEW(PawnSMSMPawnEWPawnEW aPawnSMSMPawnEWPawnEW)
  {
    pawnSMSMPawnEWPawnEW = aPawnSMSMPawnEWPawnEW;
    if (pawnSMSMPawnEW != PawnSMSMPawnEW.pawnEW && aPawnSMSMPawnEWPawnEW != PawnSMSMPawnEWPawnEW.Null) { setPawnSMSMPawnEW(PawnSMSMPawnEW.pawnEW); }
  }
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
  }

  public boolean hasCurrentGame()
  {
    boolean has = currentGame != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setCurrentGame(Game aNewCurrentGame)
  {
    boolean wasSet = false;
    currentGame = aNewCurrentGame;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPlayer(Player aNewPlayer)
  {
    boolean wasSet = false;
    player = aNewPlayer;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    currentGame = null;
    player = null;
  }


  /**
   * Returns the current row number of the pawn
   */
  // line 85 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnRow(){
    if (getCurrentGame().getBlackPlayer().equals(getPlayer())) {
    	return getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
    } else {
    	return getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
    }
  }


  /**
   * Returns the current column number of the pawn
   */
  // line 87 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnColumn(){
	if (getCurrentGame().getBlackPlayer().equals(getPlayer())) {
		return getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
	} else {
	    return getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
	}
  }


  /**
   * Returns if it is legal to step in the given direction
   */
  // line 89 "../../../../../PawnStateMachine.ump"
  public boolean isLegalStep(MoveDirection dir){
	  
	  //Assume legal until we prove it false
	  Boolean isLegal = true;
	  
	  int curPawnRow = getCurrentPawnRow();
	  int curPawnCol = getCurrentPawnColumn();
	  //Get other pawn info here!
	  int otherPawnRow = 0;
	  int otherPawnCol = 0;
	  int wallRow = 0;
	  int wallCol = 0;
		if (getCurrentGame().getBlackPlayer().equals(getPlayer())) {
			otherPawnCol = getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
			otherPawnRow = getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		} else {
		    otherPawnCol = getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		    otherPawnRow = getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		}
		
		//Now make a list of all the walls on the board!
		ArrayList<Wall> wallList = new ArrayList<Wall>();
		for (Wall wall : getCurrentGame().getCurrentPosition().getBlackWallsOnBoard()) {
			wallList.add(wall);
		}
		for (Wall wall : getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard()) {
			wallList.add(wall);
		}
	 
	//Now, depending on the direction, check first against the pawn being in the way, and then if a wall is in the way!
	 switch (dir) {
	    case North:
	    	if (curPawnCol == otherPawnCol && curPawnRow == otherPawnRow + 1) {
	    		isLegal = false;
	    		return isLegal;
	    	}
	    	
	    	for (Wall wall : wallList) {
	    		if (wall.getMove().getWallDirection().equals(Direction.Horizontal)) {
	    			wallRow = wall.getMove().getTargetTile().getRow();
	    			wallCol = wall.getMove().getTargetTile().getColumn();
	    			if (curPawnRow == wallRow + 1 && curPawnCol == wallCol + 1) {
	    				isLegal = false;
	    				return isLegal;
	    			} else if (curPawnRow == wallRow + 1 && curPawnCol == wallCol) {
	    				isLegal = false;
	    				return isLegal;
	    			}
	    		}
	    	}
	    	break;
	    case South:
	    	if (curPawnCol == otherPawnCol && curPawnRow + 1 == otherPawnRow) {
	    		isLegal = false;
	    		return isLegal;
	    	}
	    	
	    	for (Wall wall : wallList) {
	    		if (wall.getMove().getWallDirection().equals(Direction.Horizontal)) {
	    			wallRow = wall.getMove().getTargetTile().getRow();
	    			wallCol = wall.getMove().getTargetTile().getColumn();
	    			if (curPawnRow == wallRow && curPawnCol == wallCol) {
	    				isLegal = false;
	    				return isLegal;
	    			} else if (curPawnRow == wallRow && curPawnCol == wallCol + 1	) {
	    				isLegal = false;
	    				return isLegal;
	    			}
	    		}
	    	}
	    	break;
	    case East:
	    	if (curPawnCol + 1 == otherPawnCol && curPawnRow == otherPawnRow) {
	    		isLegal = false;
	    		return isLegal;
	    	}
	    	
	    	for (Wall wall : wallList) {
	    		if (wall.getMove().getWallDirection().equals(Direction.Vertical)) {
	    			wallRow = wall.getMove().getTargetTile().getRow();
	    			wallCol = wall.getMove().getTargetTile().getColumn();
	    			if (curPawnRow == wallRow && curPawnCol == wallCol) {
	    				isLegal = false;
	    				return isLegal;
	    			} else if (curPawnRow == wallRow + 1 && curPawnCol == wallCol) {
	    				isLegal = false;
	    				return isLegal;
	    			}
	    		}
	    	}
	    	break;
	    case West:
	    	if (curPawnCol == otherPawnCol + 1 && curPawnRow == otherPawnRow) {
	    		isLegal = false;
	    		return isLegal;
	    	}
	    	
	    	for (Wall wall : wallList) {
	    		if (wall.getMove().getWallDirection().equals(Direction.Vertical)) {
	    			wallRow = wall.getMove().getTargetTile().getRow();
	    			wallCol = wall.getMove().getTargetTile().getColumn();
	    			if (curPawnRow == wallRow && curPawnCol == wallCol + 1) {
	    				isLegal = false;
	    				return isLegal;
	    			} else if (curPawnRow == wallRow + 1 && curPawnCol == wallCol + 1) {
	    				isLegal = false;
	    				return isLegal;
	    			}
	    		}
	    	}
	    	break;
	    }
	return isLegal;
  }


  /**
   * Returns if it is legal to jump in the given direction
   */
  // line 91 "../../../../../PawnStateMachine.ump"
	public boolean isLegalJump(MoveDirection dir) {
		// Assume legal until we prove it false
		Boolean isLegal = true;

		int curPawnRow = getCurrentPawnRow();
		int curPawnCol = getCurrentPawnColumn();
		// Get other pawn info here!
		int otherPawnRow = 0;
		int otherPawnCol = 0;
		int wallRow = 0;
		int wallCol = 0;
		if (getCurrentGame().getBlackPlayer().equals(getPlayer())) {
			otherPawnCol = getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
			otherPawnRow = getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		} else {
			otherPawnCol = getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
			otherPawnRow = getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		}

		// Now make a list of all the walls on the board!
		ArrayList<Wall> wallList = new ArrayList<Wall>();
		for (Wall wall : getCurrentGame().getCurrentPosition().getBlackWallsOnBoard()) {
			wallList.add(wall);
		}
		for (Wall wall : getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard()) {
			wallList.add(wall);
		}

		switch (dir) {
		case North:
			if (curPawnCol == otherPawnCol && curPawnRow == otherPawnRow + 1) {
				for (Wall wall : wallList) {
					if (wall.getMove().getWallDirection().equals(Direction.Horizontal)) {
						wallRow = wall.getMove().getTargetTile().getRow();
						wallCol = wall.getMove().getTargetTile().getColumn();
						if (curPawnRow == wallRow + 2 && curPawnCol == wallCol + 1) {
							isLegal = false;
							return isLegal;
						} else if (curPawnRow == wallRow + 2 && curPawnCol == wallCol) {
							isLegal = false;
							return isLegal;
						} else if (curPawnRow == 2) {
							isLegal = false;
							return isLegal;
						}
					}
				}
				return isLegal;
			} else {
				isLegal = false;
				return isLegal;
			}
		case South:
			if (curPawnCol == otherPawnCol && curPawnRow + 1 == otherPawnRow) {
				for (Wall wall : wallList) {
					if (wall.getMove().getWallDirection().equals(Direction.Horizontal)) {
						wallRow = wall.getMove().getTargetTile().getRow();
						wallCol = wall.getMove().getTargetTile().getColumn();
						if (curPawnRow + 1 == wallRow && curPawnCol == wallCol) {
							isLegal = false;
							return isLegal;
						} else if (curPawnRow + 1 == wallRow && curPawnCol == wallCol + 1) {
							isLegal = false;
							return isLegal;
						} else if (curPawnRow == 8) {
							isLegal = false;
							return isLegal;
						}
					}
				}
				return isLegal;
			} else {
				isLegal = false;
				return isLegal;
			}
		case East:
			if (curPawnCol + 1 == otherPawnCol && curPawnRow == otherPawnRow) {
				for (Wall wall : wallList) {
					if (wall.getMove().getWallDirection().equals(Direction.Vertical)) {
						wallRow = wall.getMove().getTargetTile().getRow();
						wallCol = wall.getMove().getTargetTile().getColumn();
		    			if (curPawnRow == wallRow && curPawnCol + 1 == wallCol) {
		    				isLegal = false;
		    				return isLegal;
		    			} else if (curPawnRow == wallRow + 1 && curPawnCol + 1 == wallCol) {
		    				isLegal = false;
		    				return isLegal;
		    			} else if (curPawnCol == 8) {
							isLegal = false;
							return isLegal;
						}
					}
				}
				return isLegal;
			} else {
				isLegal = false;
				return isLegal;
			}
			
		case West:
			if (curPawnCol == otherPawnCol + 1 && curPawnRow == otherPawnRow) {
				for (Wall wall : wallList) {
					if (wall.getMove().getWallDirection().equals(Direction.Vertical)) {
						wallRow = wall.getMove().getTargetTile().getRow();
						wallCol = wall.getMove().getTargetTile().getColumn();
		    			if (curPawnRow == wallRow && curPawnCol == wallCol + 2) {
		    				isLegal = false;
		    				return isLegal;
		    			} else if (curPawnRow == wallRow + 1 && curPawnCol == wallCol + 2) {
		    				isLegal = false;
		    				return isLegal;
		    			} else if (curPawnCol == 2) {
							isLegal = false;
							return isLegal;
						}
					}
				}
				return isLegal;
			} else {
				isLegal = false;
				return isLegal;
			}
			
		case NorthWest:

			

		case SouthEast:

			
		case SouthWest:

			

		case NorthEast:

			

		}
		return isLegal;
	}


  /**
   * Action to be called when an illegal move is attempted
   */
  // line 142 "../../../../../PawnStateMachine.ump"
  public void illegalMove(){
    
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 146 "../../../../../PawnStateMachine.ump"
  enum MoveDirection 
  {
    East, South, West, North, NorthWest, NorthEast, SouthWest, SouthEast;
  }

  
}
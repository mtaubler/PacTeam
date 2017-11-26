
package dosequis;

import java.awt.Point;
import java.util.List;
import pacsim.*;
/**
 * University of Central Florida
 * CAP4630 - Fall 2017
 * Authors: Brooke Norton and Matthew Taubler
 */
public class Player extends AbstractPlayer {
	 private List<Point> path;
	 
   public Player() {
      System.out.println("Team dosEquis Player constructor...");
   }

   @Override
   public void init() {
      System.out.println( morphTeam + " " + morphID + " dosEquis Player init..." );
   }
   
   @Override
   public PacFace action(Object state) {
      PacCell[][] grid = ( PacCell[][] ) state;      
      PacFace[] faces = PacUtils.randomFaces();
      Point p = morph.getLoc();
      // If convenient, eat a pacman
      if( morph.getMode() != MorphMode.GHOST ) {         
         for( PacFace face : faces ) {
            PacCell pc = PacUtils.neighbor( face, p, grid );            
            if( pc instanceof MorphCell && ((MorphCell) pc).getTeam() != morph.getTeam() && ((MorphCell) pc).getMode() == MorphMode.PACMAN )
               return face;
            }        
      }
      return advance( grid, p, faces );
   }
   
   private PacFace advance( PacCell[][] grid, Point p, PacFace[] faces ) {
      PacTeam opponent = PacUtils.opposingTeam( morphTeam );
      Point target = PacUtils.nearestFood( grid, p, opponent );
   
         List<Point> tmp = BFSPath.getPath(grid, p, target );
         Point next = tmp.remove( 0 );
         PacFace face = PacUtils.direction( p, next );
         return face;
   }
}
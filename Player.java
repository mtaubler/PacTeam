package brooko;

import java.awt.Point;
import java.util.List;
import pacsim.*; 


/**
 * University of Central Florida
 * Cap4630 - Fall 2017
 * Authors: Matthew Taubler and Brooke Norton
 * PacTeam
 */

public class Player extends AbstractPlayer{
	
	private Point target = null
	private BFSPath bfs = null; 
	
	public Player(){
		System.out.println("Team Brooko Player Constructor")
	}
	
	@override
	public void init(){
		target = null;
		bfs = null; 
		System.out.println(;)
	}
	
	@override
	public PacFace action(Object state){
		PacCell[][] grid = (PacCell[][]) state;
		PacFace[] faces = PacUtils.randomFaces();
		Point p = morph.getLoc();
		
		
		if( morph.getMode() == MorphMode.GHOST ) {         
      
      for( PacFace face : faces ) {
         PacCell pc = PacUtils.neighbor( face, p, grid );            
         if( pc instanceof MorphCell &&
             ((MorphCell) pc).getTeam() != morph.getTeam() &&
             ((MorphCell) pc).getMode() == MorphMode.PACMAN )
         {
            target = null;
            bfs = null;
            return face;
         }
      }
      
      if( morph.getPlayer().getID() == 1 ) {
         return advance( grid, p, faces );
      }
      
      else {
         return hunt( grid, p, faces );
      }
		}
		else {
      for( PacFace face : faces ) {
         PacCell pc = PacUtils.neighbor( face, p, grid );
         if( pc instanceof MorphFoodCell &&
             ((MorphFoodCell) pc).getTeam() != morph.getTeam()  )
         {
            target = null;
            bfs = null;
            return face;
         }
      }
      
      return advance( grid, p, faces );
   }
   

	}
	private PacFace hunt( PacCell[][] grid, Point p, PacFace[] faces ) {
    
    Point next = null;
    if( target == null ) {
       PacTeam opponent = PacUtils.opposingTeam( morphTeam );
       target = PacUtils.nearestMorph( grid, p, opponent );
       if( target != null ) {
          List<Point> path = BFSPath.getPath(grid, p, target );
          if( path != null && path.size() > 0 ) {
             next = path.remove( 0 );
          }
       }
    }

    if( next != null && open( grid, next ) ) {
       PacFace face = PacUtils.direction( p, next );
       target = null;
       bfs = null;            
       return face;
    }
       
    target = null;
    bfs = null;
    return randomOpen( faces, grid );
 }
 
 private PacFace advance( PacCell[][] grid, Point p, PacFace[] faces ) {
    
    Point next = null;
    if( target == null ) {
       PacTeam opponent = PacUtils.opposingTeam( morphTeam );
       target = PacUtils.nearestFood( grid, p, opponent );
       if( target != null ) {
          List<Point> path = BFSPath.getPath(grid, p, target );
          if( path != null && path.size() > 0 ) {
             next = path.remove( 0 );
          }
       }
    }

    if( next != null && open( grid, next ) ) {
       PacFace face = PacUtils.direction( p, next );
       target = null;
       bfs = null;            
       return face;
    }
       
    target = null;
    bfs = null;
    return randomOpen( faces, grid );
 }
 
 private boolean open( PacCell[][] grid, Point p ) {
    
    if( grid[ p.x ][ p.y ] instanceof FoodCell ) return true;
    if( grid[ p.x ][ p.y ] instanceof PowerCell ) return true;
    if( grid[ p.x ][ p.y ] instanceof MorphCell ) return false;
    if( grid[ p.x ][ p.y ] instanceof WallCell ) return false;
    return true;      
 }
 
 private PacFace randomOpen( PacFace[] faces, PacCell[][] grid ) {   
    
    for( PacFace face : faces ) {      
       Point p = morph.getLoc();
       PacCell pc = PacUtils.neighbor( face, p, grid );
       
       if( !(pc instanceof WallCell) && 
           !(pc instanceof MorphCell) &&               
           ( morph.getMode() == MorphMode.GHOST || !(pc instanceof HouseCell) ) )  
       {
          return face;
       }      
    }
    return null;
 }
}

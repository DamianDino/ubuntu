        
// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.

import hlt.*;

import java.util.ArrayList;
import java.util.Random;

public class MyBot {
    public static void main(final String[] args) {
        final long rngSeed;
        if (args.length > 1) {
            rngSeed = Integer.parseInt(args[1]);
        } else {
            rngSeed = System.nanoTime();
        }
        final Random rng = new Random(rngSeed);

        Game game = new Game();
        // At this point "game" variable is populated with initial map data.
        // This is a good place to do computationally expensive start-up pre-processing.
        // As soon as you call "ready" function below, the 2 second per turn timer will start.
        game.ready("MyJavaBot");

        Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
        
        int count  = 0;

        for (;;) {
            game.updateFrame();
            final Player me = game.me;
            final GameMap gameMap = game.gameMap;
            Position dropoffPosition = me.shipyard.position;
            
            final ArrayList<Command> commandQueue = new ArrayList<>();
            
            //save the next position for all ships in current round
            final ArrayList<Position> nextPositions = new ArrayList<>();
            
            for (final Ship ship : me.ships.values()) {
            	
            	if(count%2 == 1) {
            		//collect
            		commandQueue.add(ship.stayStill());
            	}else {
            		//move
            		if(ship.halite > 850) {
            			//go back home
            			Position shipPosition = ship.position;
            			Direction direction;
            			Log.log(ship.position.x + " " + ship.position.y  + "-" + dropoffPosition.x +
        						" " + dropoffPosition.y);
            			if(dropoffPosition.x == shipPosition.x) {
            				
            				if(shipPosition.y > dropoffPosition.y) {
            					direction = Direction.NORTH;
            				}else {
            					direction = Direction.SOUTH;
            				}
            					
            			}else {
            				if(dropoffPosition.x > shipPosition.x) {
            					direction = Direction.EAST;
            				}else {
            					direction = Direction.WEST;
            				}
            			}
 
            			//resolve collision
            			Position next = shipPosition.directionalOffset(direction);
            			
            			if(gameMap.at(next).isOccupied()||nextPositions.contains(next)) {
            				commandQueue.add(ship.stayStill());
            				nextPositions.add(ship.position);
            			}else {
            				commandQueue.add(ship.move(direction));
            				nextPositions.add(next);
            			}
            				
            		}else {
            			
            			//collect more halite
            			//still random until he reaches 850
            			final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
            			Position next1 = ship.position.directionalOffset(randomDirection);
            			
            			if(gameMap.at(next1).isOccupied()||(nextPositions.contains(next1))) {
            				commandQueue.add(ship.stayStill());
            				nextPositions.add(ship.position);
            			}else {
            				commandQueue.add(ship.move(randomDirection));
            				nextPositions.add(next1);
            			}   
            		}	
            	}
            }
            count++;

            if (
                game.turnNumber <= 200 &&
                me.halite >= Constants.SHIP_COST &&
                !gameMap.at(me.shipyard).isOccupied())
            {
                commandQueue.add(me.shipyard.spawn());
            }

            game.endTurn(commandQueue);
        }
    }
}

    
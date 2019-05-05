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
		/* At this point "game" variable is populated with initial map data. */
		
		game.ready("MyJavaBot");
		Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		
		int count  = 0;
		
		/* list with all the ships; constant ship ID */
		ArrayList<EntityId> allShips = new ArrayList<EntityId>();
		
		for (;;) {
			game.updateFrame();
			final Player me = game.me;
			final GameMap gameMap = game.gameMap;
			Position dropoffPosition = me.shipyard.position;
			final ArrayList<Command> commandQueue = new ArrayList<>();
			/*save the next position for all ships in current round */
			final ArrayList<Position> nextPositions = new ArrayList<>();
			
			for (final Ship ship : me.ships.values()) {
				/* check if we have already added the ship in the list */
				if (allShips.contains(ship.id)) {
					/* set correct ID */
					ship.number = allShips.indexOf(ship.id);
				} else {
					/* add ship to the list + set its ID */
					allShips.add(ship.id);
					ship.number = allShips.indexOf(ship);
				}
			
				/* compare the number of remaining rounds with the distance to the shipyard.
				 * ships are in different quarters of the map, so we don't check for collisions.
				 */
				if (Constants.MAX_TURNS - game.turnNumber - Constants.ERROR_MARGIN < 2 * gameMap.calculateDistance(ship.position, dropoffPosition)) {
					/* collect every 2 turns */
					if (game.turnNumber % 2 == 1) {
						ship.stayStill();
					} else {
						Direction direction = gameMap.goHome(ship, dropoffPosition);
						Position next = ship.position.directionalOffset(direction);
						if (gameMap.at(next).position.equals(me.shipyard.position)) {
							commandQueue.add(ship.move(direction));
							nextPositions.add(next);
						} else {
							/* check for occupied cells */
							if (gameMap.at(next).isOccupied() || nextPositions.contains(next)) {
								commandQueue.add(ship.stayStill());
								nextPositions.add(ship.position);
							} else {
								commandQueue.add(ship.move(direction));
								nextPositions.add(next);
							}
						}
					}

				} else {
					/* collect every 2 turns */
					if (count % 2 == 1) {
						commandQueue.add(ship.stayStill());
					} else {
						/*
						 * if cargo > 475, go back home 
						 */
						if (ship.halite > 475) {
							Position shipPosition = ship.position;
							Direction direction;
		
							if (dropoffPosition.x == shipPosition.x) {
								if (shipPosition.y > dropoffPosition.y) {
									direction = Direction.NORTH;
								} else {
									direction = Direction.SOUTH;
								}
							} else {
								if (dropoffPosition.x > shipPosition.x) {
									direction = Direction.EAST;
								} else {
									direction = Direction.WEST;
								}
							}
							/* resolve collisions */
							Position next = shipPosition.directionalOffset(direction);
							if (gameMap.at(next).isOccupied() || nextPositions.contains(next)) {
								commandQueue.add(ship.stayStill());
								nextPositions.add(ship.position);
							} else {
								commandQueue.add(ship.move(direction));
								nextPositions.add(next);
							}
						} else {
							Direction direction = gameMap.getNextDirection(ship);
							Position next1 = ship.position.directionalOffset(direction);
							if (gameMap.at(next1).isOccupied() || (nextPositions.contains(next1))) {
								direction = Direction.WEST;
								commandQueue.add(ship.move(direction));
								nextPositions.add(ship.position);
							} else {
								commandQueue.add(ship.move(direction));
								nextPositions.add(next1);
							}
						}
					}
				}
			}
			count++;
			/**
			 * generate ships in the first 200 turns
			 * check if we have the money to generate a new ship
			 * do not exceed 8 ships in total
			 */
			if ( game.turnNumber <= 200 && me.halite >= Constants.SHIP_COST &&
							!gameMap.at(me.shipyard).isOccupied() && me.ships.size() != 8) {
				commandQueue.add(me.shipyard.spawn());
			}
			game.endTurn(commandQueue);
		}
	}
}
package hlt;
import java.util.ArrayList;
public class GameMap {
    
	public final int width;
    public final int height;
    public final MapCell[][] cells;
    
    public GameMap(final int width, final int height) {
        this.width = width;
        this.height = height;
        cells = new MapCell[height][];
        for (int y = 0; y < height; ++y) {
            cells[y] = new MapCell[width];
        }
    }
    
    public MapCell at(final Position position) {
        final Position normalized = normalize(position);
        return cells[normalized.y][normalized.x];
    }
    
    public MapCell at(final Entity entity) {
        return at(entity.position);
    }
    
    public int calculateDistance(final Position source, final Position target) {
        final Position normalizedSource = normalize(source);
        final Position normalizedTarget = normalize(target);
        final int dx = Math.abs(normalizedSource.x - normalizedTarget.x);
        final int dy = Math.abs(normalizedSource.y - normalizedTarget.y);
        final int toroidal_dx = Math.min(dx, width - dx);
        final int toroidal_dy = Math.min(dy, height - dy);
        return toroidal_dx + toroidal_dy;
    }
    
    public Position normalize(final Position position) {
        final int x = ((position.x % width) + width) % width;
        final int y = ((position.y % height) + height) % height;
        return new Position(x, y);
    }
    
    public ArrayList<Direction> getUnsafeMoves(final Position source, final Position destination) {
        final ArrayList<Direction> possibleMoves = new ArrayList<>();
        final Position normalizedSource = normalize(source);
        final Position normalizedDestination = normalize(destination);
        final int dx = Math.abs(normalizedSource.x - normalizedDestination.x);
        final int dy = Math.abs(normalizedSource.y - normalizedDestination.y);
        final int wrapped_dx = width - dx;
        final int wrapped_dy = height - dy;
        if (normalizedSource.x < normalizedDestination.x) {
            possibleMoves.add(dx > wrapped_dx ? Direction.WEST : Direction.EAST);
        } else if (normalizedSource.x > normalizedDestination.x) {
            possibleMoves.add(dx < wrapped_dx ? Direction.WEST : Direction.EAST);
        }
        if (normalizedSource.y < normalizedDestination.y) {
            possibleMoves.add(dy > wrapped_dy ? Direction.NORTH : Direction.SOUTH);
        } else if (normalizedSource.y > normalizedDestination.y) {
            possibleMoves.add(dy < wrapped_dy ? Direction.NORTH : Direction.SOUTH);
        }
        return possibleMoves;
    }
    
    public Direction naiveNavigate(final Ship ship, final Position destination) {
        // getUnsafeMoves normalizes for us
        for (final Direction direction : getUnsafeMoves(ship.position, destination)) {
            final Position targetPos = ship.position.directionalOffset(direction);
            if (!at(targetPos).isOccupied()) {
                at(targetPos).markUnsafe(ship);
                return direction;
            }
        }
        return Direction.STILL;
    }
    
    void _update() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                cells[y][x].ship = null;
            }
        }
        final int updateCount = Input.readInput().getInt();
        for (int i = 0; i < updateCount; ++i) {
            final Input input = Input.readInput();
            final int x = input.getInt();
            final int y = input.getInt();
            cells[y][x].halite = input.getInt();
        }
    }
    
    static GameMap _generate() {
        final Input mapInput = Input.readInput();
        final int width = mapInput.getInt();
        final int height = mapInput.getInt();
        final GameMap map = new GameMap(width, height);
        for (int y = 0; y < height; ++y) {
            final Input rowInput = Input.readInput();
            for (int x = 0; x < width; ++x) {
                final int halite = rowInput.getInt();
                map.cells[y][x] = new MapCell(new Position(x, y), halite);
            }
        }
        return map;
    }

    /**
     * Compute all positions - N, S, E, W
     * Normalize computed positions
     * According to the ID, set a quarter of the map for a ship
     * Greedy until reaches 475 cargo
     * @param ship - the current ship we are moving
     * @return direction - the ship's next direction
     */
    public Direction getNextDirection(Ship ship) {
    	/* current position coord*/
        int pozX = ship.position.x;
        int pozY = ship.position.y;
        
        /* compute all possible new positions*/
        Position positionN = new Position(pozY - 1, pozX);
        Position positionS = new Position(pozY + 1, pozX);
        Position positionE = new Position(pozY, pozX + 1);
        Position positionW = new Position(pozY, pozX - 1);
        
        /* normalize positions*/
        positionN = this.normalize(positionN);
        positionS = this.normalize(positionS);
        positionE = this.normalize(positionE);
        positionW = this.normalize(positionW);
        
        Direction direction;
        
        switch (ship.number % 4) {
            case 0:
                if (this.cells[positionN.x][positionN.y].halite > this.cells[positionE.x][positionE.y].halite) {
                    direction = Direction.NORTH;
                    break;
                } else {
                    direction = Direction.EAST;
                    break;
                }
            case 1:
                if (this.cells[positionS.x][positionS.y].halite > this.cells[positionE.x][positionE.y].halite) {
                    direction = Direction.SOUTH;
                    break;
                } else {
                    direction = Direction.EAST;
                    break;
                }
            case 2:
                if (this.cells[positionS.x][positionS.y].halite > this.cells[positionW.x][positionW.y].halite) {
                    direction = Direction.SOUTH;
                    break;
                } else {
                    direction = Direction.WEST;
                    break;
                }
            default:
                if (this.cells[positionN.x][positionN.y].halite > this.cells[positionW.x][positionW.y].halite) {
                    direction = Direction.NORTH;
                    break;
                } else {
                    direction = Direction.WEST;
                    break;
                }
        }
        
        return direction;
    }
   
    /**
     * 
     * @param ship - the current ship 
     * @param dropoffPosition - the shipyard position (final destination)
     * @return - the ship's next direction
     */
    public Direction goHome(Ship ship, Position dropoffPosition) {
    	/* current position coord*/
    	int pozX = ship.position.x;
        int pozY = ship.position.y;
        
        /* compute all possible new positions*/
        Position positionN = new Position(pozX, pozY - 1);
        Position positionS = new Position(pozX, pozY + 1);
        Position positionE = new Position(pozX + 1, pozY);
        Position positionW = new Position(pozX - 1, pozY);
        
        /* normalize positions*/
        positionN = this.normalize(positionN);
        positionS = this.normalize(positionS);
        positionE = this.normalize(positionE);
        positionW = this.normalize(positionW);
        
        Direction direction;
        
        /* arrived at shipyard */
        if (ship.position.x == dropoffPosition.x && ship.position.y == dropoffPosition.y) {
            return Direction.STILL;
        }
        
        switch (ship.number % 4) {
            case 0:
                if (ship.position.y != dropoffPosition.y) {
                    direction = Direction.SOUTH;
                    break;
                } else {
                    direction = Direction.WEST;
                    break;
                }
            case 1:
                if (ship.position.x != dropoffPosition.x) {
                    direction = Direction.WEST;
                    break;
                } else {
                    direction = Direction.NORTH;
                    break;
                }
            case 2:
                if (ship.position.y != dropoffPosition.y) {
                    direction = Direction.NORTH;
                    break;
                } else {
                    direction = Direction.EAST;
                    break;
                }
            default:
                if (ship.position.x != dropoffPosition.x) {
                    direction = Direction.EAST;
                    break;
                } else {
                    direction = Direction.SOUTH;
                    break;
                }
        }
        return direction;
    }
}

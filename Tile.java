import java.util.LinkedList;

public class Tile {
  String name; // name of the tile
  LinkedList<Player> occupants; // list of players currently on this tile

  public Tile() {
    name = "";
    occupants = new LinkedList<>();
  }

  public Tile(String name) {
    this.name = name;
    occupants = new LinkedList<>();
  }

  public String getName() {
    return name;
  }

  public LinkedList<Player> getOccupants() {
    return occupants;
  }
}

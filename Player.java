import java.util.LinkedList;

public class Player {
  String name; // name of the player
  int money; // amount of money left
  int location; // location is index along the tileset, default is a value 0..40
  LinkedList<Purchasable> ownedTiles;
  int ownedRailRoads;
  int ownedUtilities;

  public Player() {
    name = "";
    money = 0;
    location = 0;
    ownedTiles = new LinkedList<>();
    ownedRailRoads = 0;
    ownedUtilities = 0;
  }

  public Player(String name, int money) {
    this.name = name;
    this.money = money;
    location = 0;
    ownedTiles = new LinkedList<>();
  }

  // should i make unique player ids?
  public boolean equals(Player other) {
    return name == other.name;
  }
}

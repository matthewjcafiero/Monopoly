import java.util.LinkedList;

public class Property extends Purchasable {
  int[] rent; // an array like [a, b, c, d, e, f, g] where a is no building rent, b is 1
              // house,
              // ..., g is hotel rent
  boolean mortgaged; // conditional if mortgaged
  int houseCost;
  int houseLevel; // 0...5 signifying number of houses on this property

  public Property(String name, int[] rent, int cost, int houseCost) {
    this.name = name;
    this.rent = rent;
    this.cost = cost;
    this.houseCost = houseCost;
    owner = null;
    mortgaged = false;
    houseLevel = 0;
    occupants = new LinkedList<>();
  }

  // returns the rent of this tile dependent on number of buildings denoted i
  // (0...6)
  @Override
  public int getRent() {
    return rent[houseLevel];
  }

}

import java.util.LinkedList;

public class Railroad extends Purchasable {

  public Railroad(String name, int cost) {
    this.name = name;
    this.cost = cost;
    occupants = new LinkedList<>();
  }

  @Override
  public int getRent() {
    int temp = owner.ownedRailRoads;
    if (temp <= 2) {
      return temp * 25;
    } else if (temp == 3) {
      return 100;
    } else {
      return 200;
    }
  }
}

import java.util.LinkedList;

public class Utility extends Purchasable {



  public Utility(String name, int cost) {
    this.name = name;
    this.cost = cost;
    occupants = new LinkedList<>();
  }

  public int getRent(int roll) {
    if (owner.ownedUtilities == 1) {
      return roll * 4;
    } else if (owner.ownedUtilities == 2) {
      return roll * 10;
    } else {
      return 0;
    }
  }

  //may need later if abstract starts workings
  @Override
  int getRent() {
    // TODO Auto-generated method stub
    return 0;
  }

 
}

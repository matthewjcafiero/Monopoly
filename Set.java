public class Set {
  String name;
  Purchasable[] members;
  Player[] owners;

  public Set(String name, Purchasable[] members) {
    this.name = name;
    this.members = members;
    owners = new Player[members.length];
  }

  public Player getOwner() {
    Player temp = owners[0];
    for (int i = 0; i < owners.length; i++) {
      if (!temp.equals(owners[i])) {
        return null;
      }
    }
    return temp;
  }

  public int getIndex(Purchasable P) {
    for (int i = 0; i < members.length; i++) {
      if (P.name.equals(members[i].name)) {
        return i;
      }
    }
    return -1;
  }
}

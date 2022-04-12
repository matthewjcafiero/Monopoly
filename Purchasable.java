abstract class Purchasable extends Tile {
  Player owner; // null if unowned, 1 if p1, 2 if p2, ...
  int cost;

  // all subclasses of purchasable need a getRent function; how do I make sure
  // that happens
  abstract int getRent();

  Set set;
}

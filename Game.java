public class Game {
  int players;
  Tile[] tileSet;
  Player[] playerSet;
  Player currentPlayer;
  int currentPlayerIndex;
  int goValue;
  int railroadCost = 200;
  int utilityCost = 150;

  public Game() {
    players = 2;
    createTypicalTileSet();
    playerSet = new Player[players];
    playerSet[0] = new Player("Matt", 1500);
    playerSet[1] = new Player("Bob", 1500);
    currentPlayerIndex = 0;
    currentPlayer = playerSet[currentPlayerIndex];
    goValue = 200;
    railroadCost = 200;
    utilityCost = 150;
  }

  private void createTypicalTileSet() {
    tileSet = new Tile[40];
    tileSet[0] = new Go();
    int[] temp1 = { 2, 10, 30, 90, 160, 250 };
    tileSet[1] = new Property("Mediterranean Ave", temp1, 60, 50);
    tileSet[2] = new Tile("Community Chest");
    int[] temp2 = { 4, 20, 60, 180, 320, 450 };
    tileSet[3] = new Property("Baltic Ave", temp2, 60, 50);
    tileSet[4] = new Tile("Income Tax");
    tileSet[5] = new Railroad("Reading Railroad", railroadCost);
    int[] temp3 = { 6, 30, 90, 270, 400, 550 };
    tileSet[6] = new Property("Oriental Ave", temp3, 100, 50);
    tileSet[7] = new Tile("Chance");
    tileSet[8] = new Property("Vermont Ave", temp3, 100, 50);
    int[] temp4 = { 8, 40, 100, 300, 450, 600 };
    tileSet[9] = new Property("Connecticut Ave", temp4, 120, 50);
    tileSet[10] = new Tile("Jail");
    int[] temp5 = { 10, 50, 150, 450, 625, 750 };
    tileSet[11] = new Property("St. Charles Place", temp5, 140, 100);
    tileSet[12] = new Utility("Electronic Company", utilityCost);
    tileSet[13] = new Property("States Ave", temp5, 140, 100);
    int[] temp6 = { 12, 60, 180, 500, 700, 900 };
    tileSet[14] = new Property("Virgina Ave", temp6, 160, 100);
    tileSet[15] = new Railroad("Pennsylvania Railroad", railroadCost);
    int[] temp7 = { 14, 70, 200, 550, 750, 950 };
    tileSet[16] = new Property("St.James Place", temp7, 180, 100);
    tileSet[17] = new Tile("Community Chest");
    tileSet[18] = new Property("Tennessee Ave", temp7, 180, 100);
    int[] temp8 = { 16, 80, 220, 600, 800, 1000 };
    tileSet[19] = new Property("New York Ave", temp8, 200, 100);
    tileSet[20] = new Tile("Free Parking");
    int[] temp9 = { 18, 90, 250, 700, 875, 1050 };
    tileSet[21] = new Property("Kentucky Ave", temp9, 220, 150);
    tileSet[22] = new Tile("Chance");
    tileSet[23] = new Property("Indiana Ave", temp9, 220, 150);
    int[] temp10 = { 20, 100, 300, 750, 925, 1100 };
    tileSet[24] = new Property("Illinois Ave", temp10, 240, 150);
    tileSet[25] = new Railroad("B&O Railroad", railroadCost);
    int[] temp11 = { 22, 110, 330, 800, 975, 1150 };
    tileSet[26] = new Property("Atlantic Ave", temp11, 260, 150);
    tileSet[27] = new Property("Ventor Ave", temp11, 260, 150);
    tileSet[28] = new Utility("Water Works", utilityCost);
    int[] temp12 = { 24, 120, 350, 850, 1025, 1200 };
    tileSet[29] = new Property("Marvin Gardens", temp12, 280, 150);
    tileSet[30] = new Tile("Go to Jail");
    int[] temp13 = { 26, 130, 390, 900, 1100, 1275 };
    tileSet[31] = new Property("Pacific Ave", temp13, 300, 200);
    tileSet[32] = new Property("North Carolina Ave", temp13, 300, 200);
    tileSet[33] = new Tile("Community Chest");
    int[] temp14 = { 28, 150, 450, 1000, 1200, 1400 };
    tileSet[34] = new Property("Pennsylvania Ave", temp14, 320, 200);
    tileSet[35] = new Railroad("Short Line", railroadCost);
    tileSet[36] = new Tile("Chance");
    int[] temp15 = { 35, 175, 500, 1100, 1300, 1500 };
    tileSet[37] = new Property("Park Place", temp15, 350, 200);
    tileSet[38] = new Tile("Luxury Tax");
    int[] temp16 = { 50, 200, 600, 1400, 1700, 2000 };
    tileSet[39] = new Property("Boardwalk", temp16, 400, 200);
  }

  // have an repl that runs for this
  private void createPlayerSet() {

  }

  public void movePlayer(int spaces) {
    // need to update the current player information, the tile you left information,
    // and the new tile information, and add gocollect if necessary
    // Updating tile you left
    Tile oldTile = tileSet[currentPlayer.location];
    oldTile.occupants.remove(currentPlayer);
    // Updating current player info
    currentPlayer.location = moveLocationCalculator(currentPlayer.location, spaces);
    // Updating the tile you land on
    Tile newTile = tileSet[currentPlayer.location];
    newTile.occupants.add(currentPlayer);
  }

  // Calculates new position based on old position, accounting for overflow of
  // spaces
  public int moveLocationCalculator(int currentSpace, int spaces) {
    int boardSize = tileSet.length;
    currentSpace += spaces;
    if (currentSpace >= boardSize) {
      // adding go value to currentPlayer's money
      currentPlayer.money += goValue;
      System.out.println("You passed Go, you collected $" + goValue + ".  You now have $" + currentPlayer.money + ".");
      return currentSpace - boardSize;
    }
    return currentSpace;
  }

  public void iterateCurrentPlayer() {
    int temp = currentPlayerIndex + 1;
    if (temp >= playerSet.length) {
      currentPlayerIndex = 0;
      currentPlayer = playerSet[currentPlayerIndex];
    } else {
      currentPlayerIndex = temp;
      currentPlayer = playerSet[currentPlayerIndex];
    }
  }

  public boolean purchaseTile(Purchasable tile) {
    if (tile.owner != null) {
      return false;
    } else {
      currentPlayer.ownedTiles.add(tile);
      tile.owner = currentPlayer;
      return true;
    }
  }
}

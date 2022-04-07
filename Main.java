//TODO: double rent thing for unowned, clear terminal line?, chance cards, community chest cards, jail stuff, create characters, more than 2 players, timers for read out, auctioning, morgating, ability to check what you own, build houses (building shortages), doubles get extra rolls and if 3 doubles jail boi, trading

import java.io.*;
import java.util.LinkedList;
import java.util.Random;

public class Main {

  public static void main(String[] args) throws Exception {
    startRepl();
  }

  public static void startRepl() throws Exception {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    boolean end = false;
    System.out.println("*** Welcome to monopoly.  Enter [start] to create a new game.");
    while (!end) {
      System.out.print(" > ");
      String command = input.readLine();
      if (command.equals("quit")) {
        end = true;
      } else if (command.equals("start")) {
        Game game = new Game();
        System.out.println("New game created");
        gameREPL(game);
        end = true;
      } else {
        System.out.println("Invalid command");
      }
    }
  }

  public static void gameREPL(Game game) throws Exception {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Boolean end = false;
    boolean newPlayerTurn = true;

    while (!end) {
      Player currentPlayer = game.currentPlayer;
      if (newPlayerTurn) {
        System.out.println("******************************************************************************");
        System.out.println("*** " + currentPlayer.name + ", it is your turn.  Press [any key] to continue.");
        System.out.print(" > ");
        if (input.readLine().equals("quit")) {
          end = true;
          break;
        }
        System.out.println("You have $" + currentPlayer.money + " in savings currently.  You own "
            + currentPlayer.ownedTiles.size() + " properties.");
        System.out.println("Enter [roll] to roll for movement, [list props] to list owned properties.");
      }
      newPlayerTurn = false;

      System.out.print(" > ");
      String command = input.readLine();
      if (command.equals("quit")) {
        end = true;
      } else if (command.equals("roll")) {
        rollCommand(game, input, end);
        newPlayerTurn = true;
      } else if (command.equals("list props")) {
        listPropCommand(game);
      } else {
        System.out.println("Invalid command");
      }
    }
  }

  public static int rollDice() {
    Random numGen = new Random();
    return 2 + numGen.nextInt(6) + numGen.nextInt(6);
  }

  public static void rollCommand(Game game, BufferedReader input, Boolean end) throws Exception {
    Player currentPlayer = game.currentPlayer;
    int rollValue = rollDice();
    System.out.println("You rolled a " + rollValue + ".");
    game.movePlayer(rollValue);
    Tile tile = game.tileSet[game.currentPlayer.location];
    System.out.println("You landed on " + tile.name + ".");
    // System.out.println("Current tile index: " + game.currentPlayer.location);
    // class checks
    if (tile.getClass().getSuperclass() == Purchasable.class) {
      Purchasable purchasable = (Purchasable) tile;
      if (purchasable.owner == null) {
        purchasableProcedure(currentPlayer, purchasable, input, end);
      } else {
        payRentProcedure(currentPlayer, purchasable, rollValue);
      }
    }
    game.iterateCurrentPlayer();
  }

  // This is a procedure method which provides the current player the oppurtunity
  // to purchase an unowned Purchasable tile, and runs the associated text and
  // commands through the REPL
  public static void purchasableProcedure(Player currentPlayer, Purchasable purchasable, BufferedReader input,
      Boolean end) throws Exception {
    // This if statement differentiates what happens based on if the current player
    // can afford to purchase the given purchasable tile or not
    if (currentPlayer.money >= purchasable.cost) {
      System.out.println(purchasable.name + " is for sale for $" + purchasable.cost
          + ", would you like to buy it?  \nType [yes] to buy, [no] to not buy.");
      boolean validResponse = false;

      // Starts a new repl to answer the above question of whether or not you will buy
      // the tile. Note: quit must be included to allow the player to quit the entire
      // game while in this REPL, and it uses the Boolean end value for the other REPL
      while (!validResponse) {
        System.out.print(" > ");
        String response = input.readLine();

        // This set of if statements manages the possible responses including [yes],
        // [no], [quit], and any other possible input
        if (response.equals("yes")) {
          // TODO: potentially add a buy method to each purchasable subclass

          // If yes, we subtract the cost from current player's money, add the Purchasable
          // tile to current players owned tiles, and sets the purchaseables owner to the
          // current player.
          currentPlayer.money -= purchasable.cost;
          currentPlayer.ownedTiles.add(purchasable);
          purchasable.owner = currentPlayer;

          // This is a set of checks for Railroads and Utilities to iterate the
          // tracker values for the currentPlayer, which is important to calculating the
          // rent correctly for these Purchasables
          if (purchasable.getClass() == Railroad.class) {
            currentPlayer.ownedRailRoads += 1;
          } else if (purchasable.getClass() == Utility.class) {
            currentPlayer.ownedUtilities += 1;
          }

          // This prints out that the Purchasable was purchased, as well as displaying
          // useful info to the player
          System.out.println(purchasable.name + " was purchased for $" + purchasable.cost + ".  You now have $"
              + currentPlayer.money + " in your savings.");

          // Finally we set validResponse to true, which ends this sub-REPL
          validResponse = true;
        } else if (response.equals("no")) {
          // TODO: add auctioning here
          // Simply print a small confirmation message if currentPlayer doesn't purchase
          System.out.println(purchasable.name + " was not purchased.");

          // Finally we set validResponse to true, which ends this sub-REPL
          validResponse = true;
        } else if (response.equals("quit")) {
          // Sets Boolean end to true in order to true in order to end the larger REPL in
          // the future, and then sets validResponse to true to end the sub-REPL
          end = true;
          validResponse = true;
        } else {
          // In the event of any other command being entered, print out this statement
          System.out.println("Invalid command");
        }
      }
    } else {
      // This covers the case of when currentPlayer lands on an unowned tile but can
      // not afford to buy it, and we simply write a message detailing such a case
      System.out.println("This property is unowned, but you can not afford to buy it.");
    }
  }

  // This is a procedure method which makes the currentPlayer pay rent to the
  // owner of the tile they landed on, given that said tile is owned by any player
  // (this procedure also handles landing on self-owned procedure tiles)
  public static void payRentProcedure(Player currentPlayer, Purchasable purchasable, int rollValue) {
    Player tileOwner = purchasable.owner;
    // This if statement differentiates between the case when currentPlayer is
    // tileOwner, and for when this is not true
    if (!currentPlayer.equals(tileOwner)) {
      // TODO: figure out a way to do this without having to cast each one

      // These 3 if statements check the class of each Purchasable tile and handle
      // rent payment for each seperate case; as rent is calculated differently for
      // properties, utilities, and railroads. Each case does pratically the same
      // thing; calculates the rent according to type, subtracts that value from
      // currentPlayer's money, and adds that to tileOwner's money, and then prints
      // out a confirmation statement with extra info
      Class purchasableClass = purchasable.getClass();
      if (purchasableClass == Property.class) {
        int rent = ((Property) purchasable).getRent();
        currentPlayer.money -= rent;
        tileOwner.money += rent;

        System.out.println(purchasable.name + " is owned by " + tileOwner.name + ".  Therefore, you own them $"
            + rent + " in rent.  \n$" + rent + " was taken from your savings.");

      } else if (purchasableClass == Railroad.class) {
        int rent = ((Railroad) purchasable).getRent();
        currentPlayer.money -= rent;
        tileOwner.money += rent;

        System.out.println(purchasable.name + " is owned by " + tileOwner.name + ".  They own "
            + currentPlayer.ownedRailRoads + " railroads, therefore you own them $"
            + rent + " in rent.  \n$" + rent + " was taken from your savings.");

      } else if (purchasableClass == Utility.class) {
        int rent = ((Utility) purchasable).getRent(rollValue);
        currentPlayer.money -= rent;
        tileOwner.money += rent;

        System.out.println(purchasable.name + " is owned by " + tileOwner.name + ".  They own "
            + currentPlayer.ownedUtilities + " utility tiles, therefore you own them $"
            + rent + " in rent.  \n$" + rent + " was taken from your savings.");

      }
    } else {
      // This handles the case of landing on a purchasable tile you already own,
      // where you wouldn't need to pay rent; therefore, just a simple confirmation
      // statement is printed
      System.out.println("You landed on " + purchasable.name + " which you own.");
    }
  }

  public static void listPropCommand(Game game) {
    System.out.println("***********");

    Player currentPlayer = game.currentPlayer;
    LinkedList<Purchasable> ownedTiles = currentPlayer.ownedTiles;
    int numRailroads = currentPlayer.ownedRailRoads;
    int numUtilities = currentPlayer.ownedUtilities;
    Property[] properties = new Property[ownedTiles.size() - numRailroads - numUtilities];
    Railroad[] railroads = new Railroad[numRailroads];
    Utility[] utilities = new Utility[numUtilities];
    int propTracker = 0;
    int railTracker = 0;
    int utilTracker = 0;

    for (Purchasable tile : ownedTiles) {
      Class tileClass = tile.getClass();
      if (tileClass == Property.class) {
        properties[propTracker] = (Property) tile;
        propTracker++;
      } else if (tileClass == Railroad.class) {
        railroads[railTracker] = (Railroad) tile;
        railTracker++;
      } else if (tileClass == Utility.class) {
        utilities[utilTracker] = (Utility) tile;
        utilTracker++;
      }
    }

    if (propTracker + railTracker + utilTracker == 0) {
      System.out.println("You own no properties.");
    } else {
      if (propTracker != 0) {
        System.out.println("Properties:");
        for (Property tile : properties) {
          System.out.print(" - " + tile.name + " - Contains: " +
              houseStringHelper(tile.houseLevel) + " - " + mortgagedStringHelper(tile.mortgaged) + " -\n\t - Rents: ");
          for (int i = 0; i < 6; i++) {
            System.out.print("$" + tile.rent[i]);
            if (i != 5) {
              System.out.print(",");
            }
          }
          System.out.print("\n\t - " + "House Cost: $" + tile.houseCost);
          System.out.print("\n\t - ");
          System.out.println("Mortgage Value: $" + tile.cost / 2);
        }
      }
      if (railTracker != 0) {
        System.out.println("Railroads:");
        for (Railroad tile : railroads) {
          System.out.println(" - " + tile.name);
        }
      }
      if (utilTracker != 0) {
        System.out.println("Utilities:");
        for (Utility tile : utilities) {
          System.out.print(" - " + tile.name);
        }
      }

    }
    System.out.println("***********\nEnter [roll] to roll for movement, [list props] to list owned properties.");

  }

  public static String houseStringHelper(int numHouse) {
    if (numHouse < 5) {
      return (numHouse + " houses");
    } else {
      return "Hotel";
    }
  }

  public static String mortgagedStringHelper(boolean mortgaged) {
    if (mortgaged) {
      return "Mortgaged";
    } else {
      return "Unmortgaged";
    }
  }

}

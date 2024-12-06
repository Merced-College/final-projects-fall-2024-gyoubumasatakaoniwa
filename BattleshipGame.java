//Henry Lam
//12/1/24
//CPSC-39-12111

import java.util.ArrayList; // Data Structure: ArrayList
import java.util.HashMap; // Data Structure: HashMap
import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {
    private char[][] playerBoard;   // Data Structure: 2D Array for player's board
    private char[][] enemyBoard;    // Data Structure: 2D Array for enemy's board
    private ArrayList<Ship> ships;  // Data Structure: ArrayList for ships
    private HashMap<String, Boolean> shots; // Data Structure: HashMap to track hits and misses
    private final int BOARD_SIZE = 10; // Constant: Board size

    // Constructor to initialize the game
    public BattleshipGame() { // Not an algorithm
        playerBoard = new char[BOARD_SIZE][BOARD_SIZE]; // Initialize player's board
        enemyBoard = new char[BOARD_SIZE][BOARD_SIZE];  // Initialize enemy's board
        ships = new ArrayList<>(); // Initialize ship list
        shots = new HashMap<>(); // Initialize shots map
        initializeBoard(); // Call to algorithm: Initialize Board
        distributeShips(); // Call to algorithm: Ship Placement Manager
    } // End of Constructor: Initialize the game

    // Method to initialize the game board
    private void initializeBoard() { // Algorithm: Initialize Board
        for (int i = 0; i < BOARD_SIZE; i++) { // Loop: Iterate over board rows
            for (int j = 0; j < BOARD_SIZE; j++) { // Loop: Iterate over board columns
                playerBoard[i][j] = '~'; // Water
                enemyBoard[i][j] = '~'; // Water
            } // End of Loop: Iterate over board columns
        } // End of Loop: Iterate over board rows
    } // End of Algorithm: Initialize Board

    // Method to distribute ships randomly
    private void distributeShips() { // Algorithm: Ship Placement Manager
        // Define ship sizes and place them randomly
        ships.add(new Ship("Battleship", 4)); // Data Structure: ArrayList (ships)
        ships.add(new Ship("Cruiser", 3));
        ships.add(new Ship("Destroyer", 2));

        for (Ship ship : ships) { // Loop: Iterate over all ships
            boolean placed = false; // Track if the ship has been placed
            while (!placed) { // Loop: Try placing ship until successful
                int row = new Random().nextInt(BOARD_SIZE); // Generate random row
                int col = new Random().nextInt(BOARD_SIZE); // Generate random column
                boolean horizontal = new Random().nextBoolean(); // Random orientation

                // Attempt to place the ship
                if (canPlaceShip(row, col, ship.size, horizontal)) { // Conditional: Check if placement is valid
                    markShipOnBoard(row, col, ship.size, horizontal); // Place the ship
                    placed = true; // Mark ship as placed
                } // End of Conditional: Check if placement is valid
            } // End of Loop: Try placing ship until successful
        } // End of Loop: Iterate over all ships
    } // End of Algorithm: Ship Placement Manager

    // Check if a ship can be placed
    private boolean canPlaceShip(int row, int col, int size, boolean horizontal) { // Algorithm: Validate Ship Placement
        if (horizontal) { // Conditional: Check horizontal bounds
            if (col + size > BOARD_SIZE) return false; // Out of bounds
            for (int i = 0; i < size; i++) { // Loop: Check each horizontal position
                if (playerBoard[row][col + i] != '~') return false; // Already occupied
            } // End of Loop: Check each horizontal position
        } else { // Conditional: Check vertical bounds
            if (row + size > BOARD_SIZE) return false; // Out of bounds
            for (int i = 0; i < size; i++) { // Loop: Check each vertical position
                if (playerBoard[row + i][col] != '~') return false; // Already occupied
            } // End of Loop: Check each vertical position
        } // End of Conditional: Check horizontal bounds
        return true; // Ship can be placed
    } // End of Algorithm: Validate Ship Placement

    // Place the ship on the board
    private void markShipOnBoard(int row, int col, int size, boolean horizontal) { // Algorithm: Mark Ship
        if (horizontal) { // Conditional: Check if horizontal placement
            for (int i = 0; i < size; i++) { // Loop: Place ship horizontally
                playerBoard[row][col + i] = 'S'; // S for Ship
            } // End of Loop: Place ship horizontally
        } else { // Conditional: Check if vertical placement
            for (int i = 0; i < size; i++) { // Loop: Place ship vertically
                playerBoard[row + i][col] = 'S'; // S for Ship
            } // End of Loop: Place ship vertically
        } // End of Conditional: Check if horizontal placement
    } // End of Algorithm: Mark Ship

    // Method for a player to take a shot
    public boolean takeShot(int row, int col, boolean isPlayer1) { // Algorithm: Take Shot
        char[][] board = isPlayer1 ? enemyBoard : playerBoard; // Select appropriate board

        if (board[row][col] == 'S') { // Conditional: Check if hit
            board[row][col] = 'H'; // Hit
            shots.put(row + "," + col, true); // Track hit
            return true;
        } else { // Conditional: Check if miss
            board[row][col] = 'M'; // Miss
            shots.put(row + "," + col, false); // Track miss
            return false;
        } // End of Conditional: Check if miss
    } // End of Algorithm: Take Shot

    // Check if all ships are destroyed
    public boolean allShipsDestroyed(char[][] board) { // Algorithm: Check Ships Destroyed
        for (int i = 0; i < BOARD_SIZE; i++) { // Loop: Check board rows
            for (int j = 0; j < BOARD_SIZE; j++) { // Loop: Check board columns
                if (board[i][j] == 'S') return false; // Ship still exists
            } // End of Loop: Check board columns
        } // End of Loop: Check board rows
        return true; // All ships destroyed
    } // End of Algorithm: Check Ships Destroyed

    // Main Method: Runs the game
    public static void main(String[] args) {
        BattleshipGame game = new BattleshipGame(); // Initialize the game
        Scanner scanner = new Scanner(System.in);  // Input for players
        boolean playerOneTurn = true; // Player 1 starts the game
        int player1ShipsRemaining = game.ships.size(); // Count of Player 1's ships
        int player2ShipsRemaining = game.ships.size(); // Count of Player 2's ships

        // Game loop: Continues until a player wins
        while (player1ShipsRemaining > 0 && player2ShipsRemaining > 0) {
            if (playerOneTurn) { // Conditional: Player 1's turn
                System.out.println("Player 1, enter your shot coordinates (row, col):");
                String input = scanner.nextLine();
                String[] parts = input.split(",");
                int row = Integer.parseInt(parts[0].trim());
                int col = Integer.parseInt(parts[1].trim());
                
                // Check if the shot hits a ship
                if (game.enemyBoard[row][col] == 'S') {
                    game.enemyBoard[row][col] = 'X'; // Mark hit
                    System.out.println("Hit!");
                    player2ShipsRemaining--; // Decrease Player 2's ship count
                } else {
                    game.enemyBoard[row][col] = 'O'; // Mark miss
                    System.out.println("Miss!");
                }
            } else { // Conditional: Player 2's turn
                System.out.println("Player 2, enter your shot coordinates (row, col):");
                String input = scanner.nextLine();
                String[] parts = input.split(",");
                int row = Integer.parseInt(parts[0].trim());
                int col = Integer.parseInt(parts[1].trim());
                
                // Check if the shot hits a ship
                if (game.playerBoard[row][col] == 'S') {
                    game.playerBoard[row][col] = 'X'; // Mark hit
                    System.out.println("Hit!");
                    player1ShipsRemaining--; // Decrease Player 1's ship count
                } else {
                    game.playerBoard[row][col] = 'O'; // Mark miss
                    System.out.println("Miss!");
                }
            }

            // Switch turns
            playerOneTurn = !playerOneTurn;
        }

        // Display the winner
        if (player1ShipsRemaining == 0) {
            System.out.println("Player 2 wins! All Player 1's ships are destroyed.");
        } else {
            System.out.println("Player 1 wins! All Player 2's ships are destroyed.");
        }

        scanner.close(); // Close the scanner
    }
} // End of Class: BattleshipGame

class Ship { // Data Structure: Ship
    String name; // Ship name
    int size; // Ship size

    Ship(String name, int size) { // Constructor
        this.name = name; // Initialize name
        this.size = size; // Initialize size
    } // End of Constructor
} // End of Class: Ship

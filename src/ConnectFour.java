import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.Scanner;


public class ConnectFour {
	
    // we define characters for players (R for Red, Y for Yellow)
    private static final char[] PLAYERS = {'R', 'Y'};
    // dimensions for our board
    private final int width, height;
    // grid for the board
    private final char[][] grid;
    // we store last move made by a player
    private int lastCol = -1, lastTop = -1;
  
    public ConnectFour(int w, int h) {
      width = w;
      height = h;
      grid = new char[h][];
  
      // init the grid will blank cell
      for (int i = 0; i < h; i++) {
        Arrays.fill(grid[i] = new char[w], '.');
      }
    }

    //We use Streams to make a more concise method for representing the board
    public String toString() {
        return IntStream.range(0, width).mapToObj(Integer::toString).collect(Collectors.joining()) + "\n" + 
            Arrays.stream(grid).map(String::new).collect(Collectors.joining("\n"));
    }

    public String horizontal() {
        return new String(grid[lastTop]);
    }

    public String vertical() {
        StringBuilder sb = new StringBuilder(height);
        for (int h = 0; h < height; h++) {
            sb.append(grid[h][lastCol]); 
        }

        return sb.toString();
    }
    // get string representation of the slash diagonal containing the last play of the user
    public String slashDiagonal() {
        StringBuilder sb = new StringBuilder(height);

        for (int h = 0; h < height; h++) {
            int w = lastCol + lastTop - h;

            if (0 <= w && w < width) {
                sb.append(grid[h] [w]);
            }
        }
        return sb.toString();
    }

    // get string representation of the other slash diagonal containing the same thing as the other
    public String backslashDiagonal() {
        StringBuilder sb = new StringBuilder(height);

        for(int h=0; h<height; h++) {
            int w = lastCol - lastTop + h;

            if (0 <= w && w < width) {
                sb.append(grid[h] [w]);
            }
        }
        return sb.toString();
    }
    public static boolean contains(String str, String substring) {
        return str.indexOf(substring) >= 0;
    }
    // create a method checking if last play is a winning play
    public boolean isWinningPlay() {
        if (lastCol == 1) {
            System.err.println("No move has been made yet");
            return false;
        }
        char sym = grid[lastTop][lastCol];
        // Winning streak with the last play symbol
        String streak = String.format("%c%c%c", sym, sym, sym, sym);

        //check if streak is in row, col, diagonal or backslash diagonal
        return contains(horizontal(), streak) || contains(vertical(), streak) || contains(slashDiagonal(), streak) || contains(backslashDiagonal(), streak);
    
    }
// prompts the user for a column, repeating until a valid choice is made
public void chooseAndDrop(char symbol, Scanner input) {
    do {
      System.out.println("\nPlayer " + symbol + " turn: ");
      int col = input.nextInt();
  
      // check if column is ok
      if (!(0 <= col && col < width)) {
        System.out.println("Column must be between 0 and " + (width - 1));
        continue;
      }
  
      // now we can place the symbol to the first available row in the asked column
      for (int h = height - 1; h >= 0; h--) {
        if (grid[h][col] == '.') {
          grid[lastTop = h][lastCol = col] = symbol;
          return;
        }
      }
  
      // if column is full ==> we need to ask for a new input
      System.out.println("Column " + col + " is full.");
    } while (true);
  }
    public static void main(String[] args) {
        // we assemble all the pieces of the puzzle for building our Connect Four hame

        try (Scanner input = new Scanner(System.in)) {
            // we define some variables for our game like dimensions and nb max of movement
            int height = 6; int width = 8; int moves = height * width;

            //we create dthe connect for instance
            ConnectFour board = new ConnectFour(width, height);

            //we explain users how to enter their choices
            System.out.println("Use 0-" + (width - 1) + " to choose a column");
            System.out.println(board);

            //we iterate until max nb moves is reached
            for (int player = 0; moves-- > 0; player = 1 - player){
                char symbol = PLAYERS[player];

                //we ask to choose a column
                board.chooseAndDrop(symbol, input);
                
                System.out.println(board);

                //we need to check if a player won. If not, we continue, otherwise, we display a message
                if (board.isWinningPlay()) {
                    System.out.println("\nPlayer " + symbol + " wins!");
                    return;
                }
            }

            System.out.println("Game over, no winner, try again!");
        }
    }
}

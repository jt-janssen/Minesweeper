import java.util.*;

/**
 * Command line game board structure
 * @author JT Janssen
*/
/* Uses an alphanumeric coordinate system where:
 *  - Letters are columns on the horizontal axis, and
 *  - Numbers are rows on the vertical axis
 *  - Eg. A4, F1, G10, etc.
 * 
 * Each tile has two main values:
 *  - The String that is stored in the matrix
 *  - The visibility boolean
 * 
 * If a tile is visible, the matrix value will be printed
 * If a tile is hidden, the hiddenValue will be displayed
 * 
 * This is beneficial for games where data needs to be stored on the board,
 * but cannot be shown to the player yet
 */

public class Board {
    private String[][] board;
    private Hashtable<String, Boolean> visibleSet = new Hashtable<String, Boolean>();

    private Random random = new Random();
    private Scanner keys = new Scanner(System.in);

    int dimension;
    String initialValue, hiddenValue;

    /**
     * Example main method code
     * @param args Not used
     */
    /*
     * Creates a Board object, board, with 10x10 dimensions, "x" as the default value, " " as the hidden value, and al tiles set to hidden
     * Prints board - all tiles are " "
     * Changes the visibility of tile A3 to visible
     * Prints board - tile A3 is "x"
     */
    public static void main(String[] args) {
        Board board = new Board(10, "x", " ", false);
        board.printBoard(); 
        board.changeVisibility("A3", true);   
        board.printBoard(); 
    
    }

    /**
     * Creates the Board object
    * @param dimension Side length of board (always square)
    * @param initialValue Value to fill board with initally, shown when visible - can include color escape sequences, but the printable characters should be 1 character long (chars, emojis, etc.)
    * @param hiddenValue Value to show when tile is not visible - same as above, printable characters should be 1 charater long
    * @param initialVisibility State of visibility at beginning - all tiles
    */
    public Board(int dimension, String initialValue, String hiddenValue, boolean initialVisibility){
        if(dimension < 1){
            dimension = 1;
        }
        this.dimension = dimension;
        this.initialValue = initialValue;
        this.hiddenValue = hiddenValue;
        board = new String[dimension + 2][dimension + 2];
        for(int i = 0; i < dimension + 2; i++){
            for(int j = 0; j < dimension + 2; j++){
                board[i][j] = initialValue;
            }
        }
        setVisibleSet(initialVisibility);
    }


    /**
     * Returns true if the input position is visible
     * @param pos Alphanumeric coordinate
     * @return True if visible, false if hidden
     */
    public boolean isPosVisible(String pos){
        return visibleSet.get(pos);
    }

    /**
     * Changes the visibility of the input position
     * @param pos Alphanumeric coordinate
     * @param visibility True to set visible, false for hidden
     */
    public void changeVisibility(String pos, boolean visibility){
        visibleSet.put(pos, visibility);
        // System.out.print("visi changed");

    }

    //Used to set each tile to the default value when creating object
    private void setVisibleSet(boolean visibility){
        for(int row = -1; row < dimension + 1; row++){  //these values are so that it will include the border tiles
            for(int col = -1; col < dimension + 1; col++){
                visibleSet.put(getPosSouth(getPosEast("A1", col), row), visibility);
            }
        }
    }

    /**
     * Clears all tiles around a certain visible value
     * @param value Tile value to continually uncover
     */
    /*
     * Used to clear all tiles of a certain value (eg. Minesweeper when a blank square is uncovered)
     * Loops through the matrix, and if there is a tile of the specified value that is visible, it makes all surrounding tiles visible.
     * Continues until no tiles are made visible.
     */
    public void uncoverTilesAroundAll(String value){
        boolean tileChanged = true;
        String currentPos;
        while(tileChanged){
            tileChanged = false;
            for(int i = 0; i < dimension * dimension; i++){
                currentPos = "" + ((char) (65 + i % dimension)) + ((i / dimension) + 1);
                if(isPosVisible(currentPos) && getTile(currentPos) == value && (!isPosVisible(getPosNorth(currentPos, 1)) || !isPosVisible(getPosSouth(currentPos, 1)) || !isPosVisible(getPosEast(currentPos, 1)) || !isPosVisible(getPosWest(currentPos, 1)) || !isPosVisible(getPosNW(currentPos)) || !isPosVisible(getPosNE(currentPos)) || !isPosVisible(getPosSW(currentPos)) || !isPosVisible(getPosSE(currentPos)))){
                    changeVisibility(getPosNorth(currentPos, 1), true);
                    changeVisibility(getPosSE(currentPos), true);
                    changeVisibility(getPosSW(currentPos), true);
                    changeVisibility(getPosNE(currentPos), true);
                    changeVisibility(getPosNW(currentPos), true);
                    changeVisibility(getPosWest(currentPos, 1), true);
                    changeVisibility(getPosEast(currentPos, 1), true);
                    changeVisibility(getPosSouth(currentPos, 1), true);
                    tileChanged = true;
                }
            }
        }
    }

    /**
     * Prints the board to the console with coordinates
     */
    public void printBoard(){
        //Prints out letters above grid
        System.out.print("     ");
        for(int i = 0; i < dimension; i++){
            System.out.print(String.valueOf((char) (i + 65)) + "   ");
        }
        System.out.print("\n");

        for(int boardRow = 1; boardRow <= dimension; boardRow++){
            //prints horizontal line
            System.out.print("   +");
            for(int i = 0; i < dimension; i++){
                System.out.print("---+");
            }
            System.out.print("\n");

            //prints out numbers
            if(String.valueOf(boardRow).length() != 2){
                System.out.print(" " + (boardRow) + " | ");
            } else {
                System.out.print(boardRow+ " | ");
            }

            //prints out elements
            for(int boardColumn = 1; boardColumn <= dimension; boardColumn++){
                if(visibleSet.get(getPos(boardRow, boardColumn))){  //if the tile is visible
                    System.out.print(board[boardRow][boardColumn] + " | ");
                    // System.out.print("true");

                } else if (!visibleSet.get(getPos(boardRow, boardColumn))){ //if the tile is not visible
                    System.out.print(hiddenValue + " | ");
                    // System.out.print("false");
                }
            }
            System.out.print("\n");
        }
        //prints out last horizontal line
        System.out.print("   +");
        for(int i = 0; i < dimension; i++){
            System.out.print("---+");
        }
        System.out.print("\n");
    }

    // Returns true if the input position is within the visible matrix and not in border
    private boolean isPosValid(String pos){ //to avoid placing tiles in border
        if(getRowIndex(pos) >= 1 && getRowIndex(pos) <= dimension && getColIndex(pos) >= 1 && getColIndex(pos) <= dimension){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets a valid position from the user
     * @param inputMessage The printed message to recieve input (add \n to end if a line break is wanted)
     * @param errorMessage The error message printed if input is not valid
     * @return Returns the valid position (eg. E3)
     */
    public String getInput(String inputMessage, String errorMessage){
        System.out.print(inputMessage);
        String input = keys.next();
        if(isPosValid(input)){  
            return input;
        } else {
            System.out.println(errorMessage);
            input = "";
            return getInput(inputMessage, errorMessage);
        }
    }

    /**
     * Gets the value of the value in the tile (whether hidden or not)
     * @param pos Alphanumeric coordinate
     * @return String value of tile
     */
    public String getTile(String pos){
        return board[getRowIndex(pos)][getColIndex(pos)];
    }

    /**
     * Sets the tile value of a specified coordinate
     * @param pos Alphanumeric coordinate
     * @param value Value to assign tile - can include color escape sequences, but the printable characters should be 1 character long (chars, emojis, etc.)
     */
    public void setTile(String pos, String value){
        board[getRowIndex(pos)][getColIndex(pos)] = value;
    }

    /**
     * Used to convert the alplanumeric coordinate into row index value
     * @param pos Alphanumeric coordinate
     * @return Index value of row
     */
    // Calculated by creating a substring from the input string staring at index 1, then converting that to an integer
    public int getRowIndex(String pos){
        //offset by 1 to account for extra rows
        return Integer.parseInt(pos.substring(1));
    }

    /**
     * Used to convert the alplanumeric coordinate into column index value
     * @param pos Alphanumeric coordinate
     * @return Index value of column
     */
    // Calculated by taking the first character's ASCII value, and shifting it so that A = 1
    public int getColIndex(String pos){
        //offset by 1 to account for extra columns
        return (int) pos.charAt(0) - 64;
    }

    /**
     * Returns the alphanumeric coordinate based on row and column index
     * @param row Index value of row
     * @param column Index value of column
     * @return Alphanumeric coordinate
     */
    public String getPos(int row, int column){
        return "" + ((char) (64 + column)) + (row);
    }

    /**
     * Gets a random tile on the board
     * @return Random alphanumeric coordinate
     */
    public String getRandomTilePos(){
        return "" + ((char) (random.nextInt((65 + dimension)  - 65) + 65)) + (random.nextInt((dimension + 1) - 1) + 1);
    }


    // The following methods return the alphanumeric coordinate of the position in the specified direction
    // If an int is specified, that is how many tiles adjacent it looks

    /**
     * Returns the nth position north of the coordinate
     * @param pos Alphanumeric coordinate
     * @param n n number of tiles north
     * @return Alphanumeric coordinate n tiles north of pos
     */
    public String getPosNorth(String pos, int n){
        return String.valueOf((char) pos.charAt(0)) + (Integer.parseInt(pos.substring(1)) - n);
    }
    public String getPosSouth(String pos, int n){
        return String.valueOf((char) pos.charAt(0)) + (Integer.parseInt(pos.substring(1)) + n);
    }
    public String getPosWest(String pos, int n){
        return String.valueOf((char) (pos.charAt(0) - n)) + (Integer.parseInt(pos.substring(1)));
    }
    public String getPosEast(String pos, int n){
        return String.valueOf((char) (pos.charAt(0) + n)) + (Integer.parseInt(pos.substring(1)));
    }

    /**
     * Returns the position Southwest of the coordinate
     * @param pos Alphanumeric coordinate
     * @return Alphanumeric coordinate Southwest of pos
     */
    public String getPosSW(String pos){
        return String.valueOf((char) (pos.charAt(0) - 1)) + (Integer.parseInt(pos.substring(1)) + 1);
    }
    public String getPosSE(String pos){
        return String.valueOf((char) (pos.charAt(0) + 1)) + (Integer.parseInt(pos.substring(1)) + 1);
    }
    public String getPosNE(String pos){
        return String.valueOf((char) (pos.charAt(0) + 1)) + (Integer.parseInt(pos.substring(1)) - 1);
    }
    public String getPosNW(String pos){
        return String.valueOf((char) (pos.charAt(0) - 1)) + (Integer.parseInt(pos.substring(1)) - 1);
    }
}

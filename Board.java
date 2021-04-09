import java.util.*;

public class Board {
    private char[][] board;
    private Hashtable<Character, String> dictionary = new Hashtable<Character, String>();
    private Hashtable<String, Boolean> visibleSet = new Hashtable<String, Boolean>();


    private Random random = new Random();
    private Scanner keys = new Scanner(System.in);

    int dimension;
    char defaultValue;

    String[] tiles;

    // <------Example main method code------->

    
    public static void main(String[] args) {
        Board board = new Board(10, ' ', false);
        board.setDictionary(' ', ".");
        board.printBoard(); 
        for(int i = 0; i < board.dimension * board.dimension; i++){
            System.out.println(board.iterateTiles(i));
        }       
    }

    

    Board(int dimension, char defaultValue, boolean defaultVisibility){
        if(dimension < 1){
            dimension = 1;
        }
        this.dimension = dimension;
        this.defaultValue = defaultValue;
        board = new char[dimension + 2][dimension + 2];
        for(int i = 0; i < dimension + 2; i++){
            for(int j = 0; j < dimension + 2; j++){
                board[i][j] = defaultValue;
            }
        }
        setVisibleSet(defaultVisibility);
    }


    boolean isPosVisible(String pos){
        return visibleSet.get(pos);
    }

    void changeVisibility(String pos, boolean visibility){
        visibleSet.put(pos, visibility);
    }

    private void setVisibleSet(boolean defaultValue){
        for(int row = -1; row < dimension + 1; row++){  //these values are so that it will include the border tiles
            for(int col = -1; col < dimension + 1; col++){
                visibleSet.put(getPosSouth(getPosEast("A1", col), row), defaultValue);
            }
        }
    }

    String iterateTiles(int x){
        return "" + ((char) (65 + x % dimension)) + ((x / dimension) + 1);
    }

    void printBoard(){
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
                for (char key : dictionary.keySet()) {
                    if(board[boardRow][boardColumn] == key && visibleSet.get(getPos(boardRow, boardColumn))){
                        System.out.print(dictionary.get(key) + " | ");
                    } else if (board[boardRow][boardColumn] == key && !visibleSet.get(getPos(boardRow, boardColumn))){
                        System.out.print(defaultValue + " | ");
                    }
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

    private boolean isPosValid(String pos){
        if(getRowIndex(pos) >= 1 && getRowIndex(pos) <= dimension && getColIndex(pos) >= 1 && getColIndex(pos) <= dimension){
            return true;
        } else {
            return false;
        }
    }

    String getInput(String inputMessage, String errorMessage){
        System.out.print(inputMessage);
        String input = keys.next();
        if(isPosValid(input)){  
            return input;
        } else {
            System.out.println(errorMessage);
            input = "";
            return getInput(inputMessage, errorMessage);   //recursion - repeats the method until condition is satisfied
        }
    }
    char getTile(String pos){
        return board[getRowIndex(pos)][getColIndex(pos)];
    }
    void setTile(String pos, char value){
        board[getRowIndex(pos)][getColIndex(pos)] = value;
    }
    private int getRowIndex(String pos){
        //offset by 1 to account for extra rows
        return Integer.parseInt(pos.substring(1));
    }

    private int getColIndex(String pos){
        //offset by 1 to account for extra columns
        return (int) pos.charAt(0) - 64;
    }

    void setDictionary(char hidden, String visible){
        dictionary.put(hidden, visible);
    }

    String getPos(int row, int column){
        return "" + ((char) (64 + column)) + (row);
    }

    String getRandomTilePos(){
        return "" + ((char) (random.nextInt((65 + dimension)  - 65) + 65)) + (random.nextInt((dimension + 1) - 1) + 1);
    }

    String getPosSouth(String pos, int n){
        return String.valueOf((char) pos.charAt(0)) + (Integer.parseInt(pos.substring(1)) + n);
    }
    String getPosNorth(String pos, int n){
        return String.valueOf((char) pos.charAt(0)) + (Integer.parseInt(pos.substring(1)) - n);
    }
    String getPosWest(String pos, int n){
        return String.valueOf((char) (pos.charAt(0) - n)) + (Integer.parseInt(pos.substring(1)));
    }
    String getPosEast(String pos, int n){
        return String.valueOf((char) (pos.charAt(0) + n)) + (Integer.parseInt(pos.substring(1)));
    }

    String getPosSW(String pos){
        return String.valueOf((char) (pos.charAt(0) - 1)) + (Integer.parseInt(pos.substring(1)) + 1);
    }
    String getPosSE(String pos){
        return String.valueOf((char) (pos.charAt(0) + 1)) + (Integer.parseInt(pos.substring(1)) + 1);
    }
    String getPosNE(String pos){
        return String.valueOf((char) (pos.charAt(0) + 1)) + (Integer.parseInt(pos.substring(1)) - 1);
    }
    String getPosNW(String pos){
        return String.valueOf((char) (pos.charAt(0) - 1)) + (Integer.parseInt(pos.substring(1)) - 1);
    }
}

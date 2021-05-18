public class Minesweeper {

    Board board;
    int numOfBombs;

    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_WHITE = "\u001B[37m";

    static final String TILE_BOMB = "*";
    static final String TILE_1 = ANSI_BLUE + "1" + ANSI_RESET;
    static final String TILE_2 = ANSI_GREEN + "2" + ANSI_RESET;
    static final String TILE_3 = ANSI_RED + "3" + ANSI_RESET;
    static final String TILE_4 = ANSI_PURPLE + "4" + ANSI_RESET;
    static final String TILE_5 = ANSI_RED + "5" + ANSI_RESET;
    static final String TILE_6 = ANSI_CYAN + "6" + ANSI_RESET;
    static final String TILE_7 = ANSI_BLACK + "7" + ANSI_RESET;
    static final String TILE_8 = "8";
    static final String TILE_EMPTY = " ";



    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper(10, 10);
        clearScreen();
        printTitle();
        minesweeper.board.printBoard();
        while(minesweeper.uncoverTile()){
            clearScreen();
            printTitle();
            minesweeper.board.printBoard();
        }
        System.out.println(ANSI_RED + "Game over." + ANSI_RESET);
    }

    Minesweeper(int dimension, int numOfBombs){
        // board = new Board(dimension, 'X', false);
        board = new Board(dimension, " ", "X", false);
        this.numOfBombs = numOfBombs;
        placeBombs();
        fillBoard();
    }

    static void printTitle(){
        System.out.println("\n __  __ _\n|  \\/  (_)" +
        "\n| \\  / |_ _ __   ___  _____      _____  ___ _ __   ___ _ __"+
        "\n| |\\/| | | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|"+
        "\n| |  | | | | | |  __/\\__ \\  V  V /  __/  __/ |_) |  __/ |"+
        "\n|_|  |_|_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|"+
        "\n                                           | |"+
        "\n                                           |_|");
    }

    boolean uncoverTile(){
        String input = board.getInput("Enter a tile to uncover: ", "Not a valid tile, enter another.");
        board.changeVisibility(input, true);
        if(board.getTile(input) == TILE_EMPTY){
            board.uncoverTilesAroundAll(TILE_EMPTY);
            return true;
        } else if (board.getTile(input) == TILE_BOMB){
            return false;
        } else {
            return true;
        }
    }

    private void placeBombs(){
        for(int i = 0; i < numOfBombs; i++){
            String randomTile = board.getRandomTilePos();
            if(board.getTile(randomTile) != TILE_BOMB){
                board.setTile(randomTile, TILE_BOMB);
            } else {
                i--;
            }
        }
    } 

    private void fillBoard(){
        int counter;
        for(int row = 0; row < board.dimension; row++){
            for(int col = 0; col < board.dimension; col++){
                String currentPos = board.getPosSouth(board.getPosEast("A1", col), row);
                counter = 0;
                if(board.getTile(currentPos) != TILE_BOMB){
                    if(board.getTile(board.getPosNorth(currentPos, 1)) == TILE_BOMB){
                        counter++;
                    }
                    if(board.getTile(board.getPosSouth(currentPos, 1)) == TILE_BOMB){
                        counter++;
                    }
                    if(board.getTile(board.getPosEast(currentPos, 1)) == TILE_BOMB){
                        counter++;
                    }
                    if(board.getTile(board.getPosWest(currentPos, 1)) == TILE_BOMB){
                        counter++;
                    }
                    if(board.getTile(board.getPosNW(currentPos)) == TILE_BOMB){
                        counter++;
                    }
                    if(board.getTile(board.getPosNE(currentPos)) == TILE_BOMB){
                        counter++;
                    }
                    if(board.getTile(board.getPosSE(currentPos)) == TILE_BOMB){
                        counter++;
                    }
                    if(board.getTile(board.getPosSW(currentPos)) == TILE_BOMB){
                        counter++;
                    }
                    
                    if(counter == 0){
                        board.setTile(currentPos, TILE_EMPTY);
                    } else if (counter  == 1){
                        board.setTile(currentPos, TILE_1);
                    }else if (counter  == 2){
                        board.setTile(currentPos, TILE_2);
                    }else if (counter  == 3){
                        board.setTile(currentPos, TILE_3);
                    }else if (counter  == 4){
                        board.setTile(currentPos, TILE_4);
                    }else if (counter  == 5){
                        board.setTile(currentPos, TILE_5);
                    }else if (counter  == 6){
                        board.setTile(currentPos, TILE_6);
                    }else if (counter  == 7){
                        board.setTile(currentPos, TILE_7);
                    }else if (counter  == 8){
                        board.setTile(currentPos, TILE_8);
                    }
                }
            }
        }
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}
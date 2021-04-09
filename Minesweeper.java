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

    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper(10, 10);
        minesweeper.board.printBoard();
        while(minesweeper.uncoverTile()){
            minesweeper.board.printBoard();
        }
        System.out.println(ANSI_RED + "Game over." + ANSI_RESET);
    }

    Minesweeper(int dimension, int numOfBombs){
        board = new Board(dimension, 'X', false);
        this.numOfBombs = numOfBombs;
        defineChars();
        placeBombs();
        fillBoard();
    }

    boolean uncoverTile(){
        String input = board.getInput("Enter a tile to uncover: ", "Not a valid tile, enter another.");
        board.changeVisibility(input, true);
        if(board.getTile(input) == '0'){
            tileSweep();
            return true;
        } else if (board.getTile(input) == '*'){
            return false;
        } else {
            return true;
        }
    }

    private void placeBombs(){
        for(int i = 0; i < numOfBombs; i++){
            String randomTile = board.getRandomTilePos();
            if(board.getTile(randomTile) != '*'){
                board.setTile(randomTile, '*');
            } else {
                i--;
            }
        }
    }

    private void defineChars(){
        board.setDictionary('*', "*");
        board.setDictionary('1', ANSI_BLUE + "1" + ANSI_RESET);
        board.setDictionary('2', ANSI_GREEN + "2" + ANSI_RESET);
        board.setDictionary('3', ANSI_RED + "3" + ANSI_RESET);
        board.setDictionary('4', ANSI_PURPLE + "4" + ANSI_RESET);
        board.setDictionary('5', ANSI_RED + "5" + ANSI_RESET);
        board.setDictionary('6', ANSI_RED + "6" + ANSI_RESET);
        board.setDictionary('7', ANSI_RED + "7" + ANSI_RESET);
        board.setDictionary('8', ANSI_RED + "8" + ANSI_RESET);
        board.setDictionary('0', " ");
    }

    private void tileSweep(){
        boolean tileChanged = true;
        String currentPos;
        while(tileChanged){
            tileChanged = false;
            for(int i = 0; i < board.dimension * board.dimension; i++){
                currentPos = board.iterateTiles(i);
                if(board.isPosVisible(currentPos) && board.getTile(currentPos) == '0' && (!board.isPosVisible(board.getPosNorth(currentPos, 1)) || !board.isPosVisible(board.getPosSouth(currentPos, 1)) || !board.isPosVisible(board.getPosEast(currentPos, 1)) || !board.isPosVisible(board.getPosWest(currentPos, 1)) || !board.isPosVisible(board.getPosNW(currentPos)) || !board.isPosVisible(board.getPosNE(currentPos)) || !board.isPosVisible(board.getPosSW(currentPos)) || !board.isPosVisible(board.getPosSE(currentPos)))){
                    board.changeVisibility(board.getPosNorth(currentPos, 1), true);
                    board.changeVisibility(board.getPosSE(currentPos), true);
                    board.changeVisibility(board.getPosSW(currentPos), true);
                    board.changeVisibility(board.getPosNE(currentPos), true);
                    board.changeVisibility(board.getPosNW(currentPos), true);
                    board.changeVisibility(board.getPosWest(currentPos, 1), true);
                    board.changeVisibility(board.getPosEast(currentPos, 1), true);
                    board.changeVisibility(board.getPosSouth(currentPos, 1), true);
                    tileChanged = true;
                }
            }
        }
    }

    private void fillBoard(){
        int counter;
        for(int row = 0; row < board.dimension; row++){
            for(int col = 0; col < board.dimension; col++){
                String currentPos = board.getPosSouth(board.getPosEast("A1", col), row);
                counter = 0;
                if(board.getTile(currentPos) != '*'){
                    if(board.getTile(board.getPosNorth(currentPos, 1)) == '*'){
                        counter++;
                    }
                    if(board.getTile(board.getPosSouth(currentPos, 1)) == '*'){
                        counter++;
                    }
                    if(board.getTile(board.getPosEast(currentPos, 1)) == '*'){
                        counter++;
                    }
                    if(board.getTile(board.getPosWest(currentPos, 1)) == '*'){
                        counter++;
                    }
                    if(board.getTile(board.getPosNW(currentPos)) == '*'){
                        counter++;
                    }
                    if(board.getTile(board.getPosNE(currentPos)) == '*'){
                        counter++;
                    }
                    if(board.getTile(board.getPosSE(currentPos)) == '*'){
                        counter++;
                    }
                    if(board.getTile(board.getPosSW(currentPos)) == '*'){
                        counter++;
                    }
                    board.setTile(currentPos, (char) (counter + '0'));
                }
            }
        }
    }
}
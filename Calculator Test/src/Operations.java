public enum Operations {
    ADD('+'),
    SUBTRACT('-'),
    MULTIPLY('x'),
    DIVIDE('/'),
    OPEN_BRACKET('('),
    CLOSE_BRACKET(')');


    private final char symbol;

    Operations(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    /**
     * Checks if a symbol is a valid symbol in the defined subset
     * @return true or false depending on if symbol is valid or not
     */
    public static boolean isOperator(Character character){
        return ((character.equals(Operations.ADD.getSymbol())) || (character.equals(Operations.SUBTRACT.getSymbol()))
                || (character.equals(Operations.DIVIDE.getSymbol())) || (character.equals(Operations.MULTIPLY.getSymbol()))
                || (character.equals(Operations.OPEN_BRACKET.getSymbol())) || (character.equals(Operations.CLOSE_BRACKET.getSymbol())));
    }
}
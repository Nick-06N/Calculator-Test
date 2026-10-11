import java.awt.*;
import java.util.ArrayList;
import java.math.*;

public class Calculator{
//    private String input;
    private Operations operation;
    private char digitOfCalculation;
    private int index;

    public String calculate(String input){
        index = 0;
        ArrayList<String> numbers = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<Character> currentNumber = new ArrayList<>();
        ArrayList<Character> currentOperator = new ArrayList<>();
        if(input != null){
            char[] calcChars = input.toCharArray();
            // A leading '-' (optionally after spaces) is a negative sign on the first number,
            // not a subtraction operator, as long as a digit or decimal point actually follows it.
            index = spaceRemover(calcChars, index);
            if (index < calcChars.length && calcChars[index] == Operations.SUBTRACT.getSymbol()) {
                int afterSign = spaceRemover(calcChars, index + 1);
                if (afterSign < calcChars.length && (Character.isDigit(calcChars[afterSign]) || calcChars[afterSign] == '.')) {
                    currentNumber.add(Operations.SUBTRACT.getSymbol());
                    index++;
                }
            }
            // Computes and adds numbers and operators to two separate ArrayLists, ending this process short if invalid format.
            while (index < calcChars.length) {
                currentNumber = parseNumber(currentNumber, index, calcChars, input);
                boolean numberUsed = numberAdder(currentNumber, numbers);
                currentNumber.clear();
                if (!numberUsed) {
                    if(numbers.size() == 1){
                        return numbers.getFirst();
                    } else {
                        return "Invalid Number Used";
                    }
                }
                if (index < calcChars.length){
                    currentOperator = parseOperator(currentOperator, index, calcChars, input);
                    boolean operatorUsed = operatorSimplify(currentOperator, operators);
                    if (!operatorUsed){
                        return "Invalid Operator";
                    }
                    currentOperator.clear();
                }
            }
        }
        boolean success = computeBrackets(numbers, operators);
        if(!success){
            return "Cannot divide by 0";
        }
        success = computePrecedenceOrder(numbers, operators, 0, (operators.size() - 1));
        if(!success){
            return "Cannot divide by 0";
        }
        return numbers.getFirst();
    }

    /**
     * Returns Character array contents as a string
     */
    public String toString(ArrayList<Character> arrayList) {
        StringBuilder text = new StringBuilder();
        for(Character i: arrayList){
            text.append(i);
        }
        return text.toString();
    }

    /**
     * Convert a number into an ArrayList of type Character.
     * @param number    takes in the number that the argument is
     * @param i                 index of position of list
     * @param calcChars         the char Array of the whole calculation which is to be processed
     * @return                  returns ArrayList of Characters corresponding to first numerical argument
     */
    public ArrayList<Character> parseNumber(ArrayList<Character> number, int i, char[] calcChars, String input){
        if(input != null){
            digitOfCalculation = calcChars[i];
            i = spaceRemover(calcChars, i);
            // Uses an int value to check the number of decimal places in the current number
            int validDecimal = 0;
            while ((i) < calcChars.length && (Character.isDigit(digitOfCalculation) || digitOfCalculation == '.')) {
                if(digitOfCalculation != '.'){
                    number.add(digitOfCalculation);
                    i++;
                    if (i < calcChars.length) {
                        digitOfCalculation = calcChars[i];
                    }
                } else {
                    validDecimal = validDecimal + 1;
                    // Valid decimal place check
                    if(validDecimal > 1){
                        return null;
                    }
                    number.add(digitOfCalculation);
                    i++;
                    if (i < calcChars.length) {
                        digitOfCalculation = calcChars[i];
                    }
                }
            }
            i = spaceRemover(calcChars, i);
            index = i;
            return number;
        }
        return null;
    }

    private ArrayList<Character> parseOperator(ArrayList<Character> operator, int i, char[] calcChars, String input){
        if(input != null){
            digitOfCalculation = calcChars[i];
            // Uses an int value to check the number of decimal places in the current number
            while ((i) < calcChars.length && Operations.isOperator(digitOfCalculation)) {
                if(digitOfCalculation == Operations.ADD.getSymbol()) {
                    operator.add(Operations.ADD.getSymbol());
                    i++;
                }
                else if(digitOfCalculation == Operations.SUBTRACT.getSymbol()) {
                    operator.add(Operations.SUBTRACT.getSymbol());
                    i++;
                }
                else if(digitOfCalculation == Operations.MULTIPLY.getSymbol()) {
                    operator.add(Operations.MULTIPLY.getSymbol());
                    i++;
                }
                else if(digitOfCalculation == Operations.DIVIDE.getSymbol()){
                    operator.add(Operations.DIVIDE.getSymbol());
                    i++;
                }
                else if(digitOfCalculation == Operations.OPEN_BRACKET.getSymbol()){
                    operator.add(Operations.OPEN_BRACKET.getSymbol());
                    i++;
                }
                else if(digitOfCalculation == Operations.CLOSE_BRACKET.getSymbol()){
                    operator.add(Operations.CLOSE_BRACKET.getSymbol());
                    i++;
                }
                if (i < calcChars.length) {
                    digitOfCalculation = calcChars[i];
                }
            }
            
            index = i;
            return operator;
        }
        return null;
    }

    /**
     * Checks for spaces before and after numbers given in calculation and removes them
     */
    private int spaceRemover(char[] calcChars, int i){
        if(i < calcChars.length){
            while(calcChars[i] == ' ' && (i+1) < calcChars.length){
                i++;
                digitOfCalculation = calcChars[i];
            }
        }
        return i;
    }

    /**
     * Simplifies current operator ArrayList (e.g. +-+ becomes -).
     * @param currentOperator current operator substring to be simplified.
     * @param operators operators ArrayList in current calculation.
     * @return true if valid operator ArrayList is provided that can be simplified, false if operator cannot be
     *         simplified (e.g. /* cannot be simplified.
     */
    private boolean operatorSimplify(ArrayList<Character> currentOperator, ArrayList<String> operators){
        // Normalises a run of consecutive operator characters (which may include +/- runs mixed
        // with brackets, e.g. "x(") into the correct sequence of single-character tokens.
        if (currentOperator == null || currentOperator.isEmpty()) {
            return false;
        }
        int opNum = 0;
        boolean plusMinus = false;
        for (Character symbol : currentOperator) {
            if (symbol == Operations.SUBTRACT.getSymbol()) {
                opNum++;
                plusMinus = true;
            } else if (symbol == Operations.ADD.getSymbol()) {
                plusMinus = true;
            } else if (symbol == Operations.MULTIPLY.getSymbol() || symbol == Operations.DIVIDE.getSymbol()) {
                if (plusMinus) {
                    // e.g. "+x" or "-/" - a +/- run directly touching * or / with no number
                    // or bracket between them is not a valid expression.
                    return false;
                }
                operators.add(symbol.toString());
            } else if (symbol == Operations.OPEN_BRACKET.getSymbol() || symbol == Operations.CLOSE_BRACKET.getSymbol()) {
                if (plusMinus) {
                    operators.add(((opNum % 2) == 0) ? Operations.ADD.getSymbol() + "" : Operations.SUBTRACT.getSymbol() + "");
                    opNum = 0;
                    plusMinus = false;
                }
                operators.add(symbol.toString());
            }
        }
        if (plusMinus) {
            operators.add(((opNum % 2) == 0) ? Operations.ADD.getSymbol() + "" : Operations.SUBTRACT.getSymbol() + "");
        }
        return true;
    }

    private boolean numberAdder(ArrayList<Character> currentNumber, ArrayList<String> numbers){
        if (currentNumber != null && !currentNumber.isEmpty()) {
            numbers.add(toString(currentNumber));
            return true;
        } else {
            return false;
        }
    }

    private String rounder(Double result){
        if(result != null){
            result = result * 1000000;
            long roundedResult = Math.round(result);
            double finalResult = roundedResult / 1000000.0;
            return "" + finalResult;
        }
        return "Invalid calculation";
    }

    /**
     * Computes and removes brackets from operators ArrayList.
     * @param numbers ArrayList of numbers in calculation.
     * @param operators ArrayList of operators in calculation.
     */
    public boolean computeBrackets(ArrayList<String> numbers, ArrayList<String> operators) {
        // Pad any unmatched open brackets with a closing bracket at the end.
        long openCount = 0;
        long closeCount = 0;
        for (String element : operators) {
            if (element.equals(Operations.OPEN_BRACKET.getSymbol() + "")) {
                openCount++;
            } else if (element.equals(Operations.CLOSE_BRACKET.getSymbol() + "")) {
                closeCount++;
            }
        }
        while (openCount > closeCount) {
            operators.add(Operations.CLOSE_BRACKET.getSymbol() + "");
            closeCount++;
        }

        // Resolve one bracket pair at a time: the first close bracket paired with the nearest
        // preceding open bracket is always the innermost pair. Remove the close bracket (higher
        // index) before the open bracket so the earlier removal doesn't invalidate the index of
        // the token still to be removed.
        int currentCloseBracketIndex;
        while ((currentCloseBracketIndex = operators.indexOf(Operations.CLOSE_BRACKET.getSymbol() + "")) != -1) {
            int currentOpenBracketIndex = -1;
            for (int i = currentCloseBracketIndex - 1; i >= 0; i--) {
                if (operators.get(i).equals(Operations.OPEN_BRACKET.getSymbol() + "")) {
                    currentOpenBracketIndex = i;
                    break;
                }
            }
            if (currentOpenBracketIndex == -1) {
                return false;
            }
            operators.remove(currentCloseBracketIndex);
            operators.remove(currentOpenBracketIndex);
            boolean success = computePrecedenceOrder(numbers, operators, currentOpenBracketIndex, (currentCloseBracketIndex - 2));
            if (!success) {
                return false;
            }
        }
        return true;
    }

    /**
     * Computes precedence order and condenses list of numbers, working towards having one singular number in the
     * numbers ArrayList, that being the final answer.
     *
     * @param numbers ArrayList of all the calculation's numbers in the order they were typed
     * @param operators ArrayList of all calculation's numbers in the order they were typed
     * @param startIndex the index at which you want to start the calculation
     * @param endIndex the index at which you want to end the calculation
     * @return true if successful computation false if not (division by 0)
     */
    public boolean computePrecedenceOrder(ArrayList<String> numbers, ArrayList<String> operators, int startIndex, int endIndex){
        for (int e = startIndex; e <= endIndex; e++) {
            if (operators.get(e).equals(Operations.DIVIDE.getSymbol() + "")) {
                if (numbers.get((e+1)).equals("0")) {
                    return false;
                }
                double result = (Double.parseDouble(numbers.get(e)) / Double.parseDouble(numbers.get((e + 1))));
                String roundResult = rounder(result);
                numbers.set(e, roundResult);
                numbers.remove((e + 1));
                operators.remove(e);
                e--;
                endIndex--;
            }
        }
        for (int e = startIndex; e <= endIndex; e++) {
            if (operators.get(e).equals((Operations.MULTIPLY.getSymbol() + ""))) {
                double result = (Double.parseDouble(numbers.get(e)) * Double.parseDouble(numbers.get((e + 1))));
                String roundResult = rounder(result);
                numbers.set(e, roundResult);
                numbers.remove((e + 1));
                operators.remove(e);
                e--;
                endIndex--;
            }
        }
        for (int e = startIndex; e <= endIndex; e++) {
            if (operators.get(e).equals(Operations.ADD.getSymbol() + "")) {
                double result = (Double.parseDouble(numbers.get(e)) + Double.parseDouble(numbers.get((e + 1))));
                String roundResult = rounder(result);
                numbers.set(e, roundResult);
                numbers.remove((e + 1));
                operators.remove(e);
                e--;
                endIndex--;
            }else if (operators.get(e).equals(Operations.SUBTRACT.getSymbol() + "")) {
                double result = (Double.parseDouble(numbers.get(e)) - Double.parseDouble(numbers.get((e + 1))));
                String roundResult = rounder(result);
                numbers.set(e, roundResult);
                numbers.remove((e + 1));
                operators.remove(e);
                e--;
                endIndex--;
            }
        }
        return true;
    }

    /**
     * Internal method of operatorSimplify method written to reduce code reuse.
     * @param opNum
     * @param currentOperator
     * @param opIndex
     */
    public void plusMinusSimplify(int opNum, ArrayList<Character> currentOperator, int opIndex) {
        if ((opNum % 2) == 0) {
            currentOperator.add(opIndex, Operations.ADD.getSymbol());
        } else if ((opNum % 2) == 1) {
            currentOperator.add(opIndex, Operations.SUBTRACT.getSymbol());
        }
        opNum = 0;
    }
}





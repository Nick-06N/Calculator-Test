import java.awt.*;
import java.util.ArrayList;
import java.math.*;

public class Calculator{
    private String input;
    private Operations operation;
    private char digitOfCalculation;
    private int index = 0;

    void main() {
        while(true){
            System.out.println(("What would you like to calculate?"));
            UserInput current = new UserInput();
            input = current.getCalculation();
            System.out.println(calculate());
            index = 0;
        }
    }

    private String calculate(){
        ArrayList<String> numbers = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<Character> currentNumber = new ArrayList<>();
        ArrayList<Character> currentOperator = new ArrayList<>();
        if(input != null){
            char[] calcChars = input.toCharArray();
            while (index < calcChars.length) {
                currentNumber = parseNumber(currentNumber, index, calcChars);
                if (index < calcChars.length){
                    currentOperator = parseOperator(currentOperator, index, calcChars);
                    operatorSimplify(currentOperator, operators);
                }
                }
                // Adds parsed number to numbers ArrayList
                if (currentNumber != null) {
                    numbers.add(toString(currentNumber));
                } else {
                    if (numbers.size() == 1) {
                        return numbers.getFirst();
                    } else {
                        return "Invalid Calculation";
                    }
                }
                // Change this to support negative nums

            
            // If no operator is provided after the first number, that number is returned
            // if(digitOfCalculation != Operations.ADD.getSymbol() && digitOfCalculation != Operations.SUBTRACT.getSymbol() && digitOfCalculation != Operations.DIVIDE.getSymbol() && digitOfCalculation != Operations.MULTIPLY.getSymbol()){
            //    return toString(currentNumber);
            // }

            // Determine the operation type
            if(digitOfCalculation == Operations.ADD.getSymbol()){
                operation = Operations.ADD;
                index++;
            } else if(digitOfCalculation == Operations.SUBTRACT.getSymbol()){
                operation = Operations.SUBTRACT;
                index++;
            } else if(digitOfCalculation == Operations.MULTIPLY.getSymbol()){
                operation = Operations.MULTIPLY;
                index++;
            } else{
                operation = Operations.DIVIDE;
                index++;
            }

            currentOperator = parseNumber(currentOperator, index, calcChars);
            if(currentOperator.isEmpty()){
                return "Invalid Calculation";
            }
        }

        Double result = null;
        switch(operation){
            case ADD:
                result = Double.parseDouble(toString(currentNumber)) + Double.parseDouble(toString(currentOperator));
                break;
            case SUBTRACT:
                result = Double.parseDouble(toString(currentNumber)) - Double.parseDouble(toString(currentOperator));
                break;
            case MULTIPLY:
                result = Double.parseDouble(toString(currentNumber)) * Double.parseDouble(toString(currentOperator));
                break;
            case DIVIDE:
                if(Double.parseDouble(toString(currentOperator)) == 0){
                    return "Invalid Calculation";
                }
                result = Double.parseDouble(toString(currentNumber)) / Double.parseDouble(toString(currentOperator));
                break;
        }

        if(result != null){
            result = result * 1000000;
            long roundedResult = Math.round(result);
            double finalResult = roundedResult / 1000000.0;
            return "" + finalResult;
        }
        return "Invalid calculation";
    }

    /**
     * Returns array contents as a string
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
    public ArrayList<Character> parseNumber(ArrayList<Character> number, int i, char[] calcChars){
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

    private ArrayList<Character> parseOperator(ArrayList<Character> operator, int i, char[] calcChars){
        if(input != null){
            digitOfCalculation = calcChars[i];
            // Uses an int value to check the number of decimal places in the current number
            while ((i) < calcChars.length && isOperator(digitOfCalculation)) {
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
     * Checks if a symbol is a valid symbol in the defined subset
     * @return true or false depending on if symbol is valid or not
     */
    private boolean isOperator(char digitOfCalculation){
        return (digitOfCalculation == Operations.ADD.getSymbol() || digitOfCalculation == Operations.SUBTRACT.getSymbol() || digitOfCalculation == Operations.DIVIDE.getSymbol() || digitOfCalculation == Operations.MULTIPLY.getSymbol());
    }

    private void operatorSimplify(ArrayList<Character> currentOperator, ArrayList<String> operators){
        // Normalises valid multiple operator combinations to normalise into their simplest equivalent
        if (currentOperator != null) {
            if (currentOperator.size() == 1) {
                operators.add(toString(currentOperator));
            } else if (currentOperator.size() > 1) {
                int opNum = 0;
                for (Character o : currentOperator) {
                    if ((o == '/') || (o == '*')) {
                        return; // FIX THIS TO STOP CALCULATION
                    } else if (o == '-') {
                        opNum++;
                    }
                }
                currentOperator.clear();
                if ((opNum % 2) == 0) {
                    currentOperator.add('+');
                } else if ((opNum % 2) == 1) {
                    currentOperator.add('-');
                }
                operators.add(toString(currentOperator));
            }
        }
    }
}





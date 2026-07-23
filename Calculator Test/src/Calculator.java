import java.awt.*;
import java.util.ArrayList;
import java.math.*;

public class Calculator{
    private int output;
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
        ArrayList<Character> firstNum = new ArrayList<>();
        ArrayList<Character> secondNum = new ArrayList<>();
        if(input != null){
            char[] calcChars = input.toCharArray();
            firstNum = parseNumber(firstNum, index, calcChars);
            if(firstNum.isEmpty()){
                return "Invalid Calculation";
            }

            // If no operator is provided after the first number, that number is returned
            if(digitOfCalculation != Operations.ADD.getSymbol() && digitOfCalculation != Operations.SUBTRACT.getSymbol() && digitOfCalculation != Operations.DIVIDE.getSymbol() && digitOfCalculation != Operations.MULTIPLY.getSymbol()){
                return toString(firstNum);
            }

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

            secondNum = parseNumber(secondNum, index, calcChars);
            if(secondNum.isEmpty()){
                return "Invalid Calculation";
            }
        }

        Double result = null;
        switch(operation){
            case ADD:
                result = Double.parseDouble(toString(firstNum)) + Double.parseDouble(toString(secondNum));
                break;
            case SUBTRACT:
                result = Double.parseDouble(toString(firstNum)) - Double.parseDouble(toString(secondNum));
                break;
            case MULTIPLY:
                result = Double.parseDouble(toString(firstNum)) * Double.parseDouble(toString(secondNum));
                break;
            case DIVIDE:
                if(Double.parseDouble(toString(secondNum)) == 0){
                    return "Invalid Calculation";
                }
                result = Double.parseDouble(toString(firstNum)) / Double.parseDouble(toString(secondNum));
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
     * @param argumentNumber    takes in the number that the argument is
     * @param i                 index of position of list
     * @param calcChars         the char Array of the whole calculation which is to be processed
     * @return                  returns ArrayList of Characters corresponding to first numerical argument
     */
    public ArrayList<Character> parseNumber(ArrayList<Character> argumentNumber, int i, char[] calcChars){
        if(input != null){
            digitOfCalculation = calcChars[i];
            i = spaceRemover(calcChars, i);
            // Uses an int value to check the number of decimal places in the current number
            int validDecimal = 0;
            while ((i) < calcChars.length && (Character.isDigit(digitOfCalculation) || digitOfCalculation == '.')) {
                if(digitOfCalculation != '.'){
                    argumentNumber.add(digitOfCalculation);
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
                    argumentNumber.add(digitOfCalculation);
                    i++;
                    if (i < calcChars.length) {
                        digitOfCalculation = calcChars[i];
                    }
                }
            }
            i = spaceRemover(calcChars, i);
            index = i;
            return argumentNumber;
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
}





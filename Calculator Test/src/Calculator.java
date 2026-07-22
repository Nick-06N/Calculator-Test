import java.awt.*;
import java.util.ArrayList;

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

        Integer result = null;
        switch(operation){
            case ADD:
                result = Integer.parseInt(toString(firstNum)) + Integer.parseInt(toString(secondNum));
                break;
            case SUBTRACT:
                result = Integer.parseInt(toString(firstNum)) - Integer.parseInt(toString(secondNum));
                break;
            case MULTIPLY:
                result = Integer.parseInt(toString(firstNum)) * Integer.parseInt(toString(secondNum));
                break;
            case DIVIDE:
                if(Integer.parseInt(toString(secondNum)) == 0){
                    return "Invalid Calculation";
                }
                result = Integer.parseInt(toString(firstNum)) / Integer.parseInt(toString(secondNum));
                break;
        }

        if(result != null){
            return "" + result;
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
            while ((i) < calcChars.length && Character.isDigit(digitOfCalculation)) {
                argumentNumber.add(digitOfCalculation);
                i++;
                if (i < calcChars.length) {
                    digitOfCalculation = calcChars[i];
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





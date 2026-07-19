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
            firstNum = parseNumber(firstNum,index);
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

            secondNum = parseNumber(secondNum, index);
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

    public ArrayList<Character> parseNumber(ArrayList<Character> argumentNumber,int i){
        if(input != null){
            char[] calcChars = input.toCharArray();
            digitOfCalculation = calcChars[i];
            while ((i) < calcChars.length && Character.isDigit(digitOfCalculation)) {
                argumentNumber.add(digitOfCalculation);
                index++;
                i = index;
                if (i < calcChars.length) {
                    digitOfCalculation = calcChars[i];
                }
            }
            return argumentNumber;
        }
        return null;
    }
}





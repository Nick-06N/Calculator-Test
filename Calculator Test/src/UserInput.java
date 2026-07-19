import java.util.Scanner;

public class UserInput {

    private String calculation;

    public UserInput(){
        Scanner scanner = new Scanner(System.in);
        calculation = scanner.nextLine();
    }

    public String getCalculation(){
        return calculation;
    }
}



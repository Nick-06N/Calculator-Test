import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;

// REMOVE CLOSE BRACKETS IF THEY ARE INSERTED BEFORE OPEN BRACKETS

public class CalculatorApp extends Application {

    private final Calculator calculator = new Calculator();
    private final StringBuilder currentExpression = new StringBuilder();
    Label calculatorScreen = new Label();
    private String previousResult;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        GridPane numbersGrid = new GridPane();
        HBox numbersGridCentred = new HBox();

        String[] calcLabels = {"7", "8", "9", "/", "DEL",
                               "4", "5", "6", "x", "(",
                               "1", "2", "3", "-", ")",
                               ".", "0", "=", "+"
        };
        calculatorScreen.setFont(new Font("Arial", 24));
        int currentCol = 0;
        int currentRow = 0;
        for (String label: calcLabels){
            Button button = new Button(label);
            button.setPrefSize(100, 75);
            button.setMinSize(50, 38);
            button.setOnAction(e -> handleButtonPress(button.getText()));

            numbersGrid.add(button, currentCol, currentRow);
            currentCol++;
            if (currentCol == 5){
                currentCol = 0;
                currentRow++;
            }
//            ColumnConstraints col = new ColumnConstraints();
//            col.setPercentWidth(25);
//            numbersGrid.getColumnConstraints().add(col);
        }

        numbersGridCentred.getChildren().add(numbersGrid);
        numbersGridCentred.setAlignment(Pos.CENTER);
        root.setCenter(calculatorScreen);
        root.setBottom(numbersGridCentred);

        Scene scene = new Scene(root, 500, 700);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressed ->{
            if ((keyPressed.getCode() == KeyCode.ENTER) && !currentExpression.isEmpty()){
                outputResult();
            } else if ((keyPressed.getCode() == KeyCode.BACK_SPACE) && !currentExpression.isEmpty()) {
                currentExpression.deleteCharAt((currentExpression.length() - 1));
                updateScreen();
            }
        });
        scene.addEventFilter(KeyEvent.KEY_TYPED, keyTyped ->{
            String character = keyTyped.getCharacter();
            handleKeyPress(character);
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void handleButtonPress(String label){
        if (label.equals("=")){
            outputResult();
//          currentExpression.append(result);   allows usage of result
        }
        else {
            if ((label.equals("+") || label.equals("-") || label.equals("x") || label.equals("/")) && (previousResult != null)){
                currentExpression.append(previousResult);
                currentExpression.append(label);
                updateScreen();
            } else {
                currentExpression.append(label);
                updateScreen();
            }
        }
    }

    private void handleKeyPress(String key){
        Character keyChar;
        if(key.length() == 1){
            keyChar = key.charAt(0);
            if (Character.isDigit(keyChar) || Operations.isOperator(keyChar) || keyChar == '.'){
                currentExpression.append(keyChar);
                updateScreen();
            }
        } else if (key.equals("ENTER")){
            outputResult();
        } else if (key.equals("BACKSPACE")){
            if(!currentExpression.isEmpty()){
                currentExpression.deleteCharAt((currentExpression.length() - 1));
                updateScreen();
            }
        }
    }

    private void outputResult() {
        String result = calculator.calculate(currentExpression.toString());
        calculatorScreen.setText(result);
        previousResult = result;
        currentExpression.setLength(0);
    }

    private void updateScreen() {
        calculatorScreen.setText(currentExpression.toString());
    }
}

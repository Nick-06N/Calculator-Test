import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        GridPane numbersGrid = new GridPane();
        HBox numbersGridCentred = new HBox();

        String[] calcLabels = {"7", "8", "9", "/",
                               "4", "5", "6", "x",
                               "1", "2", "3", "-",
                               ".", "0", "=", "+"
        };

        int currentCol = 0;
        int currentRow = 0;
        for (String label: calcLabels){
            Button button = new Button(label);
            numbersGrid.add(button, currentCol, currentRow);
            currentCol++;
            if (currentCol == 4){
                currentCol = 0;
                currentRow++;
            }
        }
        Label myLabel = new Label("Testing");

        numbersGridCentred.getChildren().add(numbersGrid);
        numbersGridCentred.setAlignment(Pos.CENTER);
        root.setCenter(myLabel);
        root.setBottom(numbersGridCentred);

        Scene scene = new Scene(root, 500, 700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

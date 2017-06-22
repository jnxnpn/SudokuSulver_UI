package sample;
//Author: jpan@hamdenhall.org (valid thru 2018.8)
//Date: Jun 21, 2017 (sudoku solver code reused from past)


import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.soap.Text;

public class Main extends Application /*implements EventHandler<ActionEvent>*/ {

    private TextField[][] input = new TextField[9][9];

    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        window.setTitle("Solve This Sudoku");
        GridPane gpLayout = new GridPane();
        gpLayout.setPadding(new Insets(20, 20, 20, 20));
        gpLayout.setHgap(8);
        gpLayout.setVgap(8);
        for (int i = 0; i < input.length; i++) {
            for (int u = 0; u < input[0].length; u++) {
                input[i][u] = new TextField();
                gpLayout.getChildren().add(input[i][u]);
                GridPane.setConstraints(input[i][u], i, u);
            }
        }

        Button slv = new Button("Solve!");
        //GridPane.setConstraints(solve,4,10);
        //gpLayout.getChildren().add(solve);

        Label msg = new Label("Please input a puzzle to solve");
        //GridPane.setConstraints(msg,5,10);
        //gpLayout.getChildren().add(msg);

        VBox v = new VBox(0);
        VBox v2 = new VBox();
        msg.setPadding(new Insets(10, 0, 0, 0));
        v2.setAlignment(Pos.CENTER);
        v2.getChildren().addAll(slv, msg);
        v.getChildren().addAll(gpLayout, v2);

        slv.setOnAction(e ->
                {
                    if (slv.getText().equals("OK")) {
                        slv.setText("Solve!");
                        msg.setText("Please input another puzzle to solve");
                        for (TextField[] i : input)
                            for (TextField u : i) {
                                u.setDisable(false);
                                u.setText("");
                            }
                    } else switch (clicked()) {
                        case 0:
                            msg.setText("Failed. Please check input. ");
                            break;
                        case 1:
                            msg.setText("Solved. -Difficulty: Not Hard-");
                            slv.setText("OK");
                            for (TextField[] i : input)
                                for (TextField u : i)
                                    u.setDisable(true);
                            //clearInput();
                            break;
                        case 2:
                            msg.setText("Solved. -Difficulty: Hard-");
                            slv.setText("OK");
                            for (TextField[] i : input)
                                for (TextField u : i)
                                    u.setDisable(true);
                            //clearInput();
                            break;
                    }

                }
        );


        window.setScene(new Scene(v, 350, 450));


        window.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    private int clicked() {
        int[][] temp = new int[9][9];
        for (int i = 0; i < input.length; i++)
            for (int u = 0; u < input[0].length; u++) {
                if (input[i][u].getText().length() == 0) temp[i][u] = 0;
                else {
                    char c = input[i][u].getText().charAt(0);
                    if (c > '0' && c <= '9') {
                        temp[i][u] = c - '0';
                    } else temp[i][u] = 0;
                }
            }
        Sudoku sudoku = new Sudoku(temp);
        if (!sudoku.ifSolved()) return 0;
        else {
            updateBox (sudoku.getResult());
            if (sudoku.isHard()) return 2;
            else return 1;
        }
    }

    private void updateBox (int[][] result) {
        for (int i = 0; i < input.length; i++)
            for (int u = 0; u < input[0].length; u++) {
                input[i][u].setText(new Integer(result[i][u]).toString());
            }
    }
}

package com.example.regexvisualizer;

import com.example.regexvisualizer.regexutils.State;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegexVisualizer extends Application {
    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("REGEX Visualizer");
        primaryStage.initStyle(StageStyle.DECORATED);

        var grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(25, 25, 25, 25));

        var scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        var sceneTitle = new Text("Regex Visualizer");
        sceneTitle.setFont(Font.font("TimesNewRoman", FontWeight.BOLD, 25));
        grid.add(sceneTitle, 0, 0, 2, 1);

        var regexLabel = new Label("Enter regex: ");
        grid.add(regexLabel, 0, 1);

        var regex = new TextField();
        grid.add(regex, 1, 1);

        var userTextLabel = new Label("Enter text: ");
        grid.add(userTextLabel, 0, 2);

        var userText = new TextArea();
        grid.add(userText, 1, 2, 2, 2);

        var submitForAnalysis = new Button("Regex!");
        grid.add(submitForAnalysis, 1, 5);

        State state1 = new State(1, true);
        State state2 = new State(2, true);
        State state3 = new State(3, true);

        state1.addTransition('a', state2);
        state1.addTransition('b', state3);
        state1.addTransition('c', state1);
        state1.addTransition('b', state2);

        state1.printTransitions();

        primaryStage.show();
    }
}
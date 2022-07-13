package com.example.regexvisualizer;

import com.example.regexvisualizer.regexutils.ParseHelper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;


public class RegexVisualizer extends Application {
    public static void main(String [] args) {
        launch(args);
    }

    private String regexString = "";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("REGEX Visualizer");
        primaryStage.initStyle(StageStyle.DECORATED);

        var grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(0, 0, 0, 0));

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        var scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        var sceneTitle = new Text("Regex Visualizer");
        sceneTitle.setFont(Font.font("TimesNewRoman", FontWeight.BOLD, 25));
        grid.add(sceneTitle, 0, 1, 2, 1);

        var regexLabel = new Label("Enter regex: ");
        grid.add(regexLabel, 0, 2);

        var regex = new TextField();
        grid.add(regex, 1, 2);

        var userTextLabel = new Label("Enter text: ");
        grid.add(userTextLabel, 0, 3);

        var userTextBox = new InlineCssTextArea();
        grid.add(userTextBox, 1, 3, 2, 2);

        var submitForAnalysis = new Button("Regex!");
        var uploadFileButton = new Button("Select file to analyze");

        uploadFileButton.setOnAction(actionEvent -> {
           System.out.println("In select file");
           File selectedFile = fileChooser.showOpenDialog(primaryStage);
           Path fileName = Path.of(String.valueOf(selectedFile));
           String textFromFile = "";

           userTextBox.clear();

           try {
               textFromFile = Files.readString(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(selectedFile);
            System.out.println(textFromFile);

            userTextBox.appendText(textFromFile);
        });

        submitForAnalysis.setOnAction(actionEvent -> {
            regexString = String.valueOf(regex.getText());

            Set<Integer> highlightedIndices = ParseHelper.getWordsThatMatchRegex(userTextBox.getText(), regexString);
            GraphicsHelper.modifyTextArea(userTextBox, highlightedIndices);
        });

        grid.add(submitForAnalysis, 1, 5);
        grid.add(uploadFileButton, 2, 5);

        primaryStage.show();
    }
}

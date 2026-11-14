package org.example;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

public class MenuScreenView {

    public static Scene create(
            String mainMenuText,
            String languageText,
            String currentLanguage,
            String simulatorText,
            String historyText,
            String howToUseText,
            String backText,
            String exitText,
            Consumer<String> languageChangeAction,
            Runnable simulatorAction,
            Runnable historyAction,
            Runnable howToUseAction,
            Runnable backAction,
            Runnable exitAction
    ) {
        Label titleLabel = new Label(mainMenuText);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 34));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(new DropShadow(18, Color.BLACK));

        Label languageLabel = new Label(languageText);
        languageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        languageLabel.setTextFill(Color.LIGHTGRAY);

        ChoiceBox<String> languageBox = new ChoiceBox<>();
        languageBox.getItems().addAll("EN", "JP");
        languageBox.setValue(currentLanguage);
        languageBox.setPrefWidth(90);
        languageBox.getStyleClass().add("language-choice-box");

        languageBox.setOnAction(event -> {
            languageChangeAction.accept(languageBox.getValue());
        });

        Button simulatorButton = createStyledButton(simulatorText);
        Button historyButton = createStyledButton(historyText);
        Button howToUseButton = createStyledButton(howToUseText);
        Button backButton = createStyledButton(backText);
        Button exitButton = createStyledButton(exitText);

        simulatorButton.setOnAction(event -> {
            simulatorAction.run();
        });

        historyButton.setOnAction(event -> {
            historyAction.run();
        });

        howToUseButton.setOnAction(event -> {
            howToUseAction.run();
        });

        backButton.setOnAction(event -> {
            backAction.run();
        });

        exitButton.setOnAction(event -> {
            exitAction.run();
        });

        VBox menuBox = new VBox();
        menuBox.setSpacing(15);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(430);
        menuBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.72);"
                        + "-fx-background-radius: 18;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 18;"
                        + "-fx-padding: 35;"
        );

        menuBox.getChildren().addAll(
                titleLabel,
                languageLabel,
                languageBox,
                simulatorButton,
                historyButton,
                howToUseButton,
                backButton,
                exitButton
        );

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #050505, #151008, #2a1a00);"
        );

        root.getChildren().add(menuBox);

        Scene scene = new Scene(root, 900, 600);

        scene.getStylesheets().add(
                MenuScreenView.class
                        .getResource("/menu.css")
                        .toExternalForm()
        );

        return scene;
    }

    private static Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        button.setPrefWidth(260);
        button.setPrefHeight(45);
        button.setCursor(Cursor.HAND);

        String normalStyle =
                "-fx-background-color: #101010;"
                        + "-fx-text-fill: #ff9f1c;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;";

        String hoverStyle =
                "-fx-background-color: #ff9f1c;"
                        + "-fx-text-fill: #101010;"
                        + "-fx-border-color: #ffffff;"
                        + "-fx-border-width: 2;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;";

        button.setStyle(normalStyle);

        button.setOnMouseEntered(event -> {
            button.setStyle(hoverStyle);
        });

        button.setOnMouseExited(event -> {
            button.setStyle(normalStyle);
        });

        return button;
    }
}
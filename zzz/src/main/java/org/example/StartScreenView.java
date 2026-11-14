package org.example;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;

public class StartScreenView {
    public static Scene create(
            Stage mainStage,
            String titleText,
            String infoText,
            String startText,
            Runnable startAction
    ) {
        Image backgroundImage = new Image(
                StartScreenView.class.getResource("/images/background.png").toExternalForm()
        );

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.fitWidthProperty().bind(mainStage.widthProperty());
        backgroundView.fitHeightProperty().bind(mainStage.heightProperty());

        Pane contentPane = new Pane();
        contentPane.setPrefSize(760, 540);
        contentPane.setMinSize(760, 540);
        contentPane.setMaxSize(760, 540);

        contentPane.setStyle(
                "-fx-background-color: rgba(5, 10, 12, 0.55);"
                        + "-fx-background-radius: 20;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 20;"
        );

        Label titleLabel = new Label(titleText);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 46));
        titleLabel.setTextFill(Color.web("#ffb347"));
        titleLabel.setLayoutX(45);
        titleLabel.setLayoutY(45);

        Label infoLabel = new Label(infoText);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 22));
        infoLabel.setTextFill(Color.web("#ffcc80"));
        infoLabel.setLayoutX(50);
        infoLabel.setLayoutY(120);

        Button startButton = new Button(startText);
        startButton.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        startButton.setPrefWidth(240);
        startButton.setPrefHeight(70);
        startButton.setLayoutX(260);
        startButton.setLayoutY(370);

        String normalButtonStyle =
                "-fx-background-color: #101010;"
                        + "-fx-text-fill: #ff9f1c;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;";

        String hoverButtonStyle =
                "-fx-background-color: #ff9f1c;"
                        + "-fx-text-fill: #101010;"
                        + "-fx-border-color: #ffffff;"
                        + "-fx-border-width: 2;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;";

        startButton.setStyle(normalButtonStyle);


        Image mascotImage = new Image(
                StartScreenView.class.getResource("/images/mascot.png").toExternalForm()
        );

        ImageView mascotView = new ImageView(mascotImage);
        mascotView.setPreserveRatio(true);
        mascotView.setFitWidth(190);
        mascotView.setLayoutX(410);
        mascotView.setLayoutY(110);
        mascotView.setOpacity(0);
        mascotView.setTranslateY(-30);

        startButton.setOnAction(event -> {
            startButton.setDisable(true);

            ScaleTransition clickAnimation = new ScaleTransition(Duration.millis(700), mascotView);
            clickAnimation.setFromX(mascotView.getScaleX());
            clickAnimation.setFromY(mascotView.getScaleY());
            clickAnimation.setToX(1.35);
            clickAnimation.setToY(1.35);
            clickAnimation.setAutoReverse(true);
            clickAnimation.setCycleCount(2);

            clickAnimation.setOnFinished(finishEvent -> {
                startAction.run();
            });

            clickAnimation.playFromStart();

        });
        startButton.setCursor(javafx.scene.Cursor.HAND);

        ScaleTransition mascotGrowAnimation = new ScaleTransition(Duration.millis(160), mascotView);
        mascotGrowAnimation.setToX(1.08);
        mascotGrowAnimation.setToY(1.08);

        ScaleTransition mascotShrinkAnimation = new ScaleTransition(Duration.millis(160), mascotView);
        mascotShrinkAnimation.setToX(1.0);
        mascotShrinkAnimation.setToY(1.0);

        startButton.setOnMouseEntered(event -> {
            startButton.setStyle(hoverButtonStyle);
            mascotShrinkAnimation.stop();
            mascotGrowAnimation.playFromStart();
        });

        startButton.setOnMouseExited(event -> {
            startButton.setStyle(normalButtonStyle);
            mascotGrowAnimation.stop();
            mascotShrinkAnimation.playFromStart();
        });

        contentPane.getChildren().addAll(
                titleLabel,
                infoLabel,
                startButton,
                mascotView
        );

        StackPane root = new StackPane();
        root.getChildren().addAll(
                backgroundView,
                contentPane
        );

        Scene scene = new Scene(root, 1200, 800);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), mascotView);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        TranslateTransition dropTransition = new TranslateTransition(Duration.millis(700), mascotView);
        dropTransition.setFromY(-30);
        dropTransition.setToY(0);

        ParallelTransition appearAnimation = new ParallelTransition(
                fadeTransition,
                dropTransition
        );

        TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(1.4), mascotView);
        floatingAnimation.setFromY(0);
        floatingAnimation.setToY(-8);
        floatingAnimation.setAutoReverse(true);
        floatingAnimation.setCycleCount(Animation.INDEFINITE);

        appearAnimation.setOnFinished(event -> {
            floatingAnimation.play();
        });

        appearAnimation.play();

        return scene;
    }
}
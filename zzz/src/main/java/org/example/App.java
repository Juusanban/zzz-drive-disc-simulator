package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class App extends Application {

    private Stage mainStage;

    private String language = "EN";

    private boolean simulationResultReady = false;
    private int lastCurrentLevel;
    private String lastSubStat1 = "";
    private String lastSubStat2 = "";
    private String lastSubStat3 = "";
    private String lastSubStat4 = "";
    private  String lastUsefulStat1 = "";
    private  String lastUsefulStat2 = "";
    private  int lastRequiredHits;
    private  int  lastSimulationCount;
    private double lastChance;
    private String lastRecommendation = "";
    private String lastResultText = "";

    @Override
    public void start(Stage stage) {
        DatabaseManager.initializeDatabase();
        // Save main window
        mainStage = stage;
        mainStage.setWidth(1200);
        mainStage.setHeight(800);

        mainStage.setMinWidth(900);
        mainStage.setMinHeight(600);

        mainStage.setResizable(true);

        // Show first screen
        showStartScreen();

        // Set window title
        mainStage.setTitle("ZZZ Simulator");

        // Show application window
        mainStage.show();
    }



    private void showStartScreen() {
        // Start screen title
        Scene scene = StartScreenView.create(
                mainStage,
                text("appTitle"),
                text("startInfo"),
                text("start"),
                () -> {
                    showMenuScreen();
                }
        );
        // Set scene to main window
        mainStage.setScene(scene);
    }

    private void showHowToUseScreen() {
        Label titleLabel = new Label(text("howToUse"));
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 34)
        );
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(
                new DropShadow(18, Color.BLACK)
        );

        Label infoLabel = new Label(
                text("howToUseInstructions")
        );

        infoLabel.setFont(
                Font.font("Arial", FontWeight.NORMAL, 17)
        );
        infoLabel.setTextFill(Color.LIGHTGRAY);
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(650);
        infoLabel.setLineSpacing(7);

        Button backButton = new Button(text("back"));
        backButton.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );
        backButton.setPrefWidth(220);
        backButton.setPrefHeight(45);
        backButton.setCursor(Cursor.HAND);

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

        backButton.setStyle(normalStyle);

        backButton.setOnMouseEntered(event -> {
            backButton.setStyle(hoverStyle);
        });

        backButton.setOnMouseExited(event -> {
            backButton.setStyle(normalStyle);
        });

        backButton.setOnAction(event -> {
            showMenuScreen();
        });

        VBox panel = new VBox(28);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(740);
        panel.setPadding(new Insets(35));

        panel.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.78);"
                        + "-fx-background-radius: 18;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 18;"
        );

        panel.getChildren().addAll(
                titleLabel,
                infoLabel,
                backButton
        );

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(35));

        root.setStyle(
                "-fx-background-color: "
                        + "linear-gradient(to bottom right, "
                        + "#050505, #151008, #2a1a00);"
        );

        root.getChildren().add(panel);

        Scene scene = new Scene(root, 900, 600);
        mainStage.setScene(scene);
    }


    private void showMenuScreen() {
        Scene scene = MenuScreenView.create(
                text("mainMenu"),
                text("language"),
                language,
                text("driveDiscSimulator"),
                text("history"),
                text("howToUse"),
                text("back"),
                text("exit"),
                newLanguage -> {
                    language = newLanguage;
                    showMenuScreen();
                },
                () -> showSimulatorScreen(),
                () -> showHistoryScreen(),
                () -> showHowToUseScreen(),
                () -> showStartScreen(),
                () -> mainStage.close()
        );

        mainStage.setScene(scene);
    }

    private void showSimulatorScreen() {
        // Simulator title
        Label titleLabel = new Label(text("driveDiscSimulator"));

        // Field labels
        Label levelLabel = new Label(text("currentLevel"));
        Label remainingUpgradesLabel =
                new Label(text("remainingUpgrades") + ": 5");

        Label subStat1Label = new Label(text("subStat1"));
        Label subStat2Label = new Label(text("subStat2"));
        Label subStat3Label = new Label(text("subStat3"));
        Label subStat4Label = new Label(text("subStat4"));

        Label usefulStat1Label = new Label(text("usefulStat1"));
        Label usefulStat2Label = new Label(text("usefulStat2"));

        Label simulationCountLabel = new Label(text("simulationCount"));

        // Current level select
        ChoiceBox<String> levelBox = new ChoiceBox<>();
        levelBox.getItems().addAll("0", "3", "6", "9", "12");
        levelBox.setValue("0");

        // Sub Stat 1 select
        ChoiceBox<String> subStat1Box = new ChoiceBox<>();
        subStat1Box.getItems().addAll("None");
        subStat1Box.getItems().addAll(StatData.getSubStats());
        subStat1Box.setValue("Crit Rate");

        // Sub Stat 2 select
        ChoiceBox<String> subStat2Box = new ChoiceBox<>();
        subStat2Box.getItems().addAll("None");
        subStat2Box.getItems().addAll(StatData.getSubStats());
        subStat2Box.setValue("Crit DMG");

        // Sub Stat 3 select
        ChoiceBox<String> subStat3Box = new ChoiceBox<>();
        subStat3Box.getItems().addAll("None");
        subStat3Box.getItems().addAll(StatData.getSubStats());
        subStat3Box.setValue("HP");

        // Sub Stat 4 select
        ChoiceBox<String> subStat4Box = new ChoiceBox<>();
        subStat4Box.getItems().addAll("None");
        subStat4Box.getItems().addAll(StatData.getSubStats());
        subStat4Box.setValue("None");

        // Useful Stat 1 select
        ChoiceBox<String> usefulStat1Box = new ChoiceBox<>();
        usefulStat1Box.getItems().addAll(StatData.getSubStats());
        usefulStat1Box.setValue("Crit Rate");

        // Useful Stat 2 select
        ChoiceBox<String> usefulStat2Box = new ChoiceBox<>();
        usefulStat2Box.getItems().addAll(StatData.getSubStats());
        usefulStat2Box.setValue("Crit DMG");

        // Required useful hits select
        ChoiceBox<String> requiredHitsStat1Box = new ChoiceBox<>();
        requiredHitsStat1Box.getItems().addAll("0", "1", "2", "3", "4", "5");
        requiredHitsStat1Box.setValue("1");

        ChoiceBox<String> requiredHitsStat2Box = new ChoiceBox<>();
        requiredHitsStat2Box.getItems().addAll("0", "1", "2", "3", "4", "5");
        requiredHitsStat2Box.setValue("1");

        Runnable updateRequiredHitsOptions = () -> {
            int currentLevel = Integer.parseInt(levelBox.getValue());
            int remainingUpgrades = (15 - currentLevel) / 3;

            boolean hasNone =
                    "None".equals(subStat1Box.getValue())
                            || "None".equals(subStat2Box.getValue())
                            || "None".equals(subStat3Box.getValue())
                            || "None".equals(subStat4Box.getValue());

            int availableUpgradeHits =
                    remainingUpgrades - (hasNone ? 1 : 0);

            availableUpgradeHits = Math.max(0, availableUpgradeHits);

            int selectedStat1 =
                    requiredHitsStat1Box.getValue() == null
                            ? 0
                            : Integer.parseInt(requiredHitsStat1Box.getValue());

            int selectedStat2 =
                    requiredHitsStat2Box.getValue() == null
                            ? 0
                            : Integer.parseInt(requiredHitsStat2Box.getValue());

            selectedStat1 = Math.min(selectedStat1, availableUpgradeHits);
            selectedStat2 = Math.min(
                    selectedStat2,
                    availableUpgradeHits - selectedStat1
            );

            requiredHitsStat1Box.getItems().clear();
            requiredHitsStat2Box.getItems().clear();

            for (int i = 0; i <= availableUpgradeHits; i++) {
                String value = String.valueOf(i);

                requiredHitsStat1Box.getItems().add(value);
                requiredHitsStat2Box.getItems().add(value);
            }

            requiredHitsStat1Box.setValue(
                    String.valueOf(selectedStat1)
            );

            requiredHitsStat2Box.setValue(
                    String.valueOf(selectedStat2)
            );
        };

        updateRequiredHitsOptions.run();

        HBox usefulStat1Row = new HBox(10);
        usefulStat1Row.setAlignment(Pos.CENTER);
        usefulStat1Row.getChildren().addAll(
                usefulStat1Box,
                new Label(text("required") + ":"),
                requiredHitsStat1Box
        );

        HBox usefulStat2Row = new HBox(10);
        usefulStat2Row.setAlignment(Pos.CENTER);
        usefulStat2Row.getChildren().addAll(
                usefulStat2Box,
                new Label(text("required") + ":"),
                requiredHitsStat2Box
        );

        // Update required hits when level changes
        levelBox.setOnAction(event -> {
                    int currentLevel = Integer.parseInt(levelBox.getValue());
                    int remainingUpgrades = (15 - currentLevel) / 3;
            remainingUpgradesLabel.setText(
                    text("remainingUpgrades")
                            + ": "
                            + remainingUpgrades
            );
                    updateRequiredHitsOptions.run();
                });

        // Simulation count select
        ChoiceBox<String> simulationCountBox = new ChoiceBox<>();
        simulationCountBox.getItems().addAll("1000", "10000", "100000");
        simulationCountBox.setValue("10000");

        TextField subStat1ValueField = new TextField("2.4");
        subStat1ValueField.setPrefWidth(80);

        TextField subStat2ValueField = new TextField("9.6");
        subStat2ValueField.setPrefWidth(80);

        TextField subStat3ValueField = new TextField("112");
        subStat3ValueField.setPrefWidth(80);

        TextField subStat4ValueField = new TextField("");
        subStat4ValueField.setPrefWidth(80);

        // Clear value when sub stat is None
        subStat1Box.setOnAction(event -> {
            if (subStat1Box.getValue().equals("None")) {
                subStat1ValueField.setText("");
            }
            updateRequiredHitsOptions.run();
        });

        subStat2Box.setOnAction(event -> {
            if (subStat2Box.getValue().equals("None")) {
                subStat2ValueField.setText("");
            }
            updateRequiredHitsOptions.run();
        });

        subStat3Box.setOnAction(event -> {
            if (subStat3Box.getValue().equals("None")) {
                subStat3ValueField.setText("");
            }
            updateRequiredHitsOptions.run();
        });

        subStat4Box.setOnAction(event -> {
            if (subStat4Box.getValue().equals("None")) {
                subStat4ValueField.setText("");
            }
            updateRequiredHitsOptions.run();
        });

        HBox subStat1Row = new HBox(10);
        subStat1Row.setAlignment(Pos.CENTER);
        subStat1Row.getChildren().addAll(subStat1Box, new Label(text("value") + ":"), subStat1ValueField);

        HBox subStat2Row = new HBox(10);
        subStat2Row.setAlignment(Pos.CENTER);
        subStat2Row.getChildren().addAll(subStat2Box, new Label(text("value") + ":"), subStat2ValueField);

        HBox subStat3Row = new HBox(10);
        subStat3Row.setAlignment(Pos.CENTER);
        subStat3Row.getChildren().addAll(subStat3Box, new Label(text("value") + ":"), subStat3ValueField);

        HBox subStat4Row = new HBox(10);
        subStat4Row.setAlignment(Pos.CENTER);
        subStat4Row.getChildren().addAll(subStat4Box, new Label(text("value") + ":"), subStat4ValueField);

        // Action buttons
        Button runButton = new Button(text("runSimulation"));
        Button copyButton = new Button(text("copyResult"));
        Button saveButton = new Button(text("saveResult"));
        Button backButton = new Button(text("back"));

        copyButton.setDisable(true);
        saveButton.setDisable(true);

        // Result output label
        Label resultLabel = new Label(text("resultPlaceholder"));

        // Allow long result text to wrap
        resultLabel.setWrapText(true);

        // Limit result text width
        resultLabel.setMaxWidth(650);
        // Scroll area for long result text
        ScrollPane resultScrollPane = new ScrollPane(resultLabel);
        resultScrollPane.setFitToWidth(true);
        resultScrollPane.setPrefHeight(220);
        resultScrollPane.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(resultScrollPane,javafx.scene.layout.Priority.ALWAYS);

        // Run simulation button action
        runButton.setOnAction(event -> {
            simulationResultReady = false;
            copyButton.setDisable(true);
            saveButton.setDisable(true);

            try {
                // Read current level
                int currentLevel = Integer.parseInt(levelBox.getValue());

                // Calculate remaining upgrade actions
                int remainingUpgrades = (15 - currentLevel) / 3;

                // Read selected sub stats
                String subStat1 = subStat1Box.getValue();
                String subStat2 = subStat2Box.getValue();
                String subStat3 = subStat3Box.getValue();
                String subStat4 = subStat4Box.getValue();

                // Count None values
                int noneCount = 0;

                if (subStat1.equals("None")) {
                    noneCount++;
                }

                if (subStat2.equals("None")) {
                    noneCount++;
                }

                if (subStat3.equals("None")) {
                    noneCount++;
                }

                if (subStat4.equals("None")) {
                    noneCount++;
                }

                // Drive Disc must have at least 3 sub stats
                if (noneCount > 1) {
                    resultLabel.setText(
                            text("onlyOneNone")
                    );
                    return;
                }

                // Check duplicated sub stats
                if (!subStat1.equals("None") && subStat1.equals(subStat2)) {
                    resultLabel.setText(text("duplicateSubStats"));
                    return;
                }

                if (!subStat1.equals("None") && subStat1.equals(subStat3)) {
                    resultLabel.setText(text("duplicateSubStats"));
                    return;
                }

                if (!subStat1.equals("None") && subStat1.equals(subStat4)) {
                    resultLabel.setText(text("duplicateSubStats"));
                    return;
                }

                if (!subStat2.equals("None") && subStat2.equals(subStat3)) {
                    resultLabel.setText(text("duplicateSubStats"));
                    return;
                }

                if (!subStat2.equals("None") && subStat2.equals(subStat4)) {
                    resultLabel.setText(text("duplicateSubStats"));
                    return;
                }

                if (!subStat3.equals("None") && subStat3.equals(subStat4)) {
                    resultLabel.setText(text("duplicateSubStats"));
                    return;
                }

                // Convert None to blank value for simulation logic
                if (subStat1.equals("None")) {
                    subStat1 = "";
                }

                if (subStat2.equals("None")) {
                    subStat2 = "";
                }

                if (subStat3.equals("None")) {
                    subStat3 = "";
                }

                if (subStat4.equals("None")) {
                    subStat4 = "";
                }
                // Read current sub stat values
                double subStat1Value = 0.0;
                double subStat2Value = 0.0;
                double subStat3Value = 0.0;
                double subStat4Value = 0.0;

                if (!subStat1.equals("")) {
                    subStat1Value = Double.parseDouble(subStat1ValueField.getText());
                }

                if (!subStat2.equals("")) {
                    subStat2Value = Double.parseDouble(subStat2ValueField.getText());
                }

                if (!subStat3.equals("")) {
                    subStat3Value = Double.parseDouble(subStat3ValueField.getText());
                }

                if (!subStat4.equals("")) {
                    subStat4Value = Double.parseDouble(subStat4ValueField.getText());
                }

                // Current stat values must be greater than 0
                if ((!subStat1.equals("") && subStat1Value <= 0)
                        || (!subStat2.equals("") && subStat2Value <= 0)
                        || (!subStat3.equals("") && subStat3Value <= 0)
                        || (!subStat4.equals("") && subStat4Value <= 0)) {

                    resultLabel.setText(
                            text("positiveValues")
                    );
                    return;
                }

                // Read useful stats
                String usefulStat1 = usefulStat1Box.getValue();
                String usefulStat2 = usefulStat2Box.getValue();
                // Useful stats cannot be same
                if (usefulStat1.equals(usefulStat2)) {
                    resultLabel.setText(
                            text("duplicateUsefulStats")
                    );
                    return;
                }

                // Read simulation settings
                int requiredHitsStat1 =
                        Integer.parseInt(requiredHitsStat1Box.getValue());

                int requiredHitsStat2 =
                        Integer.parseInt(requiredHitsStat2Box.getValue());

                int requiredHits = requiredHitsStat1 + requiredHitsStat2;
                int simulationCount = Integer.parseInt(simulationCountBox.getValue());

                // Required hits cannot be greater than remaining actions
                boolean hasMissingStat =
                        subStat1.isBlank()
                                || subStat2.isBlank()
                                || subStat3.isBlank()
                                || subStat4.isBlank();

                int availableUpgradeHits =
                        remainingUpgrades - (hasMissingStat ? 1 : 0);

                if (requiredHits == 0) {
                    resultLabel.setText(
                            text("atLeastOneRequired")
                    );
                    return;
                }

                if (requiredHits > availableUpgradeHits) {
                    resultLabel.setText(
                            text("requiredExceedsUpgrades")
                    );
                    return;
                }

                // Simulation count must be positive
                if (simulationCount <= 0) {
                    resultLabel.setText(
                            text("simulationCountPositive")
                    );
                    return;
                }

                // Run calculation in Main.java
                String result = Main.runSimulation(
                        subStat1,
                        subStat2,
                        subStat3,
                        subStat4,
                        subStat1Value,
                        subStat2Value,
                        subStat3Value,
                        subStat4Value,
                        usefulStat1,
                        usefulStat2,
                        remainingUpgrades,
                        requiredHitsStat1,
                        requiredHitsStat2,
                        simulationCount,
                        language
                );

// Save latest successful simulation data
                lastCurrentLevel = currentLevel;

                lastSubStat1 = subStat1.isBlank() ? "None" : subStat1;
                lastSubStat2 = subStat2.isBlank() ? "None" : subStat2;
                lastSubStat3 = subStat3.isBlank() ? "None" : subStat3;
                lastSubStat4 = subStat4.isBlank() ? "None" : subStat4;

                lastUsefulStat1 = usefulStat1;
                lastUsefulStat2 = usefulStat2;
                lastRequiredHits = requiredHits;
                lastSimulationCount = simulationCount;
                lastChance = Main.getLastChance();
                lastRecommendation = Main.getLastRecommendation();
                lastResultText = result;

                simulationResultReady = true;
                copyButton.setDisable(false);
                saveButton.setDisable(false);

// Show result
                resultLabel.setText(result);

            } catch (NumberFormatException e) {
                // Show error if number conversion fails
                resultLabel.setText(
                        text("enterNumbers")
                );
            }
        });
        copyButton.setOnAction(event -> {
            if (!simulationResultReady) {
                return;
            }

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();

            content.putString(lastResultText);
            clipboard.setContent(content);

            resultLabel.setText(
                    lastResultText
                            + "\n\n"
                            + text("resultCopied")
            );
        });

        saveButton.setOnAction(event -> {
            if (!simulationResultReady) {
                return;
            }

            boolean saved = DatabaseManager.saveResult(
                    lastCurrentLevel,
                    lastSubStat1,
                    lastSubStat2,
                    lastSubStat3,
                    lastSubStat4,
                    lastUsefulStat1,
                    lastUsefulStat2,
                    lastRequiredHits,
                    lastSimulationCount,
                    lastChance,
                    lastRecommendation,
                    lastResultText
            );

            if (saved) {
                resultLabel.setText(
                        lastResultText
                                + "\n\n"
                                + text("resultSaved")
                );

                saveButton.setDisable(true);
            } else {
                resultLabel.setText(
                        lastResultText
                                + "\n\n"
                                + text("saveFailed")
                );
            }
        });

        // Back to menu
        backButton.setOnAction(event -> {
            showMenuScreen();
        });

        // Left panel with simulation settings
        VBox settingsPanel = new VBox(8);
        settingsPanel.setAlignment(Pos.TOP_CENTER);
        settingsPanel.setPrefWidth(330);

        settingsPanel.getChildren().addAll(
                levelLabel,
                levelBox,
                remainingUpgradesLabel,

                subStat1Label,
                subStat1Row,

                subStat2Label,
                subStat2Row,

                subStat3Label,
                subStat3Row,

                subStat4Label,
                subStat4Row,

                usefulStat1Label,
                usefulStat1Row,

                usefulStat2Label,
                usefulStat2Row,


                simulationCountLabel,
                simulationCountBox
        );

        // Center panel with simulation result
        Label resultTitleLabel = new Label(text("simulationResult"));

        VBox resultPanel = new VBox(8);
        resultPanel.setAlignment(Pos.TOP_CENTER);
        resultPanel.setMaxWidth(Double.MAX_VALUE);
        resultPanel.setMaxHeight(Double.MAX_VALUE);

        resultLabel.setMaxWidth(Double.MAX_VALUE);

        resultScrollPane.setPrefWidth(650);
        resultScrollPane.setPrefHeight(650);
        resultScrollPane.setMaxWidth(Double.MAX_VALUE);
        resultScrollPane.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(resultScrollPane, Priority.ALWAYS);

        resultPanel.getChildren().addAll(
                resultTitleLabel,
                resultScrollPane
        );

        // Right panel with action buttons
        Label actionsLabel = new Label(text("actions"));

        VBox actionPanel = new VBox(12);
        actionPanel.setAlignment(Pos.TOP_CENTER);
        actionPanel.setPrefWidth(160);

        actionPanel.getChildren().addAll(
                actionsLabel,
                runButton,
                copyButton,
                saveButton,
                backButton
        );

        // Three vertical screen sections
        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setMaxWidth(Double.MAX_VALUE);
        mainContent.setMaxHeight(Double.MAX_VALUE);

        HBox.setHgrow(resultPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(
                settingsPanel,
                resultPanel,
                actionPanel
        );

        // Main screen layout
        VBox root = new VBox(12);
        root.setAlignment(Pos.TOP_CENTER);
        root.setFillWidth(true);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(mainContent, Priority.ALWAYS);

        root.getChildren().addAll(
                titleLabel,
                mainContent
        );

        Scene scene = new Scene(root, 1200, 800);
        String css = getClass()
                .getResource("/menu.css")
                .toExternalForm();

        scene.getStylesheets().add(css);

// Main background
        root.setPadding(new Insets(18));
        root.setStyle(
                "-fx-background-color: "
                        + "linear-gradient(to bottom right, "
                        + "#050505, #151008, #2a1a00);"
        );

// Main title
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 32)
        );
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(
                new DropShadow(18, Color.BLACK)
        );

// Apply basic style to all labels, including Value and Required
        root.applyCss();

        for (javafx.scene.Node node : root.lookupAll(".label")) {
            if (node instanceof Label label) {
                label.setTextFill(Color.LIGHTGRAY);
                label.setFont(
                        Font.font("Arial", FontWeight.NORMAL, 14)
                );
            }
        }

// Important labels
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 32)
        );

        resultTitleLabel.setTextFill(Color.WHITE);
        resultTitleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 20)
        );

        actionsLabel.setTextFill(Color.WHITE);
        actionsLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 20)
        );

        remainingUpgradesLabel.setTextFill(
                Color.web("#ff9f1c")
        );
        remainingUpgradesLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 14)
        );

// Control style
        String controlStyle =
                "-fx-background-color: #242424;"
                        + "-fx-text-fill: #ffffff;"
                        + "-fx-mark-color: #ff9f1c;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 6;"
                        + "-fx-background-radius: 6;"
                        + "-fx-font-size: 13px;";

        ChoiceBox<?>[] choiceBoxes = {
                levelBox,
                subStat1Box,
                subStat2Box,
                subStat3Box,
                subStat4Box,
                usefulStat1Box,
                usefulStat2Box,
                requiredHitsStat1Box,
                requiredHitsStat2Box,
                simulationCountBox
        };

        for (ChoiceBox<?> choiceBox : choiceBoxes) {
            choiceBox.setStyle(controlStyle);
            choiceBox.setPrefHeight(34);
        }

// Control widths
        levelBox.setPrefWidth(100);

        subStat1Box.setPrefWidth(145);
        subStat2Box.setPrefWidth(145);
        subStat3Box.setPrefWidth(145);
        subStat4Box.setPrefWidth(145);

        usefulStat1Box.setPrefWidth(145);
        usefulStat2Box.setPrefWidth(145);

        requiredHitsStat1Box.setPrefWidth(60);
        requiredHitsStat2Box.setPrefWidth(60);

        simulationCountBox.setPrefWidth(140);

// Text field style
        String textFieldStyle =
                "-fx-background-color: #eeeeee;"
                        + "-fx-text-fill: #101010;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 6;"
                        + "-fx-background-radius: 6;"
                        + "-fx-font-size: 13px;";

        TextField[] valueFields = {
                subStat1ValueField,
                subStat2ValueField,
                subStat3ValueField,
                subStat4ValueField
        };

        for (TextField field : valueFields) {
            field.setStyle(textFieldStyle);
            field.setPrefHeight(34);
            field.setPrefWidth(75);
        }

// Button style
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

        Button[] actionButtons = {
                runButton,
                copyButton,
                saveButton,
                backButton
        };

        for (Button button : actionButtons) {
            button.setFont(
                    Font.font("Arial", FontWeight.BOLD, 14)
            );
            button.setPrefWidth(145);
            button.setPrefHeight(42);
            button.setCursor(Cursor.HAND);
            button.setStyle(normalButtonStyle);

            button.setOnMouseEntered(event -> {
                button.setStyle(hoverButtonStyle);
            });

            button.setOnMouseExited(event -> {
                button.setStyle(normalButtonStyle);
            });
        }

// Panels
        String panelStyle =
                "-fx-background-color: rgba(0, 0, 0, 0.78);"
                        + "-fx-background-radius: 14;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 14;";

        settingsPanel.setStyle(panelStyle);
        settingsPanel.setPadding(new Insets(18));
        settingsPanel.setPrefWidth(340);
        settingsPanel.setAlignment(Pos.TOP_LEFT);

        resultPanel.setStyle(panelStyle);
        resultPanel.setPadding(new Insets(18));

        actionPanel.setStyle(panelStyle);
        actionPanel.setPadding(new Insets(18));
        actionPanel.setPrefWidth(180);

// Align setting rows
        subStat1Row.setAlignment(Pos.CENTER_LEFT);
        subStat2Row.setAlignment(Pos.CENTER_LEFT);
        subStat3Row.setAlignment(Pos.CENTER_LEFT);
        subStat4Row.setAlignment(Pos.CENTER_LEFT);

        usefulStat1Row.setAlignment(Pos.CENTER_LEFT);
        usefulStat2Row.setAlignment(Pos.CENTER_LEFT);

// Result area
        resultLabel.setTextFill(Color.LIGHTGRAY);
        resultLabel.setFont(
                Font.font("Consolas", FontWeight.NORMAL, 14)
        );
        resultLabel.setPadding(new Insets(15));

        resultLabel.setStyle(
                "-fx-background-color: #171717;"
                        + "-fx-text-fill: #eeeeee;"
        );

        resultScrollPane.setStyle(
                "-fx-background: #171717;"
                        + "-fx-background-color: #171717;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
        );

        mainContent.setSpacing(15);

        mainStage.setScene(scene);

    }

    private void showHistoryScreen() {
        Label titleLabel = new Label(text("historyTitle"));
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 32)
        );
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(
                new DropShadow(18, Color.BLACK)
        );

        ListView<SimulationResult> historyList = new ListView<>();

        historyList.getItems().addAll(
                DatabaseManager.getAllResults()
        );

        historyList.setPrefHeight(220);
        historyList.setMaxWidth(Double.MAX_VALUE);

        historyList.setStyle(
                "-fx-background-color: #171717;"
                        + "-fx-control-inner-background: #171717;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-font-size: 14px;"
        );

        TextArea detailArea = new TextArea();
        detailArea.setEditable(false);
        detailArea.setWrapText(true);
        detailArea.setPrefHeight(300);
        detailArea.setMaxWidth(Double.MAX_VALUE);
        detailArea.setText("Select a saved result.");

        detailArea.setStyle(
                "-fx-control-inner-background: #171717;"
                        + "-fx-text-fill: #eeeeee;"
                        + "-fx-highlight-fill: #ff9f1c;"
                        + "-fx-highlight-text-fill: #101010;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-font-family: Arial;"
                        + "-fx-font-size: 14px;"
        );

        historyList.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        detailArea.setText(
                                newValue.getResultText()
                        );
                    }
                });

        if (historyList.getItems().isEmpty()) {
            detailArea.setText("No saved results.");
        }

        Button backButton = new Button(text("back"));
        backButton.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );
        backButton.setPrefWidth(220);
        backButton.setPrefHeight(45);
        backButton.setCursor(Cursor.HAND);

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

        backButton.setStyle(normalStyle);

        backButton.setOnMouseEntered(event -> {
            backButton.setStyle(hoverStyle);
        });

        backButton.setOnMouseExited(event -> {
            backButton.setStyle(normalStyle);
        });

        backButton.setOnAction(event -> {
            showMenuScreen();
        });

        VBox panel = new VBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(760);
        panel.setPadding(new Insets(25));

        panel.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.78);"
                        + "-fx-background-radius: 18;"
                        + "-fx-border-color: #ff9f1c;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 18;"
        );

        VBox.setVgrow(detailArea, Priority.ALWAYS);

        panel.getChildren().addAll(
                titleLabel,
                historyList,
                detailArea,
                backButton
        );

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        root.setStyle(
                "-fx-background-color: "
                        + "linear-gradient(to bottom right, "
                        + "#050505, #151008, #2a1a00);"
        );

        root.getChildren().add(panel);

        Scene scene = new Scene(root, 800, 650);
        mainStage.setScene(scene);
    }

    private String text(String key) {
        return TextData.get(key, language);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
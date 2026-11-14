package org.example;

public class SimulationResult {

    private final int id;
    private final String createdAt;
    private final int currentLevel;
    private final double chance;
    private final String recommendation;
    private final String resultText;

    public SimulationResult(
            int id,
            String createdAt,
            int currentLevel,
            double chance,
            String recommendation,
            String resultText
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.currentLevel = currentLevel;
        this.chance = chance;
        this.recommendation = recommendation;
        this.resultText = resultText;
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public double getChance() {
        return chance;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getResultText() {
        return resultText;
    }

    @Override
    public String toString() {
        return createdAt
                + " | Level " + currentLevel
                + " | Chance " + String.format("%.2f", chance) + "%";
    }
}
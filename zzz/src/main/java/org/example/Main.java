package org.example;

import java.util.Random;

public class Main {

    public static String runSimulation(
            String subStat1,
            String subStat2,
            String subStat3,
            String subStat4,
            double value1,
            double value2,
            double value3,
            double value4,
            String usefulStat1,
            String usefulStat2,
            int remainingUpgrades,
            int requiredHitsStat1,
            int requiredHitsStat2,
            int simulationCount,
            String language
    ) {
        Random random = new Random();

        // Count successful simulations
        int successCount = 0;

        // Count all useful hits from all simulations
        int totalUsefulHits = 0;

        // Count generated missing stats
        int generatedCount = 0;

        // Count generated stats that were useful
        int generatedUsefulCount = 0;

        // All possible sub stats
        String[] allStats = StatData.getSubStats();

        // Count how many times each missing stat was generated
        int[] generatedStatCounts = new int[allStats.length];

        int [] generatedStatUpgradeCounts = new int[allStats.length];

        // Count how many times each slot was upgraded
        int[] totalUpgradeCounts = new int[4];

        // Current values of sub stats
        double[] subStatValues={
                value1,
                value2,
                value3,
                value4,
        };

        // Run Monte Carlo simulation
        for (int i = 0; i < simulationCount; i++) {
            int usefulStat1Hits = 0;
            int usefulStat2Hits = 0;

            // Copy current sub stats for this simulation
            String[] subStats = {
                    subStat1,
                    subStat2,
                    subStat3,
                    subStat4
            };

            // Remaining upgrade actions
            int upgradesLeft = remainingUpgrades;

            // Find missing sub stat slot
            int emptyIndex = findEmptyIndex(subStats);
            int generatedStatIndex = -1;

            // If one sub stat is missing, generate it first
            if (emptyIndex != -1) {
                String newSubStat = getRandomNewSubStat(random, subStats);

                subStats[emptyIndex] = newSubStat;
                upgradesLeft--;

                generatedCount++;

                // Count which stat was generated
                generatedStatIndex = findStatIndex(allStats, newSubStat);

                if (generatedStatIndex != -1) {
                    generatedStatCounts[generatedStatIndex]++;
                }

                // Count how often the generated stat was useful
                if (isUsefulStat(newSubStat, usefulStat1, usefulStat2)) {
                    generatedUsefulCount++;
                }
            }
            // Randomly upgrade one of 4 sub stats
            for (int j = 0; j < upgradesLeft; j++) {
                int randomIndex = random.nextInt(4);
                String upgradedStat = subStats[randomIndex];

                totalUpgradeCounts[randomIndex]++;

                if (randomIndex == emptyIndex && generatedStatIndex != -1) {
                    generatedStatUpgradeCounts[generatedStatIndex]++;
                }

                // Count useful upgrade
                if (upgradedStat.equals(usefulStat1)) {
                    usefulStat1Hits++;
                }

                if (upgradedStat.equals(usefulStat2)) {
                    usefulStat2Hits++;
                }
            }

            int usefulHits = usefulStat1Hits + usefulStat2Hits;
            totalUsefulHits = totalUsefulHits + usefulHits;

            // Check if this simulation was successful
            if (usefulStat1Hits >= requiredHitsStat1
                    && usefulStat2Hits >= requiredHitsStat2) {

                successCount++;
            }
        }

        // Calculate final chance
        double chance = (double) successCount / simulationCount * 100;

        // Calculate average useful hits
        double averageUsefulHits = (double) totalUsefulHits / simulationCount;

        //Restore current level from remaining upgrades
        int currentLevel = 15 - remainingUpgrades *3;

        String recommendation = getRecommendation(
                chance,
                language
        );

        lastChance = chance;
        lastRecommendation = recommendation;

// Check simulation mode
        boolean hasMissingStat = subStat1.isBlank()
                || subStat2.isBlank()
                || subStat3.isBlank()
                || subStat4.isBlank();

// Build result text
        String result = "";

        result = result
                + "=== "
                + text("simulationSummary", language)
                + " ===";

        result = result
                + "\n"
                + getModeText(hasMissingStat, language);

        result = result
                + "\n"
                + text("totalSimulations", language)
                + ": "
                + simulationCount;

        result = result
                + "\n"
                + text("currentLevel", language)
                + ": "
                + currentLevel;

        result = result
                + "\n"
                + text("remainingUpgrades", language)
                + ": "
                + remainingUpgrades;

        result = result
                + "\n"
                + text("usefulStats", language)
                + ": "
                + usefulStat1
                + " / "
                + usefulStat2;

        result = result
                + "\n"
                + text("requiredHits", language)
                + ": "
                + usefulStat1
                + " >= "
                + requiredHitsStat1
                + " / "
                + usefulStat2
                + " >= "
                + requiredHitsStat2;

        result = result
                + "\n"
                + text("success", language)
                + ": "
                + successCount;

        result = result
                + "\n"
                + text("chance", language)
                + ": "
                + String.format("%.2f", chance)
                + "%";

        result = result
                + "\n"
                + text("averageUsefulHits", language)
                + ": "
                + String.format("%.2f", averageUsefulHits);

        result = result
                + "\n"
                + recommendation;

        // Add generated stat result if missing stat existed
        if (hasMissingStat) {
            result = result + getGeneratedStatResult(
                    allStats,
                    generatedStatCounts,
                    generatedCount,
                    generatedUsefulCount,
                    language
            );
        }

        // Add average upgrade result
        result = result + getAverageUpgradeResult(
                subStat1,
                subStat2,
                subStat3,
                subStat4,
                totalUpgradeCounts,
                simulationCount,
                language
        );
        // Add expected stat value result
        result = result + getExpectedValueResult(
                subStat1,
                subStat2,
                subStat3,
                subStat4,
                subStatValues,
                totalUpgradeCounts,
                simulationCount,
                allStats,
                generatedStatCounts,
                generatedStatUpgradeCounts,
                generatedCount,
                language
        );

        return result;

    }
    private static double lastChance;
    private static String lastRecommendation = "";
    private static String text(String key, String language) {
        return TextData.get(key, language);
    }

    private static int findEmptyIndex(String[] subStats) {
        // Return index of missing sub stat
        for (int i = 0; i < subStats.length; i++) {
            if (subStats[i].isBlank()) {
                return i;
            }
        }

        return -1;
    }

    private static boolean isUsefulStat(String statName, String usefulStat1, String usefulStat2) {
        // Check if stat is useful
        return statName.equals(usefulStat1) || statName.equals(usefulStat2);
    }

    private static String getRandomNewSubStat(Random random, String[] currentSubStats) {
        String[] possibleSubStats = StatData.getSubStats();

        while (true) {
            int randomIndex = random.nextInt(possibleSubStats.length);
            String newSubStat = possibleSubStats[randomIndex];

            boolean alreadyExists = false;

            // Do not generate duplicated sub stat
            for (int i = 0; i < currentSubStats.length; i++) {
                if (newSubStat.equals(currentSubStats[i])) {
                    alreadyExists = true;
                }
            }

            if (!alreadyExists) {
                return newSubStat;
            }
        }
    }

    private static int findStatIndex(String[] allStats, String statName) {
        // Find stat index in allStats
        for (int i = 0; i < allStats.length; i++) {
            if (allStats[i].equals(statName)) {
                return i;
            }
        }

        return -1;
    }

    private static String getModeText(
            boolean hasMissingStat,
            String language
    ) {
        if (hasMissingStat) {
            return text("modeThreeStats", language);
        }

        return text("modeFourStats", language);
    }

    private static String getRecommendation(
            double chance,
            String language
    ) {
        String recommendationText;

        if (chance >= 60) {
            recommendationText = text(
                    "goodCandidate",
                    language
            );
        } else if (chance >= 30) {
            recommendationText = text(
                    "risky",
                    language
            );
        } else {
            recommendationText = text(
                    "notRecommended",
                    language
            );
        }

        return text("recommendation", language)
                + ": "
                + recommendationText;
    }

    private static String getGeneratedStatResult(
            String[] allStats,
            int[] generatedStatCounts,
            int generatedCount,
            int generatedUsefulCount,
            String language
    ) {
        String result = "";

        result = result
                + "\n\n=== "
                + text(
                "generatedMissingStatChances",
                language
        )
                + " ===";

        // Show chance for each generated missing stat
        for (int i = 0; i < allStats.length; i++) {
            if (generatedStatCounts[i] > 0) {
                double generatedChance =
                        (double) generatedStatCounts[i]
                                / generatedCount
                                * 100;

                result = result
                        + "\n"
                        + allStats[i]
                        + ": "
                        + generatedStatCounts[i]
                        + " "
                        + text("times", language)
                        + " / "
                        + String.format(
                        "%.2f",
                        generatedChance
                )
                        + "%";
            }
        }

        double generatedUsefulChance =
                (double) generatedUsefulCount
                        / generatedCount
                        * 100;

        result = result
                + "\n"
                + text(
                "usefulGeneratedStatChance",
                language
        )
                + ": "
                + String.format(
                "%.2f",
                generatedUsefulChance
        )
                + "%";

        int maxIndex = -1;
        int maxCount = 0;

        for (int i = 0; i < allStats.length; i++) {
            if (generatedStatCounts[i] > maxCount) {
                maxCount = generatedStatCounts[i];
                maxIndex = i;
            }
        }

        if (maxIndex != -1) {
            double maxChance =
                    (double) maxCount
                            / generatedCount
                            * 100;

            result = result
                    + "\n"
                    + text(
                    "mostGeneratedMissingStat",
                    language
            )
                    + ": "
                    + allStats[maxIndex]
                    + " / "
                    + String.format(
                    "%.2f",
                    maxChance
            )
                    + "%";
        }

        return result;
    }

    private static String getAverageUpgradeResult(
            String subStat1,
            String subStat2,
            String subStat3,
            String subStat4,
            int[] totalUpgradeCounts,
            int simulationCount,
            String language
    ) {
        String result = "";

        result = result
                + "\n\n=== "
                + text("averageUpgradeCounts", language)
                + " ===";

        result = result
                + "\n"
                + text("subStat1", language)
                + " ("
                + getDisplayName(subStat1, language)
                + "): +"
                + String.format(
                "%.2f",
                (double) totalUpgradeCounts[0]
                        / simulationCount
        );

        result = result
                + "\n"
                + text("subStat2", language)
                + " ("
                + getDisplayName(subStat2, language)
                + "): +"
                + String.format(
                "%.2f",
                (double) totalUpgradeCounts[1]
                        / simulationCount
        );

        result = result
                + "\n"
                + text("subStat3", language)
                + " ("
                + getDisplayName(subStat3, language)
                + "): +"
                + String.format(
                "%.2f",
                (double) totalUpgradeCounts[2]
                        / simulationCount
        );

        result = result
                + "\n"
                + text("subStat4", language)
                + " ("
                + getDisplayName(subStat4, language)
                + "): +"
                + String.format(
                "%.2f",
                (double) totalUpgradeCounts[3]
                        / simulationCount
        );

        return result;
    }

    private static String getExpectedValueResult(
            String subStat1,
            String subStat2,
            String subStat3,
            String subStat4,
            double[] subStatValues,
            int[] totalUpgradeCounts,
            int simulationCount,
            String[] allStats,
            int[] generatedStatCounts,
            int[] generatedStatUpgradeCounts,
            int generatedCount,
            String language
    ) {
        String result = "";

        String[] subStats = {
                subStat1,
                subStat2,
                subStat3,
                subStat4
        };

        result = result
                + "\n\n=== "
                + text("expectedAverageValues", language)
                + " ===";

        for (int i = 0; i < subStats.length; i++) {
            String statName = subStats[i];

            if (statName.isBlank()) {
                result = result
                        + "\n\n["
                        + text("subStat", language)
                        + " "
                        + (i + 1)
                        + "] "
                        + text("generatedStat", language);

                for (int j = 0; j < allStats.length; j++) {
                    if (generatedStatCounts[j] > 0) {
                        double generatedChance =
                                (double) generatedStatCounts[j]
                                        / generatedCount
                                        * 100;

                        double averageProcCount =
                                (double) generatedStatUpgradeCounts[j]
                                        / generatedStatCounts[j];

                        double oneProcValue =
                                StatValueData.getUpgradeValue(
                                        allStats[j]
                                );

                        double expectedAverageValue =
                                oneProcValue
                                        * (1.0 + averageProcCount);

                        String unit =
                                StatValueData.getUnit(
                                        allStats[j]
                                );

                        result = result
                                + "\n"
                                + allStats[j]
                                + ": "
                                + text("chance", language)
                                + " "
                                + String.format(
                                "%.2f",
                                generatedChance
                        )
                                + "%"
                                + " | "
                                + text("averageProcs", language)
                                + ": +"
                                + String.format(
                                "%.2f",
                                averageProcCount
                        )
                                + " | "
                                + text("expected", language)
                                + ": "
                                + String.format(
                                "%.2f",
                                expectedAverageValue
                        )
                                + unit;
                    }
                }
            } else {
                double currentValue =
                        subStatValues[i];

                double averageProcCount =
                        (double) totalUpgradeCounts[i]
                                / simulationCount;

                double oneProcValue =
                        StatValueData.getUpgradeValue(
                                statName
                        );

                double expectedAverageValue =
                        currentValue
                                + averageProcCount
                                * oneProcValue;

                String unit =
                        StatValueData.getUnit(
                                statName
                        );

                result = result
                        + "\n\n["
                        + text("subStat", language)
                        + " "
                        + (i + 1)
                        + "] "
                        + statName
                        + "\n"
                        + text("current", language)
                        + ": "
                        + String.format(
                        "%.2f",
                        currentValue
                )
                        + unit
                        + "\n"
                        + text("averageProcs", language)
                        + ": +"
                        + String.format(
                        "%.2f",
                        averageProcCount
                )
                        + "\n"
                        + text("expected", language)
                        + ": "
                        + String.format(
                        "%.2f",
                        expectedAverageValue
                )
                        + unit;
            }
        }

        return result;
    }

    private static String getDisplayName(
            String statName,
            String language
    ) {
        if (statName.isBlank()) {
            return text("generatedStat", language);
        }

        return statName;
    }
    public static double getLastChance() {
        return lastChance;
    }

    public static String getLastRecommendation() {
        return lastRecommendation;
    }
}
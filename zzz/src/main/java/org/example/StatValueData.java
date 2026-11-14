package org.example;

import java.util.HashMap;
import java.util.Map;
public class StatValueData {
    private static final Map<String, Double> upgradeValues = new HashMap<>();
    private static final Map<String, String> units = new HashMap<>();

    static {
        // Upgrade value for one proc
        upgradeValues.put("HP", 112.0);
        upgradeValues.put("HP%", 3.0);
        upgradeValues.put("ATK", 19.0);
        upgradeValues.put("ATK%", 3.0);
        upgradeValues.put("DEF", 15.0);
        upgradeValues.put("DEF%", 4.8);
        upgradeValues.put("PEN", 9.0);
        upgradeValues.put("Crit Rate", 2.4);
        upgradeValues.put("Crit DMG", 4.8);
        upgradeValues.put("Anomaly Proficiency", 9.0);

        // Unit for result display
        units.put("HP%", "%");
        units.put("ATK%", "%");
        units.put("DEF%", "%");
        units.put("Crit Rate", "%");
        units.put("Crit DMG", "%");
    }

    public static double getUpgradeValue(String statName) {
        return upgradeValues.getOrDefault(statName, 0.0);
    }

    public static String getUnit(String statName) {
        return units.getOrDefault(statName, "");
    }
}

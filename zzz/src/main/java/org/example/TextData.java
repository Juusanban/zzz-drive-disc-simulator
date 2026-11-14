package org.example;

public class TextData {

    public static String get(String key, String language) {
        if ("JP".equals(language)) {
            return getJapanese(key);
        }

        return getEnglish(key);
    }

    public static String getStatName(
            String statName,
            String language
    ) {
        if (!"JP".equals(language)) {
            if (statName == null || statName.isBlank()) {
                return "None";
            }

            return statName;
        }

        if (statName == null || statName.isBlank()) {
            return "なし";
        }

        return switch (statName) {
            case "None" -> "なし";
            case "HP" -> "HP";
            case "HP%" -> "HP%";
            case "ATK" -> "攻撃力";
            case "ATK%" -> "攻撃力%";
            case "DEF" -> "防御力";
            case "DEF%" -> "防御力%";
            case "PEN" -> "貫通値";
            case "Crit Rate" -> "会心率";
            case "Crit DMG" -> "会心ダメージ";
            case "Anomaly Proficiency" -> "異常マスタリー";
            default -> statName;
        };
    }

    private static String getEnglish(String key) {
        return switch (key) {
            case "appTitle" -> "ZZZ Drive Disc Simulator";
            case "startInfo" -> "Drive Disc upgrade decision tool";
            case "start" -> "Start";

            case "mainMenu" -> "Main Menu";
            case "language" -> "Language";
            case "driveDiscSimulator" -> "Drive Disc Simulator";
            case "history" -> "History";
            case "howToUse" -> "How to Use";
            case "back" -> "Back";
            case "exit" -> "Exit";

            case "howToUseInstructions" ->
                    "1. Select current sub stats of the Drive Disc.\n"
                            + "2. If the disc has only 3 sub stats, choose None for one slot.\n"
                            + "3. Select useful stats.\n"
                            + "4. Select the required upgrade hits for each useful stat.\n"
                            + "5. Select simulation count.\n"
                            + "6. Press Run Simulation.\n"
                            + "7. The program calculates the chance by Monte Carlo simulation.";

            case "currentLevel" -> "Current Level";
            case "remainingUpgrades" -> "Remaining Upgrades";
            case "subStat" -> "Sub Stat";
            case "subStat1" -> "Sub Stat 1";
            case "subStat2" -> "Sub Stat 2";
            case "subStat3" -> "Sub Stat 3";
            case "subStat4" -> "Sub Stat 4";
            case "usefulStat1" -> "Useful Stat 1";
            case "usefulStat2" -> "Useful Stat 2";
            case "value" -> "Value";
            case "required" -> "Required";
            case "simulationCount" -> "Simulation Count";
            case "simulationResult" -> "Simulation Result";
            case "actions" -> "Actions";
            case "runSimulation" -> "Run Simulation";
            case "copyResult" -> "Copy Result";
            case "saveResult" -> "Save Result";
            case "resultPlaceholder" -> "Result will be shown here.";

            case "historyTitle" -> "Simulation History";
            case "selectSavedResult" -> "Select a saved result.";
            case "noSavedResults" -> "No saved results.";

            case "onlyOneNone" ->
                    "Only one None is allowed. Drive Disc must have at least 3 sub stats.";
            case "duplicateSubStats" ->
                    "Sub stats cannot be duplicated.";
            case "duplicateUsefulStats" ->
                    "Useful stats cannot be same.";
            case "enterNumbers" ->
                    "Please enter numbers.";
            case "positiveValues" ->
                    "Sub stat values must be greater than 0.";
            case "atLeastOneRequired" ->
                    "At least one required hit must be selected.";
            case "requiredExceedsUpgrades" ->
                    "The total required hits cannot exceed available upgrades.";
            case "simulationCountPositive" ->
                    "Simulation count must be greater than 0.";
            case "resultCopied" ->
                    "Result copied to clipboard.";
            case "resultSaved" ->
                    "Result saved.";
            case "saveFailed" ->
                    "Could not save result.";

            case "simulationSummary" -> "Simulation Summary";
            case "modeThreeStats" ->
                    "Mode: 3 sub stats. Missing sub stat was generated randomly.";
            case "modeFourStats" ->
                    "Mode: 4 sub stats. Only upgrades were simulated.";
            case "totalSimulations" -> "Total Simulations";
            case "usefulStats" -> "Useful Stats";
            case "requiredHits" -> "Required Hits";
            case "success" -> "Success";
            case "chance" -> "Chance";
            case "averageUsefulHits" -> "Average Useful Hits";
            case "recommendation" -> "Recommendation";
            case "goodCandidate" -> "Good candidate";
            case "risky" -> "Risky";
            case "notRecommended" -> "Not recommended";

            case "generatedMissingStatChances" ->
                    "Generated Missing Stat Chances";
            case "times" -> "times";
            case "usefulGeneratedStatChance" ->
                    "Useful generated stat chance";
            case "mostGeneratedMissingStat" ->
                    "Most generated missing stat in this simulation";

            case "averageUpgradeCounts" ->
                    "Average Upgrade Counts";
            case "expectedAverageValues" ->
                    "Expected Average Values";
            case "generatedStat" -> "Generated Stat";
            case "current" -> "Current";
            case "averageProcs" -> "Avg Procs";
            case "expected" -> "Expected";

            default -> key;
        };
    }

    private static String getJapanese(String key) {
        return switch (key) {
            case "appTitle" ->
                    "ZZZ ドライブディスクシミュレーター";
            case "startInfo" ->
                    "ドライブディスク強化判定ツール";
            case "start" ->
                    "開始";

            case "mainMenu" ->
                    "メインメニュー";
            case "language" ->
                    "言語";
            case "driveDiscSimulator" ->
                    "ドライブディスクシミュレーター";
            case "history" ->
                    "履歴";
            case "howToUse" ->
                    "使い方";
            case "back" ->
                    "戻る";
            case "exit" ->
                    "終了";

            case "howToUseInstructions" ->
                    "1. ドライブディスクの現在のサブステータスを選択します。\n"
                            + "2. サブステータスが3つの場合は、1つの枠で「なし」を選択します。\n"
                            + "3. 有効ステータスを選択します。\n"
                            + "4. 各有効ステータスに必要な強化回数を選択します。\n"
                            + "5. シミュレーション回数を選択します。\n"
                            + "6. 「シミュレーション実行」を押します。\n"
                            + "7. モンテカルロ法で成功確率を計算します。";

            case "currentLevel" ->
                    "現在のレベル";
            case "remainingUpgrades" ->
                    "残り強化回数";
            case "subStat" ->
                    "サブステータス";
            case "subStat1" ->
                    "サブステータス1";
            case "subStat2" ->
                    "サブステータス2";
            case "subStat3" ->
                    "サブステータス3";
            case "subStat4" ->
                    "サブステータス4";
            case "usefulStat1" ->
                    "有効ステータス1";
            case "usefulStat2" ->
                    "有効ステータス2";
            case "value" ->
                    "現在値";
            case "required" ->
                    "必要回数";
            case "simulationCount" ->
                    "シミュレーション回数";
            case "simulationResult" ->
                    "シミュレーション結果";
            case "actions" ->
                    "操作";
            case "runSimulation" ->
                    "シミュレーション実行";
            case "copyResult" ->
                    "結果をコピー";
            case "saveResult" ->
                    "結果を保存";
            case "resultPlaceholder" ->
                    "結果はここに表示されます。";

            case "historyTitle" ->
                    "シミュレーション履歴";
            case "selectSavedResult" ->
                    "保存した結果を選択してください。";
            case "noSavedResults" ->
                    "保存された結果はありません。";

            case "onlyOneNone" ->
                    "「なし」は1つまでです。ドライブディスクには3つ以上のサブステータスが必要です。";
            case "duplicateSubStats" ->
                    "サブステータスを重複して選択できません。";
            case "duplicateUsefulStats" ->
                    "有効ステータスを同じにできません。";
            case "enterNumbers" ->
                    "数値を入力してください。";
            case "positiveValues" ->
                    "サブステータスの値は0より大きくしてください。";
            case "atLeastOneRequired" ->
                    "必要回数を1回以上選択してください。";
            case "requiredExceedsUpgrades" ->
                    "必要回数の合計は、使用可能な強化回数を超えられません。";
            case "simulationCountPositive" ->
                    "シミュレーション回数は0より大きくしてください。";
            case "resultCopied" ->
                    "結果をクリップボードにコピーしました。";
            case "resultSaved" ->
                    "結果を保存しました。";
            case "saveFailed" ->
                    "結果を保存できませんでした。";

            case "simulationSummary" ->
                    "シミュレーション概要";
            case "modeThreeStats" ->
                    "モード：サブステータス3つ。4つ目をランダムに生成しました。";
            case "modeFourStats" ->
                    "モード：サブステータス4つ。強化のみをシミュレーションしました。";
            case "totalSimulations" ->
                    "総シミュレーション回数";
            case "usefulStats" ->
                    "有効ステータス";
            case "requiredHits" ->
                    "必要強化回数";
            case "success" ->
                    "成功回数";
            case "chance" ->
                    "成功確率";
            case "averageUsefulHits" ->
                    "有効強化の平均回数";
            case "recommendation" ->
                    "おすすめ";
            case "goodCandidate" ->
                    "強化候補として良い";
            case "risky" ->
                    "リスクあり";
            case "notRecommended" ->
                    "おすすめしない";

            case "generatedMissingStatChances" ->
                    "生成された4つ目のステータス";
            case "times" ->
                    "回";
            case "usefulGeneratedStatChance" ->
                    "有効ステータス生成確率";
            case "mostGeneratedMissingStat" ->
                    "最も多く生成されたステータス";

            case "averageUpgradeCounts" ->
                    "平均強化回数";
            case "expectedAverageValues" ->
                    "期待平均値";
            case "generatedStat" ->
                    "生成ステータス";
            case "current" ->
                    "現在値";
            case "averageProcs" ->
                    "平均強化回数";
            case "expected" ->
                    "期待値";

            default -> key;
        };
    }
}
package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final Path DATABASE_DIRECTORY =
            Paths.get(System.getProperty("user.home"), "ZZZDriveDiscSimulator");

    private static final Path DATABASE_FILE =
            DATABASE_DIRECTORY.resolve("zzz_simulator.db");

    private static final String DATABASE_URL =
            "jdbc:sqlite:" + DATABASE_FILE.toAbsolutePath();

    public static void initializeDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS simulation_results (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    current_level INTEGER NOT NULL,
                    sub_stat_1 TEXT NOT NULL,
                    sub_stat_2 TEXT NOT NULL,
                    sub_stat_3 TEXT NOT NULL,
                    sub_stat_4 TEXT NOT NULL,
                    useful_stat_1 TEXT NOT NULL,
                    useful_stat_2 TEXT NOT NULL,
                    required_hits INTEGER NOT NULL,
                    simulation_count INTEGER NOT NULL,
                    chance REAL NOT NULL,
                    recommendation TEXT NOT NULL,
                    result_text TEXT NOT NULL
                )
                """;

        try {
            Files.createDirectories(DATABASE_DIRECTORY);

            try (
                    Connection connection =
                            DriverManager.getConnection(DATABASE_URL);
                    Statement statement = connection.createStatement()
            ) {
                statement.execute(sql);
                System.out.println("Database initialized: " + DATABASE_FILE);
            }
        } catch (IOException | SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }

    }
    public static boolean saveResult(
            int currentLevel,
            String subStat1,
            String subStat2,
            String subStat3,
            String subStat4,
            String usefulStat1,
            String usefulStat2,
            int requiredHits,
            int simulationCount,
            double chance,
            String recommendation,
            String resultText
    ) {
        String sql = """
            INSERT INTO simulation_results (
                current_level,
                sub_stat_1,
                sub_stat_2,
                sub_stat_3,
                sub_stat_4,
                useful_stat_1,
                useful_stat_2,
                required_hits,
                simulation_count,
                chance,
                recommendation,
                result_text
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection =
                        DriverManager.getConnection(DATABASE_URL);
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, currentLevel);
            statement.setString(2, subStat1);
            statement.setString(3, subStat2);
            statement.setString(4, subStat3);
            statement.setString(5, subStat4);
            statement.setString(6, usefulStat1);
            statement.setString(7, usefulStat2);
            statement.setInt(8, requiredHits);
            statement.setInt(9, simulationCount);
            statement.setDouble(10, chance);
            statement.setString(11, recommendation);
            statement.setString(12, resultText);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(
                    "Result saving error: " + e.getMessage()
            );
            return false;
        }
    }
    public static List<SimulationResult> getAllResults() {
        List<SimulationResult> results = new ArrayList<>();

        String sql = """
            SELECT
                id,
                created_at,
                current_level,
                chance,
                recommendation,
                result_text
            FROM simulation_results
            ORDER BY id DESC
            """;

        try (
                Connection connection =
                        DriverManager.getConnection(DATABASE_URL);
                Statement statement =
                        connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                SimulationResult result = new SimulationResult(
                        resultSet.getInt("id"),
                        resultSet.getString("created_at"),
                        resultSet.getInt("current_level"),
                        resultSet.getDouble("chance"),
                        resultSet.getString("recommendation"),
                        resultSet.getString("result_text")
                );

                results.add(result);
            }
        } catch (SQLException e) {
            System.out.println(
                    "History loading error: " + e.getMessage()
            );
        }

        return results;
    }
}
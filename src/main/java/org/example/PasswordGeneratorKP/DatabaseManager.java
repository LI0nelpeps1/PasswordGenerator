package org.example.PasswordGeneratorKP;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Класс для управления базой данных пользователей.
 */
public class DatabaseManager {

    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);

    // URL для подключения к базе данных SQLite
    private static final String DB_URL = "jdbc:sqlite:your_database.db";

    /**
     * Конструктор класса DatabaseManager.
     * Создает таблицу пользователей в базе данных, если она еще не существует.
     */
    public DatabaseManager() {
        logger.info("DatabaseManager инициализирован");
        createUsersTable();
    }

    /**
     * Создает таблицу пользователей в базе данных, если она еще не существует.
     */
    private void createUsersTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "login TEXT PRIMARY KEY," +
                "password TEXT NOT NULL);";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
            logger.info("Таблица пользователей создана или уже существует.");
        } catch (SQLException e) {
            logger.error("Ошибка создания таблицы пользователей: {}", e.getMessage());
        }
    }

    /**
     * Получает пароль из базы данных по логину.
     *
     * @param login логин пользователя.
     * @return пароль, если он найден, или null, если пользователя с таким логином нет.
     */
    public String getPasswordByLogin(String login) {
        String query = "SELECT password FROM users WHERE login = ?;";
        String password = null;
        logger.info("Получение пароля для логиина: {}", login);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                password = rs.getString("password");
                logger.info("Пароль для логина получен: {}", login);
            } else {
                logger.warn("Не найден пароль для логина: {}", login);
            }
        } catch (SQLException e) {
            logger.error("Ошибка получения пароля: {}", e.getMessage());
        }
        return password;
    }

    /**
     * Добавляет логин и пароль в базу данных.
     *
     * @param login    логин пользователя.
     * @param password пароль пользователя.
     */
    public void addPasswordToDatabase(String login, String password) {
        String query = "INSERT INTO users (login, password) VALUES (?, ?);";
        logger.info("Добавление пароль для логина: {}", login);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            logger.info("Добавлен пароль для логина: {}", login);
        } catch (SQLException e) {
            logger.error("Ошибка добавления пароля для входа {}: {}", login, e.getMessage());
        }
    }
}

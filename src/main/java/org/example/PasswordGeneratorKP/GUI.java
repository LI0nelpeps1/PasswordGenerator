package org.example.PasswordGeneratorKP;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для графического интерфейса приложения.
 * Он предоставляет функционал для ввода параметров, генерации паролей и взаимодействия с пользователем.
 */
public class GUI extends Application {

    private static final Logger logger = LogManager.getLogger(GUI.class);

    private PasswordGenerator passwordGenerator = new PasswordGenerator();
    private long ttime;
    private String psswrd;

    private TextField lengthField;
    private TextField loginField;
    private TextArea resultArea;
    private CheckBox useUppercaseCheckBox;
    private CheckBox useDigitsCheckBox;
    private CheckBox useSpecialCharsCheckBox;

    /**
     * Метод main является точкой входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Метод start инициализирует и отображает графический интерфейс приложения.
     *
     * @param primaryStage основное окно приложения
     */
    @Override
    public void start(Stage primaryStage) {
        logger.info("Запуск GUI приложения.");
        primaryStage.setTitle("Генератор паролей");

        // Настройка макета
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Поле ввода длины пароля
        Label LF = new Label("Длина пароля:");
        LF.setStyle("-fx-text-fill: #FFFFFF;");
        grid.add(LF, 0, 0);
        lengthField = new TextField();
        grid.add(lengthField, 1, 0);

        // Поле ввода логина
        Label LGF = new Label("Логин:");
        LGF.setStyle("-fx-text-fill: #FFFFFF;");
        grid.add(LGF, 0, 1);

        loginField = new TextField();
        grid.add(loginField, 1, 1);

        // Чекбоксы для выбора набора символов
        useUppercaseCheckBox = new CheckBox("Включить заглавные буквы (A-Z)");
        useUppercaseCheckBox.setStyle("-fx-text-fill: #FFFFFF;");
        useDigitsCheckBox = new CheckBox("Включить цифры (0-9)");
        useDigitsCheckBox.setStyle("-fx-text-fill: #FFFFFF;");
        useSpecialCharsCheckBox = new CheckBox("Включить специальные символы (!@#$%)");
        useSpecialCharsCheckBox.setStyle("-fx-text-fill: #FFFFFF;");

        grid.add(useUppercaseCheckBox, 0, 2, 2, 1);
        grid.add(useDigitsCheckBox, 0, 3, 2, 1);
        grid.add(useSpecialCharsCheckBox, 0, 4, 2, 1);

        // Кнопки
        Button generateButton = new Button("Сгенерировать пароль");
        Button timeButton = new Button("Посчитать время генерации");
        Button checkButton = new Button("Проверить пароль");
        Button addButton = new Button("Добавить пароль");

        grid.add(generateButton, 0, 5);
        grid.add(timeButton, 1, 5);
        grid.add(checkButton, 0, 6);
        grid.add(addButton, 1, 6);

        // Область для отображения результатов
        resultArea = new TextArea();
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(100);
        resultArea.setEditable(false);

        VBox vbox = new VBox(10, grid, resultArea);
        Stop[] stops = new Stop[] { new Stop(0, Color.LIGHTCORAL), new Stop(1, Color.DARKCYAN) };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        vbox.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        String buttonStyle = "-fx-background-color: #2E8B57; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;";
        vbox.setPadding(new Insets(10));

        // Настройка действий для каждой кнопки
        generateButton.setOnAction(e -> generatePasswordAction());
        timeButton.setOnAction(e -> calculateTimeAction());
        checkButton.setOnAction(e -> checkPasswordAction());
        addButton.setOnAction(e -> addPasswordAction());

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Обработчик для кнопки "Сгенерировать пароль".
     * Выполняет генерацию пароля на основе введённых пользователем параметров.
     */
    private void generatePasswordAction() {
        logger.info("Нажата кнопка генерации пароля.");
        try {
            int length = Integer.parseInt(lengthField.getText());
            logger.debug("Введённая длина пароля: {}", length);

            boolean useUppercase = useUppercaseCheckBox.isSelected();
            boolean useDigits = useDigitsCheckBox.isSelected();
            boolean useSpecialChars = useSpecialCharsCheckBox.isSelected();

            int requiredLength = 1;
            if (useUppercase) requiredLength++;
            if (useDigits) requiredLength++;
            if (useSpecialChars) requiredLength++;

            if (length <= 0) {
                throw new PasswordGeneratorException("Пароль не может быть нулевым.");
            }

            if (length < requiredLength) {
                throw new PasswordGeneratorException("Длина пароля должна быть не менее " + requiredLength + ".");
            }

            PasswordResult result = passwordGenerator.generatePassword(length, useUppercase, useDigits, useSpecialChars);
            psswrd = result.getPassword();
            ttime = result.getGenerationTime();

            logger.info("Пароль успешно сгенерирован.");
            resultArea.setText("Сгенерированный пароль: " + psswrd);
        } catch (NumberFormatException ex) {
            logger.error("Ошибка ввода длины пароля: {}", lengthField.getText(), ex);
            resultArea.setText("Введите корректную длину пароля.");
        } catch (PasswordGeneratorException ex) {
            logger.error("Ошибка при генерации пароля: {}", ex.getMessage(), ex);
            resultArea.setText(ex.getMessage());
        }
    }

    /**
     * Обработчик для кнопки "Посчитать время генерации".
     * Показывает время, затраченное на генерацию последнего пароля.
     */
    private void calculateTimeAction() {
        logger.info("Нажата кнопка расчёта времени генерации.");
        try {
            int length = Integer.parseInt(lengthField.getText());
            logger.debug("Длина пароля для расчёта времени: {}", length);

            resultArea.setText("Время генерации пароля длины " + length + ": " + ttime + " наносекунд.");
        } catch (NumberFormatException ex) {
            logger.error("Некорректный ввод для расчёта времени: {}", lengthField.getText(), ex);
            resultArea.setText("Введите корректную длину пароля.");
        }
    }

    /**
     * Обработчик для кнопки "Проверить пароль".
     * Выполняет поиск пароля по логину в базе данных.
     */
    private void checkPasswordAction() {
        logger.info("Нажата кнопка проверки пароля.");
        String login = loginField.getText();

        if (login.isEmpty()) {
            logger.warn("Поле логина пустое.");
            resultArea.setText("Введите логин.");
            return;
        }

        String password = passwordGenerator.getPasswordByLogin(login);
        if (password != null) {
            logger.info("Пароль найден для логина: {}", login);
            resultArea.setText("Пароль для логина '" + login + "': " + password);
        } else {
            logger.warn("Пароль не найден для логина: {}", login);
            resultArea.setText("Пароль не найден для логина '" + login + "'.");
        }
    }

    /**
     * Обработчик для кнопки "Добавить пароль".
     * Добавляет сгенерированный пароль в базу данных для указанного логина.
     */
    private void addPasswordAction() {
        logger.info("Нажата кнопка добавления пароля.");
        String login = loginField.getText();

        if (login.isEmpty() || psswrd == null || psswrd.isEmpty()) {
            logger.warn("Логин или пароль пустой. Логин: {}, Пароль: {}", login, psswrd);
            resultArea.setText("Введите логин и сгенерируйте пароль.");
            return;
        } else if (passwordGenerator.getPasswordByLogin(login) != null) {
            logger.warn("Логин '{}' уже используется.", login);
            resultArea.setText("Этот логин уже используется.");
            return;
        }

        passwordGenerator.addPasswordToDatabase(login, psswrd);
        logger.info("Пароль успешно добавлен для логина: {}", login);
        resultArea.setText("Пароль успешно добавлен для логина: " + login);
    }
}

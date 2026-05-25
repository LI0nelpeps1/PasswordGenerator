package org.example.PasswordGeneratorKP;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для генерации паролей.
 */
public class PasswordGenerator extends DatabaseManager {

    private static final Logger logger = LogManager.getLogger(PasswordGenerator.class);

    // Наборы символов для генерации паролей
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyzйцукеннгшщзхъёфывапролджэячсмитьбю";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZЙЦУКЕННГШЩЗХЪЁФЫВАПРОЛДЖЭЯЧСМТЬБЮ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+<>?";

    // Генератор случайных чисел
    private SecureRandom random = new SecureRandom();

    /**
     * Генерирует случайный пароль заданной длины с учетом указанных параметров.
     *
     * @param length          длина пароля.
     * @param useUppercase    использовать ли заглавные буквы.
     * @param useDigits       использовать ли цифры.
     * @param useSpecialChars использовать ли специальные символы.
     * @return объект {@link PasswordResult}, содержащий сгенерированный пароль и время генерации.
     * @throws PasswordGeneratorException если длина пароля меньше 1.
     */
    public PasswordResult generatePassword(int length, boolean useUppercase, boolean useDigits, boolean useSpecialChars) throws PasswordGeneratorException {
        long startTime = System.nanoTime();
        logger.info("Генерация пароля длиной: {}, верхний регистр: {}, цифры: {}, специальные символы: {}", length, useUppercase, useDigits, useSpecialChars);

        if (length < 1) {
            logger.error("Длина пароля должна быть не менее 1.");
            throw new PasswordGeneratorException("Длина пароля должна быть не менее 1.");
        }
        if (length > 10000000) {
            throw new PasswordGeneratorException("Длина пароля слишком велика.");
        }

        StringBuilder password = new StringBuilder();
        StringBuilder charset = new StringBuilder(LOWERCASE);

        // Добавление обязательных символов для указанных наборов
        if (useUppercase) {
            password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
            charset.append(UPPERCASE);
        }
        if (useDigits) {
            password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
            charset.append(DIGITS);
        }
        if (useSpecialChars) {
            password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
            charset.append(SPECIAL_CHARS);
        }

        // Заполнение оставшихся символов
        for (int i = password.length(); i < length; i++) {
            password.append(charset.charAt(random.nextInt(charset.length())));
        }

        // Перемешивание пароля
        String randomizedPassword = shuffleString(password.toString());
        long endTime = System.nanoTime();
        long generationTime = endTime - startTime;

        logger.info("Пароль сгенерирован за {} наносекунд: {}", generationTime, randomizedPassword);
        return new PasswordResult(randomizedPassword, generationTime);
    }

    /**
     * Перемешивает символы в строке.
     *
     * @param input исходная строка.
     * @return строка с перемешанными символами.
     */
    private String shuffleString(String input) {
        List<Character> characters = input.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(characters);
        StringBuilder shuffled = new StringBuilder();
        for (char c : characters) {
            shuffled.append(c);
        }
        return shuffled.toString();
    }
}

package org.example.PasswordGeneratorKP;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса {@link PasswordGenerator}.
 * Проверяются различные аспекты генерации пароля: длина, состав, валидация входных данных.
 */
public class PasswordGeneratorTest {

    /**
     * Логгер для записи результатов тестов.
     */
    private static final Logger logger = LogManager.getLogger(PasswordGeneratorTest.class);

    /**
     * Проверяет, что сгенерированный пароль имеет корректную длину.
     *
     * @throws PasswordGeneratorException если возникает ошибка при генерации пароля.
     */
    @Test
    public void testGeneratePasswordLength() throws PasswordGeneratorException {
        PasswordGenerator generator = new PasswordGenerator();
        PasswordResult result = generator.generatePassword(12, true, true, true);
        assertEquals(12, result.getPassword().length(), "Пароль должен быть 12 символов");

        logger.info("testGeneratePasswordLength: Корректная длина пароля.");
    }

    /**
     * Проверяет, что сгенерированный пароль содержит хотя бы одну заглавную букву.
     *
     * @throws PasswordGeneratorException если возникает ошибка при генерации пароля.
     */
    @Test
    public void testGeneratePasswordUppercaseIncluded() throws PasswordGeneratorException {
        PasswordGenerator generator = new PasswordGenerator();
        PasswordResult result = generator.generatePassword(12, true, false, false);
        assertTrue(result.getPassword().matches(".*[A-Z].*"), "Пароль должен содержать как минимум 1 символ верхнего регистра");

        logger.info("testGeneratePasswordUppercaseIncluded: Пароль содержит символ верхнего регистра.");
    }

    /**
     * Проверяет, что сгенерированный пароль содержит хотя бы одну цифру.
     *
     * @throws PasswordGeneratorException если возникает ошибка при генерации пароля.
     */
    @Test
    public void testGeneratePasswordDigitsIncluded() throws PasswordGeneratorException {
        PasswordGenerator generator = new PasswordGenerator();
        PasswordResult result = generator.generatePassword(12, false, true, false);
        assertTrue(result.getPassword().matches(".*\\d.*"), "Пароль должен содержать хотя бы 1 цифру");

        logger.info("testGeneratePasswordDigitsIncluded: Пароль содержит цифру.");
    }

    /**
     * Проверяет, что сгенерированный пароль содержит хотя бы один специальный символ.
     *
     * @throws PasswordGeneratorException если возникает ошибка при генерации пароля.
     */
    @Test
    public void testGeneratePasswordSpecialCharsIncluded() throws PasswordGeneratorException {
        PasswordGenerator generator = new PasswordGenerator();
        PasswordResult result = generator.generatePassword(12, false, false, true);
        assertTrue(result.getPassword().matches(".*[!@#$%^&*()\\-_=+<>?].*"), "Пароль должен содержать хотя бы 1 специальный символ");

        logger.info("testGeneratePasswordSpecialCharsIncluded: Пароль содержит специальный символ.");
    }

    /**
     * Проверяет, что генерация пароля с длиной 0 выбрасывает {@link PasswordGeneratorException}.
     */
    @Test
    public void testInvalidPasswordLength() {
        PasswordGenerator generator = new PasswordGenerator();

        PasswordGeneratorException exception = assertThrows(PasswordGeneratorException.class, () -> {
            generator.generatePassword(0, true, true, true);
        });

        assertEquals("Длина пароля должна быть не менее 1.", exception.getMessage(), "Сообщение об исключении должно быть таким: Длина пароля должна быть не менее 1.");

        logger.info("testInvalidPasswordLength: Исключение, выданное с сообщением: " + exception.getMessage());
    }
}

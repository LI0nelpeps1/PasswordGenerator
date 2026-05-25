package org.example.PasswordGeneratorKP;

/**
 * Исключение, которое возникает при ошибках в генерации пароля.
 * Используется для обработки некорректных входных данных или ошибок в процессе генерации.
 */
public class PasswordGeneratorException extends Exception {

    /**
     * Конструктор, создающий исключение с заданным сообщением.
     *
     * @param message сообщение об ошибке, описывающее причину исключения.
     */
    public PasswordGeneratorException(String message) {
        super(message);
    }
}

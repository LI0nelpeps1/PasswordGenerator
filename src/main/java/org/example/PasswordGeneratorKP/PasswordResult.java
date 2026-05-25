package org.example.PasswordGeneratorKP;

/**
 * Класс для хранения результата генерации пароля.
 * Наследуется от {@link Result} для хранения времени выполнения операции.
 */
public class PasswordResult extends Result {

    /**
     * Сгенерированный пароль.
     */
    private final String password;

    /**
     * Конструктор для создания объекта результата генерации пароля.
     *
     * @param password        сгенерированный пароль.
     * @param generationTime  время выполнения операции генерации пароля в наносекундах.
     */
    public PasswordResult(String password, long generationTime) {
        super(generationTime);
        this.password = password;
    }

    /**
     * Возвращает сгенерированный пароль.
     *
     * @return сгенерированный пароль.
     */
    public String getPassword() {
        return password;
    }
}

package org.example.PasswordGeneratorKP;

/**
 * Базовый класс для хранения результатов выполнения операций.
 */
public class Result {

    /**
     * Время выполнения операции (например, генерации пароля) в наносекундах.
     */
    private final long generationTime;

    /**
     * Конструктор для создания объекта результата.
     *
     * @param generationTime время выполнения операции в наносекундах.
     */
    public Result(long generationTime) {
        this.generationTime = generationTime;
    }

    /**
     * Возвращает время выполнения операции.
     *
     * @return время выполнения операции в наносекундах.
     */
    public long getGenerationTime() {
        return generationTime;
    }
}

package exceptions;

public class ManagerSaveException extends RuntimeException { // Создаём исключение для проверки записи задач в файл

    public ManagerSaveException(final Throwable cause) {
        super(cause);
    }

}
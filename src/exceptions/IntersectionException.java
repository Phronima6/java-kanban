package exceptions;

// Создаём исключение для проверки наложения временных интервалов задач
public class IntersectionException extends Exception {

    public IntersectionException(final String message) {
        super(message);
    }

}
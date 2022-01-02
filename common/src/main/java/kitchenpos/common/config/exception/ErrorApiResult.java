package kitchenpos.common.config.exception;

public class ErrorApiResult<T> {
    private int statusCode;
    private String message;
    private T error;

    public ErrorApiResult(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorApiResult(int statusCode, String message, T error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }


    public static ErrorApiResult of(int statusCode, String message) {
        return new ErrorApiResult(statusCode, message);
    }

    public static <T> ErrorApiResult of(int statusCode, String message, T error) {
        return new ErrorApiResult(statusCode, message, error);
    }
}

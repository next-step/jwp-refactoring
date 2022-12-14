package kitchenpos.exception;

public enum EntityNotFoundExceptionCode {
    NOT_FOUND_BY_ID(400, "the entity not found by id.");

    private static final String TITLE = "[ERROR] ";

    private int code;
    private String message;

    EntityNotFoundExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }

    public int getCode() {
        return code;
    }
}

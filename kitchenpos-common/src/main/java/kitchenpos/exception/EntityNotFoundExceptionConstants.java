package kitchenpos.exception;

public enum EntityNotFoundExceptionConstants {
    NOT_FOUND_BY_ID("해당 id에 해당하는 엔티티는 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private String message;

    EntityNotFoundExceptionConstants(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return TITLE + message;
    }

}

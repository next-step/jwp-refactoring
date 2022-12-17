package kitchenpos.exception;

public enum MenuGroupErrorMessage {
    REQUIRED_NAME("이름은 필수 필드입니다."),
    NOT_FOUND_BY_ID("ID로 메뉴 그룹을 찾을 수 없습니다.");

    private static final String TITLE = "[ERROR] ";

    private final String message;

    MenuGroupErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}

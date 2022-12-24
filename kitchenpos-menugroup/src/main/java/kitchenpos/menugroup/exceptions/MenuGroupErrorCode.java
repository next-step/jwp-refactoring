package kitchenpos.menugroup.exceptions;

public enum MenuGroupErrorCode {
    MENU_GROUP_NOT_FOUND("메뉴 그룹 을 찾을 수 없습니다");


    private final String message;

    MenuGroupErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

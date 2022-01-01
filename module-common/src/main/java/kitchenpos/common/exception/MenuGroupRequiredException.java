package kitchenpos.common.exception;

public class MenuGroupRequiredException extends RuntimeException {
    public static final String MENU_GROUP_REQUIRED_EXCEPTION = "메뉴 그룹은 존재해야 합니다.";

    public MenuGroupRequiredException() {
        super(MENU_GROUP_REQUIRED_EXCEPTION);
    }
}

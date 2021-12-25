package kitchenpos.exception;

public class MenuGroupNotFoundException extends RuntimeException {
    public static final String MENU_GROUP_NOT_FOUND = "메뉴 그룹을 찾을 수 없습니다.";

    public MenuGroupNotFoundException() {
        super(MENU_GROUP_NOT_FOUND);
    }
}

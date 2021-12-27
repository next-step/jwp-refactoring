package kitchenpos.menugroup.exception;

public class MenuGroupNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_NOT_EXIST_MENU_GROUP = "메뉴 그룹이 존재하지 않습니다.";

    public MenuGroupNotFoundException() {
        super(ERROR_MESSAGE_NOT_EXIST_MENU_GROUP);
    }
}

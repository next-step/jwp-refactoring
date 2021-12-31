package kitchenpos.common.exceptions;

public class EmptyMenuGroupException extends CustomException{
    public static final String EMPTY_MENU_GROUP_MESSAGE = "메뉴 그룹은 필수로 존재해야 합니다.";

    public EmptyMenuGroupException() {
        super(EMPTY_MENU_GROUP_MESSAGE);
    }
}

package kitchenpos.menugroup.exception;

public class NoSuchMenuGroupException extends IllegalArgumentException {

    private static final long serialVersionUID = 540236956800849912L;
    private static final String NO_MENU_GROUP = "존재하지 않는 메뉴 그룹입니다.";

    public NoSuchMenuGroupException() {
        super(NO_MENU_GROUP);
    }

    public NoSuchMenuGroupException(String message) {
        super(message);
    }

}

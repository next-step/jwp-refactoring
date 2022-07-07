package kitchenpos.exception;

public class NotFoundMenuGroupException extends RuntimeException {
    public static final NotFoundMenuGroupException NOT_FOUND_MENU_GROUP_EXCEPTION = new NotFoundMenuGroupException(
            "존재하지 않는 메뉴그룹 입니다.");

    public NotFoundMenuGroupException(String message) {
        super(message);
    }
}

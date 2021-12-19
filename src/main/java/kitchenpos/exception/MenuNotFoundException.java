package kitchenpos.exception;

public class MenuNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "메뉴를 찾을 수 없습니다 : %d";

    public MenuNotFoundException(Long menuId) {
        super(String.format(DEFAULT_MESSAGE, menuId));
    }
}

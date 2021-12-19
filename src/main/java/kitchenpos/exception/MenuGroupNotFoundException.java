package kitchenpos.exception;

public class MenuGroupNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "메뉴 그룹을 찾을 수 없습니다 : %d";

    public MenuGroupNotFoundException(Long menuGroupId) {
        super(String.format(DEFAULT_MESSAGE, menuGroupId));
    }
}

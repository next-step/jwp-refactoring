package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class MenuFindException extends BadRequestException {
    public MenuFindException() {
        super(MENU_NOT_FOUND);
    }
}

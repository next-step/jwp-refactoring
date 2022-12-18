package kitchenpos.order.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class MenuFindException extends BadRequestException {
    public MenuFindException() {
        super(MENU_NOT_FOUND);
    }
}

package kitchenpos.order.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class MenuFindException extends BadRequestException {
    public MenuFindException() {
        super(MENU_NOT_FOUND);
    }
}

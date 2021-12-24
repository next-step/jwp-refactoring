package kitchenpos.menugroup.exception;

import kitchenpos.common.NotFoundException;

public class NotFoundMenuGroupException extends NotFoundException {
    public NotFoundMenuGroupException(String message) {
        super(message);
    }
}

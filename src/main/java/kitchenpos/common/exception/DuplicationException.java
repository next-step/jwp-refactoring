package kitchenpos.common.exception;

import kitchenpos.common.exception.ServiceException;

public class DuplicationException extends ServiceException {

    public DuplicationException(String message) {
        super(message);
    }

}

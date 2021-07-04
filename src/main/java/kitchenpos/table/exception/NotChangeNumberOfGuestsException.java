package kitchenpos.table.exception;

import java.io.Serializable;

public class NotChangeNumberOfGuestsException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -7135329073200707181L;

    public NotChangeNumberOfGuestsException(String message) {
        super(message);
    }
}

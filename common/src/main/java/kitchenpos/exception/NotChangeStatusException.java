package kitchenpos.exception;

import java.io.Serializable;

public class NotChangeStatusException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 528862018346792641L;

    public NotChangeStatusException(String message) {
        super(message);
    }
}

package kitchenpos.handler.exception;

import java.io.Serializable;

public class NotCreateOrderException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -3134322472177840186L;

    public NotCreateOrderException(String message) {
        super(message);
    }
}

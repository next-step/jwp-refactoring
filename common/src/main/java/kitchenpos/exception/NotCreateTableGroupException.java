package kitchenpos.exception;

import java.io.Serializable;

public class NotCreateTableGroupException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 3467942490089641099L;

    public NotCreateTableGroupException(String message) {
        super(message);
    }
}

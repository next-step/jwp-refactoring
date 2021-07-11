package kitchenpos.exception;

import java.io.Serializable;

public class NotFoundEntityException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 2574942015123553578L;

    public NotFoundEntityException() {
        super();
    }

    public NotFoundEntityException(String message) {
        super(message);
    }
}

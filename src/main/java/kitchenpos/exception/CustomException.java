package kitchenpos.exception;

import kitchenpos.dto.dto.ExceptionDTO;

public abstract class CustomException extends RuntimeException {

    private ExceptionDTO exceptionDTO;

    public CustomException(String classification, String message) {
        super(message);
        exceptionDTO = new ExceptionDTO(classification, message);
    }

    public ExceptionDTO getExceptionDTO() {
        return exceptionDTO;
    }
}

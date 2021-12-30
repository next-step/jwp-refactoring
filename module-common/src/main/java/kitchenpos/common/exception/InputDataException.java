package kitchenpos.common.exception;

public class InputDataException extends RuntimeException {
    private String errorMessage;
    private InputDataErrorCode inputDataErrorCode;

    public InputDataException(InputDataErrorCode inputDataErrorCode) {
        this(inputDataErrorCode, inputDataErrorCode.errorMessage());
    }

    public InputDataException(InputDataErrorCode inputDataErrorCode, String errorMessage) {
        super(errorMessage);
        this.inputDataErrorCode = inputDataErrorCode;
        this.errorMessage = errorMessage;
    }
}

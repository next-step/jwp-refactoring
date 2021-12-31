package kitchenpos.order.exceptions;

public class InputOrderDataException extends RuntimeException {

    private String errorMessage;
    private InputOrderDataErrorCode inputOrderDataErrorCode;

    public InputOrderDataException(InputOrderDataErrorCode inputOrderDataErrorCode) {
        this(inputOrderDataErrorCode, inputOrderDataErrorCode.errorMessage());
    }

    public InputOrderDataException(InputOrderDataErrorCode inputOrderDataErrorCode, String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.inputOrderDataErrorCode = inputOrderDataErrorCode;
    }
}

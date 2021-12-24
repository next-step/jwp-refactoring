package kitchenpos.order.exceptions;

public class InputOrderDateException extends RuntimeException {

    private String errorMessage;
    private InputOrderDataErrorCode inputOrderDataErrorCode;

    public InputOrderDateException(InputOrderDataErrorCode inputOrderDataErrorCode) {
        this(inputOrderDataErrorCode, inputOrderDataErrorCode.errorMessage());
    }

    public InputOrderDateException(InputOrderDataErrorCode inputOrderDataErrorCode, String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.inputOrderDataErrorCode = inputOrderDataErrorCode;
    }
}

package kitchenpos.order.exceptions;

public class InputOrderDateException extends RuntimeException {

    private String errorMessage;
    private InputOrderDataErrorCode inputOrderDataErrorCode;

    public InputOrderDateException(InputOrderDataErrorCode inputOrderDataErrorCode) {
        this(inputOrderDataErrorCode.errorMessage(), inputOrderDataErrorCode);
    }

    public InputOrderDateException(String errorMessage, InputOrderDataErrorCode inputOrderDataErrorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.inputOrderDataErrorCode = inputOrderDataErrorCode;
    }
}

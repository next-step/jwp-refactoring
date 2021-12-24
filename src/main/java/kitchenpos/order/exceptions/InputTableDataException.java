package kitchenpos.order.exceptions;

public class InputTableDataException extends RuntimeException {
    private String errorMessage;
    private InputTableDataErrorCode inputTableDataErrorCode;

    public InputTableDataException(InputTableDataErrorCode inputTableDataErrorCode) {
        this(inputTableDataErrorCode, inputTableDataErrorCode.errorMessage());
    }

    public InputTableDataException(InputTableDataErrorCode inputTableDataErrorCode, String errorMessage) {
        super(errorMessage);
        this.inputTableDataErrorCode = inputTableDataErrorCode;
        this.errorMessage = errorMessage;
    }
}

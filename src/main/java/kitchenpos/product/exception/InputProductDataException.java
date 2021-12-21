package kitchenpos.product.exception;

public class InputProductDataException extends RuntimeException {
    private String errorMessage;
    private InputProductDataErrorCode inputProductDataErrorCode;

    public InputProductDataException(InputProductDataErrorCode inputProductDataErrorCode) {
        this(inputProductDataErrorCode, inputProductDataErrorCode.errorMessage());
        this.inputProductDataErrorCode = inputProductDataErrorCode;
    }

    public InputProductDataException(InputProductDataErrorCode inputProductDataErrorCode, String errorMessage) {
        super(errorMessage);
        this.inputProductDataErrorCode = inputProductDataErrorCode;
        this.errorMessage = errorMessage;
    }
}

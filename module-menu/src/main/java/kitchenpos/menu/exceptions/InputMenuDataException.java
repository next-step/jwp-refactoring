package kitchenpos.menu.exceptions;

public class InputMenuDataException extends RuntimeException{
    private String errorMessage;
    private InputMenuDataErrorCode inputMenuDataErrorCode;

    public InputMenuDataException(InputMenuDataErrorCode inputMenuDataErrorCode) {
        this(inputMenuDataErrorCode, inputMenuDataErrorCode.errorMessage());
    }

    public InputMenuDataException(InputMenuDataErrorCode inputMenuDataErrorCode, String errorMessage) {
        super(errorMessage);
        this.inputMenuDataErrorCode = inputMenuDataErrorCode;
        this.errorMessage = errorMessage;
    }
}

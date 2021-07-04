package kitchenpos.common.error;

public class CustomException extends RuntimeException{
    public CustomException(ErrorInfo errorInfo) {
        super(errorInfo.message());
    }
}

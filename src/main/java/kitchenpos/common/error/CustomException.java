package kitchenpos.common.error;

public class CustomException extends RuntimeException{
    public CustomException() {
        super();
    }

    public CustomException(String errorInfo) {
        super(errorInfo);
    }
}

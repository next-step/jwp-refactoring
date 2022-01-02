package menu.exception;

public class NotCreatedProductException extends RuntimeException {
    public NotCreatedProductException() {
        super("상품이 생성되어 있지 않습니다.");
    }
}

package menu.exception;

import javax.persistence.EntityNotFoundException;

public class NotCreatedProductException extends EntityNotFoundException {
    public NotCreatedProductException() {
        super("상품이 생성되어 있지 않습니다.");
    }
}

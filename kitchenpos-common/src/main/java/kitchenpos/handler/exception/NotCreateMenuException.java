package kitchenpos.handler.exception;

import java.io.Serializable;

public class NotCreateMenuException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2344672487900342186L;

    public NotCreateMenuException() {
        super("메뉴의 가격은 메뉴 상품 가격의 합보다 같거나 작아야 합니다.");
    }
}

package kitchenpos.table.exception;

import java.io.Serializable;

public class ChangeEmptyException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 528862018346792641L;

    public ChangeEmptyException(String s) {
        super("주문 테이블 상태 변경 불가능: " + s);
    }
}

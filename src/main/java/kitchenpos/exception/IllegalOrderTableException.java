package kitchenpos.exception;

/**
 * packageName : kitchenpos.exception
 * fileName : IllegalOrderTableException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class IllegalOrderTableException extends RuntimeException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "테이블 정보가 올바르지 않습니다.";

    public IllegalOrderTableException() {
        super(message);
    }
}

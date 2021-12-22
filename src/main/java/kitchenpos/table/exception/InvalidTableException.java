package kitchenpos.table.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : InvalidTableException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class InvalidTableException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "테이블을 사용할 수 없습니다.";

    public InvalidTableException() {
        super(message);
    }
}

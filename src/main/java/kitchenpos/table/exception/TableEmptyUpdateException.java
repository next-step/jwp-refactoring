package kitchenpos.table.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : TableEmptyUpdateException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class TableEmptyUpdateException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "테이블 상태를 변경할 수 없습니다.";

    public TableEmptyUpdateException() {
        super(message);
    }
}

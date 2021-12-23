package kitchenpos.table.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : TableGuestNumberUpdateException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class TableGuestNumberUpdateException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "테이블 사용자 수를 변경할 수 없습니다.";

    public TableGuestNumberUpdateException() {
        super(message);
    }
}

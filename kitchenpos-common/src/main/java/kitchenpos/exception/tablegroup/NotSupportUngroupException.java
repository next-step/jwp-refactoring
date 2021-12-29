package kitchenpos.tablegroup.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : NotSupportUngroupException
 * author : haedoang
 * date : 2021/12/22
 * description :
 */
public class NotSupportUngroupException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "테이블을 분리할 수 없습니다.";

    public NotSupportUngroupException() {
        super(message);
    }
}

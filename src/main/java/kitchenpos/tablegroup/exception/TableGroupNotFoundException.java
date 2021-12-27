package kitchenpos.tablegroup.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : TableGroupNotFoundException
 * author : haedoang
 * date : 2021/12/22
 * description :
 */
public class TableGroupNotFoundException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "테이블 그룹이 존재하지 않습니다.";

    public TableGroupNotFoundException() {
        super(message);
    }
}

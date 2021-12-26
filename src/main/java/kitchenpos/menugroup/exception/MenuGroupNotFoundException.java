package kitchenpos.menugroup.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : MenuGroupNotExistException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class MenuGroupNotFoundException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "메뉴 그룹이 존재하지 않습니다.";

    public MenuGroupNotFoundException() {
        super(message);
    }
}

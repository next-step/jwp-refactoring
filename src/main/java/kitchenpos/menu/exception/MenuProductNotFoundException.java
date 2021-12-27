package kitchenpos.menu.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.menu.exception
 * fileName : MenuProductNotFoundException
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
public class MenuProductNotFoundException extends ServiceException {
    private static final Long serialVersionUID = 1L;

    public MenuProductNotFoundException(String message) {
        super(message);
    }
}

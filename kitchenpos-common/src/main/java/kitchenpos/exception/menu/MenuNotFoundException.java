package kitchenpos.menu.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : MenuNotFoundException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class MenuNotFoundException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "메뉴가 존재하지 않습니다.";

    public MenuNotFoundException() {
        super(message);
    }
}

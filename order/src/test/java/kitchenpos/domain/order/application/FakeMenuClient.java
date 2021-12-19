package kitchenpos.domain.order.application;

import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;

import java.util.List;

public class FakeMenuClient implements MenuClient {

    @Override
    public void validateMenuExist(List<Long> menuIds) {
        if (menuIds.size() != 1) {
            throw new BusinessException(ErrorCode.MENU_NOT_EXIST);
        }
    }
}

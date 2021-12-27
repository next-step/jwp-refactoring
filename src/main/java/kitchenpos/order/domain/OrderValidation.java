package kitchenpos.order.domain;

import kitchenpos.common.exception.Message;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderValidation {

    private MenuDao menuDao;

    public OrderValidation(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public void validSizeIsNotEquals(OrderRequest orderRequest) {
        final Long count = menuDao.countByIdIn(orderRequest.getMenuIds());
        if (orderRequest.getOrderItemSize() != count) {
            throw new IllegalArgumentException(Message.ORDER_SIZE_IS_NOT_EQUALS.getMessage());
        }
    }

}

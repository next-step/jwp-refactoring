package kitchenpos.order.domain;

import kitchenpos.common.exception.Message;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderValidation {

    private MenuDao menuDao;
    private OrderTableDao orderTableDao;
    private MenuService menuService;

    public OrderValidation(MenuDao menuDao, OrderTableDao orderTableDao,
        MenuService menuService) {
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
        this.menuService = menuService;
    }

    public void valid(OrderRequest orderRequest) {
        validSizeIsNotEquals(orderRequest);
        validIsExistOrderTable(orderRequest);
        validIsExistMenu(orderRequest);
    }

    private void validIsExistMenu(OrderRequest orderRequest) {
        orderRequest.toOrderItems()
            .stream()
            .forEach(m -> menuService.findById(m.getMenuId()));
    }

    private void validIsExistOrderTable(OrderRequest orderRequest) {
        orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(NoResultDataException::new);
    }

    public void validSizeIsNotEquals(OrderRequest orderRequest) {
        final Long count = menuDao.countByIdIn(orderRequest.toMenuIds());
        if (orderRequest.getOrderItemSize() != count) {
            throw new IllegalArgumentException(Message.ORDER_SIZE_IS_NOT_EQUALS.getMessage());
        }
    }

}

package kitchenpos.moduledomain.order;

import java.util.List;
import kitchenpos.moduledomain.common.exception.DomainMessage;
import kitchenpos.moduledomain.common.exception.NoResultDataException;
import kitchenpos.moduledomain.menu.MenuDao;
import kitchenpos.moduledomain.table.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderValidation {

    private MenuDao menuDao;
    private OrderTableDao orderTableDao;

    public OrderValidation(MenuDao menuDao, OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
    }

    private void validIsExistMenu(List<OrderLineItem> orderLineItems) {
        orderLineItems
            .stream()
            .forEach(m -> menuDao.findById(m.getMenuId()));
    }

    private void validIsExistOrderTable(Long orderTableId) {
        orderTableDao.findById(orderTableId)
            .orElseThrow(NoResultDataException::new);
    }

    public void validSizeIsNotEquals(List<Long> menuIds, int size) {
        final Long count = menuDao.countByIdIn(menuIds);
        if (size != count) {
            throw new IllegalArgumentException(DomainMessage.ORDER_SIZE_IS_NOT_EQUALS.getMessage());
        }
    }

}

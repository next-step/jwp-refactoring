package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.table.validator.TableOrderValidator;
import org.springframework.stereotype.Component;

@Component
public class TableOrderValidatorImpl implements TableOrderValidator {

    private final OrderDao orderDao;

    public TableOrderValidatorImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public boolean existsDinningOrder(Long tableId) {
        List<Order> orders = orderDao.findAllByOrderTableId(tableId);
        return hasDinningOrder(orders);
    }

    private boolean hasDinningOrder(List<Order> orders) {
        return orders.stream()
                .map(Order::isDinning)
                .findFirst()
                .isPresent();
    }
}

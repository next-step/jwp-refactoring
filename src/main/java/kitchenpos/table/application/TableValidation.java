package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import org.springframework.stereotype.Service;

@Service
public class TableValidation {
    private final OrderDao orderDao;

    public TableValidation(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void validInCookingOrMeal(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    public void validIsNotEqualsSize(OrderTables savedOrderTables,
        List<OrderTableRequest> orderTableRequests) {
        if (savedOrderTables.size() != orderTableRequests.size()) {
            throw new IllegalArgumentException();
        }
    }
}

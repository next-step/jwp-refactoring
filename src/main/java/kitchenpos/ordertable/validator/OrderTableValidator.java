package kitchenpos.ordertable.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUpdateEmpty(OrderTable orderTable) {
        validateHasOrderTable(orderTable);
        validateOrderStatus(orderTable);
    }

    private void validateHasOrderTable(OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_TABLE_GROUP.getMessage());
        }
    }

    private void validateOrderStatus(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        orders.forEach(Order::validateOrderStatusShouldComplete);
    }

    public void validateUpdateNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
        }
    }
}

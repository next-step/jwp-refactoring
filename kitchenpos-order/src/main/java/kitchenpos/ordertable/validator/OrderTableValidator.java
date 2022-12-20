package kitchenpos.ordertable.validator;

import java.util.List;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator implements OrderValidator {

    private final OrderRepository orderRepository;

    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validatorTable(OrderTable orderTable) {
        List<Order> orders = findAllOrderByOrderTableId(orderTable);
        validateNotCompleteOrders(orders);
    }

    private List<Order> findAllOrderByOrderTableId(OrderTable orderTable) {
        return orderRepository.findAllByOrderTableId(orderTable.getId());
    }

    @Override
    public void validatorOrder(OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        validateOrderTable(orderTable);
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_NOT_EMPTY.getErrorMessage());
        }
    }

    @Override
    public void validateNotCompleteOrders(List<Order> orders) {
        orders.forEach(Order::validateNotCompleteOrder);
    }
}

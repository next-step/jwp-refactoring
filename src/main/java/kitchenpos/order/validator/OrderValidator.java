package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validator(OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        validateOrderTable(orderTable);
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage()));
    }

    private void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.주문_테이블은_비어있으면_안됨.getErrorMessage());
        }
    }

    public void validateIfNotCompletionOrders(List<Order> orders) {
        orders.forEach(Order::validateIfNotCompletionOrder);
    }
}

package kitchenpos.validator.order.impl;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.validator.order.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableExistAndEmptyValidator implements OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderTableExistAndEmptyValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "주문 등록시, 등록된 주문 테이블만 지정할 수 있습니다 [orderTableId:" + order.getOrderTableId() + "]"));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 등록시, 주문 테이블은 비어있으면 안됩니다");
        }
    }
}

package kitchenpos.core.ordertable.validator;

import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.exception.CanNotOrderException;
import kitchenpos.core.ordertable.domain.OrderTable;
import kitchenpos.core.ordertable.domain.OrderTableRepository;
import kitchenpos.core.order.validator.OrderCreateValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableOrderCreateValidator implements OrderCreateValidator {
    private static final String EMPTY_ERROR_MESSAGE = "테이블이 비어있을 경우 주문을 할 수 없습니다.";
    private static final String NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE = "주문 테이블이 존재하지 않아 주문을 할 수 없습니다.";
    private final OrderTableRepository orderTableRepository;

    public OrderTableOrderCreateValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> {
                    throw new CanNotOrderException(NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE);
                });
        if (orderTable.isEmpty()) {
            throw new CanNotOrderException(EMPTY_ERROR_MESSAGE);
        }
    }
}

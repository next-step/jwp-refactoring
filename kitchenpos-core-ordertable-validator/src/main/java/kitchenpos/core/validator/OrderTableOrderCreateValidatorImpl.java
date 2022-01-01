package kitchenpos.core.validator;

import kitchenpos.core.domain.Order;
import kitchenpos.core.domain.OrderTable;
import kitchenpos.core.domain.OrderTableRepository;
import kitchenpos.core.exception.CanNotOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableOrderCreateValidatorImpl implements OrderCreateValidator {
    private static final String EMPTY_ERROR_MESSAGE = "테이블이 비어있을 경우 주문을 할 수 없습니다.";
    private static final String NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE = "주문 테이블이 존재하지 않아 주문을 할 수 없습니다.";
    private final OrderTableRepository orderTableRepository;

    public OrderTableOrderCreateValidatorImpl(OrderTableRepository orderTableRepository) {
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

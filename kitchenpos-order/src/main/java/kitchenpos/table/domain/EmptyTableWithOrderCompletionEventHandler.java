package kitchenpos.table.domain;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderCompletionEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmptyTableWithOrderCompletionEventHandler {
    private static final String ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT = "존재하지 않는 주문 테이블입니다. ID : %d";

    private final OrderTableRepository orderTableRepository;

    public EmptyTableWithOrderCompletionEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(OrderCompletionEvent event) {
        OrderTable orderTable = findById(event.orderTableId());
        orderTable.validateGrouped();
        orderTable.changeEmpty();
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT, orderTableId)));
    }
}

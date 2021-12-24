package kitchenpos.table.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.orders.event.ValidateEmptyTableEvent;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.table.exception.EmptyOrderTableException;

@Component
public class ValidateEmptyTableHandler {
    private final OrderTableRepository orderTableRepository;

    public ValidateEmptyTableHandler(
        OrderTableRepository orderTableRepository
    ) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(ValidateEmptyTableEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId()).orElseThrow(NotFoundOrderException::new);

        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException("주문테이블이 빈테이블입니다.");
        }
    }
}

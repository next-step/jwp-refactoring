package kitchenpos.domain.table.application;

import kitchenpos.domain.order.domain.EmptyTableValidatedEvent;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.OrderTableRepository;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmptyTableValidatedHandler  {

    private final OrderTableRepository orderTableRepository;

    public EmptyTableValidatedHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(EmptyTableValidatedEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_TABLE_NOT_FOUND));

        if (orderTable.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_TABLE);
        }
    }
}

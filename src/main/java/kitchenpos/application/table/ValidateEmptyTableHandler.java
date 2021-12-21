package kitchenpos.application.table;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.event.orders.ValidateEmptyTableEvent;

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
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId()).orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

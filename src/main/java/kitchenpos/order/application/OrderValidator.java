package kitchenpos.order.application;

import kitchenpos.exception.NotExistException;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public Long notEmptyOrderTableId(OrderRequest orderRequest) {
        final OrderTable persistOrderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(NotExistException::new);
        validateOrderTable(persistOrderTable);
        return persistOrderTable.getId();
    }

    private void validateOrderTable(OrderTable orderTable) {
        orderTable.validateEmpty();
    }
}

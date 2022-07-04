package kitchenpos.order.application;

import javax.persistence.EntityNotFoundException;
import kitchenpos.order.dto.OrderRequestDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {

    private static final String EMPTY_TABLE_EXCEPTION_MESSAGE = "orderTable must be notEmpty";

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void checkCreatable(OrderRequestDto request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId()).orElseThrow(EntityNotFoundException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_TABLE_EXCEPTION_MESSAGE);
        }
    }
}

package order.application;

import javax.persistence.EntityNotFoundException;
import order.dto.OrderRequestDto;
import org.springframework.stereotype.Service;
import table.domain.OrderTable;
import table.repository.OrderTableRepository;

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

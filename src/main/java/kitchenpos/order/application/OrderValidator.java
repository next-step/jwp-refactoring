package kitchenpos.order.application;

import kitchenpos.order.dto.OrderRequestDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void checkCreatable(OrderRequestDto request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId()).orElseThrow(EntityNotFoundException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

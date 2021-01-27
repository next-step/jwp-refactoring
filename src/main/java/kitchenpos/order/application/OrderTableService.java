package kitchenpos.order.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}

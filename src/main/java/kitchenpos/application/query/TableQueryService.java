package kitchenpos.application.query;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableQueryService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableQueryService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }
}

package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundOrderTableException(orderTableId));
    }

    public List<OrderTable> findOrderTables(List<Long> orderTables) {
        return orderTableRepository.findAllByIdIn(orderTables);
    }
}

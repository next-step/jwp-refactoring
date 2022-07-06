package kitchenpos.table.application;

import java.util.List;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(NotFoundOrderTableException::new);
    }

    public List<OrderTable> findAllOrderTablesByIdIn(List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }
}

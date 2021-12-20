package kichenpos.table.table.domain;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableQueryService {

    private final OrderTableRepository orderTableRepository;

    public TableQueryService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public List<OrderTable> findAll() {
        return orderTableRepository.findAll();
    }
}

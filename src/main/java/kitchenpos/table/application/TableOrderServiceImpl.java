package kitchenpos.table.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
public class TableOrderServiceImpl implements TableOrderService {
    private final OrderTableRepository orderTableRepository;

    public TableOrderServiceImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public Optional<OrderTable> findTableById(Long orderTableId) {
        return this.orderTableRepository.findById(orderTableId);
    }
}

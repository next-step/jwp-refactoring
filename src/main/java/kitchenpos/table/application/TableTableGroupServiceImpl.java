package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
public class TableTableGroupServiceImpl implements TableTableGroupService{
    private final OrderTableRepository orderTableRepository;

    public TableTableGroupServiceImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public List<OrderTable> findOrderTableByIds(List<Long> orderTableIds) {
        return orderTableRepository.findByIdIn(orderTableIds);
    }

    @Override
    public List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}

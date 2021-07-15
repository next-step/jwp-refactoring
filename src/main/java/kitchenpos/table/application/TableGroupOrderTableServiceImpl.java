package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupOrderTableService;

@Component
public class TableGroupOrderTableServiceImpl implements TableGroupOrderTableService {

    private final OrderTableRepository orderTableRepository;

    public TableGroupOrderTableServiceImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}

package kitchenpos.table_group.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TableGroupValidator {
    private static final String ORDER_TABLE_IS_NOT_EXIST = "주문테이블이 존재하지 않습니다";

    private final OrderTableRepository orderTableRepository;
    private final List<TableUngroupValidator> tableUngroupValidators;

    public TableGroupValidator(OrderTableRepository orderTableRepository, List<TableUngroupValidator> tableUngroupValidators) {
        this.orderTableRepository = orderTableRepository;
        this.tableUngroupValidators = tableUngroupValidators;
    }

    public List<GroupTable> checkOrderTableIds(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findByIdIn(orderTableIds);
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST);
        }
        return savedOrderTables.stream()
                .map(GroupTable::new)
                .collect(toList());
    }

    public void checkUngroupTables(TableGroup tableGroup) {
        final List<OrderTable> groupedOrderTables = orderTableRepository.findByTableGroupId(tableGroup.getId());
        List<Long> orderTableIds = groupedOrderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());
        tableUngroupValidators.forEach(validator -> validator.validate(orderTableIds));
    }
}

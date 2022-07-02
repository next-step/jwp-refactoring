package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private static final String TABLE_GROUP_IS_NOT_EXIST = "지정된 단체를 찾을 수 없습니다";
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroup create(final List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableValidator.findExistsOrderTableByIdIn(orderTableIds);
        return tableGroupRepository.save(TableGroup.group(savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findByIdWithOrderTable(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(TABLE_GROUP_IS_NOT_EXIST));

        List<Long> orderTableIds = tableGroup.getOrderTables().getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        orderTableValidator.checkOrderStatusIn(orderTableIds);

        tableGroup.ungroup();
    }
}

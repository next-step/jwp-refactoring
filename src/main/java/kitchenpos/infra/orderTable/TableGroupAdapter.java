package kitchenpos.infra.orderTable;

import kitchenpos.domain.orderTable.SafeTableGroup;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupAdapter implements SafeTableGroup {
    private final TableGroupRepository tableGroupRepository;

    public TableGroupAdapter(final TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Override
    public boolean isGroupedOrderTable(final Long orderTableId) {
        return tableGroupRepository.existsByOrderTablesOrderTableId(orderTableId);
    }
}

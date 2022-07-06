package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.exception.CannotUngroupException;
import kitchenpos.table.exception.NotExistTableGroupException;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupDomainService {
    private final TableOrderStatusChecker tableOrderStatusChecker;
    private final TableGroupRepository tableGroupRepository;

    public TableUngroupDomainService(TableOrderStatusChecker tableOrderStatusChecker,
                                     TableGroupRepository tableGroupRepository) {
        this.tableOrderStatusChecker = tableOrderStatusChecker;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NotExistTableGroupException::new);
        final List<Long> orderTableIds = extractTableIds(tableGroup);
        if (tableOrderStatusChecker.isExistTablesBeforeBillingStatus(orderTableIds)) {
            throw new CannotUngroupException();
        }
        tableGroup.ungroup();
    }

    private List<Long> extractTableIds(TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}

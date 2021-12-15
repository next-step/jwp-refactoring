package kitchenpos.domain.table_group.application;

import kitchenpos.domain.table.domain.OrderTables;
import kitchenpos.domain.table_group.domain.TableGroup;
import kitchenpos.domain.table_group.domain.TableGroupRepository;
import kitchenpos.domain.table_group.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final OrderTables orderTables = tableGroupValidator.getValidOrderTables(request.getOrderTableIds());
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        orderTables.group(tableGroup.getId());
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = tableGroupValidator.getCompleteOrderTables(tableGroupId);
        orderTables.ungroup();
    }
}

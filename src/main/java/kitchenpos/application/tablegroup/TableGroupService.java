package kitchenpos.application.tablegroup;

import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    public TableGroupService(
        final TableGroupRepository tableGroupRepository,
        final TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroup) {
        final OrderTables savedOrderTables = tableGroupValidator.getValidatedOrderTables(tableGroup);
        return TableGroupDto.of(tableGroupRepository.save(TableGroup.of(savedOrderTables)), savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = tableGroupValidator.getComplateOrderTable(tableGroupId);
        
        for (int index = 0; index < orderTables.size(); index++){
            orderTables.get(index).unGroupTable();
        }            
    }
}

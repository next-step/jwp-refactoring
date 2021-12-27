package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.tablegroup.domain.TableGroupValidator;

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
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final OrderTables validatedOrderTables = tableGroupValidator.getValidatedOrderTables(tableGroupDto);
        
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());

        validatedOrderTables.groupingTable(TableGroupId.of(savedTableGroup.getId()));

        return TableGroupDto.of(savedTableGroup, validatedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables validatedOrderTables = tableGroupValidator.getComplateOrderTable(tableGroupId);
        
        validatedOrderTables.unGroupTable();
    }
}

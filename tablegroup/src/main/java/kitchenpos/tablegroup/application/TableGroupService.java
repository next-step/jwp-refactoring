package kitchenpos.tablegroup.application;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.tablegroup.domain.TableGroupValidator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final TableService tableService;
    
    public TableGroupService(
        final TableGroupRepository tableGroupRepository,
        final TableGroupValidator tableGroupValidator,
        final TableService tableService
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.tableService = tableService;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final List<Long> orderTableIds = tableGroupDto.getOrderTables().stream()
                                                    .map(OrderTableDto::getId)
                                                    .collect(Collectors.toList());
        
        final OrderTables savedOrderTables = OrderTables.of(tableService.findAllByIdIn(orderTableIds));

        tableGroupValidator.checkAllExistOfOrderTables(tableGroupDto.getOrderTables(), savedOrderTables);
        tableGroupValidator.checkOrderTableSize(savedOrderTables);

        for (int index = 0; index < savedOrderTables.size(); index++) {
            tableGroupValidator.checkHasTableGroup(savedOrderTables.get(index));
            tableGroupValidator.checkNotEmptyTable(savedOrderTables.get(index));
        }

        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());
        savedOrderTables.groupingTable(TableGroupId.of(savedTableGroup.getId()));
        
        return TableGroupDto.of(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(tableService.findByTableGroupId(tableGroupId));
        
        tableGroupValidator.validateForUnGroup(orderTables);

        orderTables.unGroupTable();
    }
}

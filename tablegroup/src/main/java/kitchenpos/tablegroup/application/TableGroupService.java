package kitchenpos.tablegroup.application;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.tablegroup.exception.NotFoundTableGroupException;
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

        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());
        savedTableGroup.groupingTable(tableGroupDto.getOrderTables(), savedOrderTables, tableGroupValidator);

        return TableGroupDto.of(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(NotFoundTableGroupException::new);

        final OrderTables orderTables = OrderTables.of(tableService.findByTableGroupId(tableGroup.getId()));
        
        tableGroup.ungroup(orderTables, tableGroupValidator);
    }
}

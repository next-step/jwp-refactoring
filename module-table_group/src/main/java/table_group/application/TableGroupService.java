package table_group.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import table.domain.OrderTable;
import table.repository.OrderTableRepository;
import table_group.domain.TableGroup;
import table_group.dto.TableGroupRequestDto;
import table_group.dto.TableGroupResponseDto;
import table_group.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final TableGroupValidator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableGroupValidator tableGroupValidator, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponseDto create(final TableGroupRequestDto request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);
        tableGroupValidator.checkCreatable(orderTables, request.getOrderTableIds());

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.forEach(orderTable -> orderTable.group(tableGroup.getId()));
        return new TableGroupResponseDto(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
        tableGroupValidator.checkValidUngroup(orderTableIds);
        orderTables.forEach(OrderTable::ungroup);
    }
}

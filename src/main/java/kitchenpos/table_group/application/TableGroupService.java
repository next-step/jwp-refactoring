package kitchenpos.table_group.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableGroupRequestDto;
import kitchenpos.table.dto.TableGroupResponseDto;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        checkNotFoundOrderTables(orderTableIds, orderTables);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.forEach(orderTable -> orderTable.group(tableGroup.getId()));
        return new TableGroupResponseDto(tableGroup);
    }

    private void checkNotFoundOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
        tableGroupValidator.checkValidUngroup(orderTableIds);
        orderTables.forEach(OrderTable::ungroup);
    }
}

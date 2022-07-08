package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderValidator orderValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderValidator orderValidator, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderValidator = orderValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTables();

        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        checkNotExistOrderTable(orderTableIds, orderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderValidator.validateOrderTableStatus(orderTableIds);

        orderTables.forEach(OrderTable::ungroup);

        orderTableRepository.saveAll(orderTables);
    }

    private void checkNotExistOrderTable(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.domain.domainService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableTableGroupDomainService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableTableGroupDomainService(OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    public void checkOrderTableForCreateTableGroup(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = getOrderTablesByTableGroupRequest(
            tableGroupRequest);

        validateOrderTableOverTwo(orderTables);
        validateOrderTableCanGrouping(orderTables);
    }

    private void validateOrderTableOverTwo(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException("OVER 2 TABLE CAN GROUPING");
        }
    }

    private void validateOrderTableCanGrouping(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new TableGroupException("TABLE ALREADY GROUPED");
            }
        }
    }

    public TableGroup saveTableGroup(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = getOrderTablesByTableGroupRequest(
            tableGroupRequest);

        TableGroup tableGroup = new TableGroup();
        tableGroup = tableGroupRepository.save(tableGroup);
        final Long tableGroupId = tableGroup.getId();

        for (final OrderTable orderTable : orderTables) {
            orderTable.useTable();
            orderTable.mapToTableGroup(tableGroupId);
        }
        return tableGroup;
    }

    private List<OrderTable> getOrderTablesByTableGroupRequest(
        TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTables()
            .stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public void checkAllMenuIsCompleteInTableGroup(Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
            tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new TableGroupException("TABLE_CONTAIN_NOT_COMPLETE_ORDER");
        }
    }

    public void unGroupAllTableInTableGroup(Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
            tableGroupId);
        for (OrderTable orderTable : orderTables) {
            orderTable.unGroupTable();
        }
    }
}

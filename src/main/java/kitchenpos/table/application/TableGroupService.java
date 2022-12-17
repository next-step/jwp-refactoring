package kitchenpos.table.application;

import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        tableGroupRequest.validate(savedOrderTables);

        TableGroup tableGroup = TableGroup.create();
        TableGroup createTableGroup = tableGroupRepository.save(tableGroup);

        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.update(createTableGroup, false);
            orderTableRepository.save(savedOrderTable);
        }

        return TableGroupResponse.from(createTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroup(tableGroupId);

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        validateExistsOrderTable(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.removeTableGroup();
            orderTableRepository.save(orderTable);
        }
    }

    private void validateExistsOrderTable(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup getTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
    }

}

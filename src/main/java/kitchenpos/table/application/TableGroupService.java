package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.order.domain.OrderStatusV2;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableV2;
import kitchenpos.table.domain.TableGroupV2;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTableV2> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTableV2 savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.existTableGroupId()) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroupV2 tableGroup = tableGroupRequest.toTableGroup();
        final TableGroupV2 savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTableV2 savedOrderTable : savedOrderTables) {
            savedOrderTable.mappingTableGroupId(savedTableGroup);
            savedOrderTable.notEmpty();
            savedTableGroup.addOrderTable(savedOrderTable);
        }

        return savedTableGroup.toTableGroupResponse();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTableV2> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableV2::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsOrdersV2ByOrderTableIdInAndOrderStatusNot(orderTableIds, OrderStatusV2.COMPLETION)) {
            throw new IllegalArgumentException();
        }

        orderTables.forEach(OrderTableV2::ungroupTable);
    }
}

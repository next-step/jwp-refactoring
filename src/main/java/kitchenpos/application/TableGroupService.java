package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.*;
import kitchenpos.domain.dto.TableGroupRequest;
import kitchenpos.domain.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        TableGroup tableGroup = request.toEntity(orderTables);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(savedTableGroup, savedTableGroup.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}

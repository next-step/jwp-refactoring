package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderTableDuplicateException;
import kitchenpos.table.domain.*;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.TableGroupNotFoundException;
import kitchenpos.table.exception.TableUngroupFailException;
import kitchenpos.table.exception.TableUngroupInvalidStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new OrderTableDuplicateException();
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new TableUngroupInvalidStatusException();
        }

        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
    }
}

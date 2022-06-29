package kitchenpos.tablegroup.application;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.toOrderTableIds();

        final List<OrderTable> savedOrderTables = findSavedOrderTables(orderTableIds);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    private List<OrderTable> findSavedOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateSavedOrderTables(orderTableIds, savedOrderTables);
        return savedOrderTables;
    }

    private void validateSavedOrderTables(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(NoSuchElementException::new);
        List<Order> ordersInTableGroup = findOrdersInTableGroup(tableGroupId);
        tableGroup.ungroup(ordersInTableGroup);
    }

    private List<Order> findOrdersInTableGroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Order> result = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            result.addAll(findOrderByOrderTableId(orderTable.getId()));
        }
        return result;
    }

    private List<Order> findOrderByOrderTableId(Long orderTableId) {
        return orderRepository.findAllByOrderTableId(orderTableId);
    }

}

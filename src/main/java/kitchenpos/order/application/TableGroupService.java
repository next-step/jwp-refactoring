package kitchenpos.order.application;

import kitchenpos.exception.OrderTableErrorMessage;
import kitchenpos.exception.TableGroupErrorMessage;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = findAllOrderTableByIds(request.getOrderTables());
        return TableGroupResponse.of(tableGroupRepository.save(TableGroupRequest.toTableGroup(orderTables)));
    }

    private List<OrderTable> findAllOrderTableByIds(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException(OrderTableErrorMessage.NOT_FOUND_BY_ID.getMessage());
        }
        return orderTables;
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        List<Order> orders = findAllOrderByOrderTableIds(tableGroup.getOrderTableIds());

        tableGroup.ungroup(orders);

        tableGroupRepository.save(tableGroup);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(TableGroupErrorMessage.NOT_FOUND_BY_ID.getMessage()));
    }

    private List<Order> findAllOrderByOrderTableIds(List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTableIds(orderTableIds);
    }
}

package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
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

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTables = findOrderTable(request.getOrderTables());
        final TableGroup tableGroup = new TableGroup(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findById(tableGroupId);
        List<Order> orders = findOrder(tableGroup.getOrderTables());
        tableGroup.ungroupOrderTables(orders);
    }

    private List<OrderTable> findOrderTable(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validRegisteredOrderTable(orderTables.size(), orderTableIds.size());
        return orderTableRepository.findAllById(orderTableIds);
    }

    private void validRegisteredOrderTable(int orderTableSize, int orderTableIdSize) {
        if (orderTableSize != orderTableIdSize) {
            throw new IllegalArgumentException("등록된 주문테이블만 그룹화 할 수 있습니다.");
        }
    }

    private TableGroup findById(Long tableGroupId){
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 테이블 그룹만 그룹해제 가능합니다."));
    }

    private List<Order> findOrder(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderRepository.findAllByOrderTableIds(orderTableIds);
    }
}

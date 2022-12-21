package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository, final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTables orderTables = OrderTables.of(orderTableById(request.getOrderTableIds()));
        List<OrderTableResponse> orderTableResponses = orderTables.get()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.group(tableGroup.getId());
        return TableGroupResponse.of(tableGroup, orderTableResponses);
    }

    private List<OrderTable> orderTableById(List<Long> ids) {
        return ids.stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(tableGroupId));
        List<Order> orders = findAllOrderByTableIds(orderTables.getOrderTableIds());
        tableGroup.ungroup(orders);
        tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> findAllOrderTablesByIds(List<Long> ids) {
        return ids.stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.NOT_EXISTS_ORDER_TABLE.message()));
    }

    private List<Order> findAllOrderByTableIds(List<Long> ids) {
        return orderRepository.findAllByOrderTableIdIn(ids);
    }
    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.NOT_EXISTS_TABLE_GROUP.message()));
    }
}

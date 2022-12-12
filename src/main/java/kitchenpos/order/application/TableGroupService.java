package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = OrderTables.from(findAllOrderTablesById(tableGroupRequest.getOrderTables()));
        List<OrderTableResponse> orderTableResponses = orderTables.unmodifiableOrderTables()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.from());
        orderTables.registerTableGroup(tableGroup.getId());
        return TableGroupResponse.from(tableGroup, orderTableResponses);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        OrderTables orderTables = OrderTables.from(findAllOrderTableByTableGroupId(tableGroupId));
        List<Order> orders = findAllOrderByOrderTableIds(orderTables.unmodifiableOrderTables());
        tableGroup.ungroup(orders);
        orderTables.ungroupOrderTables();
    }

    private List<OrderTable> findAllOrderTablesById(List<Long> ids) {
        return ids.stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage()));
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_단체_그룹.getErrorMessage()));
    }

    private List<Order> findAllOrderByOrderTableIds(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderRepository.findAllByOrderTableIdIn(orderTableIds);
    }

    private List<OrderTable> findAllOrderTableByTableGroupId(Long id) {
        return orderTableRepository.findAllByTableGroupId(id);
    }
}

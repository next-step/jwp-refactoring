package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequests;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTableRequests orderTableRequests = new OrderTableRequests(request.getOrderTables());
        List<Long> orderTableIds = orderTableRequests.getOrderTableIds();
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllById(orderTableIds));
        orderTables.validateSizeForTableGroup(orderTableRequests.getSize());
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup(orderTables)));
    }

    public void ungroup(final Long tableGroupId) {
        List<OrderTable> list = orderTableRepository.findAllByTableGroup(new TableGroup(tableGroupId));
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(list, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        OrderTables orderTables = new OrderTables(list);
        orderTables.unGroupOrderTables();
    }
}

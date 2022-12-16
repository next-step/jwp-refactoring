package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupCreateValidator;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.domain.TableGroupUnGroupValidator;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository,
            final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        TableGroupCreateValidator.validate(orderTableIds, savedOrderTables);
        TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup(savedOrderTables));
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTableList = orderTableRepository.findAllByTableGroupId(tableGroupId);
        boolean completedOrderTable = orderRepository.existsByOrderTableInAndOrderStatusIn(orderTableList,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
        TableGroupUnGroupValidator.validate(completedOrderTable);
        OrderTables orderTables = OrderTables.from(orderTableList);
        orderTables.unTableGroup();
    }
}

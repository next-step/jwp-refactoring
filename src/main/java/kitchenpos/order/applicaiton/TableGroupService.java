package kitchenpos.order.applicaiton;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(
                tableGroupRequest.getOrderTableIds());

        tableGroupRequest.throwIfOrderTableSizeWrong(savedOrderTables.size());
        TableGroup tableGroup = TableGroup.of(OrderTables.toBeGrouped(savedOrderTables));
        tableGroup.group();

        TableGroup saved = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> allByTableGroupId = orderTableRepository.findAllByTableGroup_Id(tableGroupId);
        if(CollectionUtils.isEmpty(allByTableGroupId)){
            throw new IllegalArgumentException();
        }
        OrderTables orderTables = OrderTables.of(allByTableGroupId);
        throwIfSomeOrderInProgress(orderTables.getTableIds());
        orderTables.unGroup();
    }

    private void throwIfSomeOrderInProgress(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTable_IdInAndOrderStatusIn(
                orderTableIds, Order.orderStatusInProgress)) {
            throw new IllegalArgumentException();
        }
    }
}

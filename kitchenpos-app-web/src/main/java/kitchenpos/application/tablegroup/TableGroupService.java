package kitchenpos.application.tablegroup;

import kitchenpos.application.order.OrderStatus;
import kitchenpos.application.ordertable.OrderTable;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.ordertable.OrderTableRepository;
import kitchenpos.repository.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.getOrderTables());
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.fromGroupingTables(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정된 그룹이 없습니다."));

        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                tableGroup.getOrderTables(), orderStatuses)) {
            throw new IllegalArgumentException();
        }

        tableGroup.ungroupTables();
        tableGroupRepository.delete(tableGroup);
    }
}
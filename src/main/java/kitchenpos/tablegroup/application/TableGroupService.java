package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.advice.exception.TableGroupException;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
        final TableService tableService,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        tableGroupRequest.validateOrderTableSize();
        OrderTables orderTables = tableService.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        return tableGroupRepository.save(new TableGroup(orderTables));
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = tableService.findAllByTableGroupId(tableGroupId);

        validateOrderStatusNotIn(orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));

        tableGroup.getOrderTables().ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    public TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id).orElseThrow(() -> new TableGroupException("테이블 그룹이 존재하지 않습니다", id));
    }

    public void validateOrderStatusNotIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, orderStatuses)) {
            throw new OrderTableException("올바르지 않은 주문상태가 포함되어있습니다", orderStatuses);
        }
    }
}

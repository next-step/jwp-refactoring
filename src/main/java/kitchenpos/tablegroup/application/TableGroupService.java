package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        request.checkValidSize();
        final List<Long> orderTableIds = makeOrderTableIds(request);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        OrderTables orderTables = makeOrderTables(orderTableIds);
        orderTables.referenceGroupId(tableGroup.getId());
        orderTableRepository.saveAll(orderTables.getOrderTables());

        return TableGroupResponse.of(tableGroup, orderTables.getOrderTables());
    }

    private List<Long> makeOrderTableIds(TableGroupRequest request) {
        return request.getOrderTables().stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
    }

    private OrderTables makeOrderTables(List<Long> orderTableIds) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        orderTables.checkSameSize(orderTableIds.size());
        orderTables.checkNotContainsUsedTable();

        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> tables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        OrderTables orderTables = new OrderTables(tables);

        checkContainsCookingOrMealTable(orderTables);

        orderTables.unGroup();
    }

    private void checkContainsCookingOrMealTable(OrderTables orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables.getOrderTables(), OrderStatus.NOT_COMPLETED_LIST)) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }
    }
}

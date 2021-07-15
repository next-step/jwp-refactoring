package kitchenpos.tablegroup.domain;

import static java.util.Arrays.*;
import static kitchenpos.order.domain.OrderStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.generic.exception.NotEnoughTablesException;
import kitchenpos.generic.exception.OrderNotCompletedException;
import kitchenpos.generic.exception.OrderTableNotFoundException;
import kitchenpos.generic.exception.TableGroupNotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;

@Component
public class TableGroupValidator {

    private static final int MINIMUM_GROUPING_COUNT = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupValidator(OrderRepository orderRepository,
            OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void validateUngrouping(TableGroup tableGroup) {
        if (!exists(tableGroup)) {
            throw new TableGroupNotFoundException("해당 ID의 테이블 그룹이 존재하지 않습니다.");
        }

        if (includeOrderInProgess(tableGroup)) {
            throw new OrderNotCompletedException("테이블에 완결되지 않은 주문이 존재합니다.");
        }
    }

    private boolean exists(TableGroup tableGroup) {
        return tableGroupRepository.existsById(tableGroup.getId());
    }

    private boolean includeOrderInProgess(TableGroup tableGroup) {
        List<Long> orderTableIds = orderTableRepository.findAllByTableGroupId(tableGroup.getId())
            .stream().map(OrderTable::getId).collect(Collectors.toList());
        return orderRepository.existsAllByOrderTableIdInAndOrderStatusIn(orderTableIds, asList(COOKING, MEAL));
    }

    public void validateGrouping(List<Long> orderTableIds) {
        validateGrouping(getOrderTables(orderTableIds));
    }

    public void validateGrouping(OrderTables orderTables) {
        if (orderTables.isEmpty()) {
            throw new NotEnoughTablesException("테이블 목록이 비어있습니다.");
        }

        if (!orderTables.hasLargerOrSameSizeOf(MINIMUM_GROUPING_COUNT)) {
            throw new NotEnoughTablesException("그룹화 하려면 %d개 이상의 테이블이 필요합니다.", MINIMUM_GROUPING_COUNT);
        }

        if (!orderTables.isGroupable()) {
            throw new IllegalOperationException("그룹화 불가한 테이블이 포함되어 있습니다.");
        }
    }

    private OrderTables getOrderTables(List<Long> orderTableIds) {
        return OrderTables.of(orderTableIds.stream().map(this::getOrderTable).collect(Collectors.toList()));
    }

    private OrderTable getOrderTable(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new OrderTableNotFoundException("해당 ID의 테이블이 존재하지 않습니다."));
    }
}

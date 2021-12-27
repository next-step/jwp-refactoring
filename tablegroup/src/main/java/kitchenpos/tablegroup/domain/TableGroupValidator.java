package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NotEmptyOrderTableException;
import kitchenpos.table.exception.NotGroupingOrderTableCountException;
import kitchenpos.table.exception.NotRegistedMenuOrderTableException;

@Component
public class TableGroupValidator {
    private static final int GROUPING_ORDERTABLE_MINCOUNT = 2;

    private final OrderService orderService;
    private final TableService tableService;

    public TableGroupValidator(
        final OrderService orderService,
        final TableService tableService
    ) {
        this.orderService = orderService;
        this.tableService = tableService;
    }

    public void validateForUnGroup(OrderTables orderTables) {
        if (orderService.hasNotComplateStatus(orderTables.getOrderTableIds())) {
            throw new HasNotCompletionOrderException("계산완료가 되지않은 주문이 존재합니다.");
        }
    }

    public OrderTables getComplateOrderTable(Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(tableService.findByTableGroupId(tableGroupId));

        this.validateForUnGroup(orderTables);

        return orderTables;
    }

    public OrderTables getValidatedOrderTables(TableGroupDto tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                                                    .map(OrderTableDto::getId)
                                                    .collect(Collectors.toList());

        final OrderTables savedOrderTables = OrderTables.of(tableService.findAllByIdIn(orderTableIds));

        checkAllExistOfOrderTables(tableGroup.getOrderTables(), savedOrderTables);

        checkOrderTableSize(savedOrderTables);

        for (int index = 0; index < savedOrderTables.size(); index++) {
            checkHasTableGroup(savedOrderTables.get(index));
            checkNotEmptyTable(savedOrderTables.get(index));
        }

        return savedOrderTables;
    }
    
    private static void checkHasTableGroup(final OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new HasOtherTableGroupException("단체지정이 된 주문테이블입니다.");
        }
    }

    private static void checkNotEmptyTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new NotEmptyOrderTableException("주문테이블이 빈테이블이 아닙니다.");
        }
    }
    
    private static void checkOrderTableSize(final OrderTables orderTables) {
        if (orderTables.isLessSizeThan(GROUPING_ORDERTABLE_MINCOUNT)) {
            throw new NotGroupingOrderTableCountException("주문 테이블의 개수가 2개 미만입니다.");
        }
    }

    private void checkAllExistOfOrderTables(final List<OrderTableDto> orderTables, final OrderTables savedOrderTables) {
        if (savedOrderTables.isNotEqualSize(orderTables.size())) {
            throw new NotRegistedMenuOrderTableException("요청된 주문테이블 수와 조회된 주문테이블 수가 일치하지 않습니다.");
        }
    }
}

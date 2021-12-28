package kitchenpos.tablegroup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NotEmptyOrderTableException;
import kitchenpos.table.exception.NotGroupingOrderTableCountException;
import kitchenpos.table.exception.NotRegistedMenuOrderTableException;

@Component
public class TableGroupValidator {
    private static final int GROUPING_ORDERTABLE_MINCOUNT = 2;

    private final OrderService orderService;

    public TableGroupValidator(
        final OrderService orderService
    ) {
        this.orderService = orderService;
    }

    public void validateForUnGroup(OrderTables orderTables) {
        if (orderService.hasNotComplateStatus(orderTables.getOrderTableIds())) {
            throw new HasNotCompletionOrderException("계산완료가 되지않은 주문이 존재합니다.");
        }
    }
    
    public void checkHasTableGroup(final OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new HasOtherTableGroupException("단체지정이 된 주문테이블입니다.");
        }
    }

    public void checkNotEmptyTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new NotEmptyOrderTableException("주문테이블이 빈테이블이 아닙니다.");
        }
    }
    
    public void checkOrderTableSize(final OrderTables orderTables) {
        if (orderTables.isLessSizeThan(GROUPING_ORDERTABLE_MINCOUNT)) {
            throw new NotGroupingOrderTableCountException("주문 테이블의 개수가 2개 미만입니다.");
        }
    }

    public void checkAllExistOfOrderTables(final List<OrderTableDto> orderTables, final OrderTables savedOrderTables) {
        if (savedOrderTables.isNotEqualSize(orderTables.size())) {
            throw new NotRegistedMenuOrderTableException("요청된 주문테이블 수와 조회된 주문테이블 수가 일치하지 않습니다.");
        }
    }
}

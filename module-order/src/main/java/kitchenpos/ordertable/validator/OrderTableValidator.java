package kitchenpos.ordertable.validator;

import static kitchenpos.tablegroup.domain.TableGroup.ORDER_TABLE_REQUEST_MIN;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component
@Transactional(readOnly = true)
public class OrderTableValidator {
    public void validateReserveEvent(OrderTables savedOrderTables, List<Long> orderTableIds) {
        validateOrderTableIds(orderTableIds);
        validateOrderTablesSize(savedOrderTables, orderTableIds.size());
    }

    private void validateOrderTableIds(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < ORDER_TABLE_REQUEST_MIN) {
            throw new IllegalArgumentException(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
        }
    }

    private void validateOrderTablesSize(OrderTables orderTables, int size) {
        if (orderTables.isNotEqualSize(size)) {
            throw new IllegalArgumentException("비교하는 수와 주문 테이블의 수가 일치하지 않습니다.");
        }
    }
}

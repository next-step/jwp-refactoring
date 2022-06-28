package kitchenpos.ordertable.validator;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.tableGroup.domain.TableGroup.ORDER_TABLE_REQUEST_MIN;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component
@Transactional(readOnly = true)
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateComplete(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문테이블의 주문이 완료상태가 아닙니다.");
        }
    }

    public void validateComplete(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문테이블들의 주문이 완료상태가 아닙니다.");
        }
    }

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

package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.dao.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderTableCheck(Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new IllegalArgumentException("존재하지 않는 주문테이블 입니다.");
        }
    }

    public void validateOrderStatusCheck(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("계산 완료 상태가 아닌 경우 단체를 해제할 수 없습니다.");
        }
    }

    public void validateChangeableOrderStatusCheck(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문상태를 변경할 수 없습니다.");
        }
    }
}

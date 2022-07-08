package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.ChangeStateTableValidator;
import org.springframework.stereotype.Service;

@Service
public class OrderStateValidator implements ChangeStateTableValidator {
    private final OrderRepository orderRepository;

    public OrderStateValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeEmptyTable(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료된 상태여만 빈테이블로 변경이 가능합니다.");
        }
    }

    @Override
    public void validateUnGroupTableChange(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 테이블중 조리중인 경우에 단체석을 개인 주문테이블로 변경할 수 없습니다.");
        }
    }


}

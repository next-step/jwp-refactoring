package kitchenpos.order.domain;

import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        checkOrderTableIsValid(order);
        checkOrderLineItemExists(order);
        checkOrderLineItemMenuIsExists(order);
    }

    public void validateChangeable(Order order) {
        checkCompletedOrderChangeStatus(order);
    }

    private void checkOrderTableIsValid(Order order) {
        orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new NotFoundException("해당 주문 테이블을 찾을 수 없습니다."));
    }

    private void checkOrderLineItemExists(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new NotFoundException("주문 라인은 최소 1개 이상 필요합니다.");
        }
    }

    private void checkOrderLineItemMenuIsExists(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        orderLineItems.forEach(it -> menuRepository.findById(it.getMenuId())
                        .orElseThrow(() -> new NotFoundException("해당 메뉴를 찾을 수 없습니다.")));
    }

    private void checkCompletedOrderChangeStatus(Order order) {
        if (order.isComplete()) {
            throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
        }
    }
}

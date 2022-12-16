package kitchenpos.order.validator;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository,
                          MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateCreation(Order order) {
        validateOrderTable(order);
        order.validateOrderLineItemsSizeAndMenuCount(menuRepository.countByIdIn(order.makeMenuIds()));
    }

    @Transactional
    void validateOrderTable(Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "주문 등록시, 등록된 주문 테이블만 지정할 수 있습니다 [orderTableId:" + order.getOrderTableId() + "]"));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 등록시, 주문 테이블은 비어있으면 안됩니다");
        }
        orderTable.addOrder(order);
    }
}

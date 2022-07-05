package kitchenpos.table.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableValidator implements OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateOrder(Order order) {
        long menuCount = menuRepository.countByIdIn(order.getMenuIds());
        if (order.orderLineItemSize() != menuCount) {
            throw new IllegalArgumentException("중복된 메뉴가 있습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문을 할 수 없습니다.");
        }
    }
}

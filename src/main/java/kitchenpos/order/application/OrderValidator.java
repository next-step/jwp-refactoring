package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.menu.infrastructure.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infrastructure.OrderRepository;
import kitchenpos.table.application.Validator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infrastructure.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator implements Validator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderValidator(MenuRepository menuRepository,
                          OrderTableRepository orderTableRepository,
                          OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 및 식사 중일때는 변경할 수 없습니다.");
        }
    }

    public void validate(Order order) {
        Set<OrderLineItem> orderLineItems = validateOrderLineItems(order);
        validateMenu(orderLineItems);
        validateOrderTable(order);
    }

    private void validateMenu(Set<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문한 메뉴가 존재하지 않습니다.");
        }
    }

    private Set<OrderLineItem> validateOrderLineItems(Order order) {
        Set<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문한 메뉴 상품이 없습니다.");
        }
        return orderLineItems;
    }

    private void validateOrderTable(Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }
    }
}
